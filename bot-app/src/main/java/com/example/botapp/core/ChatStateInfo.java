package com.example.botapp.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ChatStateInfo {
    private final Long chatId;
    private State chatState;
}
