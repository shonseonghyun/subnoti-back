package com.sunghyun.football.integration.pay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.football.domain.pay.application.dto.PayReqDto;
import com.sunghyun.football.domain.pay.domain.model.Pay;
import com.sunghyun.football.domain.pay.domain.model.PaymentMethod;
import com.sunghyun.football.domain.pay.domain.repository.PayRepository;
import com.sunghyun.football.domain.subscription.domain.model.Subscription;
import com.sunghyun.football.domain.subscription.domain.repository.SubscriptionRepository;
import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.utils.MatchDateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("local")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@AutoConfigureMockMvc
public class PayIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PayRepository payRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;


    @Test
    void mockMvc가_null_아님(){
        assertThat(mockMvc).isNotNull();
    }

    @BeforeEach
    void cleanDb() {
        payRepository.deleteAll();
        subscriptionRepository.deleteAll();
    }

    @Test
    void 요금제에_해당되지_않는_요금으로_결제_시_결제_히스토리와_구독권_생성되지_않는다() throws Exception{
        //given
        final String url = "/api/v1/pay";
        final Long memberNo = 1L;
        final int wrongAmount = 99999999;
        final PaymentMethod availablePaymentMethod = PaymentMethod.KAKAO_PAY;

        final String today = MatchDateUtils.getNowDtStr();

        //when
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(objectMapper.writeValueAsString(new PayReqDto(memberNo, wrongAmount, availablePaymentMethod)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        //존재여부 검증
        Optional<Pay> optionalPay = payRepository.findByMemberNoAndCreatedDt(memberNo, today);
        Optional<Subscription> optionalSubscription = subscriptionRepository.findValidSubscriptionByMemberNoAndToday(memberNo, today);
        assertThat(optionalPay).isEmpty();
        assertThat(optionalSubscription).isEmpty();

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorType.INVALID_AMOUNT.getCode()))
                .andExpect(jsonPath("$.msg").value(ErrorType.INVALID_AMOUNT.getMessage()))
        ;
    }

    @Test
    void 사용_불가한_결제수단으로_결제_시_결제_히스토리와_구독권_생성되지_않는다() throws Exception{
        //given
        final String url = "/api/v1/pay";
        final Long memberNo = 1L;
        final int rightAmount = 9900;
        final PaymentMethod unavailablePaymentMethod = PaymentMethod.CREDIT_CARD;

        final String today = MatchDateUtils.getNowDtStr();

        //when
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(objectMapper.writeValueAsString(new PayReqDto(memberNo, rightAmount, unavailablePaymentMethod)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        //존재여부 검증
        Optional<Pay> optionalPay = payRepository.findByMemberNoAndCreatedDt(memberNo, today);
        Optional<Subscription> optionalSubscription = subscriptionRepository.findValidSubscriptionByMemberNoAndToday(memberNo, today);
        assertThat(optionalPay).isEmpty();
        assertThat(optionalSubscription).isEmpty();

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorType.UNAVAILABLE_PAYMENT_METHOD.getCode()))
                .andExpect(jsonPath("$.msg").value(ErrorType.UNAVAILABLE_PAYMENT_METHOD.getMessage()))
        ;
    }

    @Test
    void 존재하지_않는_결제수단으로_결제_시_결제_히스토리와_구독권_생성되지_않는다() throws Exception{
        //given
        final String url = "/api/v1/pay";
        final Long memberNo = 1L;
        final int rightAmount = 9900;
        final PaymentMethod nullPaymentMethod = null;
        final String today = MatchDateUtils.getNowDtStr();

        //when
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(objectMapper.writeValueAsString(new PayReqDto(memberNo, rightAmount, nullPaymentMethod)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        //존재여부 검증
        Optional<Pay> optionalPay = payRepository.findByMemberNoAndCreatedDt(memberNo, today);
        Optional<Subscription> optionalSubscription = subscriptionRepository.findValidSubscriptionByMemberNoAndToday(memberNo, today);
        assertThat(optionalPay).isEmpty();
        assertThat(optionalSubscription).isEmpty();

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorType.UNSUPPORTED_PAYMENT_METHOD.getCode()))
                .andExpect(jsonPath("$.msg").value(ErrorType.UNSUPPORTED_PAYMENT_METHOD.getMessage()))
        ;
    }

    @Test
    void 결제하면_결제_히스토리와_구독권이_생성된다() throws Exception {
        //given
        final String url = "/api/v1/pay";
        final Long memberNo = 1L;
        final int rightAmount = 9900;
        final PaymentMethod activePaymentMethod = PaymentMethod.KAKAO_PAY;
        final String today = MatchDateUtils.getNowDtStr();

        //when
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(objectMapper.writeValueAsString(new PayReqDto(memberNo, rightAmount, activePaymentMethod)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        //존재여부 검증
        Optional<Pay> optionalPay = payRepository.findByMemberNoAndCreatedDt(memberNo, today);
        Optional<Subscription> optionalSubscription = subscriptionRepository.findValidSubscriptionByMemberNoAndToday(memberNo, today);
        assertThat(optionalPay).isPresent();
        assertThat(optionalSubscription).isPresent();

        //값 검증
        Pay pay = optionalPay.get();
        Subscription subscription = optionalSubscription.get();
        assertThat(pay.getMemberNo()).isEqualTo(memberNo);
        assertThat(pay.getAmount()).isEqualTo(rightAmount);
        assertThat(pay.getCreatedDt()).isEqualTo(today);
        assertThat(pay.getPaymentMethod()).isEqualTo(activePaymentMethod);
        assertThat(pay.getSubscriptionNo()).isEqualTo(subscription.getSubscriptionNo());

        assertThat(subscription.getMemberNo()).isEqualTo(memberNo);
        assertThat(subscription.getCreatedDt()).isEqualTo(today);
        assertThat(subscription.getSubscriptionPlan().getAmount()).isEqualTo(rightAmount);
    }
}
