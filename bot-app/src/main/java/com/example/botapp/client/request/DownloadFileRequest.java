package com.example.botapp.client.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DownloadFileRequest {
    @JsonProperty("file_name") String fileName;
    @JsonProperty("owner_id") Long ownerId;
    @JsonProperty("receiver_id") Long receiverId;
}
