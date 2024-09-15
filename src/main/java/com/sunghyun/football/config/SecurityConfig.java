package com.sunghyun.football.config;

import com.sunghyun.football.domain.member.domain.enums.Role;
import com.sunghyun.football.domain.member.infrastructure.auth.custom.filter.CustomAuthenticationFilter;
import com.sunghyun.football.domain.member.infrastructure.auth.custom.filter.CustomJwtAuthenticationFilter;
import com.sunghyun.football.domain.member.infrastructure.auth.custom.provider.CustomAuthenticationProvider;
import com.sunghyun.football.domain.member.infrastructure.auth.jwt.JwtProvider;
import jakarta.servlet.GenericFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
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
    private final JwtProvider jwtProvider;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;


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
        ProviderManager providerManager = (ProviderManager)authenticationConfiguration.getAuthenticationManager();
        //주석 처리 => 프레임워크에서 스프링 시큐리티 초기 설정 시 AuthenticationProvider를 상속받은 클래스의 등록된 빈이 있는지 확인 후 등록하는 과정을 내부적으로 진행한다. (AuthenticationProvider 빈 등록 위에서 진행)
        //AbstractConfiguredSecurityBuilder, InitializeUserDetailsManagerConfigurer 디버깅
//        provid
//        erManager.getProviders().add(this.customAuthenticationProvider());
        return providerManager;
    }

//    @Bean //applicationFilterChain
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
    public GenericFilter jwtAuthenticationFilter(){
        return new CustomJwtAuthenticationFilter(userDetailsService,jwtProvider);
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
//                                .requestMatchers("/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE,"/api/v1/member/**").hasAuthority(Role.USER.name())
                                .requestMatchers("/api/v1/manager/**").hasAnyAuthority(Role.MANAGER.name())
                                .requestMatchers("/api/v1/admin/**").hasAnyAuthority(Role.ADMIN.name())
                                .anyRequest().permitAll()
                )
                //ExceptionTranslationFilter.handleAccessDeniedException 확인하면 아래 주석 내용이 보인다.
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(authenticationEntryPoint)) //익명의사용자가 권한필요한 페이지 접근 거절 시 후처리
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler)) //접근을 위한 특정 권한이 필요한 페이지에 익명의 사용자가 아니면서 권한이 없는 경우
                .addFilterAt(this.abstractAuthenticationProcessingFilter(this.authenticationManager()),UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(this.jwtAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class)
                ;

        return http.build();
    }
}
