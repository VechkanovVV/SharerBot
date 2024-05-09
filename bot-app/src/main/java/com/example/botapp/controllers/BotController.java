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
      return ResponseEntity.ok().build();
    }
    @PostMapping("/send_file")
    public  ResponseEntity<Void> sendFile(FileSenderRequest request){
        return ResponseEntity.ok().build();
    }
}
