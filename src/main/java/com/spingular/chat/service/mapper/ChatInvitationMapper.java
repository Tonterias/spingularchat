package com.spingular.chat.service.mapper;

import com.spingular.chat.domain.*;
import com.spingular.chat.service.dto.ChatInvitationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatInvitation} and its DTO {@link ChatInvitationDTO}.
 */
@Mapper(componentModel = "spring", uses = {ChatUserMapper.class, ChatRoomMapper.class})
public interface ChatInvitationMapper extends EntityMapper<ChatInvitationDTO, ChatInvitation> {

    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "receiver.id", target = "receiverId")
    @Mapping(source = "chatRoom.id", target = "chatRoomId")
    ChatInvitationDTO toDto(ChatInvitation chatInvitation);

    @Mapping(source = "senderId", target = "sender")
    @Mapping(source = "receiverId", target = "receiver")
    @Mapping(source = "chatRoomId", target = "chatRoom")
    @Mapping(target = "chatNotifications", ignore = true)
    @Mapping(target = "removeChatNotification", ignore = true)
    ChatInvitation toEntity(ChatInvitationDTO chatInvitationDTO);

    default ChatInvitation fromId(Long id) {
        if (id == null) {
            return null;
        }
        ChatInvitation chatInvitation = new ChatInvitation();
        chatInvitation.setId(id);
        return chatInvitation;
    }
}
