package com.sunghyun.football.domain.subscription.infrastructure.repository;

import com.sunghyun.football.domain.subscription.domain.model.SubscriptionPlan;
import com.sunghyun.football.domain.subscription.infrastructure.entity.SubscriptionEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class SubscriptionRepositoryTest {
    final Long memberNo = 1L;
    final SubscriptionPlan subscriptionPlan = SubscriptionPlan.PLAN_990;
    final String startDt = "20250807";
    final String endDt = "20250907";

    @Autowired
    private SpringJpaSubscriptionRepository springJpaSubscriptionRepository;

    @Test
    void springJpaSubscriptionRepository가_Null_아님(){
        assertThat(springJpaSubscriptionRepository).isNotNull();
    }

    @Test
    void 저장(){
        //given
        SubscriptionEntity subscriptionEntity = createSubscriptionEntity();

        //when
        SubscriptionEntity savedSuscriptionEntity= springJpaSubscriptionRepository.save(subscriptionEntity);

        //then
        assertThat(savedSuscriptionEntity).isNotNull();
    }

    @Test
    void 조회_실패_시_null_리턴(){
        //given
        final String today= "20251010";
        springJpaSubscriptionRepository.save(createSubscriptionEntity());

        //when
        SubscriptionEntity subscriptionEntity = springJpaSubscriptionRepository.findValidByMemberNo(memberNo,today);

        //then
        assertThat(subscriptionEntity).isNull();
    }

    @Test
    void 조회_성공(){
        //given
        final String today= "20250810";
        springJpaSubscriptionRepository.save(createSubscriptionEntity());

        //when
        SubscriptionEntity subscriptionEntity = springJpaSubscriptionRepository.findValidByMemberNo(memberNo,today);

        //then
        assertThat(subscriptionEntity).isNotNull();
        assertThat(subscriptionEntity.getSubscriptionNo()).isNotNull();
        assertThat(subscriptionEntity.getSubscriptionPlan()).isEqualTo(subscriptionPlan);
    }

    @Test
    void 삭제(){
        //given
        final String today= "20250810";
        SubscriptionEntity savedSubscriptionEntity = springJpaSubscriptionRepository.save(createSubscriptionEntity());

        //when
        springJpaSubscriptionRepository.deleteById(savedSubscriptionEntity.getSubscriptionNo());

        //then
        SubscriptionEntity subscriptionEntity = springJpaSubscriptionRepository.findValidByMemberNo(memberNo,today);
        assertThat(subscriptionEntity).isNull();
    }



    private SubscriptionEntity createSubscriptionEntity(){
        return SubscriptionEntity.builder()
                .memberNo(memberNo)
                .subscriptionPlan(subscriptionPlan)
                .remainingCount(0)
                .startDt(startDt)
                .endDt(endDt)
                .build();
    }

}