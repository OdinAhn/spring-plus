package org.example.expert.interceptor;

import lombok.Getter;
import org.example.expert.domain.user.entity.User;

import java.security.Principal;

@Getter
public class AuthenticatedUser implements Principal {

    private final User user;
    private final String name;

    public AuthenticatedUser(User user) {
        this.user = user;
        this.name = user.getNickname();
    }

    // Principal에서 User 꺼내기
    public static User fromPrincipal(Principal principal) {
        return ((AuthenticatedUser) principal).getUser();
    }
}