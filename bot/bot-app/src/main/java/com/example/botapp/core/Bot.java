package com.example.botapp.core;

import com.example.botapp.configuration.BotConfiguration;
import com.example.botapp.core.commands.Command;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import com.pengrad.telegrambot.request.SetMyCommands;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class Bot implements AutoCloseable, UpdatesListener {
    private final TelegramBot bot;
    private final UserMessageProcess userMessageProcess;
    private final Counter messageCounter;

    @Autowired
    public Bot(BotConfiguration botConfig, UserMessageProcess userMessageProcess, MeterRegistry registry) {
        this.bot = botConfig.telegramBot();
        this.userMessageProcess = userMessageProcess;
        messageCounter = Counter.builder("processed_messages")
                .description("The count of messages sent by users that have been processed.")
                .register(registry);
    }
    @PostConstruct
    public void init() {
        setCommands();
        bot.setUpdatesListener(this);
    }
    private void setCommands() {
        List<BotCommand> botCommands = new ArrayList<>();
        for (Command c : UserMessageProcess.commands()) {
            botCommands.add(c.toApiCommand());
        }
        SetMyCommands setMyCommands = new SetMyCommands(botCommands.toArray(new BotCommand[0]));
        bot.execute(setMyCommands);
    }

    @Override
    public int process(List<Update> updates) {
        return 0;
    }

    @Override
    public void close() throws Exception {
        bot.removeGetUpdatesListener();
    }
}
