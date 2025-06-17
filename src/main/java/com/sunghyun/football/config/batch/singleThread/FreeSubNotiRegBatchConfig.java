package com.sunghyun.football.config.batch.singleThread;

import com.sunghyun.football.config.batch.CustomJpaPagingItemReader;
import com.sunghyun.football.config.batch.CustomJpaPagingItemReaderBuilder;
import com.sunghyun.football.domain.noti.domain.FreeSubNoti;
import com.sunghyun.football.domain.noti.domain.repository.FreeSubNotiRepository;
import com.sunghyun.football.domain.noti.infrastructure.entity.FreeSubNotiEntity;
import com.sunghyun.football.global.feign.PlabFootBallOpenFeignClient;
import com.sunghyun.football.global.feign.dto.PlabMatchInfoResDto;
import com.sunghyun.football.global.noti.NotiProcessor;
import com.sunghyun.football.global.utils.MatchDateUtils;
import feign.FeignException;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FreeSubNotiRegBatchConfig {
    private final EntityManagerFactory entityManagerFactory;
    private final int chunkSize = 5;
    private final PlabFootBallOpenFeignClient plabFootBallOpenFeignClient;
    private final NotiProcessor notiProcessor;
    private final FreeSubNotiRepository freeSubNotiRepository;


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
                .<FreeSubNoti,FreeSubNoti>chunk(chunkSize,transactionManager)
                .reader(freeSubNotiRegCustomReader(null))
                .processor(freeSubNotiRegProcessor())
                .writer(freeSubNotiRegWriter())
                .listener(readListener())
                .listener(processListener())
                .listener(writerListener())
                .faultTolerant()
                .skip(FeignException.class)
                .skip(SocketTimeoutException.class)
                .skipLimit(Integer.MAX_VALUE)
                .noRollback(FeignException.class) // FeignException이 터져도 “Chunk 트랜잭션을 아예 롤백하지 않고” 바로 다음 아이템으로 넘어가기 때문에, 불필요한 롤백·재시도 사이클이 사라집니다(롤백 & 재시도 오버헤드 제거, Cursor 재조정 비용 감소, 트랜잭션 관리 콜백 비용 절감)
                .noRollback(SocketTimeoutException.class) // FeignException이 터져도 “Chunk 트랜잭션을 아예 롤백하지 않고” 바로 다음 아이템으로 넘어가기 때문에, 불필요한 롤백·재시도 사이클이 사라집니다(롤백 & 재시도 오버헤드 제거, Cursor 재조정 비용 감소, 트랜잭션 관리 콜백 비용 절감)
                .processorNonTransactional()
                .listener(skipListener2()) //SkipListener은 반드시 faultTolerant 뒤에 위치해야 함
                .build()
                ;
    }

    @Bean
    @StepScope
    public JpaCursorItemReader<FreeSubNotiEntity> freeSubNotiJpaCursorItemReader(@Value("#{jobParameters[nowDt]}") String nowDt){
        Map<String,Object> parameterValues = new HashMap<>();
        parameterValues.put("nowDt",nowDt);

        return new JpaCursorItemReaderBuilder<FreeSubNotiEntity>()
//                .pageSize(chunkSize)
                .parameterValues(parameterValues)
//                .queryString("SELECT m FROM FreeSubNotiEntity m " +
//                        "where startDt>=:nowDt " +
//                        "order by " +
//                        "startDt desc," +
//                        "startTm desc," +
//                        "notiNo desc"
//                )
                .queryString("SELECT DISTINCT m FROM FreeSubNotiEntity m " +
                        "LEFT JOIN FETCH m.freeSubNotiHistories h "+
                        "where m.startDt>=:nowDt " +
                        "order by " +
                        "m.startDt desc," +
                        "m.startTm desc," +
                        "m.notiNo desc"
                )
                .entityManagerFactory(entityManagerFactory)
                .name("jpaCursorItemReader")
                .saveState(false)
                .build();
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
                        "order by " +
                            "startDt desc," +
                            "startTm desc," +
                            "notiNo desc"
                        )
//                .queryString("SELECT m FROM FreeSubNotiEntity m " +
//                        "LEFT JOIN FETCH m.freeSubNotiHistories h "+
//                        "where m.startDt>=:nowDt " +
//                        "order by " +
//                        "m.startDt desc," +
//                        "m.startTm desc," +
//                        "m.notiNo desc"
//                )
                    .entityManagerFactory(entityManagerFactory)
                .name("JpaPagingItemReader")
                .build();
    }

    @Bean
    @StepScope
    public CustomJpaPagingItemReader<FreeSubNotiEntity,FreeSubNoti> freeSubNotiRegCustomReader(@Value("#{jobParameters[nowDt]}") String nowDt){
        Map<String,Object> parameterValues = new HashMap<>();
        parameterValues.put("nowDt",nowDt);

        return new CustomJpaPagingItemReaderBuilder<FreeSubNotiEntity,FreeSubNoti>()
                .pageSize(chunkSize)
                .parameterValues(parameterValues)
                .queryString("SELECT m FROM FreeSubNotiEntity m " +
                        "where startDt>=:nowDt " +
                        "order by " +
                        "startDt desc," +
                        "startTm desc," +
                        "notiNo desc"
                )
                .entityManagerFactory(entityManagerFactory)
                .name("CustomJpaPagingItemReader")
                .dtoClass(FreeSubNoti.class)
                .build();
    }

    @Bean
    public ItemProcessor<FreeSubNoti,FreeSubNoti> freeSubNotiRegProcessor(){
        return item->{
                if(MatchDateUtils.hasAlreadyPassedOfMatch(item.getStartDt(),item.getStartTm())){
                    log.info("매치 [{}] [{}] 처리 패스",item.getMatchName(),item.getMatchNo());
                    return null; // null 리턴된 항목은 이후 Writer 단계로 절대 전달되지 않는다.
                }

                //플랩 통신
                PlabMatchInfoResDto response = plabFootBallOpenFeignClient.getMatch(item.getMatchNo());
                boolean isManagerSubFree = Boolean.parseBoolean(response.getIs_manager_free());
                boolean isSuperSubFree = Boolean.parseBoolean(response.getIs_super_sub());

                notiProcessor.doNotiProcess(item,isManagerSubFree,isSuperSubFree);

                return item;
        };
    }

    @Bean
    public ItemWriter<FreeSubNoti> freeSubNotiRegWriter(){
        return chunk ->{
            freeSubNotiRepository.saveAll(new ArrayList<>(chunk.getItems()));
        };
    }

    public ItemReadListener<FreeSubNoti> readListener() {
        return new ItemReadListener<FreeSubNoti>() {
            @Override
            public void afterRead(FreeSubNoti item) {
                String threadName = Thread.currentThread().getName();
                log.info("[{}]Read Item: 노티 요청 번호[{}] 매치명[{}]", threadName, item.getNotiNo(),item.getMatchName());
            }
        };
    }


    public ItemProcessListener<FreeSubNoti,FreeSubNoti> processListener() {
        return new ItemProcessListener<FreeSubNoti,FreeSubNoti>() {
            @Override
            public void beforeProcess(FreeSubNoti item) {
                String threadName = Thread.currentThread().getName();
                log.info("[{}]Start Process Item: 노티 요청 번호[{}] 매치명[{}]", threadName, item.getNotiNo(),item.getMatchName());
            }

            @Override
            public void afterProcess(FreeSubNoti item, FreeSubNoti result) {
                String threadName = Thread.currentThread().getName();
                log.info("[{}]End Process Item: 노티 요청 번호[{}] 매치명[{}]", threadName, item.getNotiNo(),item.getMatchName());
            }

//            @Override
//            public void onProcessError(FreeSubNotiEntity item, Exception e) {
//                log.warn("[{}]Skip Item: 노티 요청 번호[{}] 매치명[{}]: [{}]", Thread.currentThread().getName(), item.getNotiNo(),item.getMatchName(),e.getMessage());
//            }
        };
    }

    public ItemWriteListener<FreeSubNoti> writerListener(){
        return new ItemWriteListener<FreeSubNoti>() {
            @Override
            public void beforeWrite(Chunk<? extends FreeSubNoti> items) {
//                ItemWriteListener.super.beforeWrite(items);
                log.info(String.valueOf(items.size()));
                for(FreeSubNoti item:items){
                    log.info("[{}]Before Write Item: 노티 요청 번호[{}] 매치명[{}]", Thread.currentThread().getName(), item.getNotiNo(),item.getMatchName());
                }
            }

            @Override
            public void afterWrite(Chunk<? extends FreeSubNoti> items) {
//                ItemWriteListener.super.beforeWrite(items);
                log.info(String.valueOf(items.size()));
                for(FreeSubNoti item:items){
                    log.info("[{}]After Write Item: 노티 요청 번호[{}] 매치명[{}]", Thread.currentThread().getName(), item.getNotiNo(),item.getMatchName());
                }
            }
        };
    }

    @Bean
    public SkipListener<FreeSubNotiEntity,FreeSubNotiEntity> skipListener2() {
        return new SkipListener<>() {
            @Override
            public void onSkipInProcess(FreeSubNotiEntity item, Throwable t) {
                log.warn("PROCESS SKIP notiNo={} because {}", item.getNotiNo(), t.getMessage());
            }
            @Override
            public void onSkipInWrite(FreeSubNotiEntity item, Throwable t) {
                log.warn("WRITE   SKIP notiNo={} because {}",   item.getNotiNo(), t.getMessage());
            }
            @Override
            public void onSkipInRead(Throwable t) {
                log.warn("READ    SKIP because {}", t.getMessage());
            }
        };
    }


}
