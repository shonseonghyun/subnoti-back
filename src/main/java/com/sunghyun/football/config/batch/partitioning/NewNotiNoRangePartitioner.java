package com.sunghyun.football.config.batch.partitioning;

import com.sunghyun.football.domain.noti.domain.repository.FreeSubNotiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class NewNotiNoRangePartitioner implements Partitioner {

    private final FreeSubNotiRepository freeSubNotiRepository;
    private final String startDt;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
//        ThreadPoolExecutor threadPoolExecutor= new ThreadPoolExecutor(10, 50, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        long min = freeSubNotiRepository.getMinNotiNo(startDt)
                .orElse(0L);
        long max = freeSubNotiRepository.getMaxNotiNo(startDt)
                .orElse(0L);

        if (min == 0L || max == 0L) {
            log.info("partitioner 조회 대상이 존재하지 않습니다.");
            return Collections.emptyMap(); // 이러면 해당 Step은 실행되지 않음
        }

        //하나의 파티션에서 처리할 row 수 = 나머지 존재하지 않는 경우 몫, 존재하는 경우 몫+1
        long targetSize =  (max-min+1)%gridSize==0 ? (max-min+1)/gridSize : (max-min+1)/gridSize+1;

        Map<String, ExecutionContext> result = new HashMap<>();
        long partitionNumber = 0;
        long start=min;
        long end=min+targetSize-1;

        while(start<=max){
            ExecutionContext value = new ExecutionContext();
            result.put("partition" + partitionNumber, value);

            if(end>max){
                end=max;
            }

            value.putLong("minNotiNo",start);
            value.putLong("maxNotiNo",end);

            log.info("Partition {}: minNotiNo = {}, maxNotiNo = {}", partitionNumber, start, end);

            start+=targetSize;
            end+=targetSize;
            partitionNumber++;
        }

        return result;
    }
}
