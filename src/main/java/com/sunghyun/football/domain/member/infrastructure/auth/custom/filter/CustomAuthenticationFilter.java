package com.sunghyun.football.domain.member.infrastructure.auth.custom.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.football.domain.member.application.dto.MemberLoginReqDto;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.member.auth.HttpMethodNotSupportException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;


//@Component
//@RequiredArgsConstructor
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private ObjectMapper om =  new ObjectMapper();

    public CustomAuthenticationFilter(
//            final RequestMatcher requestMatcher,
            final String filterProcessesUrl,
            final AuthenticationManager authenticationManager,
            final AuthenticationSuccessHandler authenticationSuccessHandler,
            final AuthenticationFailureHandler authenticationFailureHandler
            ){
//        RequestMatcher requestMatcher = AntPathRequestMatcher.antMatcher(HttpMethod.POST,defaultFilterProcessesUrl);
//        super(requestMatcher,authenticationManager);
        super(filterProcessesUrl,authenticationManager);
        this.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        this.setAuthenticationFailureHandler(authenticationFailureHandler);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException , IOException {
        if(!isPost(request.getMethod())){
//             throw new UsernameNotFoundException("8");
            throw new HttpMethodNotSupportException(ErrorCode.HTTP_METHOD_NOT_SUPPORT.getCode());
        }

        MemberLoginReqDto memberLoginReqDto = om.readValue(request.getReader(), MemberLoginReqDto.class);

        final String email = memberLoginReqDto.getEmail();
        final String pwd = memberLoginReqDto.getPwd();

        //인증되지 않은 토큰
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(email,pwd);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,FilterChain filterChain, Authentication authResult) throws IOException, ServletException {
        //보니깐 스프링시큐리티 컨텍스트에 작업을 하는데 공부가 필요하다.

        this.getSuccessHandler().onAuthenticationSuccess(request,response,authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        this.getFailureHandler().onAuthenticationFailure(request,response,failed);
    }

    private boolean isPost(String method){
        return HttpMethod.POST.matches(method);
    }
}
