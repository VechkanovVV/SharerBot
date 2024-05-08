package com.example.botapp.controllers.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.RestController;

public record FileSenderRequest(@JsonProperty("owner_id") Long ownerId, @JsonProperty("id") Long id, @JsonProperty("file_id") Long fileId) {
}
