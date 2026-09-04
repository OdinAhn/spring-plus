package org.example.expert.config.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RedisChatMessage {
    private Long roomId;
    private Long senderId;
    private String senderName;
    private String content;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long roomId;
        private Long senderId;
        private String senderName;
        private String content;

        public Builder roomId(Long roomId) { this.roomId = roomId; return this; }
        public Builder senderId(Long senderId) { this.senderId = senderId; return this; }
        public Builder senderName(String senderName) { this.senderName = senderName; return this; }
        public Builder content(String content) { this.content = content; return this; }

        public RedisChatMessage build() {
            return new RedisChatMessage(roomId, senderId, senderName, content);
        }
    }
}