//package com.sunghyun.football.config.batch.partitioning;
//
//import com.sunghyun.football.domain.noti.domain.repository.FreeSubNotiRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.partition.support.Partitioner;
//import org.springframework.batch.item.ExecutionContext;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//@RequiredArgsConstructor
//public class NotiNoRangePartitioner implements Partitioner {
//
//    private final FreeSubNotiRepository freeSubNotiRepository;
//    private final String nowDt ;
//    private final String startTm ;
//    private final String endTm ;
//
//    @Override
//    public Map<String, ExecutionContext> partition(int gridSize) {
//        Map<String, ExecutionContext> result = new HashMap<>();
//
//        List<Long> notiNoList = Arrays.asList(1L,3L,4L);
////        freeSubNotiRepository.getMaxNotiNo(nowDt,startTm,endTm);
////        Arrays.sort(notiNoList);
//
//        Long maxNotiNo = 1L;
//        Long minNotiNo = 2L;
//
//        ExecutionContext value1 = new ExecutionContext();
//        value1.put("minNo",minNotiNo);
//
//        ExecutionContext value2 = new ExecutionContext();
//        value2.put("maxNo",maxNotiNo);
//
//        result.put("partition" + 1, value1);
//        result.put("partition" + 2, value2);
//
//        return result;
//    }
//}
