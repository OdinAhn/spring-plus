package org.example.expert.domain.comment.dto.response;

import lombok.Getter;
import org.example.expert.domain.user.dto.response.UserResponse;

@Getter
public class CommentSaveResponse {

    private final Long id;
    private final String contents;
    private final UserResponse user;

    private CommentSaveResponse(Builder builder) {
        this.id = builder.id;
        this.contents = builder.contents;
        this.user = builder.user;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String contents;
        private UserResponse user;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder contents(String contents) { this.contents = contents; return this; }
        public Builder user(UserResponse user) { this.user = user; return this; }

        public CommentSaveResponse build() {
            return new CommentSaveResponse(this);
        }
    }
}
