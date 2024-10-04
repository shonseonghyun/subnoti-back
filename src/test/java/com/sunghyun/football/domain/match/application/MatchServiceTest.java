package com.sunghyun.football.domain.match.application;

import com.sunghyun.football.domain.match.domain.Match;
import com.sunghyun.football.domain.match.domain.enums.GenderRule;
import com.sunghyun.football.domain.match.domain.enums.MatchState;
import com.sunghyun.football.domain.match.domain.enums.MatchStatus;
import com.sunghyun.football.domain.match.domain.repository.MatchRepository;
import com.sunghyun.football.domain.match.infrastructure.SpringJpaMatchRepository;
import com.sunghyun.football.domain.member.domain.enums.MemberLevelType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest
@ActiveProfiles("local")
public class MatchServiceTest {

    @Autowired
    private MatchApplication matchApplication;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private SpringJpaMatchRepository springJpaMatchRepository;

    final Long stadiumNo=1L;
    final Long price = 10000L;
    final String startDt = "20240731";
    final String startTm = "0900";
    final Integer headCount =5;
    final Integer min = (headCount*2) + 2;
    final Integer max = headCount*3;
    final Long managerNo = 100L;

    @BeforeEach
//    @Transactional
    void setUp(){
        Match match = createMatch();
        matchRepository.save(match);
    }

    @AfterEach
    void setUpAfter(){
        matchRepository.deleteMatch(1L);
    }

    @Test
    @Transactional        //마지막 두줄을 위하여 필요(지연로딩 초기화 실패 방지)
    void 조회수_증가_통합테스트(){
        //given
//        Match match = createMatch();
//        springJpaMatchRepository.save(MatchEntity.from(match));

        final Long matchNo = 1L;
        matchApplication.getMatch(matchNo);

        Match selectedMatch = matchRepository.findByMatchNo(1L).get();
        Assertions.assertThat(selectedMatch.getViewCount()).isEqualTo(1);
    }

    @Test
//    @Transactional //이거 유무로 되고 안되고를 좌지우지 한다..
    void 조회수_증가_동시성테스트() {
        //given
//        Match match = createMatch();
//        springJpaMatchRepository.save(MatchEntity.from(match));

        final int maxThreadCnt= 14;
        final int countDownLatchCnt = 200;

        ExecutorService executorService = Executors.newFixedThreadPool(maxThreadCnt);
        CountDownLatch countDownLatch = new CountDownLatch(countDownLatchCnt);

        //when
        for(int i=0;i<countDownLatchCnt;i++){
            executorService.submit(()->{
                try{
                    //위와 다른 트랜잭션
                    matchApplication.getMatch(1L);
                }catch (Exception e){
                    System.out.println("에러 발생:"+e);
                } finally{
                    countDownLatch.countDown();
                }
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        Match selectedMatch = matchRepository.findByMatchNo(1L).get();
        Assertions.assertThat(selectedMatch.getViewCount()).isEqualTo(200);
    }

    private Match createMatch(){
        return  Match.builder()
                .stadiumNo(stadiumNo)
                .price(price)
                .startDt(startDt)
                .startTm(startTm)
                .headCount(headCount)
                .players(Collections.EMPTY_LIST)
                .levelRule(MemberLevelType.AMATEUR2)
                .genderRule(GenderRule.FEMALE)
                .matchState(MatchState.MATCH_REG_MANAGER_BEFORE)
                .matchStatus(MatchStatus.MATCH_START_BEFORE)
                .viewCount(0)
                .build();
    }
}
