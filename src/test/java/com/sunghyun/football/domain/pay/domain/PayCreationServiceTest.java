package com.sunghyun.football.domain.pay.domain;

import com.sunghyun.football.domain.pay.domain.model.Pay;
import com.sunghyun.football.domain.pay.domain.model.PaymentMethod;
import com.sunghyun.football.domain.pay.domain.service.PayCreationService;
import com.sunghyun.football.global.exception.pay.exception.UnavailablePaymentMethodException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PayCreationServiceTest {
    private final Long memberNo = 1L;
    private final Long subscriptionNo = 1L;
    private final int rightAmount = 9900;
    private final PaymentMethod activePaymentMethod = PaymentMethod.KAKAO_PAY;
    private final PaymentMethod inactivePaymentMethod = PaymentMethod.CREDIT_CARD;


    @InjectMocks
    private PayCreationService target;

    @Test
    @DisplayName("사용 불가한 지불수단으로 Pay 도메인 생성 시 예외 응답")
    void 지불수단_검증_실패_시_예외_응답(){
        //when,then
        Assertions.assertThatThrownBy(()->target.createPayDomain(memberNo,subscriptionNo,rightAmount,inactivePaymentMethod))
                .isInstanceOf(UnavailablePaymentMethodException.class);
    }

    @Test
    @DisplayName("Pay 도메인 생성 성공")
    void Pay_도메인_생성_성공(){
        //when
        Pay pay = target.createPayDomain(memberNo,subscriptionNo,rightAmount,activePaymentMethod);

        //then
        Assertions.assertThat(pay).isNotNull();
    }

}