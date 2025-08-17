//package com.sunghyun.football.domain.member.domain.enums;
//
//import com.sunghyun.football.domain.enumMapper.enums.EnumMapperType;
//import com.sunghyun.football.global.exception.ErrorType;
//import com.sunghyun.football.global.exception.exceptions.member.MemberLevelNotFoundException;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//import java.util.Arrays;
//
//@Getter
//@AllArgsConstructor
//public enum MemberLevelType implements EnumMapperType {
//    ROOKIE("루키",0),
//    STARTER1("스타터 1",1),STARTER2("스타터 1",2),STARTER3("스타터 1",3),
//    BEGINNER1("비기너 1",4),BEGINNER2("비기너 1",5),BEGINNER3("비기너 1",6),
//    AMATEUR1("아마추어 1",7),AMATEUR2("아마추어 2",8),AMATEUR3("아마추어 3",9),
//    SEMIPRO1("세미프로 1",10),SEMIPRO2("세미프로 2",11),SEMIPRO3("세미프로 3",12),
//    PRO1("프로1",13),PRO2("프로2",14),PRO3("프로3",15)
//    ;
//
//    private final String desc;
//    private final Integer levelNum;
//
//    public static MemberLevelType getMemberLevel(int averageLevelNum) {
//        return Arrays.stream(MemberLevelType.values())
//                .filter(memberLevel -> memberLevel.getLevelNum().equals(averageLevelNum))
//                .findFirst()
//                .orElseThrow(()->new MemberLevelNotFoundException(ErrorType.MEMBER_LEVEL_NOT_FOUND));
//    }
//
//    public static boolean isAvailableApply(MemberLevelType standard, MemberLevelType targetLevel) {
//        return standard.getLevelNum() >= targetLevel.getLevelNum() ;
//    }
//
//    @Override
//    public String getName() {
//        return name();
//    }
//}
