package com.example.botapp.core.commands;


import com.example.botapp.core.State;
import com.example.botapp.core.UserMessageProcess;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class FindFileCommand implements Command {
    private static final String COMMAND = "/find_file";
    private static final String DESCRIPTION = "Command to find file if it exists";
    private static final String MESSAGE = """
            Please provide the file name or prefix you wish to search for.
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
        UserMessageProcess.setState(chatId, State.Search);
        return new SendMessage(chatId,MESSAGE);
    }
}
