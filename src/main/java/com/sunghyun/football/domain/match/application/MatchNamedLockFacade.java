//package com.sunghyun.football.domain.match.application;
//
//import com.sunghyun.football.domain.match.application.dto.SelectMatchResDto;
//import com.sunghyun.football.domain.match.domain.repository.MatchRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class MatchNamedLockFacade {
//    private final MatchApplication matchApplication;
//    private final MatchRepository matchRepository;
//
//    @Transactional
//    public SelectMatchResDto getMatch(Long matchNo){
//        SelectMatchResDto selectMatchResDto = null;
//
//        try{
//            matchRepository.getLock(matchNo.toString());
//            selectMatchResDto = matchApplication.getMatch(matchNo);
//        }finally {
//            matchRepository.releaseLock(matchNo.toString());
//        }
//
//        return selectMatchResDto;
//    }
//
//}
