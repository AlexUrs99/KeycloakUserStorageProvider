package com.appsdeveloperblog.keycloak;

import com.appsdeveloperblog.keycloak.user.model.User;
import com.appsdeveloperblog.keycloak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Bootstrapper implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Value("${app.bootstrap}")
    private Boolean bootstrap;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (bootstrap) {

            userRepository.deleteAll();

            User user = User.builder()
                    .username("normal-user")
                    .password(encoder.encode("pass"))
                    .email("some_email@gmail.com")
                    .firstName("MyFirstName")
                    .lastName("MyLastName")
                    .build();
            userRepository.save(user);
        }
    }
}
