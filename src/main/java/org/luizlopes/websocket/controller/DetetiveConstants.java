package org.luizlopes.websocket.controller;

public interface DetetiveConstants {

    interface Resource {
        String MESSAGE_BROKER = "/message";
        String COMMAND_BROKER = "/command";
        String INFO_BROKER = "/info";
    }

    interface Destination {
        // QUEUES
        String CHAT_QUEUE = "/queue/chat";
        String COMMAND_QUEUE = "/queue/command";
        String INFO_QUEUE = "/queue/info";

        // TOPICS
        String CHAT_TOPIC = "/topic/chat";
        String INFO_TOPIC = "/topic/info";
    }

}
