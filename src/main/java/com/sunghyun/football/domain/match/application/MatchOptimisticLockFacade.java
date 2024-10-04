package com.sunghyun.football.domain.match.application;

import com.sunghyun.football.domain.match.application.dto.SelectMatchResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchOptimisticLockFacade {
    private final MatchApplication matchApplication;

    public SelectMatchResDto getMatch(final Long matchNo) throws InterruptedException {
        SelectMatchResDto selectMatchResDto = null;
        while(true){

            try{
                selectMatchResDto = matchApplication.getMatch(matchNo);
                break;
            }catch (ObjectOptimisticLockingFailureException e){
                log.error("retry plus view for click");
                Thread.sleep(50);
            }
        }

        return selectMatchResDto;
    }
}
