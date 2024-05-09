package com.example.botapp.controllers;


import com.example.botapp.controllers.request.FileSenderRequest;
import com.example.botapp.controllers.request.RequestPermission;
import com.example.botapp.core.Bot;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BotController {
    private final Bot bot;
    @PostMapping("/request_permission")
    public ResponseEntity<Void> requestPermission(RequestPermission request){
        //JsonProperty("file_name") String fileName, @JsonProperty("owner_id") Long ownerId, @JsonProperty("id")
        String file_name = request.fileName();;
        Long owner_id = request.ownerId();
        Long rec_id = request.id();
        bot.sendRequest(owner_id, rec_id, file_name);
      return ResponseEntity.ok().build();
    }
    @PostMapping("/send_file")
    public  ResponseEntity<Void> sendFile(FileSenderRequest request){
        return ResponseEntity.ok().build();
    }
}
