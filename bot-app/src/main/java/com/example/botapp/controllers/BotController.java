package com.example.botapp.controllers;


import com.example.botapp.controllers.request.FileSenderRequest;
import com.example.botapp.controllers.request.RequestPermission;
import com.example.botapp.core.Bot;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BotController {
    private final Bot bot;

    @PostMapping("/request_permission")
    public ResponseEntity<Void> requestPermission(@NotNull @RequestBody RequestPermission request) {
        String fileName = request.fileName();
        Long ownerId = request.ownerId();
        Long recId = request.id();
        bot.sendRequest(ownerId, recId, fileName);
      return ResponseEntity.ok().build();
    }

    @PostMapping("/send_file")
    public  ResponseEntity<Void> sendFile(@NotNull @RequestBody FileSenderRequest request) {
        String fileId = request.fileId();
        Long ownerId = request.ownerId();
        Long recId = request.id();
        bot.sendFile(fileId, ownerId, recId);
        return ResponseEntity.ok().build();
    }
}
