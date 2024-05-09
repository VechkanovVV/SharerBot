package com.example.botapp.client.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SetPermissionRequest(@JsonProperty("owner_id") Long id, @JsonProperty("receiver_id") Long receiverId, @JsonProperty("file_name") String fileName) {}
