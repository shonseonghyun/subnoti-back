package com.sunghyun.football.config;

import com.sunghyun.football.domain.member.infrastructure.auth.custom.filter.CustomAuthenticationFilter;
import com.sunghyun.football.domain.member.infrastructure.auth.custom.provider.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
    private final String loginUrl = "/api/v1/auth/login";
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserDetailsService userDetailsService;
    private final AuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final AuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider customAuthenticationProvider(){
        return new CustomAuthenticationProvider(userDetailsService,passwordEncoder());
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        ProviderManager providerManager = (ProviderManager) authenticationConfiguration.getAuthenticationManager();
        //주석 처리 => 프레임워크에서 스프링 시큐리티 초기 설정 시 AuthenticationProvider를 상속받은 클래스의 등록된 빈이 있는지 확인 후 등록하는 과정을 내부적으로 진행한다. (AuthenticationProvider 빈 등록 위에서 진행)
        //AbstractConfiguredSecurityBuilder, InitializeUserDetailsManagerConfigurer 디버깅
//        providerManager.getProviders().add(this.customAuthenticationProvider());
        return providerManager;
    }

    @Bean
    public AbstractAuthenticationProcessingFilter abstractAuthenticationProcessingFilter(AuthenticationManager authenticationManager) {
        AbstractAuthenticationProcessingFilter customAuthenticationFilter = new CustomAuthenticationFilter(
//                requestMatcher(),
                loginUrl,
                authenticationManager,
                customAuthenticationSuccessHandler,
                customAuthenticationFailureHandler
        );

        return customAuthenticationFilter;
    }

//    @Bean
//    public RequestMatcher requestMatcher(){
//        return AntPathRequestMatcher.antMatcher(HttpMethod.POST,loginUrl);
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                //h2 허용 설정
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(authorizeRequest-> authorizeRequest
                                .requestMatchers("/**").permitAll()
//                                .anyRequest().permitAll()
                )
                .addFilterAt(this.abstractAuthenticationProcessingFilter(this.authenticationManager()),UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
