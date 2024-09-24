package com.sunghyun.football.domain.match.domain.repository;

import com.sunghyun.football.domain.match.domain.Match;
import com.sunghyun.football.domain.match.domain.dto.SearchMatchesReqDto;

import java.util.List;
import java.util.Optional;

public interface MatchRepository {
    Match save(Match match);

    Optional<Match> findByMatchNo(Long matchNo);

    Optional<Match> findByMatchNoPessimistic(Long matchNo);

    void deleteMatch(Long matchNo);

    Match deletePlayer(Match match,Long playerNo);

    int findMatchPlayerByMemberNoAndPlayDt(Long memberNo, String startDt, String startTm);

    int findByStadiumNoAndPlayDt(Long stadiumNo, String startDt, String startTm);

    List<Match> findAll();

    List<Match> findAllByConditions(SearchMatchesReqDto searchMatchesReqDto);
}
