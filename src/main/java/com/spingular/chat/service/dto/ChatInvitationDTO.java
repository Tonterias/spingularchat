package com.spingular.chat.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.spingular.chat.domain.ChatInvitation} entity.
 */
public class ChatInvitationDTO implements Serializable {

    private Long id;

    private Instant creationDate;

    private Boolean acceptance;

    private Boolean denial;

    private Instant acceptanceDenialDate;


    private Long senderId;

    private Long receiverId;

    private Long chatRoomId;

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

    public Boolean isAcceptance() {
        return acceptance;
    }

    public void setAcceptance(Boolean acceptance) {
        this.acceptance = acceptance;
    }

    public Boolean isDenial() {
        return denial;
    }

    public void setDenial(Boolean denial) {
        this.denial = denial;
    }

    public Instant getAcceptanceDenialDate() {
        return acceptanceDenialDate;
    }

    public void setAcceptanceDenialDate(Instant acceptanceDenialDate) {
        this.acceptanceDenialDate = acceptanceDenialDate;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long chatUserId) {
        this.senderId = chatUserId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long chatUserId) {
        this.receiverId = chatUserId;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChatInvitationDTO chatInvitationDTO = (ChatInvitationDTO) o;
        if (chatInvitationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chatInvitationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChatInvitationDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", acceptance='" + isAcceptance() + "'" +
            ", denial='" + isDenial() + "'" +
            ", acceptanceDenialDate='" + getAcceptanceDenialDate() + "'" +
            ", sender=" + getSenderId() +
            ", receiver=" + getReceiverId() +
            ", chatRoom=" + getChatRoomId() +
            "}";
    }
}
