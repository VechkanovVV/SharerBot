package com.example.botapp.core.commands;

import com.example.botapp.core.MarkDown;
import com.example.botapp.core.UserMessageProcess;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {
    private final static String COMMAND = "/help";
    private final static String DESCRIPTION = "Display a list of commands";

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
        StringBuilder builder = new StringBuilder();
        builder.append("Available commands:\n");
        for (Command c : UserMessageProcess.commands()) {
            builder.append(c.command());
            builder.append(" - ");
            builder.append(c.description());
            builder.append("\n");
        }
        return new SendMessage(update.message().chat().id(), MarkDown.process(update.message().chat().id().toString()));
    }
}
