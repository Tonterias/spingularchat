package com.spingular.chat.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.spingular.chat.domain.enumeration.ChatNotificationReason;

/**
 * A DTO for the {@link com.spingular.chat.domain.ChatNotification} entity.
 */
public class ChatNotificationDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    private ChatNotificationReason chatNotificationReason;


    private Long chatUserId;

    private Set<ChatInvitationDTO> chatInvitations = new HashSet<>();

    private Long chatRoomId;

    private Long chatMessageId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public ChatNotificationReason getChatNotificationReason() {
        return chatNotificationReason;
    }

    public void setChatNotificationReason(ChatNotificationReason chatNotificationReason) {
        this.chatNotificationReason = chatNotificationReason;
    }

    public Long getChatUserId() {
        return chatUserId;
    }

    public void setChatUserId(Long chatUserId) {
        this.chatUserId = chatUserId;
    }

    public Set<ChatInvitationDTO> getChatInvitations() {
        return chatInvitations;
    }

    public void setChatInvitations(Set<ChatInvitationDTO> chatInvitations) {
        this.chatInvitations = chatInvitations;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public Long getChatMessageId() {
        return chatMessageId;
    }

    public void setChatMessageId(Long chatMessageId) {
        this.chatMessageId = chatMessageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChatNotificationDTO chatNotificationDTO = (ChatNotificationDTO) o;
        if (chatNotificationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chatNotificationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChatNotificationDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", chatNotificationReason='" + getChatNotificationReason() + "'" +
            ", chatUser=" + getChatUserId() +
            ", chatRoom=" + getChatRoomId() +
            ", chatMessage=" + getChatMessageId() +
            "}";
    }
}
