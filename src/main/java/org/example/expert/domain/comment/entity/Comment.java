package org.example.expert.domain.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;

@Getter
@Entity
@Table(name = "comments")
public class Comment extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;

    protected Comment() {
    }

    private Comment(Builder builder) {
        this.contents = builder.contents;
        this.user = builder.user;
        this.todo = builder.todo;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String contents;
        private User user;
        private Todo todo;

        public Builder contents(String contents) { this.contents = contents; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder todo(Todo todo) { this.todo = todo; return this; }

        public Comment build() {
            return new Comment(this);
        }
    }
}
