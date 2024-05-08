package com.example.botapp.client.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UploadFileRequest {
    @JsonProperty("owner_id") Long ownerId;
    @JsonProperty("file_id") Long fileId;
    @JsonProperty("file_description") String fileDescription;
    @JsonProperty("file_name") String fileName;

}
