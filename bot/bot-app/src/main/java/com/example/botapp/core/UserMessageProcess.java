package com.example.botapp.core;

import com.example.botapp.client.BackendClient;
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
        if (chatStateInfo == null) {
            Long chatId = updateModel.message().chat().id();
            chatState.put(chatId, new ChatStateInfo(chatId, State.Waiting));
            chatStateInfo = chatState.get(updateModel.message().chat().id());
        }
        if (textMessage != null) {
            log.info("Get user message with id: " + chatStateInfo.getChatId() + ",with message: \"" + textMessage + "\"");

        }
        if (textMessage != null && chatStateInfo.getChatState() == State.Waiting){

        }
        return null;
    }

    public static void setState(Long chatId, State state){
        chatState.put(chatId, new ChatStateInfo(chatId, state));
    }
}
