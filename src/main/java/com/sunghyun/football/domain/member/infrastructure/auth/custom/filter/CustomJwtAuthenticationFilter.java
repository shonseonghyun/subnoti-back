package com.sunghyun.football.domain.member.infrastructure.auth.custom.filter;

import com.sunghyun.football.domain.member.infrastructure.auth.jwt.JwtProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;

import java.io.IOException;

//@Component //해당 어노테이션을 선언한다면 SecurityFilterChain이 아닌 applicationfilterChain에도 추가되게 될것이다.
@Slf4j
@RequiredArgsConstructor
public class CustomJwtAuthenticationFilter extends GenericFilter {
    private final UserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String accessToken = ((HttpServletRequest)request).getHeader("authorization");
        if(!StringUtils.hasText(accessToken)){
            log.info("accessToke is Empty");
            filterChain.doFilter(request,response);
            //토큰이 없는 경우 이 밑의 로직은 타면 안되기에 return 추가
            //이 곳을 탈 경우 인가권한검사 필터에서 권한은 익명권한으로 진행되게 될것이다.
            return ;
        }

        String email = jwtProvider.getEmail(accessToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        //해당 두 줄이 없다면 어떤 결과를 얻으며, 어디를 탈까? ex. 권한 없다 거절받으며,
        //해당 두 줄이 있다면 어떻게 바뀔까?
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(UsernamePasswordAuthenticationToken.authenticated(email,null,userDetails.getAuthorities()));
        filterChain.doFilter(request,response);
    }
}
