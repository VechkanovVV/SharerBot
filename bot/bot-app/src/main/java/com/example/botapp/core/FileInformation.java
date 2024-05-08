package com.example.botapp.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FileInformation {
    String fileName;
    Long ownerId;
    Long receiverId;
}
