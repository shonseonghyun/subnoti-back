package com.sunghyun.football.global.exception;

import com.sunghyun.football.domain.enumMapper.enums.EnumMapperType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ErrorType implements EnumMapperType {
    SUCCESS(HttpStatus.OK, ErrorCode.S00, "사용자 요청 처리하였습니다."),

    // =========================
    // Global / 공통
    // =========================
    ERROR_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.G00, "에러코드가 존재하지 않습니다."),
    DUPLICATED_REGISTER(HttpStatus.CONFLICT, ErrorCode.G01, "중복된 등록입니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, ErrorCode.G02, "잘못된 파라미터 입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, ErrorCode.G03, "지원하지 않는 메소드입니다."),
    ACCESSED_DENIED(HttpStatus.FORBIDDEN, ErrorCode.G04, "접근이 거부되었습니다."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, ErrorCode.G05, "JWT 토큰이 만료되었습니다."),
    JWT_NOT_FOUND(HttpStatus.UNAUTHORIZED, ErrorCode.G06, "JWT 토큰이 존재하지 않습니다."),
    AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, ErrorCode.G07, "인증이 필요합니다."),
    JWT_PARSE_FAIL(HttpStatus.BAD_REQUEST, ErrorCode.G08, "JWT 토큰이 잘못되었습니다."),
    GENDER_RULE_REJECT(HttpStatus.BAD_REQUEST, ErrorCode.G09, "성별 조건이 맞지 않습니다."),

    // =========================
    // Member 도메인
    // =========================
    HTTP_METHOD_NOT_SUPPORT(HttpStatus.METHOD_NOT_ALLOWED, ErrorCode.M06, "지원하지 않는 HTTP Method 입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.M00, "존재하지 않는 회원입니다."),
    MEMBER_LEVEL_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.M01, "존재하지 않는 회원레벨입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, ErrorCode.M02, "비밀번호가 일치하지 않습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.M03, "존재하지 않는 이메일입니다."),
    DUPLICATED_TEL_REGISTER(HttpStatus.CONFLICT, ErrorCode.M04, "이미 등록된 전화번호입니다."),
    DUPLICATED_EMAIL_REGISTER(HttpStatus.CONFLICT, ErrorCode.M05, "이미 등록된 이메일입니다."),

    // =========================
    // Stadium 도메인
    // =========================
    STADIUM_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.ST00, "존재하지 않는 경기장입니다."),
    STADIUM_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.ST01, "존재하지 않는 경기장 이미지입니다."),
    STADIUM_IMAGE_EXCEED(HttpStatus.BAD_REQUEST, ErrorCode.ST02, "경기장 이미지는 최대 5장까지 등록 가능합니다."),
    STADIUM_IMAGE_NOT_EXIST(HttpStatus.NOT_FOUND, ErrorCode.ST03, "등록된 경기장 이미지가 없습니다."),

    // =========================
    // File 도메인
    // =========================
    FILE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, ErrorCode.F00, "파일 업로드에 실패했습니다."),

    // =========================
    // Match 도메인
    // =========================
    MATCH_STATE_NOT_FOUND(HttpStatus.BAD_REQUEST, ErrorCode.MT00, "존재하지 않는 매치 상태입니다."),
    MATCH_STATUS_NOT_FOUND(HttpStatus.BAD_REQUEST, ErrorCode.MT01, "존재하지 않는 매치 상태입니다."),
    PLAY_STATUS_NOT_FOUND(HttpStatus.BAD_REQUEST, ErrorCode.MT02, "존재하지 않는 플레이 상태입니다."),
    MATCH_NOT_FOUND(HttpStatus.BAD_REQUEST, ErrorCode.MT03, "존재하지 않는 매치입니다."),
    MATCH_PLAYER_NOT_FOUND(HttpStatus.BAD_REQUEST, ErrorCode.MT04, "존재하지 않는 매치 플레이어입니다."),
    MATCH_PLAYER_EXIST(HttpStatus.BAD_REQUEST, ErrorCode.MT05, "이미 존재하는 매치 플레이어입니다."),
    MATCH_NOT_REG_MANAGER(HttpStatus.BAD_REQUEST, ErrorCode.MT06, "매니저로 등록되지 않은 경기입니다."),
    MATCH_ALREADY_APPLY_SAME_TIME(HttpStatus.BAD_REQUEST, ErrorCode.MT07, "이미 같은 시간대에 매치를 신청했습니다."),
    MATCH_ALREADY_REQ_SAME_TIME(HttpStatus.BAD_REQUEST, ErrorCode.MT08, "이미 같은 시간대에 매치를 요청했습니다."),
    MATCH_ALREADY_DONE(HttpStatus.BAD_REQUEST, ErrorCode.MT09, "이미 종료된 매치입니다."),
    DTO_CONVERT_FAIL(HttpStatus.BAD_REQUEST, ErrorCode.MT10, "DTO 변환에 실패했습니다."),
    LEVEL_RULE_REJECT(HttpStatus.BAD_REQUEST, ErrorCode.L00, "레벨 조건이 맞지 않습니다."),

    // =========================
    // Noti 도메인
    // =========================
    FREESUB_NOTI_ALREADY_REQUEST(HttpStatus.CONFLICT, ErrorCode.N00, "이미 무료대기 신청을 하셨습니다."),
    FREESUB_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.N01, "존재하지 않는 무료대기 유형입니다."),
    ACTIVE_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.N02, "존재하지 않는 활성 유형입니다."),
    SEND_FLG_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.N03, "존재하지 않는 발송 여부입니다."),

    // =========================
    // Subscription 도메인
    // =========================
    SUBSCRIPTION_ALREADY_EXIST_THIS_MONTH(HttpStatus.CONFLICT, ErrorCode.SB00, "이번 달 구독권이 이미 존재합니다."),
    INVALID_AMOUNT(HttpStatus.BAD_REQUEST, ErrorCode.SB01, "잘못된 금액입니다."),
    NO_SUBSCRIPTION_REMAINING(HttpStatus.BAD_REQUEST, ErrorCode.SB02, "남은 구독권이 없습니다."),
    SUBSCRIPTION_EXPIRED(HttpStatus.BAD_REQUEST, ErrorCode.SB03, "만료된 구독권입니다."),
    SUBSCRIPTION_ALREADY_USE(HttpStatus.BAD_REQUEST, ErrorCode.SB04, "이미 사용된 구독권입니다."),
    SUBSCRIPTION_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, ErrorCode.SB05, "이미 취소된 구독권입니다."),
    SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.SB06, "존재하지 않는 구독권입니다."),

    // =========================
    // Pay 도메인
    // =========================
    UNAVAILABLE_PAYMENT_METHOD(HttpStatus.BAD_REQUEST, ErrorCode.P00, "사용할 수 없는 결제수단입니다."),
    UNSUPPORTED_PAYMENT_METHOD(HttpStatus.BAD_REQUEST, ErrorCode.P01, "지원하지 않는 결제수단입니다."),
    PAY_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.P02, "결제 이력이 존재하지 않습니다. 재확인바랍니다.");

    private final HttpStatus httpStatus;
    private final ErrorCode errorCode;
    private final String msg;

    public String getCode() {
        return this.errorCode.name();
    }

    public static ErrorType getErrorCodeEnum(String code) {
        return Arrays.stream(ErrorType.values())
                .filter(errorType -> errorType.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new AppException(ERROR_CODE_NOT_FOUND));
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDesc() {
        return this.getMsg();
    }
}
