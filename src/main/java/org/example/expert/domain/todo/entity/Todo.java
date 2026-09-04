package org.example.expert.domain.todo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "todos")
public class Todo extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String contents;
    private String weather;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "todo", cascade = CascadeType.PERSIST)
    private List<Manager> managers = new ArrayList<>();

    protected Todo() {
    }

    private Todo(Builder builder) {
        this.title = builder.title;
        this.contents = builder.contents;
        this.weather = builder.weather;
        this.user = builder.user;
        this.managers.add(Manager.builder().user(builder.user).todo(this).build());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private String contents;
        private String weather;
        private User user;

        public Builder title(String title) { this.title = title; return this; }
        public Builder contents(String contents) { this.contents = contents; return this; }
        public Builder weather(String weather) { this.weather = weather; return this; }
        public Builder user(User user) { this.user = user; return this; }

        public Todo build() {
            return new Todo(this);
        }
    }
}
