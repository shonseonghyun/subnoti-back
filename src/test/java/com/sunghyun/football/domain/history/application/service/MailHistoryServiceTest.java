package com.sunghyun.football.domain.history.application.service;

import com.sunghyun.football.domain.history.application.dto.SaveMailReqDto;
import com.sunghyun.football.domain.history.domain.MailHistory;
import com.sunghyun.football.domain.history.domain.repository.MailHistoryRepository;
import com.sunghyun.football.global.noti.type.NotiType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class MailHistoryServiceTest {
    private final Long historyNo=1L;
    private final NotiType notiType = NotiType.NOTI_FREE_SUB;
    private final String email = "abc@naver.com";
    private final String subject = "subject";
    private final String content = "content";
    private final String sendDt = "20250701";
    private final String sendTm = "170000";

    @Mock
    private MailHistoryRepository mailHistoryRepository;

    @InjectMocks
    private MailHistoryService target;

    @Test
    void mailHistory_저장_성공(){
        //given
        doReturn(createSavedMailHistory())
                .when(mailHistoryRepository).save(any(MailHistory.class));
//                .when(mailHistoryRepository).save(createNewMailHistory()); //MailHistoryService 객체 내부에서 직접 만들어서 save한다. Service 객체 내부에서 만든 객체와 내가 테스트를 위해 만든 객체가 서로 다르기에 에러 발생한다.

        //when
        final MailHistory result = target.saveMailHistory(new SaveMailReqDto(
//                memberNo,
                email,
                sendDt,
                sendTm,
                notiType,
                subject,
                content
        ));

        //then
        Assertions.assertThat(result.getHistoryNo()).isNotNull();
    }

    private MailHistory createNewMailHistory(){
        return  new MailHistory(
                null,
//                memberNo,
                email,
                sendDt,
                sendTm,
                notiType,
                subject,
                content
        );
    }

    private MailHistory createSavedMailHistory(){
        return  new MailHistory(
                historyNo,
//                memberNo,
                email,
                sendDt,
                sendTm,
                notiType,
                subject,
                content
        );
    }
}