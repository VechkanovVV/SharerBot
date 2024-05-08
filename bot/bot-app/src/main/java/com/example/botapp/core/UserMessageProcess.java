package com.example.botapp.core;

import com.example.botapp.client.BackendClient;
import com.example.botapp.client.response.FilesListResponse;
import com.example.botapp.core.commands.Command;
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
    private  static Map<Long, List<Long>> searchFiles = new HashMap<>();

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
        if (chatStateInfo.getChatState() == State.Search) {
            String fileName = updateModel.message().text();
            try{
                FilesListResponse response = backendClient.findFile(fileName);
                log.info(response.toString());
                String message = "";
                 if (response.files().isEmpty()){
                     message += "File does not exist, please provide another file";
                 } else{
                     searchFiles.put(chatStateInfo.getChatId(), new ArrayList<>());
                    for (int i = 0; i < response.files().size(); i++){
                        message +=("|" +(i) +"|"+  "file name: " + response.files().get(i).fileName() +"\n"
                                + response.files().get(i).fileDescription());
                        searchFiles.get(chatStateInfo.getChatId()).add(searchFiles.get(chatStateInfo.getChatId()).size(),response.files().get(i).owner_id());
                    }
                 }
                return new SendMessage(chatStateInfo.getChatId(), message);
            }  catch (WebClientResponseException e) {
                if (e.getStatusCode() != HttpStatus.UNSUPPORTED_MEDIA_TYPE) {
                    throw e;
                }
                return new SendMessage(chatStateInfo.getChatId(), "Wrong format");
            }
        }
        return null;
    }

    public static void setState(Long chatId, State state) {
        chatState.put(chatId, new ChatStateInfo(chatId, state));
    }
}
