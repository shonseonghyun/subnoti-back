package com.sunghyun.football.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    SUCCESS(HttpStatus.OK, ErrorCode.S00),

    // =========================
    // Global / 공통
    // =========================
    ERROR_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.G00),
    DUPLICATED_REGISTER(HttpStatus.CONFLICT, ErrorCode.G01),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, ErrorCode.G02),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, ErrorCode.G03),
    ACCESSED_DENIED(HttpStatus.FORBIDDEN, ErrorCode.G04),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, ErrorCode.G05),
    JWT_NOT_FOUND(HttpStatus.UNAUTHORIZED, ErrorCode.G06),
    AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, ErrorCode.G07),
    JWT_PARSE_FAIL(HttpStatus.BAD_REQUEST, ErrorCode.G08),


    // =========================
    // Member 도메인
    // =========================
    HTTP_METHOD_NOT_SUPPORT(HttpStatus.METHOD_NOT_ALLOWED, ErrorCode.M06),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.M00),
    MEMBER_LEVEL_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.M01),
    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, ErrorCode.M02),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.M03),
    DUPLICATED_TEL_REGISTER(HttpStatus.CONFLICT, ErrorCode.M04),
    DUPLICATED_EMAIL_REGISTER(HttpStatus.CONFLICT, ErrorCode.M05),

    // =========================
    // Stadium 도메인
    // =========================
    STADIUM_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.ST00),
    STADIUM_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.ST01),
    STADIUM_IMAGE_EXCEED(HttpStatus.BAD_REQUEST, ErrorCode.ST02),
    STADIUM_IMAGE_NOT_EXIST(HttpStatus.NOT_FOUND, ErrorCode.ST03),

    // =========================
    // File 도메인
    // =========================
    FILE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, ErrorCode.F00),

    // =========================
    // Match 도메인
    // =========================
    MATCH_STATE_NOT_FOUND(HttpStatus.BAD_REQUEST, ErrorCode.MT00),
    MATCH_STATUS_NOT_FOUND(HttpStatus.BAD_REQUEST, ErrorCode.MT01),
    PLAY_STATUS_NOT_FOUND(HttpStatus.BAD_REQUEST, ErrorCode.MT02),
    MATCH_NOT_FOUND(HttpStatus.BAD_REQUEST, ErrorCode.MT03),
    MATCH_PLAYER_NOT_FOUND(HttpStatus.BAD_REQUEST, ErrorCode.MT04),
    MATCH_PLAYER_EXIST(HttpStatus.BAD_REQUEST, ErrorCode.MT05),
    MATCH_NOT_REG_MANAGER(HttpStatus.BAD_REQUEST, ErrorCode.MT06),
    MATCH_ALREADY_APPLY_SAME_TIME(HttpStatus.BAD_REQUEST, ErrorCode.MT07),
    MATCH_ALREADY_REQ_SAME_TIME(HttpStatus.BAD_REQUEST, ErrorCode.MT08),
    MATCH_ALREADY_DONE(HttpStatus.BAD_REQUEST, ErrorCode.MT09),
    DTO_CONVERT_FAIL(HttpStatus.BAD_REQUEST, ErrorCode.MT10),
    LEVEL_RULE_REJECT(HttpStatus.BAD_REQUEST, ErrorCode.L00),
    GENDER_RULE_REJECT(HttpStatus.BAD_REQUEST, ErrorCode.G09),

    // =========================
    // Noti 도메인
    // =========================
    FREESUB_NOTI_ALREADY_REQUEST(HttpStatus.CONFLICT, ErrorCode.N00),
    FREESUB_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND,ErrorCode.N01),
    ACTIVE_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND,ErrorCode.N02),
    SEND_FLG_NOT_FOUND(HttpStatus.NOT_FOUND,ErrorCode.N03),

    // =========================
    // Subscription 도메인
    // =========================
    SUBSCRIPTION_ALREADY_EXIST_THIS_MONTH(HttpStatus.CONFLICT, ErrorCode.SB00),
    INVALID_AMOUNT(HttpStatus.BAD_REQUEST, ErrorCode.SB01),
    NO_SUBSCRIPTION_REMAINING(HttpStatus.BAD_REQUEST, ErrorCode.SB02),
    SUBSCRIPTION_EXPIRED(HttpStatus.BAD_REQUEST, ErrorCode.SB03),
    SUBSCRIPTION_ALREADY_USE(HttpStatus.BAD_REQUEST, ErrorCode.SB04),
    SUBSCRIPTION_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, ErrorCode.SB05),
    SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.SB06),

    // =========================
    // Pay 도메인
    // =========================
    UNAVAILABLE_PAYMENT_METHOD(HttpStatus.BAD_REQUEST, ErrorCode.P00),
    UNSUPPORTED_PAYMENT_METHOD(HttpStatus.BAD_REQUEST, ErrorCode.P01),
    PAY_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.P02)
    ;

    private final HttpStatus httpStatus;
    private final ErrorCode errorCode;

    public String getCode(){
        return this.getErrorCode().getCode();
    }

    public String getMsg(){
        return this.getErrorCode().getMsg();
    }

    public static ErrorType getErrorCodeEnum(String code) {
        return Arrays.stream(ErrorType.values()).filter(errorType->errorType.getCode().equals(code))
                .findFirst()
                .orElseThrow(()->new AppException(ERROR_CODE_NOT_FOUND));
    }

}
