package com.example.botapp.core;

import com.pengrad.telegrambot.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserMessageProcess {
    private final TelegramBot bot;

    @Autowired
    public UserMessageProcess(TelegramBot bot){
        this.bot = bot;
    }
}
