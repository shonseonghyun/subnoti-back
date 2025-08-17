package com.sunghyun.football.domain.subscription.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.football.domain.subscription.application.service.SubscriptionService;
import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.GlobalExceptionHandler;
import com.sunghyun.football.global.exception.subscription.exception.SubscriptionNotFoundException;
import com.sunghyun.football.global.utils.MatchDateUtils;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SubscriptionApiTest {

    @InjectMocks
    private SubscriptionApi target;

    @Mock
    private SubscriptionService subscriptionService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

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
    void 구독권_삭제_실패_예외_응답() throws Exception {
        //given
        final Long memberNo = 1L;
        final Long subscriptionNo = 1L;
        final String today= MatchDateUtils.getNowDtStr();
        final String url = "/api/v1/subscription/"+memberNo+"/"+subscriptionNo;

        doThrow(new SubscriptionNotFoundException(ErrorType.SUBSCRIPTION_NOT_FOUND))
                .when(subscriptionService).cancel(memberNo,subscriptionNo,today);

        //when
        final ResultActions resultActions = mockMvc.perform(
                delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorType.SUBSCRIPTION_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.msg").value(ErrorType.SUBSCRIPTION_NOT_FOUND.getMsg()))
        ;
        verify(subscriptionService).cancel(memberNo, subscriptionNo,today);
    }

    @Test
    void 구독권_삭제_성공_응답() throws Exception {
        //given
        final Long memberNo = 1L;
        final Long subscriptionNo = 1L;
        final String today= MatchDateUtils.getNowDtStr();
        final String url = "/api/v1/subscription/"+memberNo+"/"+subscriptionNo;


        //when
        final ResultActions resultActions = mockMvc.perform(
                delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorType.SUCCESS.getCode()))
                .andExpect(jsonPath("$.msg").value(ErrorType.SUCCESS.getMsg()))
        ;
        verify(subscriptionService).cancel(memberNo, subscriptionNo,today);
    }

}