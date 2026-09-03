package org.example.expert.interceptor;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String bearerToken = accessor.getFirstNativeHeader("Authorization");
            String token = jwtUtil.substringToken(bearerToken);
            Claims claims = jwtUtil.extractClaims(token);
            Long userId = Long.parseLong(claims.getSubject());

            User user = userRepository.findById(userId)
                .orElseThrow();

            accessor.setUser(new AuthenticatedUser(user));
        }

        return message;
    }
}
