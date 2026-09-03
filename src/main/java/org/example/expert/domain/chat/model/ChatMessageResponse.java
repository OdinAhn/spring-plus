package org.example.expert.domain.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.expert.entity.ChatMessage;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessageResponse {
    private Long messageId;
    private String content;
    private Long senderId;
    private String senderName;
    private LocalDateTime createdAt;

    public ChatMessageResponse(ChatMessage message) {
        this.messageId = message.getId();
        this.content = message.getContent();
        this.senderId = message.getSender().getId();
        this.senderName = message.getSender().getNickname();
        this.createdAt = message.getCreatedAt();
    }
}
