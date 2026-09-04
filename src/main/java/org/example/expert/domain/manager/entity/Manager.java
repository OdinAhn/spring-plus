package org.example.expert.domain.manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;

@Getter
@Entity
@Table(name = "managers")
public class Manager {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 일정 만든 사람 id
    private User user;
    @ManyToOne(fetch = FetchType.LAZY) // 일정 id
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;

    protected Manager() {
    }

    private Manager(Builder builder) {
        this.user = builder.user;
        this.todo = builder.todo;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private User user;
        private Todo todo;

        public Builder user(User user) { this.user = user; return this; }
        public Builder todo(Todo todo) { this.todo = todo; return this; }

        public Manager build() {
            return new Manager(this);
        }
    }
}
