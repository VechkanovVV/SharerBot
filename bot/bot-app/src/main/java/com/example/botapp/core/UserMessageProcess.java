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
import org.springframework.stereotype.Component;

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
                FilesListResponse files = backendClient.findFile(fileName);
                log.info(files.toString());

            }
        }
        return null;
    }

    public static void setState(Long chatId, State state) {
        chatState.put(chatId, new ChatStateInfo(chatId, state));
    }
}
