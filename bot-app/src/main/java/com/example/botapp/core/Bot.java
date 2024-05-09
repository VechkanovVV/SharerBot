package com.example.botapp.core;

import com.example.botapp.configuration.BotConfiguration;
import com.example.botapp.core.commands.Command;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
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
    private final static String ERROR = "Error while sending message: ";

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

    public void sendRequest(Long owner_id,Long rec_id,String file_name){

    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            SendMessage message;
            try {
                message = userMessageProcess.process(update);
            } catch (UnsupportedOperationException e) {
                message = new SendMessage(update.message().chat().id(),MarkDown.process("Sorry, I don't understand you. Try /help to see list of commands"));
            }
            if (message != null) {
                message.parseMode(ParseMode.MarkdownV2);
                BaseResponse response = bot.execute(message);
                if (!response.isOk()) {
                    log.error(ERROR + response.description());
                } else {
                    messageCounter.increment();
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    public void close() throws Exception {
        bot.removeGetUpdatesListener();
    }
}
