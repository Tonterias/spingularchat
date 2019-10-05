package com.spingular.chat.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A ChatRoom.
 */
@Entity
@Table(name = "chat_room")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "room_name", length = 50, nullable = false)
    private String roomName;

    @Size(min = 2, max = 250)
    @Column(name = "room_description", length = 250)
    private String roomDescription;

    @Column(name = "private_room")
    private Boolean privateRoom;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @OneToMany(mappedBy = "chatRoom")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChatMessage> chatMessages = new HashSet<>();

    @OneToMany(mappedBy = "chatRoom")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChatRoomAllowedUser> chatRoomAllowedUsers = new HashSet<>();

    @OneToMany(mappedBy = "chatRoom")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChatNotification> chatNotifications = new HashSet<>();

    @OneToMany(mappedBy = "chatRoom")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChatInvitation> chatInvitations = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("chatRooms")
    private ChatUser chatUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public ChatRoom creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getRoomName() {
        return roomName;
    }

    public ChatRoom roomName(String roomName) {
        this.roomName = roomName;
        return this;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public ChatRoom roomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
        return this;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public Boolean isPrivateRoom() {
        return privateRoom;
    }

    public ChatRoom privateRoom(Boolean privateRoom) {
        this.privateRoom = privateRoom;
        return this;
    }

    public void setPrivateRoom(Boolean privateRoom) {
        this.privateRoom = privateRoom;
    }

    public byte[] getImage() {
        return image;
    }

    public ChatRoom image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public ChatRoom imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Set<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public ChatRoom chatMessages(Set<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
        return this;
    }

    public ChatRoom addChatMessage(ChatMessage chatMessage) {
        this.chatMessages.add(chatMessage);
        chatMessage.setChatRoom(this);
        return this;
    }

    public ChatRoom removeChatMessage(ChatMessage chatMessage) {
        this.chatMessages.remove(chatMessage);
        chatMessage.setChatRoom(null);
        return this;
    }

    public void setChatMessages(Set<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public Set<ChatRoomAllowedUser> getChatRoomAllowedUsers() {
        return chatRoomAllowedUsers;
    }

    public ChatRoom chatRoomAllowedUsers(Set<ChatRoomAllowedUser> chatRoomAllowedUsers) {
        this.chatRoomAllowedUsers = chatRoomAllowedUsers;
        return this;
    }

    public ChatRoom addChatRoomAllowedUser(ChatRoomAllowedUser chatRoomAllowedUser) {
        this.chatRoomAllowedUsers.add(chatRoomAllowedUser);
        chatRoomAllowedUser.setChatRoom(this);
        return this;
    }

    public ChatRoom removeChatRoomAllowedUser(ChatRoomAllowedUser chatRoomAllowedUser) {
        this.chatRoomAllowedUsers.remove(chatRoomAllowedUser);
        chatRoomAllowedUser.setChatRoom(null);
        return this;
    }

    public void setChatRoomAllowedUsers(Set<ChatRoomAllowedUser> chatRoomAllowedUsers) {
        this.chatRoomAllowedUsers = chatRoomAllowedUsers;
    }

    public Set<ChatNotification> getChatNotifications() {
        return chatNotifications;
    }

    public ChatRoom chatNotifications(Set<ChatNotification> chatNotifications) {
        this.chatNotifications = chatNotifications;
        return this;
    }

    public ChatRoom addChatNotification(ChatNotification chatNotification) {
        this.chatNotifications.add(chatNotification);
        chatNotification.setChatRoom(this);
        return this;
    }

    public ChatRoom removeChatNotification(ChatNotification chatNotification) {
        this.chatNotifications.remove(chatNotification);
        chatNotification.setChatRoom(null);
        return this;
    }

    public void setChatNotifications(Set<ChatNotification> chatNotifications) {
        this.chatNotifications = chatNotifications;
    }

    public Set<ChatInvitation> getChatInvitations() {
        return chatInvitations;
    }

    public ChatRoom chatInvitations(Set<ChatInvitation> chatInvitations) {
        this.chatInvitations = chatInvitations;
        return this;
    }

    public ChatRoom addChatInvitation(ChatInvitation chatInvitation) {
        this.chatInvitations.add(chatInvitation);
        chatInvitation.setChatRoom(this);
        return this;
    }

    public ChatRoom removeChatInvitation(ChatInvitation chatInvitation) {
        this.chatInvitations.remove(chatInvitation);
        chatInvitation.setChatRoom(null);
        return this;
    }

    public void setChatInvitations(Set<ChatInvitation> chatInvitations) {
        this.chatInvitations = chatInvitations;
    }

    public ChatUser getChatUser() {
        return chatUser;
    }

    public ChatRoom chatUser(ChatUser chatUser) {
        this.chatUser = chatUser;
        return this;
    }

    public void setChatUser(ChatUser chatUser) {
        this.chatUser = chatUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatRoom)) {
            return false;
        }
        return id != null && id.equals(((ChatRoom) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", roomName='" + getRoomName() + "'" +
            ", roomDescription='" + getRoomDescription() + "'" +
            ", privateRoom='" + isPrivateRoom() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
