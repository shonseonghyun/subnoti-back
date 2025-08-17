package com.sunghyun.football.domain.pay.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.football.domain.pay.application.PayService;
import com.sunghyun.football.domain.pay.application.dto.PayReqDto;
import com.sunghyun.football.domain.pay.domain.model.PaymentMethod;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.GlobalExceptionHandler;
import com.sunghyun.football.global.exception.exceptions.pay.UnavailablePaymentMethodException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PayApiTest {
    private final Long memberNo = 1L;
    private final int rightAmount = 9900;
    private final int wrongAmount = 9;
    private final PaymentMethod activePaymentMethod = PaymentMethod.KAKAO_PAY;
    private final PaymentMethod inactivePaymentMethod = PaymentMethod.CREDIT_CARD;


    @InjectMocks
    private PayApi target;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private PayService payService;


    @BeforeEach
    public void init() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(new GlobalExceptionHandler(objectMapper))
                .build();
    }

    @Test
    void mockMvc가_null_아님(){
        assertThat(target).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void pay_결제_실패_예외_응답() throws Exception {
        //given
        final String url = "/api/v1/pay";
        doThrow(new UnavailablePaymentMethodException(ErrorCode.UNAVAILABLE_PAYMENT_METHOD))
                .when(payService).pay(any(),anyInt(),any());

        //when
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(objectMapper.writeValueAsString(new PayReqDto(memberNo,rightAmount,inactivePaymentMethod)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.UNAVAILABLE_PAYMENT_METHOD.getCode()))
                .andExpect(jsonPath("$.msg").value(ErrorCode.UNAVAILABLE_PAYMENT_METHOD.getMessage()))
        ;
        verify(payService).pay(memberNo, rightAmount, inactivePaymentMethod);
    }

    @Test
    void pay_결제_성공() throws Exception {
        //given
        final String url = "/api/v1/pay";

        //when
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(objectMapper.writeValueAsString(new PayReqDto(memberNo,rightAmount,activePaymentMethod)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        verify(payService).pay(memberNo, rightAmount, activePaymentMethod);
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.msg").value(ErrorCode.SUCCESS.getMessage()));;
    }
}