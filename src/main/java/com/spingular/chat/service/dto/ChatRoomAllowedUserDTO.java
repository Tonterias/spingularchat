package com.spingular.chat.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.spingular.chat.domain.ChatRoomAllowedUser} entity.
 */
public class ChatRoomAllowedUserDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    private Boolean bannedUser;

    private Instant bannedDate;


    private Long chatRoomId;

    private Long chatUserId;

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

    public Boolean isBannedUser() {
        return bannedUser;
    }

    public void setBannedUser(Boolean bannedUser) {
        this.bannedUser = bannedUser;
    }

    public Instant getBannedDate() {
        return bannedDate;
    }

    public void setBannedDate(Instant bannedDate) {
        this.bannedDate = bannedDate;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public Long getChatUserId() {
        return chatUserId;
    }

    public void setChatUserId(Long chatUserId) {
        this.chatUserId = chatUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChatRoomAllowedUserDTO chatRoomAllowedUserDTO = (ChatRoomAllowedUserDTO) o;
        if (chatRoomAllowedUserDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chatRoomAllowedUserDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChatRoomAllowedUserDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", bannedUser='" + isBannedUser() + "'" +
            ", bannedDate='" + getBannedDate() + "'" +
            ", chatRoom=" + getChatRoomId() +
            ", chatUser=" + getChatUserId() +
            "}";
    }
}
