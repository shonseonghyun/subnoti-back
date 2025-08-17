package com.sunghyun.football.integration.subscription;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.football.domain.pay.application.dto.PayReqDto;
import com.sunghyun.football.domain.pay.domain.model.Pay;
import com.sunghyun.football.domain.pay.domain.model.PayStatus;
import com.sunghyun.football.domain.pay.domain.model.PaymentMethod;
import com.sunghyun.football.domain.pay.domain.repository.PayRepository;
import com.sunghyun.football.domain.subscription.domain.model.Subscription;
import com.sunghyun.football.domain.subscription.domain.repository.SubscriptionRepository;
import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.utils.MatchDateUtils;
import org.junit.jupiter.api.DisplayName;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("local")
@AutoConfigureMockMvc
@EnableAspectJAutoProxy
public class SubscriptionIntegrationTest {

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

//    @AfterEach
//    void cleanDb() {
//        payRepository.deleteAll();
//        subscriptionRepository.deleteAll();
//    }

    @Test
    @DisplayName("구독권 취소 시 취소 검증 중 구독권 찾지 못하여 통과하지 못한 경우 결제환불 및 구독권 취소(삭제) 반영되지 않는다.")
    void 구독권_취소_실패() throws Exception {
        //given
        final String url = "/api/v1/subscription/";
        final Long memberNo = 1L;
        final Long subscriptionNo = 1L;
        final String finalUrl = url+memberNo+"/"+subscriptionNo;

        //when
        final ResultActions resultActions = mockMvc.perform(
                delete(finalUrl)
        );

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorType.SUBSCRIPTION_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.msg").value(ErrorType.SUBSCRIPTION_NOT_FOUND.getMessage()))
                ;
    }

    @Test
    void 결제_성공_후_생기는_구독권과_결제이력으로_구독권_취소_성공_구독권_삭제_및_결제상태_환불로_변경() throws Exception {
        //given
        final String url = "/api/v1/subscription/";
        final Long memberNo = 1L;
        final String today = MatchDateUtils.getNowDtStr();

        final String payUrl = "/api/v1/pay";
        final int rightAmount = 9900;
        final PaymentMethod activePaymentMethod = PaymentMethod.KAKAO_PAY;

        //결제 성공으로 구독권과 결제이력 먼저 생성
        mockMvc.perform(
                post(payUrl)
                        .content(objectMapper.writeValueAsString(new PayReqDto(memberNo, rightAmount, activePaymentMethod)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        Optional<Pay> optionalPay = payRepository.findByMemberNoAndCreatedDt(memberNo,MatchDateUtils.getNowDtStr());
        Optional<Subscription> optionalSubscription = subscriptionRepository.findValidSubscriptionBySubscriptionNoAndMemberNoAndToday(memberNo,optionalPay.get().getSubscriptionNo(),today);

        final String finalUrl = url+memberNo+"/"+optionalSubscription.get().getSubscriptionNo();

        //when
        final ResultActions resultActions = mockMvc.perform(
                delete(finalUrl)
        );

        //then
        //1. DB검증
        Optional<Pay> optionalPayFinal = payRepository.findByMemberNoAndCreatedDt(memberNo,MatchDateUtils.getNowDtStr());
        Optional<Subscription> optionalSubscriptionFinal = subscriptionRepository.findValidSubscriptionBySubscriptionNoAndMemberNoAndToday(memberNo,optionalPay.get().getSubscriptionNo(),today);

        assertThat(optionalPayFinal.get().getPayStatus()).isEqualTo(PayStatus.REFUND);
        assertThat(optionalSubscriptionFinal).isEmpty(); //삭제 완료


        //2. 응답 검증
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorType.SUCCESS.getCode()))
                .andExpect(jsonPath("$.msg").value(ErrorType.SUCCESS.getMessage()))
        ;
    }

}
