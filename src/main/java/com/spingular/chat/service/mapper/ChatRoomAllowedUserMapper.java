package com.spingular.chat.service.mapper;

import com.spingular.chat.domain.*;
import com.spingular.chat.service.dto.ChatRoomAllowedUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatRoomAllowedUser} and its DTO {@link ChatRoomAllowedUserDTO}.
 */
@Mapper(componentModel = "spring", uses = {ChatRoomMapper.class, ChatUserMapper.class})
public interface ChatRoomAllowedUserMapper extends EntityMapper<ChatRoomAllowedUserDTO, ChatRoomAllowedUser> {

    @Mapping(source = "chatRoom.id", target = "chatRoomId")
    @Mapping(source = "chatUser.id", target = "chatUserId")
    ChatRoomAllowedUserDTO toDto(ChatRoomAllowedUser chatRoomAllowedUser);

    @Mapping(source = "chatRoomId", target = "chatRoom")
    @Mapping(source = "chatUserId", target = "chatUser")
    ChatRoomAllowedUser toEntity(ChatRoomAllowedUserDTO chatRoomAllowedUserDTO);

    default ChatRoomAllowedUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        ChatRoomAllowedUser chatRoomAllowedUser = new ChatRoomAllowedUser();
        chatRoomAllowedUser.setId(id);
        return chatRoomAllowedUser;
    }
}
