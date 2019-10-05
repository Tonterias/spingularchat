package com.spingular.chat.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.spingular.chat.domain.enumeration.ChatNotificationReason;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.spingular.chat.domain.ChatNotification} entity. This class is used
 * in {@link com.spingular.chat.web.rest.ChatNotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /chat-notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ChatNotificationCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ChatNotificationReason
     */
    public static class ChatNotificationReasonFilter extends Filter<ChatNotificationReason> {

        public ChatNotificationReasonFilter() {
        }

        public ChatNotificationReasonFilter(ChatNotificationReasonFilter filter) {
            super(filter);
        }

        @Override
        public ChatNotificationReasonFilter copy() {
            return new ChatNotificationReasonFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private ChatNotificationReasonFilter chatNotificationReason;

    private LongFilter chatUserId;

    private LongFilter chatInvitationId;

    private LongFilter chatRoomId;

    private LongFilter chatMessageId;

    public ChatNotificationCriteria(){
    }

    public ChatNotificationCriteria(ChatNotificationCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.chatNotificationReason = other.chatNotificationReason == null ? null : other.chatNotificationReason.copy();
        this.chatUserId = other.chatUserId == null ? null : other.chatUserId.copy();
        this.chatInvitationId = other.chatInvitationId == null ? null : other.chatInvitationId.copy();
        this.chatRoomId = other.chatRoomId == null ? null : other.chatRoomId.copy();
        this.chatMessageId = other.chatMessageId == null ? null : other.chatMessageId.copy();
    }

    @Override
    public ChatNotificationCriteria copy() {
        return new ChatNotificationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(InstantFilter creationDate) {
        this.creationDate = creationDate;
    }

    public ChatNotificationReasonFilter getChatNotificationReason() {
        return chatNotificationReason;
    }

    public void setChatNotificationReason(ChatNotificationReasonFilter chatNotificationReason) {
        this.chatNotificationReason = chatNotificationReason;
    }

    public LongFilter getChatUserId() {
        return chatUserId;
    }

    public void setChatUserId(LongFilter chatUserId) {
        this.chatUserId = chatUserId;
    }

    public LongFilter getChatInvitationId() {
        return chatInvitationId;
    }

    public void setChatInvitationId(LongFilter chatInvitationId) {
        this.chatInvitationId = chatInvitationId;
    }

    public LongFilter getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(LongFilter chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public LongFilter getChatMessageId() {
        return chatMessageId;
    }

    public void setChatMessageId(LongFilter chatMessageId) {
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
        final ChatNotificationCriteria that = (ChatNotificationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(chatNotificationReason, that.chatNotificationReason) &&
            Objects.equals(chatUserId, that.chatUserId) &&
            Objects.equals(chatInvitationId, that.chatInvitationId) &&
            Objects.equals(chatRoomId, that.chatRoomId) &&
            Objects.equals(chatMessageId, that.chatMessageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        creationDate,
        chatNotificationReason,
        chatUserId,
        chatInvitationId,
        chatRoomId,
        chatMessageId
        );
    }

    @Override
    public String toString() {
        return "ChatNotificationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
                (chatNotificationReason != null ? "chatNotificationReason=" + chatNotificationReason + ", " : "") +
                (chatUserId != null ? "chatUserId=" + chatUserId + ", " : "") +
                (chatInvitationId != null ? "chatInvitationId=" + chatInvitationId + ", " : "") +
                (chatRoomId != null ? "chatRoomId=" + chatRoomId + ", " : "") +
                (chatMessageId != null ? "chatMessageId=" + chatMessageId + ", " : "") +
            "}";
    }

}
