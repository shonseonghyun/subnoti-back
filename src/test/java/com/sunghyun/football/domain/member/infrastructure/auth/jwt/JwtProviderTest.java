package com.sunghyun.football.domain.member.infrastructure.auth.jwt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local") //..이게 있어야 되네?
class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    void 토큰_생성_확인(){
        final String email = "d@aver.com";
        String token = "Bearer " + jwtProvider.generateAccessToken(email);

        Assertions.assertThat(jwtProvider.getEmail(token)).isEqualTo(email);
    }
}