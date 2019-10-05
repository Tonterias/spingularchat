package com.spingular.chat.service.mapper;

import com.spingular.chat.domain.*;
import com.spingular.chat.service.dto.ChatOffensiveMessageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatOffensiveMessage} and its DTO {@link ChatOffensiveMessageDTO}.
 */
@Mapper(componentModel = "spring", uses = {ChatUserMapper.class, ChatMessageMapper.class})
public interface ChatOffensiveMessageMapper extends EntityMapper<ChatOffensiveMessageDTO, ChatOffensiveMessage> {

    @Mapping(source = "chatUser.id", target = "chatUserId")
    @Mapping(source = "chatMessage.id", target = "chatMessageId")
    ChatOffensiveMessageDTO toDto(ChatOffensiveMessage chatOffensiveMessage);

    @Mapping(source = "chatUserId", target = "chatUser")
    @Mapping(source = "chatMessageId", target = "chatMessage")
    ChatOffensiveMessage toEntity(ChatOffensiveMessageDTO chatOffensiveMessageDTO);

    default ChatOffensiveMessage fromId(Long id) {
        if (id == null) {
            return null;
        }
        ChatOffensiveMessage chatOffensiveMessage = new ChatOffensiveMessage();
        chatOffensiveMessage.setId(id);
        return chatOffensiveMessage;
    }
}
