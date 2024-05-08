package com.example.botapp.core.commands;

import com.example.botapp.core.State;
import com.example.botapp.core.UserMessageProcess;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class RejectPermissionCommand implements Command {
    private static final String COMMAND = "/reject_permission";
    private static final String DESCRIPTION = "Command to reject file upload";
    private static final String MESSAGE = """
            You have not allowed the file to be uploaded
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
        UserMessageProcess.setState(chatId, State.RejectDownload);
        return new SendMessage(chatId,MESSAGE);
    }
}
