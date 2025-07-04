//package com.sunghyun.football.domain.match.application;
//
//import com.sunghyun.football.domain.match.domain.Match;
//import com.sunghyun.football.domain.match.domain.repository.MatchRepository;
//import com.sunghyun.football.global.exception.ErrorCode;
//import com.sunghyun.football.global.exception.exceptions.match.MatchNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public final class MatchServiceHelper {
//
//    private final MatchRepository matchRepository;
//
//    public Match findExistingMatch(Long matchNo){
//        Match selectedMatch = matchRepository.findByMatchNo(matchNo)
//                .orElseThrow(()->new MatchNotFoundException(ErrorCode.MATCH_NOT_FOUND));
//        return selectedMatch;
//    }
//}
