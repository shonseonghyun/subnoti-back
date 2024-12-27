package com.sunghyun.football.config.batch.singleThread;

import com.sunghyun.football.domain.noti.infrastructure.entity.FreeSubNotiEntity;
import com.sunghyun.football.global.feign.PlabFootBallOpenFeignClient;
import com.sunghyun.football.global.feign.dto.PlabMatchInfoResDto;
import com.sunghyun.football.global.noti.NotiProcessor;
import com.sunghyun.football.global.utils.MatchDateUtils;
import feign.FeignException;
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
    private final int chunkSize = 2;
    private final PlabFootBallOpenFeignClient plabFootBallOpenFeignClient;
    private final NotiProcessor notiProcessor;


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
                .faultTolerant()
                .skip(FeignException.class)
                .skipLimit(100)
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
                        "order by " +
                            "startDt desc," +
                            "startTm desc," +
                            "notiNo desc"
                        )
                    .entityManagerFactory(entityManagerFactory)
                .name("JpaPagingItemReader")
                .build();
    }

    @Bean
    public ItemProcessor<FreeSubNotiEntity, FreeSubNotiEntity> freeSubNotiRegProcessor(){
        return item->{
            log.info("매치 [{}] [{}] 처리",item.getMatchName(),item.getMatchNo());
            final String nowDt = MatchDateUtils.getNowDtStr();

            //시간 예외 체크
            /*주석 이유*/
            /*
               DB에서 데이터 읽어들이는 시점 부터 매치 시작일이 현재일 이상인 경우만 처리하여 읽어들이므로 주석
             */
/*
            //이미 종료된 매치인 경우(매치 시작 일자 < 현재 일자)
            if(item.getStartDt().compareTo(nowDt)<0){
                log.info("이미 종료돤 매치이므로 제외 - 매치 시작 일자[{}]/현재 일자[{}]",item.getStartDt(),nowDt);
                return item;
            }
*/
            if(MatchDateUtils.hasAlreadyPassedOfMatch(item.getStartDt(),item.getStartTm())){
                return item;
            }
//            if(item.getStartDt().compareTo(nowDt)==0){
//                final String nowTm = MatchDateUtils.getNowTmStr();
//                if(item.getStartTm().compareTo(nowTm)<=0){
//                    log.info("매치 시작시간 지났으므로 제외-매치 시작 시간[{}]/현재 시간[{}]",item.getStartTm(),nowTm);
//                    return item;
//                }
//            }

            //플랩 통신
            boolean isManagerSubFree=false;
            boolean isSuperSubFree=false;

//            try{
                PlabMatchInfoResDto response = plabFootBallOpenFeignClient.getMatch(item.getMatchNo());
                isManagerSubFree = Boolean.parseBoolean(response.getIs_manager_free());
                isSuperSubFree = Boolean.parseBoolean(response.getIs_super_sub());
//            }catch (FeignException e){
//                log.error("FeignException 발생");
//                log.error(e.getMessage());
//            }

            log.info("******** notiProcessor.doNotiProcess******");
            notiProcessor.doNotiProcess(item,isManagerSubFree,isSuperSubFree);

            return item;
        };
    }

    @Bean
    public ItemWriter<FreeSubNotiEntity> freeSubNotiRegWriter(){
        JpaItemWriter<FreeSubNotiEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);

        return writer;
    }


}
