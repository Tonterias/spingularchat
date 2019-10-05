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
 * Criteria class for the {@link com.spingular.chat.domain.ChatRoom} entity. This class is used
 * in {@link com.spingular.chat.web.rest.ChatRoomResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /chat-rooms?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ChatRoomCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private StringFilter roomName;

    private StringFilter roomDescription;

    private BooleanFilter privateRoom;

    private LongFilter chatMessageId;

    private LongFilter chatRoomAllowedUserId;

    private LongFilter chatNotificationId;

    private LongFilter chatInvitationId;

    private LongFilter chatUserId;

    public ChatRoomCriteria(){
    }

    public ChatRoomCriteria(ChatRoomCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.roomName = other.roomName == null ? null : other.roomName.copy();
        this.roomDescription = other.roomDescription == null ? null : other.roomDescription.copy();
        this.privateRoom = other.privateRoom == null ? null : other.privateRoom.copy();
        this.chatMessageId = other.chatMessageId == null ? null : other.chatMessageId.copy();
        this.chatRoomAllowedUserId = other.chatRoomAllowedUserId == null ? null : other.chatRoomAllowedUserId.copy();
        this.chatNotificationId = other.chatNotificationId == null ? null : other.chatNotificationId.copy();
        this.chatInvitationId = other.chatInvitationId == null ? null : other.chatInvitationId.copy();
        this.chatUserId = other.chatUserId == null ? null : other.chatUserId.copy();
    }

    @Override
    public ChatRoomCriteria copy() {
        return new ChatRoomCriteria(this);
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

    public StringFilter getRoomName() {
        return roomName;
    }

    public void setRoomName(StringFilter roomName) {
        this.roomName = roomName;
    }

    public StringFilter getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(StringFilter roomDescription) {
        this.roomDescription = roomDescription;
    }

    public BooleanFilter getPrivateRoom() {
        return privateRoom;
    }

    public void setPrivateRoom(BooleanFilter privateRoom) {
        this.privateRoom = privateRoom;
    }

    public LongFilter getChatMessageId() {
        return chatMessageId;
    }

    public void setChatMessageId(LongFilter chatMessageId) {
        this.chatMessageId = chatMessageId;
    }

    public LongFilter getChatRoomAllowedUserId() {
        return chatRoomAllowedUserId;
    }

    public void setChatRoomAllowedUserId(LongFilter chatRoomAllowedUserId) {
        this.chatRoomAllowedUserId = chatRoomAllowedUserId;
    }

    public LongFilter getChatNotificationId() {
        return chatNotificationId;
    }

    public void setChatNotificationId(LongFilter chatNotificationId) {
        this.chatNotificationId = chatNotificationId;
    }

    public LongFilter getChatInvitationId() {
        return chatInvitationId;
    }

    public void setChatInvitationId(LongFilter chatInvitationId) {
        this.chatInvitationId = chatInvitationId;
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
        final ChatRoomCriteria that = (ChatRoomCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(roomName, that.roomName) &&
            Objects.equals(roomDescription, that.roomDescription) &&
            Objects.equals(privateRoom, that.privateRoom) &&
            Objects.equals(chatMessageId, that.chatMessageId) &&
            Objects.equals(chatRoomAllowedUserId, that.chatRoomAllowedUserId) &&
            Objects.equals(chatNotificationId, that.chatNotificationId) &&
            Objects.equals(chatInvitationId, that.chatInvitationId) &&
            Objects.equals(chatUserId, that.chatUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        creationDate,
        roomName,
        roomDescription,
        privateRoom,
        chatMessageId,
        chatRoomAllowedUserId,
        chatNotificationId,
        chatInvitationId,
        chatUserId
        );
    }

    @Override
    public String toString() {
        return "ChatRoomCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
                (roomName != null ? "roomName=" + roomName + ", " : "") +
                (roomDescription != null ? "roomDescription=" + roomDescription + ", " : "") +
                (privateRoom != null ? "privateRoom=" + privateRoom + ", " : "") +
                (chatMessageId != null ? "chatMessageId=" + chatMessageId + ", " : "") +
                (chatRoomAllowedUserId != null ? "chatRoomAllowedUserId=" + chatRoomAllowedUserId + ", " : "") +
                (chatNotificationId != null ? "chatNotificationId=" + chatNotificationId + ", " : "") +
                (chatInvitationId != null ? "chatInvitationId=" + chatInvitationId + ", " : "") +
                (chatUserId != null ? "chatUserId=" + chatUserId + ", " : "") +
            "}";
    }

}
