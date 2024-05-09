package com.example.botapp.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FileResponse(@JsonProperty("owner_id") Long ownerId, @JsonProperty("file_description") String fileDescription, @JsonProperty("file_name") String fileName){}
