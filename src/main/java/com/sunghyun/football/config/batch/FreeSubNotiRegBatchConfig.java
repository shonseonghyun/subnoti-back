package com.sunghyun.football.config.batch;

import com.sunghyun.football.domain.noti.domain.enums.SendFlg;
import com.sunghyun.football.domain.noti.infrastructure.entity.MatchFreeSubNotiEntity;
import com.sunghyun.football.global.feign.PlabFootBallOpenFeignClient;
import com.sunghyun.football.global.feign.dto.PlabMatchInfoResDto;
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
                .<MatchFreeSubNotiEntity,MatchFreeSubNotiEntity>chunk(chunkSize,transactionManager)
                .reader(freeSubNotiRegReader(null))
                .processor(freeSubNotiRegProcessor())
                .writer(freeSubNotiRegWriter())
                .build()
                ;
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<MatchFreeSubNotiEntity> freeSubNotiRegReader(@Value("#{jobParameters[nowDt]}") String nowDt){
        Map<String,Object> parameterValues = new HashMap<>();
        parameterValues.put("nowDt",nowDt);

        return new JpaPagingItemReaderBuilder<MatchFreeSubNotiEntity>()
                .pageSize(chunkSize)
                .parameterValues(parameterValues)
                .queryString("SELECT m FROM MatchFreeSubNotiEntity m " +
                        "where sendFlg<>'I' " +
                        "and startDt>=:nowDt " +
                        "order by startDt,startTm desc ")
                .entityManagerFactory(entityManagerFactory)
                .name("JpaPagingItemReader")
                .build();
    }

    @Bean
    public ItemProcessor<MatchFreeSubNotiEntity,MatchFreeSubNotiEntity> freeSubNotiRegProcessor(){
        return item->{
            //시간 체크
            log.info("매치 종료 시간:{}",item.getEndTm());
            log.info("현재 시간:{}",MatchDateUtils.getNowTmStr());
            if(item.getStartDt().compareTo(MatchDateUtils.getNowDtStr())==0){
                if(item.getEndTm().compareTo(MatchDateUtils.getNowTmStr()) <= 0){
                    log.info("매치 종료일자 지난 case- 조회 시 해당 부분까진 검사하지 않음");
                    return item;
                }
            }

            //플랩 통신
            PlabMatchInfoResDto response = plabFootBallOpenFeignClient.getMatch(item.getMatchNo());
            boolean isManagerSubFree = response.is_manager_free();
            boolean isSuperSubFree = response.is_super_sub();

            log.info("매니저 서브 프리: {}",isManagerSubFree);
            log.info("플래버 서브 프리: {}",isSuperSubFree);

            //알림 판단
            //X인 경우 두 필드가 true로 변한게 있다면 알림 발송, 없다면 종료
            //A인 경우 두 필드가 모두 false 변해있다면 알림 발송, 그외 경우 종료
            if(item.getSendFlg().equals(SendFlg.NOT_SEND)){
                if(isManagerSubFree){
                    log.info("매니저 프리 활성화");
                }
                if(isSuperSubFree){
                    log.info("슈퍼서브 활성화");
                }
            }



            return item;
        };
    }

    @Bean
    public ItemWriter<MatchFreeSubNotiEntity> freeSubNotiRegWriter(){
        JpaItemWriter<MatchFreeSubNotiEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);

        return writer;
    }


}
