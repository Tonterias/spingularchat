package com.spingular.chat.service.mapper;

import com.spingular.chat.domain.*;
import com.spingular.chat.service.dto.ChatUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatUser} and its DTO {@link ChatUserDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ChatPhotoMapper.class})
public interface ChatUserMapper extends EntityMapper<ChatUserDTO, ChatUser> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "chatPhoto.id", target = "chatPhotoId")
    ChatUserDTO toDto(ChatUser chatUser);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "chatPhotoId", target = "chatPhoto")
    @Mapping(target = "chatRooms", ignore = true)
    @Mapping(target = "removeChatRoom", ignore = true)
    @Mapping(target = "senders", ignore = true)
    @Mapping(target = "removeSender", ignore = true)
    @Mapping(target = "receivers", ignore = true)
    @Mapping(target = "removeReceiver", ignore = true)
    @Mapping(target = "chatMessages", ignore = true)
    @Mapping(target = "removeChatMessage", ignore = true)
    @Mapping(target = "chatRoomAllowedUsers", ignore = true)
    @Mapping(target = "removeChatRoomAllowedUser", ignore = true)
    @Mapping(target = "chatOffensiveMessages", ignore = true)
    @Mapping(target = "removeChatOffensiveMessage", ignore = true)
    @Mapping(target = "chatNotifications", ignore = true)
    @Mapping(target = "removeChatNotification", ignore = true)
    ChatUser toEntity(ChatUserDTO chatUserDTO);

    default ChatUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        ChatUser chatUser = new ChatUser();
        chatUser.setId(id);
        return chatUser;
    }
}
