package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "log")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action;              // 예: "MANAGER_REGISTRATION_REQUEST"
    private Long requesterUserId;
    private Long targetUserId;
    private Long todoId;
    private String status;
    private String message;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    public Log(String action, Long requesterUserId, Long targetUserId, Long todoId, String status, String message) {
        this.action = action;
        this.requesterUserId = requesterUserId;
        this.targetUserId = targetUserId;
        this.todoId = todoId;
        this.status = status;
        this.message = message;
    }
}