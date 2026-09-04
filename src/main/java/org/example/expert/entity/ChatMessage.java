package org.example.expert.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.expert.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    private User sender;

    private String content;

    private LocalDateTime createdAt;

    protected ChatMessage() {
    }

    private ChatMessage(Builder builder) {
        this.sender = builder.sender;
        this.chatRoom = builder.chatRoom;
        this.content = builder.content;
        this.createdAt = LocalDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private User sender;
        private ChatRoom chatRoom;
        private String content;

        public Builder sender(User sender) { this.sender = sender; return this; }
        public Builder chatRoom(ChatRoom chatRoom) { this.chatRoom = chatRoom; return this; }
        public Builder content(String content) { this.content = content; return this; }

        public ChatMessage build() {
            return new ChatMessage(this);
        }
    }
}
