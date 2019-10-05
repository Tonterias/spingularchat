package com.spingular.chat.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.spingular.chat.domain.ChatInvitation} entity. This class is used
 * in {@link com.spingular.chat.web.rest.ChatInvitationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /chat-invitations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ChatInvitationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private BooleanFilter acceptance;

    private BooleanFilter denial;

    private InstantFilter acceptanceDenialDate;

    private LongFilter senderId;

    private LongFilter receiverId;

    private LongFilter chatRoomId;

    private LongFilter chatNotificationId;

    public ChatInvitationCriteria(){
    }

    public ChatInvitationCriteria(ChatInvitationCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.acceptance = other.acceptance == null ? null : other.acceptance.copy();
        this.denial = other.denial == null ? null : other.denial.copy();
        this.acceptanceDenialDate = other.acceptanceDenialDate == null ? null : other.acceptanceDenialDate.copy();
        this.senderId = other.senderId == null ? null : other.senderId.copy();
        this.receiverId = other.receiverId == null ? null : other.receiverId.copy();
        this.chatRoomId = other.chatRoomId == null ? null : other.chatRoomId.copy();
        this.chatNotificationId = other.chatNotificationId == null ? null : other.chatNotificationId.copy();
    }

    @Override
    public ChatInvitationCriteria copy() {
        return new ChatInvitationCriteria(this);
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

    public BooleanFilter getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(BooleanFilter acceptance) {
        this.acceptance = acceptance;
    }

    public BooleanFilter getDenial() {
        return denial;
    }

    public void setDenial(BooleanFilter denial) {
        this.denial = denial;
    }

    public InstantFilter getAcceptanceDenialDate() {
        return acceptanceDenialDate;
    }

    public void setAcceptanceDenialDate(InstantFilter acceptanceDenialDate) {
        this.acceptanceDenialDate = acceptanceDenialDate;
    }

    public LongFilter getSenderId() {
        return senderId;
    }

    public void setSenderId(LongFilter senderId) {
        this.senderId = senderId;
    }

    public LongFilter getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(LongFilter receiverId) {
        this.receiverId = receiverId;
    }

    public LongFilter getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(LongFilter chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public LongFilter getChatNotificationId() {
        return chatNotificationId;
    }

    public void setChatNotificationId(LongFilter chatNotificationId) {
        this.chatNotificationId = chatNotificationId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ChatInvitationCriteria that = (ChatInvitationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(acceptance, that.acceptance) &&
            Objects.equals(denial, that.denial) &&
            Objects.equals(acceptanceDenialDate, that.acceptanceDenialDate) &&
            Objects.equals(senderId, that.senderId) &&
            Objects.equals(receiverId, that.receiverId) &&
            Objects.equals(chatRoomId, that.chatRoomId) &&
            Objects.equals(chatNotificationId, that.chatNotificationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        creationDate,
        acceptance,
        denial,
        acceptanceDenialDate,
        senderId,
        receiverId,
        chatRoomId,
        chatNotificationId
        );
    }

    @Override
    public String toString() {
        return "ChatInvitationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
                (acceptance != null ? "acceptance=" + acceptance + ", " : "") +
                (denial != null ? "denial=" + denial + ", " : "") +
                (acceptanceDenialDate != null ? "acceptanceDenialDate=" + acceptanceDenialDate + ", " : "") +
                (senderId != null ? "senderId=" + senderId + ", " : "") +
                (receiverId != null ? "receiverId=" + receiverId + ", " : "") +
                (chatRoomId != null ? "chatRoomId=" + chatRoomId + ", " : "") +
                (chatNotificationId != null ? "chatNotificationId=" + chatNotificationId + ", " : "") +
            "}";
    }

}
