package com.sunghyun.football.domain.history.application.service;

import com.sunghyun.football.domain.history.application.dto.SaveMailReqDto;
import com.sunghyun.football.domain.history.domain.MailHistory;
import com.sunghyun.football.domain.history.domain.repository.MailHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailHistoryService {
    private final MailHistoryRepository mailHistoryRepository;

    @Transactional
    public MailHistory saveMailHistory(final SaveMailReqDto saveMailReqDto) {
        MailHistory newMailHistory = MailHistory.createNewMailHistory(
//                saveMailReqDto.getMemberNo(),
                saveMailReqDto.getEmail(),
                saveMailReqDto.getSendDt(),
                saveMailReqDto.getSendTm(),
                saveMailReqDto.getNotiType(),
                saveMailReqDto.getSubject(),
                saveMailReqDto.getContent()
        );

        return mailHistoryRepository.save(newMailHistory);
    }
}
