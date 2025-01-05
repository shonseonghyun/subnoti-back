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
    private final int chunkSize = 5;
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
            try{
                if(MatchDateUtils.hasAlreadyPassedOfMatch(item.getStartDt(),item.getStartTm())){
                    log.info("매치 [{}] [{}] 처리 패스",item.getMatchName(),item.getMatchNo());
                    return item;
                }

                //플랩 통신
                PlabMatchInfoResDto response = plabFootBallOpenFeignClient.getMatch(item.getMatchNo());
                boolean isManagerSubFree = Boolean.parseBoolean(response.getIs_manager_free());
                boolean isSuperSubFree = Boolean.parseBoolean(response.getIs_super_sub());

                notiProcessor.doNotiProcess(item,isManagerSubFree,isSuperSubFree);

                return item;

            }catch (Exception e){
                log.error("Error processing item with notiNo [{}]: {}", item.getNotiNo(), e.getMessage());
                return null;
            }
        };
    }

    @Bean
    public ItemWriter<FreeSubNotiEntity> freeSubNotiRegWriter(){
        JpaItemWriter<FreeSubNotiEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);

        return writer;
    }


}
