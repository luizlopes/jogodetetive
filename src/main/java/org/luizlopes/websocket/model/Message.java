package org.luizlopes.websocket.model;

import lombok.Data;

@Data
public class Message {

    private String sender;
    private String recipient;
    private String text;

    public boolean senderNotIsRecipient() {
        return !sender.equals(recipient);
    }
}
