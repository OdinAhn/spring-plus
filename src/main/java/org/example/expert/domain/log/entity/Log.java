package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "log")
@EntityListeners(AuditingEntityListener.class)
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

    protected Log() {
    }

    private Log(Builder builder) {
        this.action = builder.action;
        this.requesterUserId = builder.requesterUserId;
        this.targetUserId = builder.targetUserId;
        this.todoId = builder.todoId;
        this.status = builder.status;
        this.message = builder.message;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String action;
        private Long requesterUserId;
        private Long targetUserId;
        private Long todoId;
        private String status;
        private String message;

        public Builder action(String action) { this.action = action; return this; }
        public Builder requesterUserId(Long requesterUserId) { this.requesterUserId = requesterUserId; return this; }
        public Builder targetUserId(Long targetUserId) { this.targetUserId = targetUserId; return this; }
        public Builder todoId(Long todoId) { this.todoId = todoId; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder message(String message) { this.message = message; return this; }

        public Log build() {
            return new Log(this);
        }
    }
}
