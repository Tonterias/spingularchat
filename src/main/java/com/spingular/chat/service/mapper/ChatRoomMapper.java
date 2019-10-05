package com.spingular.chat.service.mapper;

import com.spingular.chat.domain.*;
import com.spingular.chat.service.dto.ChatRoomDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatRoom} and its DTO {@link ChatRoomDTO}.
 */
@Mapper(componentModel = "spring", uses = {ChatUserMapper.class})
public interface ChatRoomMapper extends EntityMapper<ChatRoomDTO, ChatRoom> {

    @Mapping(source = "chatUser.id", target = "chatUserId")
    ChatRoomDTO toDto(ChatRoom chatRoom);

    @Mapping(target = "chatMessages", ignore = true)
    @Mapping(target = "removeChatMessage", ignore = true)
    @Mapping(target = "chatRoomAllowedUsers", ignore = true)
    @Mapping(target = "removeChatRoomAllowedUser", ignore = true)
    @Mapping(target = "chatNotifications", ignore = true)
    @Mapping(target = "removeChatNotification", ignore = true)
    @Mapping(target = "chatInvitations", ignore = true)
    @Mapping(target = "removeChatInvitation", ignore = true)
    @Mapping(source = "chatUserId", target = "chatUser")
    ChatRoom toEntity(ChatRoomDTO chatRoomDTO);

    default ChatRoom fromId(Long id) {
        if (id == null) {
            return null;
        }
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(id);
        return chatRoom;
    }
}
