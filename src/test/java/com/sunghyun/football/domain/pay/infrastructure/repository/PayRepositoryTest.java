package com.sunghyun.football.domain.pay.infrastructure.repository;

import com.sunghyun.football.domain.pay.domain.model.PaymentMethod;
import com.sunghyun.football.domain.pay.infrastructure.entity.PayEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PayRepositoryTest {
    private final Long memberNo = 1L;
    private final Long subscriptionNo = 1L;
    private final int rightAmount = 9900;
    private final int wrongAmount = 9;
    private final String createdDt= "202508";
    private final PaymentMethod activePaymentMethod = PaymentMethod.KAKAO_PAY;
    private final PaymentMethod inactivePaymentMethod = PaymentMethod.CREDIT_CARD;

    @Autowired
    private SpringJpaPayRepository springJpaPayRepository;

    @Test
    void springJpaPayRepository가_Null_아님(){
        assertThat(springJpaPayRepository).isNotNull();
    }

    @Test
    void payEntity_save_성공(){
        //given
        PayEntity payEntity = createPayEntity();

        //when
        PayEntity savedPayEntity = springJpaPayRepository.save(payEntity);

        //then
//        assertThat(savedPayEntity.getPayNo()).isEqualTo(1L); //2L일것이다. 이유는 Db의 시퀀스가 이미 그 전에 insert되면서 +1이 되었기에.
        assertThat(savedPayEntity.getPayNo()).isNotNull();

    }

    @Test
    void payEntity_조회_실패_시_null_리턴(){
        //when
        PayEntity payEntity = springJpaPayRepository.findByMemberNo(memberNo);

        //then
        assertThat(payEntity).isNull();
    }

    @Test
    void payEntity_조회_성공(){
        //given
        PayEntity savedPayEntity = springJpaPayRepository.save(createPayEntity());

        //when
        PayEntity payEntity = springJpaPayRepository.findByMemberNo(memberNo);

        //then
        assertThat(payEntity).isNotNull();
    }


    @Test
    void payEntity_삭제(){
        //given
        PayEntity savedPayEntity = springJpaPayRepository.save(createPayEntity());
        //when
        springJpaPayRepository.deleteById(savedPayEntity.getPayNo());

        //then
        PayEntity payEntity = springJpaPayRepository.findByMemberNo(memberNo);
        assertThat(payEntity).isNull();
    }
    
    private PayEntity createPayEntity() {
        return PayEntity.builder()
                .memberNo(memberNo)
                .subscriptionNo(subscriptionNo)
                .amount(rightAmount)
                .paymentMethod(activePaymentMethod)
                .build();
    }
}