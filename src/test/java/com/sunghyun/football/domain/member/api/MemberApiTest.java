package com.sunghyun.football.domain.member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.football.domain.member.application.JoinService;
import com.sunghyun.football.domain.member.application.MemberApplication;
import com.sunghyun.football.domain.member.application.dto.MemberJoinReqDto;
import com.sunghyun.football.domain.member.application.dto.MemberJoinResDto;
import com.sunghyun.football.domain.member.domain.Member;
import com.sunghyun.football.domain.member.domain.dto.MemberUpdReqDto;
import com.sunghyun.football.domain.member.domain.enums.Gender;
import com.sunghyun.football.domain.member.domain.enums.MemberLevelType;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.member.MemberNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberApi.class)
class MemberApiTest{

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberApplication memberApplication;

    @MockBean
    private JoinService joinService;

    final Long memberNo = 1L;
    final String email = "abac@naver.com";
    final String tel="01012341234";
    final String name = "이름";
    final Gender gender = Gender.MALE;
    final String birthDt= "19950204";
    final String pwd= "1234";
    final MemberLevelType level = MemberLevelType.ROOKIE;

//    body가 empty일 경우
//    https://velog.io/@wool_ly/SpringTest-MockHttpServletResponse-body%EA%B0%80-empty-null%EC%9D%BC-%EB%95%8C


    @DisplayName("회원가입 - body데이터 인입되지 않는 경우")
    @Test
    void joinWithNotExistParam() throws Exception {
        //given
        final String url = "/api/v1/member";

        //when
        final ResultActions resultActions = mockMvc.perform(post(url));

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("회원가입 정상처리")
    @Test
    void joinMember() throws Exception {
        //given
        final String url = "/api/v1/member";
        MemberJoinReqDto memberJoinReqDto = createMemberJoinReqDto();
        String content = objectMapper.writeValueAsString(memberJoinReqDto);

        doReturn(MemberJoinResDto.from(Member.builder()
                .memberNo(1L)
                .email(email)
                .pwd(pwd)
                .tel(tel)
                .birthDt(birthDt)
                .name(name)
                .gender(gender)
                .build())
        ).when(joinService).join(any(MemberJoinReqDto.class))
         ;

        //when
        final ResultActions resultActions = mockMvc.perform(post(url)
                        .content(content)
                .contentType(MediaType.APPLICATION_JSON)
        )
//                .andDo(
//                restDocs.document(
//                        PayloadDocumentation.requestFields(
//                                PayloadDocumentation.fieldWithPath("email").description("이메일"),
//                                PayloadDocumentation.fieldWithPath("pwd").description("패스워드"),
//                                PayloadDocumentation.fieldWithPath("name").description("이름"),
//                                PayloadDocumentation.fieldWithPath("birthDt").description("생년월일"),
//                                PayloadDocumentation.fieldWithPath("gender").description("성별"),
//                                PayloadDocumentation.fieldWithPath("tel").description("전화번호")
//                        ),
//                        PayloadDocumentation.responseFields(
//                                PayloadDocumentation.fieldWithPath("body[].code").description("결과코드"),
//                                PayloadDocumentation.fieldWithPath("body[].msg").description("결과메시지")
//                        )
//                )
//        );
        ;

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.memberNo").value(memberNo))
                .andExpect(jsonPath("$.data.tel").value(tel))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.data.name").value(name))
                .andExpect(jsonPath("$.data.pwd").value(pwd))
                .andExpect(jsonPath("$.data.gender").value(gender.name()))
                .andExpect(jsonPath("$.data.birthDt").value(birthDt))
        ;
    }

    private MemberJoinReqDto createMemberJoinReqDto(){
        return MemberJoinReqDto
                .builder()
                .email(email)
                .tel(tel)
                .pwd(pwd)
                .birthDt(birthDt)
                .name(name)
                .gender(gender)
                .build();
    }


    @DisplayName("회원수정 정상처리")
    @Test
    void updateMemberWithNotAcceptableParameter() throws Exception {
        //given
        final String url = "/api/v1/member/"+memberNo;
        MemberUpdReqDto memberUpdReqDto = createMemberUpdateReqDto();

        //then
        final ResultActions resultActions = mockMvc.perform(put(url)
                .content(objectMapper.writeValueAsString(memberUpdReqDto))
                .contentType(MediaType.APPLICATION_JSON))
                ;

        resultActions.andExpect(status().isOk());
        verify(memberApplication).updateMember(refEq(memberUpdReqDto));
    }

    private MemberUpdReqDto createMemberUpdateReqDto(){
        return  MemberUpdReqDto.builder()
                .memberNo(memberNo)
                .memberLevelType(level)
                .pwd(pwd)
                .name(name)
                .gender(gender)
                .build()
                ;
    }


   @DisplayName("회원탈퇴 - 회원번호 필드에 올바르지않은 필드값 인입(null, 글자)")
//    @ParameterizedTest
   @Test
//    @MethodSource("invalidJoinParameter")
//    @ValueSource(strings = {"test"})
//    @NullSource
    void joinMemberWithNotAcceptableParameter() throws Exception {
        //given
        String parameter = "s";
        final String url = "/api/v1/member/"+parameter;

        //when
        final ResultActions resultActions=mockMvc.perform(delete(url));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_PARAMETER.getCode()))
                .andExpect(jsonPath("$.msg").value(ErrorCode.INVALID_PARAMETER.getMessage()));
    }

//    private static Stream<Arguments> invalidJoinParameter(){
//        return Stream.of(
//                Arguments.of(null),
//                Arguments.of("하이")
//        );
//    }

    @DisplayName("회원탈퇴 실패_존재하지 않는 회원번호")
    @Test
    void joinMemberWithNotExistMemberNo() throws Exception {
        //given
        Long memberNo = 1L;
        final String url = "/api/v1/member/"+ memberNo;

        doThrow(new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND))
                .when(memberApplication).deleteMember(memberNo);

        //when
        final ResultActions resultActions=mockMvc.perform(delete(url));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.MEMBER_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.msg").value(ErrorCode.MEMBER_NOT_FOUND.getMessage()));
    }

    @DisplayName("회원탈퇴 정상처리")
    @Test
    void deleteMember() throws Exception {
        //given
        Long memberNo = 1L;
        final String url = "/api/v1/member/"+memberNo;

        //when
        final ResultActions resultActions=mockMvc.perform(delete(url));

        //then
        resultActions.andExpect(status().isOk());
    }
}