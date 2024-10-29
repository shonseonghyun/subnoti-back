package com.sunghyun.football.config.batch;

import com.sunghyun.football.domain.noti.domain.enums.ActiveType;
import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import com.sunghyun.football.domain.noti.infrastructure.FreeSubNotiHistoryComparator;
import com.sunghyun.football.domain.noti.infrastructure.entity.FreeSubNotiEntity;
import com.sunghyun.football.domain.noti.infrastructure.entity.FreeSubNotiHistoryEntity;
import com.sunghyun.football.global.feign.PlabFootBallOpenFeignClient;
import com.sunghyun.football.global.feign.dto.PlabMatchInfoResDto;
import com.sunghyun.football.global.mail.MailSendReqDto;
import com.sunghyun.football.global.mail.MailService;
import com.sunghyun.football.global.utils.MatchDateUtils;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FreeSubNotiRegBatchConfig {
    private final EntityManagerFactory entityManagerFactory;
    private final MailService mailService;
    private final int chunkSize=1;
    private final PlabFootBallOpenFeignClient plabFootBallOpenFeignClient;


    @Bean
    public Job freeSubNotiRegJob(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new JobBuilder("freeSubNotiReg",jobRepository)
                .start(freeSubNotiRegStep(transactionManager,jobRepository))
                .build()
                ;
    }

    @Bean
    @JobScope
    public Step freeSubNotiRegStep(PlatformTransactionManager transactionManager,JobRepository jobRepository){
        return new StepBuilder("freeSubNotiRegStep",jobRepository)
                .<FreeSubNotiEntity, FreeSubNotiEntity>chunk(chunkSize,transactionManager)
                .reader(freeSubNotiRegReader(null))
                .processor(freeSubNotiRegProcessor())
                .writer(freeSubNotiRegWriter())
                .build()
                ;
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<FreeSubNotiEntity> freeSubNotiRegReader(@Value("#{jobParameters[nowDt]}") String nowDt){
        Map<String,Object> parameterValues = new HashMap<>();
        parameterValues.put("nowDt",nowDt);

        return new JpaPagingItemReaderBuilder<FreeSubNotiEntity>()
                .pageSize(chunkSize)
                .parameterValues(parameterValues)
                .queryString("SELECT m FROM FreeSubNotiEntity m " +
                        "where startDt>=:nowDt " +
//                        "and sendFlg<>'I' " +
                        "order by startDt,startTm desc ")
                .entityManagerFactory(entityManagerFactory)
                .name("JpaPagingItemReader")
                .build();
    }

    @Bean
    public ItemProcessor<FreeSubNotiEntity, FreeSubNotiEntity> freeSubNotiRegProcessor(){

        return item->{
            //시간 체크
            if(item.getStartDt().compareTo(MatchDateUtils.getNowDtStr())==0){
                if(item.getStartDt().compareTo(MatchDateUtils.getNowTmStr()) <= 0){
                    log.info("매치 시작시간 지났으므로 제외-매치 시작 시간[{}]/현재 시간[{}]",item.getStartTm(),MatchDateUtils.getNowTmStr());
                    return item;
                }
            }

            //플랩 통신
            PlabMatchInfoResDto response = plabFootBallOpenFeignClient.getMatch(item.getMatchNo());
            boolean isManagerSubFree = Boolean.parseBoolean(response.getIs_manager_free());
            boolean isSuperSubFree = Boolean.parseBoolean(response.getIs_super_sub());


            //historyNo 기준으로 내림차순으로 정렬
            item.getFreeSubNotiHistories().sort(new FreeSubNotiHistoryComparator());

            //알림 판단
            //프리 서브 활성화 체킹
            if(item.getFreeSubNotiHistories().isEmpty() || item.getFreeSubNotiHistories().get(0).getActiveType().equals(ActiveType.INACTIVE)){
                if(item.getSubType().equals(FreeSubType.MANAGER_FREE)){
                    if(isManagerSubFree){
                        log.info("매니저프리 활성화");
                        String subject = item.getMatchName() + item.getStartDt().substring(4,6)+"/"+item.getStartDt().substring(6)+item.getStartTm()+"시"+" 매니저 프리 활성화";

                        //메일 발송
                        mailService.send(new MailSendReqDto(item.getEmail(),subject,subject));

                        //발송 내역 생성
                        FreeSubNotiHistoryEntity freeSubNotiHistory =  FreeSubNotiHistoryEntity.builder()
                                .activeType(ActiveType.ACTIVE)
                                .subType(FreeSubType.MANAGER_FREE)
                                .sendDt(MatchDateUtils.getNowDtStr())
                                .sendTm(MatchDateUtils.getNowTmStr())
                                .build();

                        //발송 내역 저장
                        item.getFreeSubNotiHistories().add(freeSubNotiHistory);
                    }
                }

                if(item.getSubType().equals(FreeSubType.SUPER_SUB)){
                    if(isSuperSubFree){
                        log.info("슈퍼서브 활성화");

                        String subject = item.getMatchName() + item.getStartDt().substring(4,6)+"/"+item.getStartDt().substring(6)+item.getStartTm()+"시"+" 슈퍼서브 활성화";

                        //메일 발송
                        mailService.send(new MailSendReqDto(item.getEmail(),subject,subject));

                        //발송 내역 생성
                        FreeSubNotiHistoryEntity freeSubNotiHistory =  FreeSubNotiHistoryEntity.builder()
                                .activeType(ActiveType.ACTIVE)
                                .subType(FreeSubType.SUPER_SUB)
                                .sendDt(MatchDateUtils.getNowDtStr())
                                .sendTm(MatchDateUtils.getNowTmStr())
                                .build();

                        //발송 내역 저장
                        item.getFreeSubNotiHistories().add(freeSubNotiHistory);
                    }
                }
            }

            //프리 서브 비활성화 체킹
            else{
                if(item.getFreeSubNotiHistories().get(0).getActiveType().equals(ActiveType.ACTIVE)){
                    if(item.getSubType().equals(FreeSubType.MANAGER_FREE)){
                        if(!isManagerSubFree){
                            log.info("매니저프리 비활성화");
                            String subject = item.getMatchName() + item.getStartDt().substring(4,6)+"/"+item.getStartDt().substring(6)+item.getStartTm()+"시"+" 매니저 프리 비활성화";

                            //메일 발송
                            mailService.send(new MailSendReqDto(item.getEmail(),subject,subject));

                            //발송 내역 생성
                            FreeSubNotiHistoryEntity freeSubNotiHistory =  FreeSubNotiHistoryEntity.builder()
                                    .activeType(ActiveType.INACTIVE)
                                    .subType(FreeSubType.MANAGER_FREE)
                                    .sendDt(MatchDateUtils.getNowDtStr())
                                    .sendTm(MatchDateUtils.getNowTmStr())
                                    .build();

                            //발송 내역 저장
                            item.getFreeSubNotiHistories().add(freeSubNotiHistory);
                        }
                    }

                    if(item.getSubType().equals(FreeSubType.SUPER_SUB)){
                        if(!isSuperSubFree){
                            log.info("슈퍼서브 비활성화");

                            String subject = item.getMatchName() + item.getStartDt().substring(4,6)+"/"+item.getStartDt().substring(6)+item.getStartTm()+"시"+" 슈퍼서브 비활성화";

                            //메일 발송
                            mailService.send(new MailSendReqDto(item.getEmail(),subject,subject));

                            //발송 내역 생성
                            FreeSubNotiHistoryEntity freeSubNotiHistory =  FreeSubNotiHistoryEntity.builder()
                                    .activeType(ActiveType.INACTIVE)
                                    .subType(FreeSubType.SUPER_SUB)
                                    .sendDt(MatchDateUtils.getNowDtStr())
                                    .sendTm(MatchDateUtils.getNowTmStr())
                                    .build();

                            //발송 내역 저장
                            item.getFreeSubNotiHistories().add(freeSubNotiHistory);
                        }
                    }
                }
            }

            return item;
        };
    }

    @Bean
    public ItemWriter<FreeSubNotiEntity> freeSubNotiRegWriter(){
        JpaItemWriter<FreeSubNotiEntity> writer = new JpaItemWriter<>();
//        writer.setUsePersist(true);
        writer.setEntityManagerFactory(entityManagerFactory);

        return writer;
    }


}
