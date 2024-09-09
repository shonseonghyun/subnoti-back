package com.sunghyun.football.domain.match.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.football.domain.match.application.MatchApplication;
import com.sunghyun.football.domain.match.application.dto.RegMatchReqDto;
import com.sunghyun.football.domain.match.domain.enums.GenderRule;
import com.sunghyun.football.domain.member.domain.enums.MemberLevelType;
import com.sunghyun.football.domain.stadium.enums.EnumMapper;
import com.sunghyun.football.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatchApi.class)
class MatchApiTest {

    final Long price =10000L;
    final String startDt = "20240827";
    final String startTm = "1600";
    final Integer headCount = 5;
    final MemberLevelType levelRule = MemberLevelType.AMATEUR2;
    final GenderRule genderRule = GenderRule.FEMALE;

    @MockBean
    private MatchApplication matchApplication;

    @MockBean
    private EnumMapper enumMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("매치 등록 시 @Valid 유효성 검증 실패 테스트")
    @Test
    void regMatchWithValidFailure() throws Exception {
        //given
        final String url = "/api/v1/match";
        final RegMatchReqDto regMatchReqDto = createRegMatchReqDtoWithOutGenderRuleField();

        final String content = objectMapper.writeValueAsString(regMatchReqDto);

        //when
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(content)
                        .contentType("application/json"))
                ;

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_PARAMETER.getCode()));
    }

    @DisplayName("특정 날짜 매치 전체 조회 실패_시작 날짜파라미터 자료형 올바르지 않음(@Pattern)")
    @Test
    void getMatchesByStartDtButParamIsInvalid() throws Exception {
        final String url = "/api/v1/matches";
        final ResultActions resultActions = mockMvc.perform(
                get(url)
                        .param("startDt","asd")
        ).andDo(print());

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_PARAMETER.getCode()));

    }

    @DisplayName("특정 날짜 매치 전체 조회 성공")
    @Test
    void getMatchesByStartDt() throws Exception {
        final String startDt = "20240828";
        final String url = "/api/v1/matches";
        final ResultActions resultActions = mockMvc.perform(
                get(url)
                        .param("startDt",startDt)
        ).andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.SUCCESS.getCode()));
    }

    @DisplayName("특정 날짜,스타디움의 매치 전체 조회 성공")
    @Test
    void getMatchesByStartDtAndStadiumNo() throws Exception {
        final String startDt = "20240828";
        final Long stadiumNo = 1L;
        final String url = "/api/v1/matches";
        final ResultActions resultActions = mockMvc.perform(
                get(url)
                        .param("startDt",startDt)
                        .param("stadiumNo", String.valueOf(stadiumNo))
        ).andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.SUCCESS.getCode()));
    }



    private RegMatchReqDto createRegMatchReqDtoWithOutGenderRuleField(){
        return RegMatchReqDto.builder()
                .stadiumNo(1L)
                .price(price)
                .startDt(startDt)
                .startTm(startTm)
                .headCount(headCount)
                .levelRule(levelRule)
//                .genderRule(null)
                .build()
                ;
    }
}