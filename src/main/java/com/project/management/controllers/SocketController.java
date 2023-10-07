package com.project.management.controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {

    @MessageMapping("/hardware/button/{token}")
    @SendTo("/ws/topic/hardware/button/{token}")
    public String sendMessage(@DestinationVariable String token, String json) {
        System.out.println(json);
        return token;
    }
}
