package com.example.botapp.client;

import com.example.botapp.client.request.UploadFileRequest;
import com.example.botapp.client.response.FilesListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class BackendClient {
    private final WebClient backendClient;

    @Autowired
    public BackendClient(WebClient backendClient){
        this.backendClient = backendClient;
    }
    public FilesListResponse findFile(String fileName){
        try {
            return backendClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .pathSegment("find_file")
                            .queryParam("file_name", fileName)
                            .build())
                    .retrieve()
                    .bodyToMono(FilesListResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
            return new FilesListResponse(null);
        }
    }
    public void uploadFile(UploadFileRequest uploadFile){
        backendClient.post()
                .uri("upload_file")
                .bodyValue(uploadFile)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void setPermission()

}
