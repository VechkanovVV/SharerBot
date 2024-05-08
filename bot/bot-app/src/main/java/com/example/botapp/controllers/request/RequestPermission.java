package com.example.botapp.controllers.request;


import com.fasterxml.jackson.annotation.JsonProperty;


public record RequestPermission(@JsonProperty("message") String message, @JsonProperty("owner_id") Long ownerId, @JsonProperty("id") Long id) {}
