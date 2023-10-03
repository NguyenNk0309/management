package com.project.management.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {

    @MessageMapping("ws/hardware")
    @SendTo("ws/topic/hardware")
    public String sendMessage(String message) {
        return message;
    }
}
