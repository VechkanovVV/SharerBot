package com.example.botapp.core;

import com.example.botapp.client.BackendClient;
import com.example.botapp.client.request.DownloadFileRequest;
import com.example.botapp.client.request.SetPermissionRequest;
import com.example.botapp.client.request.UploadFileRequest;
import com.example.botapp.client.response.FilesListResponse;
import com.example.botapp.core.commands.Command;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Document;
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
    private static final Map<Long, ChatStateInfo> CHAT_STATE = new HashMap<>();
    private static final Map<Long, List<FileInformation>> SEARCH_FIELS = new HashMap<>();
    private final static String UNSUPPORTED_COMMAND = "Unsupported command";
    private final static String WRONG_FORMAT = "wrong format";

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
        if (updateModel.message() == null) {
            return null;
        }
        ChatStateInfo chatStateInfo = CHAT_STATE.get(updateModel.message().chat().id());
        String textMessage = updateModel.message().text();
        if (chatStateInfo != null) {
            log.info("Get user message with id: " + chatStateInfo.getChatId() + ",with message: \"" + textMessage + "\"");

        } else if (textMessage != null) {
            log.info("Get user message \"" + textMessage + "\"");
        }
        if (chatStateInfo == null) {
            if (textMessage.startsWith("/")) {
                for (Command com : commands()) {
                    if (com.supports(updateModel)) {
                        return com.handle(updateModel);
                    }
                }
            }
        } else if (chatStateInfo.getChatState() == State.Search) {
            String fileName = updateModel.message().text();
            try {
                FilesListResponse response = backendClient.findFile(fileName);
                log.info(response.toString());
                StringBuilder message = new StringBuilder();
                if (response.files().isEmpty()) {
                    message.append("File does not exist, please provide another file");
                } else {
                    SEARCH_FIELS.put(chatStateInfo.getChatId(), new ArrayList<>());
                    for (int i = 0; i < response.files().size(); i++) {
                        message.append("|").append(i).append("|").append("file name: ").append(response.files().get(i).fileName()).append("\n").append(response.files().get(i).fileDescription());

                        FileInformation f = new FileInformation();
                        f.setFileName(response.files().get(i).fileName());
                        f.setReceiverId(chatStateInfo.getChatId());
                        f.setOwnerId(response.files().get(i).ownerId());
                        SEARCH_FIELS.get(chatStateInfo.getChatId()).add(SEARCH_FIELS.get(chatStateInfo.getChatId()).size(), f);
                    }
                }
                CHAT_STATE.remove(chatStateInfo.getChatId());
                return new SendMessage(chatStateInfo.getChatId(), MarkDown.process(message.toString()));
            } catch (WebClientResponseException e) {
                if (e.getStatusCode() != HttpStatus.UNSUPPORTED_MEDIA_TYPE) {
                    throw e;
                }
                CHAT_STATE.remove(chatStateInfo.getChatId());
                return new SendMessage(chatStateInfo.getChatId(), MarkDown.process(WRONG_FORMAT));
            }
        } else if (chatStateInfo.getChatState() == State.FileSelect) {
            Long chatId = chatStateInfo.getChatId();
            if (!SEARCH_FIELS.containsKey(chatId)) {
                return new SendMessage(chatStateInfo.getChatId(), MarkDown.process("Please, before using the file selection function, try using the search function"));
            }
            int index = Integer.parseInt(updateModel.message().text());
            if (index < 0 || index >= SEARCH_FIELS.get(chatId).size()) {
                return new SendMessage(chatStateInfo.getChatId(), MarkDown.process("Please, use correct index"));
            }
            FileInformation f = SEARCH_FIELS.get(chatId).get(index);
            DownloadFileRequest request = new DownloadFileRequest();
            request.setFileName(f.fileName);
            request.setReceiverId(f.receiverId);
            request.setOwnerId(f.ownerId);
            SEARCH_FIELS.remove(chatId);
            CHAT_STATE.remove(chatId);
            try {
                backendClient.downloadFile(request);
                return new SendMessage(chatStateInfo.getChatId(), MarkDown.process("Requesting permission to download from the copyright holder\n Please wait, or try again after a while!"));
            } catch (WebClientResponseException e) {
                if (e.getStatusCode() != HttpStatus.UNSUPPORTED_MEDIA_TYPE) {
                    throw e;
                }
                return new SendMessage(chatStateInfo.getChatId(), MarkDown.process(WRONG_FORMAT));
            }
        } else if (chatStateInfo.getChatState() == State.AllowDownload) {
            String[] input = (updateModel.message().text()).split(" ");
            if (input.length != 2) {
                return new SendMessage(chatStateInfo.getChatId(), MarkDown.process("Please, use correct format"));
            }
            CHAT_STATE.remove(chatStateInfo.getChatId());
            try {
                SetPermissionRequest request = new SetPermissionRequest(chatStateInfo.getChatId(), Long.parseLong(input[0]), input[1]);
                backendClient.setPermission(request);
                return new SendMessage(chatStateInfo.getChatId(), MarkDown.process("You have allowed the file to be uploaded"));
            } catch (WebClientResponseException e) {
                if (e.getStatusCode() != HttpStatus.UNSUPPORTED_MEDIA_TYPE) {
                    throw e;
                }
                return new SendMessage(chatStateInfo.getChatId(), MarkDown.process(WRONG_FORMAT));
            }

        } else if (chatStateInfo.getChatState() == State.RejectDownload) {
            CHAT_STATE.remove(chatStateInfo.getChatId());
            return new SendMessage(chatStateInfo.getChatId(), MarkDown.process("You have not allowed the file to be uploaded"));
        } else if (chatStateInfo.getChatState() == State.Upload) {
            Document document = updateModel.message().document();
            String description = updateModel.message().text();
            UploadFileRequest request = new UploadFileRequest();
            request.setFileId(document.fileId());
            request.setOwnerId(chatStateInfo.getChatId());
            request.setFileName(document.fileName());
            request.setFileDescription(description);
            CHAT_STATE.remove(chatStateInfo.getChatId());
            try {
                backendClient.uploadFile(request);
                return new SendMessage(chatStateInfo.getChatId(), MarkDown.process("You have uploaded your file to the server!"));
            } catch (WebClientResponseException e) {
                if (e.getStatusCode() != HttpStatus.UNSUPPORTED_MEDIA_TYPE) {
                    throw e;
                }
                return new SendMessage(chatStateInfo.getChatId(), MarkDown.process(WRONG_FORMAT));
            }
        }
        throw new UnsupportedOperationException(UNSUPPORTED_COMMAND);
    }

    public static void setState(Long chatId, State state) {
        CHAT_STATE.put(chatId, new ChatStateInfo(chatId, state));
    }
}
