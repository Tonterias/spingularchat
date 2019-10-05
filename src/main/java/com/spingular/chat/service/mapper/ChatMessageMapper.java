package com.spingular.chat.service.mapper;

import com.spingular.chat.domain.*;
import com.spingular.chat.service.dto.ChatMessageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatMessage} and its DTO {@link ChatMessageDTO}.
 */
@Mapper(componentModel = "spring", uses = {ChatRoomMapper.class, ChatUserMapper.class})
public interface ChatMessageMapper extends EntityMapper<ChatMessageDTO, ChatMessage> {

    @Mapping(source = "chatRoom.id", target = "chatRoomId")
    @Mapping(source = "chatUser.id", target = "chatUserId")
    ChatMessageDTO toDto(ChatMessage chatMessage);

    @Mapping(target = "chatNotifications", ignore = true)
    @Mapping(target = "removeChatNotification", ignore = true)
    @Mapping(target = "chatOffensiveMessages", ignore = true)
    @Mapping(target = "removeChatOffensiveMessage", ignore = true)
    @Mapping(source = "chatRoomId", target = "chatRoom")
    @Mapping(source = "chatUserId", target = "chatUser")
    ChatMessage toEntity(ChatMessageDTO chatMessageDTO);

    default ChatMessage fromId(Long id) {
        if (id == null) {
            return null;
        }
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(id);
        return chatMessage;
    }
}
