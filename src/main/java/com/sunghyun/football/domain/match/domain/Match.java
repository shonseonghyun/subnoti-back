package com.sunghyun.football.domain.match.domain;

import com.sunghyun.football.domain.match.domain.enums.GenderRule;
import com.sunghyun.football.domain.match.domain.enums.MatchState;
import com.sunghyun.football.domain.match.domain.enums.MatchStatus;
import com.sunghyun.football.domain.member.domain.enums.Gender;
import com.sunghyun.football.domain.member.domain.enums.MemberLevelType;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.match.*;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
public class Match {
    private Long matchNo;

    private Long stadiumNo;

    private Long price;

    private Long managerNo;

    private String startDt;

    private String startTm;

    private Integer headCount;

    private MatchState matchState;

    private GenderRule genderRule;

    private MemberLevelType levelRule;

    private List<MatchPlayer> players;

    private List<MemberLevelType> levels;

    private MemberLevelType avgMatchLevel;

    private MatchStatus matchStatus;

    private Integer minPlayerNum;

    private Integer viewCount;

    private Long version;

    public void receivePlayer(Long memberNo, MemberLevelType memberLevelType, Gender gender) {
        MatchPlayer player = MatchPlayer.createPlayer(memberNo);
        checkRegManagerInMatch();
        checkAlreadyPlayerApplyMatch(player);
        checkRuleOfMatch(memberLevelType,gender);
        players.add(player);
        changeMatchStateByPlayerNum();
    }

    private void checkRuleOfMatch(MemberLevelType memberLevelType, Gender gender){
        if(!MemberLevelType.isAvailableApply(this.levelRule, memberLevelType)){
            throw new LevelRuleException(ErrorCode.LEVEL_RULE_REJECT);
        }

        if(!GenderRule.isAvailableApply(this.genderRule,gender)){
            throw new GenderRuleException(ErrorCode.GENDER_RULE_REJECT);
        }
    }

    private void checkRegManagerInMatch(){
        if(matchState == MatchState.MATCH_REG_MANAGER_BEFORE || managerNo==null){
            throw new MatchNotReadyException(ErrorCode.MATCH_NOT_REG_MANAGER);
        }
    }

    public void cancelPlayer(Long memberNo) {
        MatchPlayer player = MatchPlayer.createPlayer(memberNo);

        checkPlayerNotYeyApplyMatch(player);
        cancel(player);
        changeMatchStateByPlayerNum();
    }

    private void cancel(MatchPlayer player){
        final int index = players.indexOf(player);
        players.remove(index);
    }

    private void checkAlreadyPlayerApplyMatch(MatchPlayer player){
        final int idx = this.getPlayers().indexOf(player);
        if(idx!=-1){
            throw new MatchPlayerExistException(ErrorCode.MATCH_PLAYER_EXIST);
        }
    }

    private void checkPlayerNotYeyApplyMatch(MatchPlayer player){
        final int idx = this.getPlayers().indexOf(player);
        if(idx==-1){
            throw new MatchPlayerNotFoundException(ErrorCode.MATCH_PLAYER_NOT_FOUND);
        }
    }

    private void changeMatchStateByPlayerNum(){
        if(players.size()==headCount*3){
            isMatchClosed();
        }
        else if(players.size()>headCount*2+2){
            isMatchAlmostClosed();
        }
        else{
            isMatchAvailable();
        }
    }

    private void isMatchAvailable(){
        setMatchState(MatchState.MATCH_AVAILABLE);
    }

    private void isMatchOpened() {
        setMatchState(MatchState.MATCH_AVAILABLE);
    }


    private void isMatchClosed() {
        setMatchState(MatchState.MATCH_END);
    }

    private void isMatchAlmostClosed() {
        setMatchState(MatchState.MATCH_ALMOST_DONE);
    }

    public void regManager(Long managerNo) {
        this.managerNo=managerNo;
        isMatchOpened();
    }

    public void setAndCalAvgLevel(List<MemberLevelType> levels) {
        this.levels = levels;
        //모든 신청자 수
        int allPlayerNum = levels.size();
        if(allPlayerNum==0){
            setAvgMatchLevel(MemberLevelType.ROOKIE);
            setLevels(levels);
            return;
        }
        
        //rookie 레벨을 제외한 신청자 수
        int playerNumWithoutRookie ;

        int levelsNum = 0;
        int rookieNum = 0;
        
        for(MemberLevelType memberLevelType : levels){
            if(memberLevelType.equals(MemberLevelType.ROOKIE)){
                rookieNum++;
            }
            levelsNum += memberLevelType.getLevelNum();
        }

        playerNumWithoutRookie = allPlayerNum - rookieNum;
        if(playerNumWithoutRookie==0){
            setAvgMatchLevel(MemberLevelType.ROOKIE);
            return;
        }
//        float a = Math.ceil(levelsNum / playerNumWithoutRookie);
        int averageLevelNum = (int)Math.ceil((double) levelsNum / (double)playerNumWithoutRookie);
        MemberLevelType averageLevel = MemberLevelType.getMemberLevel(averageLevelNum);
        setAvgMatchLevel(averageLevel);
        setLevels(levels);
    }

    public void isClicked() {
        viewCount+=1;
    }
}
