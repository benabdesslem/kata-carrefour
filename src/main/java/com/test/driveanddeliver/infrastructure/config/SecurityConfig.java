package com.test.driveanddeliver.infrastructure.config;

import com.test.driveanddeliver.infrastructure.repository.JpaUserAuthorityRepository;
import com.test.driveanddeliver.infrastructure.repository.JpaUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                       JwtTokenProvider tokenProvider,
                                                       ReactiveAuthenticationManager reactiveAuthenticationManager) {

        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(reactiveAuthenticationManager)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(it -> it
                        .pathMatchers("/api/auth/login").permitAll()
                        .pathMatchers("/api/**").authenticated()
                        .anyExchange().permitAll()
                )
                .addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
                .build();


    }

    @Bean
    public ReactiveUserDetailsService userDetailsService(JpaUserRepository userRepository, JpaUserAuthorityRepository userAuthorityRepository) {

        return login -> userRepository.findByLogin(login)
                .flatMap(u -> userAuthorityRepository.findByUserId(u.getId())
                        .collectList()
                        .map(authorities -> User.withUsername(u.getLogin())
                                .password(u.getPassword())
                                .authorities(authorities.stream()
                                        .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                                        .toArray(SimpleGrantedAuthority[]::new))
                                .accountExpired(!u.isActivated())
                                .credentialsExpired(!u.isActivated())
                                .disabled(!u.isActivated())
                                .accountLocked(!u.isActivated())
                                .build())
                );
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
                                                                       PasswordEncoder passwordEncoder) {
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }


    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
