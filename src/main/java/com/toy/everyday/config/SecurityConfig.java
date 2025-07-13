package com.toy.everyday.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                // 아래 3줄 추가: Swagger와 H2 콘솔은 누구나 접근 가능하도록 허용
                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/data/*")).permitAll()
                // 아래는 일반적으로 허용하는 경로들
                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll() // 예: 메인 페이지, css/js 등
                .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
            )
            .csrf((csrf) -> csrf
                // 기존 h2-console 경로에 더해서 API 경로도 추가
                .ignoringRequestMatchers(
                    new AntPathRequestMatcher("/h2-console/**"),
                    new AntPathRequestMatcher("/api/**") // /api/로 시작하는 모든 경로는 CSRF 검증 안 함
                )
            )
            .headers((headers) -> headers
                .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
            )
            .formLogin((formLogin) -> formLogin
                .loginPage("/member/login") // 로그인 페이지 경로 수정
                .defaultSuccessUrl("/")
            )
            .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")) // 로그아웃 페이지 경로 수정
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
            );
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}