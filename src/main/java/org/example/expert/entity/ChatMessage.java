package org.example.expert.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
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

    public ChatMessage(User sender, ChatRoom chatRoom, String content) {
        this.sender = sender;
        this.chatRoom = chatRoom;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }
}