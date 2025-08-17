package com.sunghyun.football.domain.subscription.application;

import com.sunghyun.football.domain.subscription.application.port.out.SubscriptionAdapter;
import com.sunghyun.football.domain.subscription.domain.model.Subscription;
import com.sunghyun.football.domain.subscription.domain.model.SubscriptionPlan;
import com.sunghyun.football.domain.subscription.domain.repository.SubscriptionRepository;
import com.sunghyun.football.domain.subscription.domain.service.SubscriptionCreationService;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.subscription.NoRemainingSubscriptionCountException;
import com.sunghyun.football.global.exception.exceptions.subscription.SubscriptionAlreadyExistsException;
import com.sunghyun.football.global.exception.exceptions.subscription.SubscriptionNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionAdapterTest {

    @InjectMocks
    private SubscriptionAdapter target;

    @Mock
    private SubscriptionCreationService subscriptionCreationService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private Subscription subscription;

    @Test
    @DisplayName("도메인 생성 역할을 맡은 도메인 서비스에서 예외 발생 시 전파")

    void 도메인_생성_역할_맡은_도메인_서비스에서_예외_발생_시_전파(){
        //given
        final Long memberNo= 1L;
        final int amount = 9900;
        final String today = "20250807";

        when(subscriptionCreationService.createSubscriptionDomain(any(),anyInt(),any()))
                .thenThrow(new SubscriptionAlreadyExistsException(ErrorCode.SUBSCRIPTION_ALREADY_EXIST_THIS_MONTH));
        ;

        //when,then
        assertThatThrownBy(()->target.createSubscription(memberNo,amount,today))
                .isInstanceOf(SubscriptionAlreadyExistsException.class);
    }


    @Test
    void 구독권_저장_성공(){
        //given
        final Long memberNo= 1L;
        final SubscriptionPlan subscriptionPlan = SubscriptionPlan.PLAN_4900;
        final String today = "20250807";
        final int amount = 9900;

        Subscription subscription = Subscription.create(memberNo,subscriptionPlan);
        when(subscriptionCreationService.createSubscriptionDomain(any(),anyInt(),anyString()))
                .thenReturn(subscription);

        when(subscriptionRepository.save(any(Subscription.class)))
                .thenReturn(subscription);

        //when
        target.createSubscription(memberNo,amount,today);

        //then
        verify(subscriptionRepository,times(1)).save(any(Subscription.class));
    }

    @Test
    @DisplayName("구독권 존재하지 않을 시 예외 전파")
    void 구독권_존재하지_시_않을_시_예외_전파(){
        //given
        final Long memberNo = 1L;
        final String today= "20250807";

        when(subscriptionRepository.findValidSubscriptionByMemberNoAndToday(memberNo,today))
                .thenThrow(new SubscriptionNotFoundException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        //when,then
        assertThatThrownBy(()->target.use(1L,today))
                .isInstanceOf(SubscriptionNotFoundException.class);
    }


    @Test
    @DisplayName("구독권 사용 시 예외 전파")
    void 구독권_사용_시_예외_전파(){
        //given
        final Long memberNo = 1L;
        final String today= "20250807";

        when(subscriptionRepository.findValidSubscriptionByMemberNoAndToday(memberNo,today))
                .thenReturn(Optional.of(subscription));

        doThrow(new NoRemainingSubscriptionCountException(ErrorCode.NO_SUBSCRIPTION_REMAINING))
                .when(subscription).use();

        //when,then
        assertThatThrownBy(()->target.use(memberNo,today))
                .isInstanceOf(NoRemainingSubscriptionCountException.class);
        ;
    }

    @Test
    @DisplayName("구독권 정상 사용")
    void 구독권_사용_성공(){
        //given
        final Long memberNo = 1L;
        final String today= "20250807";

        when(subscriptionRepository.findValidSubscriptionByMemberNoAndToday(memberNo,today))
                .thenReturn(Optional.of(subscription));

        doNothing().when(subscription).use();

        //when,then
        assertDoesNotThrow(()->target.use(memberNo,today));
        verify(subscription, times(1)).use(); // 실제 호출 여부까지 검증
    }
}