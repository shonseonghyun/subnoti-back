package com.sunghyun.football.config.batch.multiThread;

import com.sunghyun.football.config.batch.reader.CustomJpaPagingItemReader;
import com.sunghyun.football.domain.noti.infrastructure.entity.FreeSubNotiEntity;
import com.sunghyun.football.global.feign.PlabFootBallOpenFeignClient;
import com.sunghyun.football.global.feign.dto.PlabMatchInfoResDto;
import com.sunghyun.football.global.noti.NotiProcessor;
import com.sunghyun.football.global.utils.MatchDateUtils;
import feign.FeignException;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FreeSubNotiRegBatchMultiThreadConfig {
    private final EntityManagerFactory entityManagerFactory;
    private final int chunkSize=5;
    private final PlabFootBallOpenFeignClient plabFootBallOpenFeignClient;
    private final NotiProcessor notiProcessor;
    private final TaskExecutor taskExecutor;


    @Bean
    public Job freeSubNotiRegMultiThreadJob(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new JobBuilder("freeSubNotiRegMultiThreadJob",jobRepository)
                .start(freeSubNotiRegMultiThreadStep(transactionManager,jobRepository))
                .build()
                ;
    }

    @Bean
    @JobScope
    public Step freeSubNotiRegMultiThreadStep(PlatformTransactionManager transactionManager,JobRepository jobRepository){
        return new StepBuilder("freeSubNotiRegMultiThreadStep",jobRepository)
                .<FreeSubNotiEntity, FreeSubNotiEntity>chunk(chunkSize,transactionManager)
                .reader(freeSubNotiRegMultiThreadReader(null))
                .processor(freeSubNotiRegMultiThreadProcessor())
                .writer(freeSubNotiRegMultiThreadWriter())
                .taskExecutor(taskExecutor)
//                .throttleLimit(4)
                .listener(readListener())
                .listener(processListener())
                .faultTolerant()
                .skip(FeignException.class)
                .skipLimit(100)
                .build()
                ;
    }

    @Bean
    @StepScope
    public AbstractPagingItemReader<FreeSubNotiEntity> freeSubNotiRegMultiThreadReader(@Value("#{jobParameters[nowDt]}") String nowDt){
//        Map<String,Object> parameterValues = new HashMap<>();
//        parameterValues.put("nowDt",nowDt);
//
//        JpaPagingItemReader reader= new JpaPagingItemReaderBuilder<FreeSubNotiEntity>()
//                .parameterValues(parameterValues)
//                .queryString("SELECT m FROM FreeSubNotiEntity m " +
//                                "where startDt>=:nowDt " +
//                                "order by " +
//                                "startDt desc," +
//                                "startTm desc," +
//                                "notiNo desc"
//                )
//                .entityManagerFactory(entityManagerFactory)
//                .name("JpaPagingItemReader")
//                .pageSize(chunkSize)
//                .saveState(false)
//                .build();
//
//        return reader;
        Map<String,Object> parameterValues = new HashMap<>();
        parameterValues.put("nowDt",nowDt);

        CustomJpaPagingItemReader<FreeSubNotiEntity> reader = new CustomJpaPagingItemReader<>();
        reader.setParameterValues(parameterValues);
        reader.setQueryString("SELECT m FROM FreeSubNotiEntity m " +
                "where startDt>=:nowDt " +
                "order by " +
                "startDt desc," +
                "startTm desc," +
                "notiNo desc"
        );
//        reader.setQueryString("SELECT m FROM FreeSubNotiEntity m " +
//                "LEFT JOIN FETCH m.freeSubNotiHistories h "+
//                "where m.startDt>=:nowDt " +
//                "order by " +
//                "m.startDt desc," +
//                "m.startTm desc," +
//                "m.notiNo desc"
//        );
//        reader.setQueryString("SELECT h FROM FreeSubNotiHistoryEntity h " +
//                "RIGHT JOIN FETCH h.freeSubNotiEntity m "+
//                "where m.startDt>=:nowDt " +
//                "order by " +
//                "m.startDt desc," +
//                "m.startTm desc," +
//                "m.notiNo desc"
//        );
        reader.setPageSize(chunkSize);
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setName("customPagingReader");
        reader.setSaveState(false);

        return reader;
    }

    @Bean
    public ItemProcessor<FreeSubNotiEntity, FreeSubNotiEntity> freeSubNotiRegMultiThreadProcessor(){
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
    public ItemWriter<FreeSubNotiEntity> freeSubNotiRegMultiThreadWriter(){
        JpaItemWriter<FreeSubNotiEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);

        return writer;
    }

    public ItemReadListener<FreeSubNotiEntity> readListener() {
        return new ItemReadListener<FreeSubNotiEntity>() {
            @Override
            public void afterRead(FreeSubNotiEntity item) {
                String threadName = Thread.currentThread().getName();
                log.info("[{}]Read Item: 노티 요청 번호[{}] 매치명[{}]", threadName, item.getNotiNo(),item.getMatchName());
            }
        };
    }

    public ItemProcessListener<FreeSubNotiEntity,FreeSubNotiEntity> processListener() {
        return new ItemProcessListener<FreeSubNotiEntity,FreeSubNotiEntity>() {
            @Override
            public void beforeProcess(FreeSubNotiEntity item) {
                String threadName = Thread.currentThread().getName();
                log.info("[{}]Start Process Item: 노티 요청 번호[{}] 매치명[{}]", threadName, item.getNotiNo(),item.getMatchName());
            }

            @Override
            public void afterProcess(FreeSubNotiEntity item, FreeSubNotiEntity result) {
                String threadName = Thread.currentThread().getName();
                log.info("[{}]End Process Item: 노티 요청 번호[{}] 매치명[{}]", threadName, item.getNotiNo(),item.getMatchName());
            }
        };


    }
}
