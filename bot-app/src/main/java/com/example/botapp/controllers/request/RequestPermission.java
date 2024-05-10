package com.example.botapp.controllers.request;


import com.fasterxml.jackson.annotation.JsonProperty;


public record RequestPermission(@JsonProperty("file_name") String fileName, @JsonProperty("owner_id") Long ownerId, @JsonProperty("id") Long id) {}
