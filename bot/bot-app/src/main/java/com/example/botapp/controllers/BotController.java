package com.example.botapp.controllers;


import com.example.botapp.controllers.request.HandlerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BotController {
    //private final Bot bot;
    @PostMapping("/request_permission")
    public ResponseEntity<Void> requestPermission(HandlerRequest request){
      return ResponseEntity.ok().build();
    }
}
