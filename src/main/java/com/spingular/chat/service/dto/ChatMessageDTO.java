package com.spingular.chat.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.spingular.chat.domain.ChatMessage} entity.
 */
public class ChatMessageDTO implements Serializable {

    private Long id;

    @NotNull
    private String messageSentAt;

    @NotNull
    @Size(max = 65000)
    private String message;

    private Boolean isReceived;

    private Boolean isDelivered;


    private Long chatRoomId;

    private Long chatUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageSentAt() {
        return messageSentAt;
    }

    public void setMessageSentAt(String messageSentAt) {
        this.messageSentAt = messageSentAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean isIsReceived() {
        return isReceived;
    }

    public void setIsReceived(Boolean isReceived) {
        this.isReceived = isReceived;
    }

    public Boolean isIsDelivered() {
        return isDelivered;
    }

    public void setIsDelivered(Boolean isDelivered) {
        this.isDelivered = isDelivered;
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

        ChatMessageDTO chatMessageDTO = (ChatMessageDTO) o;
        if (chatMessageDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chatMessageDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChatMessageDTO{" +
            "id=" + getId() +
            ", messageSentAt='" + getMessageSentAt() + "'" +
            ", message='" + getMessage() + "'" +
            ", isReceived='" + isIsReceived() + "'" +
            ", isDelivered='" + isIsDelivered() + "'" +
            ", chatRoom=" + getChatRoomId() +
            ", chatUser=" + getChatUserId() +
            "}";
    }
}
