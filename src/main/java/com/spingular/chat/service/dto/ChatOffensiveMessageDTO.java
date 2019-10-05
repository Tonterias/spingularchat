package com.spingular.chat.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.spingular.chat.domain.ChatOffensiveMessage} entity.
 */
public class ChatOffensiveMessageDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    private Boolean isOffensive;


    private Long chatUserId;

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

    public Boolean isIsOffensive() {
        return isOffensive;
    }

    public void setIsOffensive(Boolean isOffensive) {
        this.isOffensive = isOffensive;
    }

    public Long getChatUserId() {
        return chatUserId;
    }

    public void setChatUserId(Long chatUserId) {
        this.chatUserId = chatUserId;
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

        ChatOffensiveMessageDTO chatOffensiveMessageDTO = (ChatOffensiveMessageDTO) o;
        if (chatOffensiveMessageDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chatOffensiveMessageDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChatOffensiveMessageDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", isOffensive='" + isIsOffensive() + "'" +
            ", chatUser=" + getChatUserId() +
            ", chatMessage=" + getChatMessageId() +
            "}";
    }
}
