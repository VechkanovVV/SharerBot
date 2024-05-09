package com.example.botapp.controllers.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FileSenderRequest(@JsonProperty("owner_id") Long ownerId, @JsonProperty("id") Long id, @JsonProperty("file_id") String fileId) {
}
