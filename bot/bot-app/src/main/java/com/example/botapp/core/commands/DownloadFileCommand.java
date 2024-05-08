package com.example.botapp.core.commands;

import com.example.botapp.core.State;
import com.example.botapp.core.UserMessageProcess;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class DownloadFileCommand implements Command {
    private static final String COMMAND = "/Download file";
    private static final String DESCRIPTION = "Command to download file";
    private static final String MESSAGE = """
            Please provide your file and save it in this chat!
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
        UserMessageProcess.setState(chatId, State.Download);
        return new SendMessage(chatId,MESSAGE);
    }
}
