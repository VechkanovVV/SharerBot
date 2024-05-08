package com.example.botapp.core;

import com.example.botapp.configuration.BotConfiguration;
import com.pengrad.telegrambot.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class Bot {
    private final TelegramBot bot;

    @Autowired
    public Bot(BotConfiguration botConfig) {
        this.bot = botConfig.telegramBot();
    }
}
