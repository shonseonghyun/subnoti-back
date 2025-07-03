package com.sunghyun.football.config.batch.config.thread.singleThread;

import com.sunghyun.football.config.batch.reader.DtoConvertJpaPagingItemReader;
import com.sunghyun.football.config.batch.reader.builder.DtoConvertJpaPagingItemReaderBuilder;
import com.sunghyun.football.config.batch.listener.process.AbstractProcessListener;
import com.sunghyun.football.config.batch.listener.read.AbstractReadListener;
import com.sunghyun.football.config.batch.listener.skip.CommonSkipListener;
import com.sunghyun.football.config.batch.listener.write.AbstractWriteListener;
import com.sunghyun.football.config.batch.noti.NotiProcessor;
import com.sunghyun.football.domain.noti.domain.FreeSubNoti;
import com.sunghyun.football.domain.noti.domain.repository.FreeSubNotiRepository;
import com.sunghyun.football.domain.noti.infrastructure.entity.FreeSubNotiEntity;
import com.sunghyun.football.global.feign.PlabFootBallOpenFeignClient;
import com.sunghyun.football.global.feign.dto.PlabMatchInfoResDto;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
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
    private final FreeSubNotiRepository freeSubNotiRepository;

    /* listener */
    private final AbstractReadListener<FreeSubNoti> freeSubNotiReadListener;
    private final AbstractProcessListener<FreeSubNoti,FreeSubNoti> freeSubNotiProcessListener;
    private final AbstractWriteListener<FreeSubNoti> freeSubNotiWriteListener;
    private final CommonSkipListener<FreeSubNoti,FreeSubNoti> skipListener;


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
                .listener(freeSubNotiReadListener)
                .listener(freeSubNotiProcessListener)
                .listener(freeSubNotiWriteListener)
                .faultTolerant()
                .skip(FeignException.class)
                .skip(SocketTimeoutException.class)
                .skipLimit(Integer.MAX_VALUE)
                .noRollback(FeignException.class) // FeignException이 터져도 “Chunk 트랜잭션을 아예 롤백하지 않고” 바로 다음 아이템으로 넘어가기 때문에, 불필요한 롤백·재시도 사이클이 사라집니다(롤백 & 재시도 오버헤드 제거, Cursor 재조정 비용 감소, 트랜잭션 관리 콜백 비용 절감)
                .noRollback(SocketTimeoutException.class) // FeignException이 터져도 “Chunk 트랜잭션을 아예 롤백하지 않고” 바로 다음 아이템으로 넘어가기 때문에, 불필요한 롤백·재시도 사이클이 사라집니다(롤백 & 재시도 오버헤드 제거, Cursor 재조정 비용 감소, 트랜잭션 관리 콜백 비용 절감)
                .processorNonTransactional()
                .listener(skipListener) //SkipListener은 반드시 faultTolerant 뒤에 위치해야 함
                .build()
                ;
    }


    @Bean
    @StepScope
    public DtoConvertJpaPagingItemReader<FreeSubNotiEntity,FreeSubNoti> freeSubNotiRegCustomReader(@Value("#{jobParameters[nowDt]}") String nowDt){
        Map<String,Object> parameterValues = new HashMap<>();
        parameterValues.put("nowDt",nowDt);

        return new DtoConvertJpaPagingItemReaderBuilder<FreeSubNotiEntity,FreeSubNoti>()
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
                .name("DtoConvertJpaPagingItemReader")
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
}
