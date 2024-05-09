package com.example.botapp.core;

import lombok.Data;

@Data
public class FileInformation {
    String fileName;
    Long ownerId;
    Long receiverId;
}
