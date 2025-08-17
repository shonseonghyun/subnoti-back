package com.sunghyun.football.domain.history.infrastructure.repository;

import com.sunghyun.football.domain.history.infrastructure.entity.MailHistoryEntity;
import com.sunghyun.football.global.noti.type.NotiType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/*
        ┌──────────────────────────────────────────────┐
        │                @DataJpaTest                  │
        ├──────────────────────────────────────────────┤
        │ ✅ 로드됨                                     │
        │  ├─ JPA 핵심 Bean                             │
        │  │   ├─ EntityManager / EntityManagerFactory │
        │  │   ├─ TransactionManager                   │
        │  │   ├─ DataSource (H2 기본)                  │
        │  │   └─ JpaRepository 프록시                  │
        │                                              │
        │ ❌ 로드되지 않음                               │
        │  ├─ @Service / @Component / @Controller      │
        │  ├─ 직접 구현한 @Repository Bean               │
        │  └─ MVC, Security 등 JPA 외 AutoConfig        │
        └──────────────────────────────────────────────┘

*/
@DataJpaTest //JPA 관련 빈만 최소한으로 로드(JpaRepository 구현체,    EntityManager, DataSource, TransactionManager 등 JPA 핵심 Bean)
//@Import(TestConfig.class) //왜 직접 추가해줘야 할까? 이유는 @DataJpaTest는 JPA 관련 빈만 최소한으로 로드하는 "슬라이스 테스트"이기에 @Repository,@Service 등 이런 어노테이션은 로드하지 않는다.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MailHistoryRepositoryTest {

//    @Autowired
//    private MailHistoryRepository mailHistoryRepository;

    @Autowired
    private SpringJpaMailHistoryRepository springJpaMailHistoryRepository;


    private final NotiType notiType = NotiType.NOTI_FREE_SUB;
    private final String email = "abc@naver.com";
    private final String subject = "subject";
    private final String content = "content";
    private final String sendDt = "20250701";
    private final String sendTm = "170000";

    @Test
    void mailHistoryRepository가Null이아님(){
//        assertThat(mailHistoryRepository).isNotNull();
        assertThat(springJpaMailHistoryRepository).isNotNull();
    }

    @Test
    void mailHistoryEntity_저장_성공(){
        //given
        MailHistoryEntity newMailHistoryEntity = createNewMailHistoryEntity();

        //when
        MailHistoryEntity savedMailHistoryEntity = springJpaMailHistoryRepository.save(newMailHistoryEntity);

        //then
        Assertions.assertThat(savedMailHistoryEntity.getHistoryNo()).isNotNull();
        Assertions.assertThat(savedMailHistoryEntity.getEmail()).isEqualTo(email);
        Assertions.assertThat(savedMailHistoryEntity.getSubject()).isEqualTo(subject);
        Assertions.assertThat(savedMailHistoryEntity.getContent()).isEqualTo(content);
        Assertions.assertThat(savedMailHistoryEntity.getSendDt()).isEqualTo(sendDt);
        Assertions.assertThat(savedMailHistoryEntity.getSendTm()).isEqualTo(sendTm);
        Assertions.assertThat(savedMailHistoryEntity.getNotiType()).isEqualTo(notiType);
    }

    @Test
    void mailHistoryEntity_조회_성공(){
        //given
        MailHistoryEntity newMailHistoryEntity = createNewMailHistoryEntity();
        MailHistoryEntity savedMailHistoryEntity = springJpaMailHistoryRepository.save(newMailHistoryEntity);

        //when
        MailHistoryEntity selectedMailHistoryEntity = springJpaMailHistoryRepository.findByEmail(email);

        //then
        Assertions.assertThat(savedMailHistoryEntity.getHistoryNo()).isEqualTo(selectedMailHistoryEntity.getHistoryNo());
    }

    @Test
    void mailHistoryEntity_조회_실패_null리턴_By_존재하지않는MemberNo(){
        //given
        final String notExistEmail = "notexist@naver.com";

        //when
        MailHistoryEntity selectedMailHistoryEntity = springJpaMailHistoryRepository.findByEmail(notExistEmail);

        //then
        Assertions.assertThat(selectedMailHistoryEntity).isNull();
    }

    private MailHistoryEntity createNewMailHistoryEntity(){
        return  new MailHistoryEntity(
                null,
                email,
                sendDt,
                sendTm,
                notiType,
                subject,
                content
        );
    }
}