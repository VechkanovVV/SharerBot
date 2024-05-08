package com.example.botapp.core.commands;

import com.example.botapp.core.State;
import com.example.botapp.core.UserMessageProcess;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class SetPermissionCommand implements Command{
    private static final String COMMAND = "/set_permission";
    private static final String DESCRIPTION = "Command to allow file upload";
    private static final String MESSAGE = """
            You have allowed the file to be uploaded
            """;
    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        UserMessageProcess.setState(chatId, State.AllowDownload);
        return new SendMessage(chatId,MESSAGE);
    }
}
