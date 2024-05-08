package com.example.botapp.core;

import com.example.botapp.client.BackendClient;
import com.example.botapp.client.request.DownloadFileRequest;
import com.example.botapp.client.request.UploadFileRequest;
import com.example.botapp.client.response.FilesListResponse;
import com.example.botapp.core.commands.Command;
import com.example.botapp.core.commands.DownloadFileCommand;
import com.example.botapp.core.commands.UploadFileCommand;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class UserMessageProcess {
    private final TelegramBot bot;
    private final BackendClient backendClient;
    private static List<? extends Command> commands;
    private static final Map<Long, ChatStateInfo> chatState = new HashMap<>();
    private static Map<Long, List<FileInformation>> searchFiles = new HashMap<>();

    @Autowired
    public UserMessageProcess(TelegramBot bot, BackendClient backendClient, List<? extends Command> commands) {
        this.bot = bot;
        this.backendClient = backendClient;
        UserMessageProcess.commands = commands;
    }

    public static List<? extends Command> commands() {
        return commands;
    }

    public SendMessage process(@NotNull Update updateModel) {
        if (updateModel.message().text() == null) {
            return null;
        }
        ChatStateInfo chatStateInfo = chatState.get(updateModel.message().chat().id());
        String textMessage = updateModel.message().text();
        if (textMessage != null) {
            log.info("Get user message with id: " + chatStateInfo.getChatId() + ",with message: \"" + textMessage + "\"");

        }
        if (chatStateInfo == null && chatStateInfo.getChatState() == State.Waiting) {
            if (textMessage.startsWith("/")) {
                for (Command com : commands()) {
                    if (com.supports(updateModel)) {
                        return com.handle(updateModel);
                    }
                }
            }
        }
        else if (chatStateInfo.getChatState() == State.Search) {
            String fileName = updateModel.message().text();
            try {
                FilesListResponse response = backendClient.findFile(fileName);
                log.info(response.toString());
                StringBuilder message = new StringBuilder();
                if (response.files().isEmpty()) {
                    message.append("File does not exist, please provide another file");
                } else {
                    searchFiles.put(chatStateInfo.getChatId(), new ArrayList<>());
                    for (int i = 0; i < response.files().size(); i++) {
                        message.append("|").append(i).append("|").append("file name: ").append(response.files().get(i).fileName()).append("\n").append(response.files().get(i).fileDescription());

                        FileInformation f = new FileInformation();
                        f.setFileName(response.files().get(i).fileName());
                        f.setReceiverId(chatStateInfo.getChatId());
                        f.setOwnerId(response.files().get(i).owner_id());
                        searchFiles.get(chatStateInfo.getChatId()).add(searchFiles.get(chatStateInfo.getChatId()).size(),f);
                    }
                }
                chatState.remove(chatStateInfo.getChatId());
                return new SendMessage(chatStateInfo.getChatId(), message.toString());
            } catch (WebClientResponseException e) {
                if (e.getStatusCode() != HttpStatus.UNSUPPORTED_MEDIA_TYPE) {
                    throw e;
                }
                chatState.remove(chatStateInfo.getChatId());
                return new SendMessage(chatStateInfo.getChatId(), "Wrong format");
            }
        } else if (chatStateInfo.getChatState() == State.FileSelect){
            Long chatId = chatStateInfo.getChatId();
            if (!searchFiles.containsKey(chatId)){
                return new SendMessage(chatStateInfo.getChatId(), "Please, before using the file selection function, try using the search function");
            }
            int index = Integer.parseInt(updateModel.message().text());
            if (index < 0 || index >=searchFiles.get(chatId).size()){
                return new SendMessage(chatStateInfo.getChatId(), "Please, use correct index");
            }
            FileInformation f = searchFiles.get(chatId).get(index);
            DownloadFileRequest request = new DownloadFileRequest();
            request.setFileName(f.fileName);
            request.setReceiverId(f.receiverId);
            request.setOwnerId(f.ownerId);
            searchFiles.remove(chatId);
            chatState.remove(chatId);
            try {
                backendClient.downloadFile(request);
                return new SendMessage(chatStateInfo.getChatId(), "Requesting permission to download from the copyright holder\n Please wait, or try again after a while!");
            } catch (WebClientResponseException e) {
                if (e.getStatusCode() != HttpStatus.UNSUPPORTED_MEDIA_TYPE) {
                    throw e;
                }
                chatState.remove(chatStateInfo.getChatId());
                return new SendMessage(chatStateInfo.getChatId(), "Wrong format");
            }
        }

        return null;
    }

    public static void setState(Long chatId, State state) {
        chatState.put(chatId, new ChatStateInfo(chatId, state));
    }
}
