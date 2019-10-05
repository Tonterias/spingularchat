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
 * Criteria class for the {@link com.spingular.chat.domain.ChatRoomAllowedUser} entity. This class is used
 * in {@link com.spingular.chat.web.rest.ChatRoomAllowedUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /chat-room-allowed-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ChatRoomAllowedUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private BooleanFilter bannedUser;

    private InstantFilter bannedDate;

    private LongFilter chatRoomId;

    private LongFilter chatUserId;

    public ChatRoomAllowedUserCriteria(){
    }

    public ChatRoomAllowedUserCriteria(ChatRoomAllowedUserCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.bannedUser = other.bannedUser == null ? null : other.bannedUser.copy();
        this.bannedDate = other.bannedDate == null ? null : other.bannedDate.copy();
        this.chatRoomId = other.chatRoomId == null ? null : other.chatRoomId.copy();
        this.chatUserId = other.chatUserId == null ? null : other.chatUserId.copy();
    }

    @Override
    public ChatRoomAllowedUserCriteria copy() {
        return new ChatRoomAllowedUserCriteria(this);
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

    public BooleanFilter getBannedUser() {
        return bannedUser;
    }

    public void setBannedUser(BooleanFilter bannedUser) {
        this.bannedUser = bannedUser;
    }

    public InstantFilter getBannedDate() {
        return bannedDate;
    }

    public void setBannedDate(InstantFilter bannedDate) {
        this.bannedDate = bannedDate;
    }

    public LongFilter getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(LongFilter chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public LongFilter getChatUserId() {
        return chatUserId;
    }

    public void setChatUserId(LongFilter chatUserId) {
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
        final ChatRoomAllowedUserCriteria that = (ChatRoomAllowedUserCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(bannedUser, that.bannedUser) &&
            Objects.equals(bannedDate, that.bannedDate) &&
            Objects.equals(chatRoomId, that.chatRoomId) &&
            Objects.equals(chatUserId, that.chatUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        creationDate,
        bannedUser,
        bannedDate,
        chatRoomId,
        chatUserId
        );
    }

    @Override
    public String toString() {
        return "ChatRoomAllowedUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
                (bannedUser != null ? "bannedUser=" + bannedUser + ", " : "") +
                (bannedDate != null ? "bannedDate=" + bannedDate + ", " : "") +
                (chatRoomId != null ? "chatRoomId=" + chatRoomId + ", " : "") +
                (chatUserId != null ? "chatUserId=" + chatUserId + ", " : "") +
            "}";
    }

}
