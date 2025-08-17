package com.sunghyun.football.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    SUCCESS(HttpStatus.OK,"S00","사용자 요청 처리 성공하였습니다."),
    ERROR_CODE_NOT_FOUND(HttpStatus.BAD_REQUEST,"E00","존재하지 않는 에러코드입니다"),
    DUPLICATED_REGISTER(HttpStatus.BAD_REQUEST, "D00", "특정 필드의 데이터 값은 이미 존재합니다."),
    DUPLICATED_TEL_REGISTER(HttpStatus.BAD_REQUEST, "D01","이미 존재하는 휴대폰 번호입니다."),
    DUPLICATED_EMAIL_REGISTER(HttpStatus.BAD_REQUEST,"D02", "이미 존재하는 이메일입니다."),
    STADIUM_NOT_FOUND(HttpStatus.BAD_REQUEST,"S01","존재하지 않는 스타디움 번호입니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST,"M00","존재하지 않는 사용자 번호입니다."),
    STADIUM_IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST,"S02","존재하지 않는 스타디움 이미지 번호입니다."),
    STADIUM_IMAGE_EXCEED(HttpStatus.BAD_REQUEST,"S03","등록 가능한 이미지수는 최대 3장입니다. 요청 이미지 수를 확인해 주세요." ),
    STADIUM_IMAGE_NOT_EXIST(HttpStatus.BAD_REQUEST,"S04","등록 요청한 이미지가 존재하지 않습니다." ),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST,"I00","올바르지 않은 파라미터가 존재합니다." ),
    MATCH_STATE_NOT_FOUND(HttpStatus.BAD_REQUEST,"M01","존재하지 않는 매치 상태 코드입니다."),
    MATCH_STATUS_NOT_FOUND(HttpStatus.BAD_REQUEST,"M02","존재하지 않는 매치 종료 상태 코드입니다."),
    PLAY_STATUS_NOT_FOUND(HttpStatus.BAD_REQUEST,"P00","존재하지 않는 참가 상태 여부 코드입니다."),
    MATCH_NOT_FOUND(HttpStatus.BAD_REQUEST,"M03" ,"존재하지 않는 매치 번호입니다." ),
    MATCH_PLAYER_NOT_FOUND(HttpStatus.BAD_REQUEST,"M04","해당 매치에 신청하지 않았습니다." ),
    MATCH_PLAYER_EXIST(HttpStatus.BAD_REQUEST,"M05" ,"이미 신청한 매치입니다." ),
    METHOD_NOT_ALLOWED(HttpStatus.BAD_REQUEST,"M06","지원하지 않는 HTTP 메소드 입니다." ),
    MATCH_NOT_REG_MANAGER(HttpStatus.BAD_REQUEST,"M07" ,"매니저가 지정되지 않은 매치입니다. 매니저 등록 완료된 후 재신청 바랍니다." ),
    MATCH_ALREADY_APPLY_SAME_TIME(HttpStatus.BAD_REQUEST,"M08" ,"해당 시간대에 이미 신청한 다른 매치가 존재합니다." ),
    MATCH_ALREADY_REQ_SAME_TIME(HttpStatus.BAD_REQUEST,"M09" ,"해당 스타디움은 요청 시간대에 등록된 다른 매치가 존재합니다." ),
    MEMBER_LEVEL_NOT_FOUND(HttpStatus.BAD_REQUEST,"M10" ,"존재하지 않는 유저 레벌 코드입니다." ),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST,"M11","존재하지 않는 이메일입니다."),
    LEVEL_RULE_REJECT(HttpStatus.BAD_REQUEST,"L00" , "레벨 제한으로 인해 신청이 불가합니다." ),
    GENDER_RULE_REJECT(HttpStatus.BAD_REQUEST,"G00" , "성별 제한으로 인해 신청이 불가합니다." ),
    FILE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST,"F00" ,"파일 업로드에 실패하였습니다." ),
    HTTP_METHOD_NOT_SUPPORT(HttpStatus.BAD_REQUEST,"H00" , "로그인 요청에 대한 지원되지 않는 Http Method입니다." ),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST,"P01" , "비밀번호가 일치하지 않습니다." ),
    ACCESSED_DENIED(HttpStatus.BAD_REQUEST,"A00" ,"접근 권한이 올바르지 않습니다." ),
    AUTHENTICATION_REQUIRED(HttpStatus.BAD_REQUEST,"A01" , "인증이 필요합니다."),
    JWT_PARSE_FAIL(HttpStatus.BAD_REQUEST,"J00" ,"토큰 파싱에 실패하였습니다." ),
    JWT_EXPIRED(HttpStatus.BAD_REQUEST,"J01" ,"토큰의 유효기간이 만료되었습니다." ),
    JWT_NOT_FOUND(HttpStatus.BAD_REQUEST,"J02" ,"존재하지 않거나 유효기간 만료된 리프레쉬 토큰입니다." ),
    FREESUB_NOTI_ALREADY_REQUEST(HttpStatus.BAD_REQUEST, "F01","이미 알림 신청된 매치입니다." ),
    MATCH_ALREADY_DONE(HttpStatus.BAD_REQUEST,"M12","이미 종료된 매치입니다."),
    DTO_CONVERT_FAIL(HttpStatus.OK,"D03","DTO 변환 실패하였습니다."),
    INVALID_AMOUNT(HttpStatus.BAD_REQUEST, "I01","올바르지 않은 금액입니다" ),
    UNAVAILABLE_PAYMENT_METHOD(HttpStatus.BAD_REQUEST, "U01","사용불가한 결제수단입니다." ),
    UNSUPPORTED_PAYMENT_METHOD(HttpStatus.BAD_REQUEST, "U02","존재하지 않는 결제수단입니다." ),
    SUBSCRIPTION_ALREADY_EXIST_THIS_MONTH(HttpStatus.BAD_REQUEST,"S05" ,"이미 해당 월 요금제가 가입되어있습니다. 환불 후 진행바랍니다." ),
    NO_SUBSCRIPTION_REMAINING(HttpStatus.BAD_REQUEST,"N01" ,"남은 구독권 횟수가 없습니다" ),
    SUBSCRIPTION_NOT_FOUND(HttpStatus.BAD_REQUEST,"S06" ,"구독권을 찾을 수 없습니다. 구독권 등록 여부를 확인해주세요." ),
    SUBSCRIPTION_EXPIRED(HttpStatus.BAD_REQUEST,"S07","만료된 구독권입니다."  ),
    SUBSCRIPTION_ALREADY_USE(HttpStatus.BAD_REQUEST,"S08","이미 사용된 구독권입니다."),
    PAY_NOT_FOUND(HttpStatus.BAD_REQUEST,"P02" ,"결제 이력이 존재하지 않습니다. 재확인바랍니다." ),
    SUBSCRIPTION_ALREADY_CANCELED(HttpStatus.BAD_REQUEST,"S09" ,"구독취소는 달에 1회만 가능합니다." )
    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public static ErrorCode getErrorCodeEnum(String code) {
        return Arrays.stream(ErrorCode.values()).filter(errCode->errCode.getCode().equals(code))
                .findFirst()
                .orElseThrow(()->new AppException(ERROR_CODE_NOT_FOUND));
    }
}
