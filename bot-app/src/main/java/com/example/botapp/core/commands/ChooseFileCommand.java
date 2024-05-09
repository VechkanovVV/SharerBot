package com.example.botapp.core.commands;

import com.example.botapp.core.MarkDown;
import com.example.botapp.core.State;
import com.example.botapp.core.UserMessageProcess;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class ChooseFileCommand  implements Command{
    private static final String COMMAND = "/choose_file";
    private static final String DESCRIPTION = "Command to choose a file from a list";
    private static final String MESSAGE = """
           Please specify the index of the file you want to download!
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
        UserMessageProcess.setState(chatId, State.FileSelect);
        return new SendMessage(chatId, MarkDown.process(MESSAGE));
    }
}
