package com.sunghyun.football.global.noti.message.build;

import com.sunghyun.football.global.noti.type.NotiType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MessageBuilderFactory {
    private final Map<NotiType,MessageBuilder<?>> messageBuilderMap = new HashMap<>();

    public MessageBuilderFactory(List<MessageBuilder<?>> messageBuilderList){
        for(MessageBuilder<?> messageBuilder:messageBuilderList){
            messageBuilderMap.put(messageBuilder.getNotiType(),messageBuilder);
        }
    }

    public <T> String buildSubject(NotiType notiType,T messageDto){
        MessageBuilder<T> messageBuilder = (MessageBuilder<T>) messageBuilderMap.get(notiType);
        if (messageBuilder == null) throw new IllegalArgumentException("No builder found for type: " + notiType);

        return messageBuilder.buildSubject(messageDto);
    }

    public <T> String buildContent(NotiType notiType,T messageDto){
        MessageBuilder<T> messageBuilder = (MessageBuilder<T>) messageBuilderMap.get(notiType);
        if (messageBuilder == null) throw new IllegalArgumentException("No builder found for type: " + notiType);
        return messageBuilder.buildContent(messageDto);
    }

}
