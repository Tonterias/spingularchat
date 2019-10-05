package com.spingular.chat.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A ChatUser.
 */
@Entity
@Table(name = "chat_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ChatUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "banned_user")
    private Boolean bannedUser;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToOne
    @JoinColumn(unique = true)
    private ChatPhoto chatPhoto;

    @OneToMany(mappedBy = "chatUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChatRoom> chatRooms = new HashSet<>();

    @OneToMany(mappedBy = "sender")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChatInvitation> senders = new HashSet<>();

    @OneToMany(mappedBy = "receiver")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChatInvitation> receivers = new HashSet<>();

    @OneToMany(mappedBy = "chatUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChatMessage> chatMessages = new HashSet<>();

    @OneToMany(mappedBy = "chatUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChatRoomAllowedUser> chatRoomAllowedUsers = new HashSet<>();

    @OneToMany(mappedBy = "chatUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChatOffensiveMessage> chatOffensiveMessages = new HashSet<>();

    @OneToMany(mappedBy = "chatUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ChatNotification> chatNotifications = new HashSet<>();

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

    public ChatUser creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean isBannedUser() {
        return bannedUser;
    }

    public ChatUser bannedUser(Boolean bannedUser) {
        this.bannedUser = bannedUser;
        return this;
    }

    public void setBannedUser(Boolean bannedUser) {
        this.bannedUser = bannedUser;
    }

    public User getUser() {
        return user;
    }

    public ChatUser user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ChatPhoto getChatPhoto() {
        return chatPhoto;
    }

    public ChatUser chatPhoto(ChatPhoto chatPhoto) {
        this.chatPhoto = chatPhoto;
        return this;
    }

    public void setChatPhoto(ChatPhoto chatPhoto) {
        this.chatPhoto = chatPhoto;
    }

    public Set<ChatRoom> getChatRooms() {
        return chatRooms;
    }

    public ChatUser chatRooms(Set<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
        return this;
    }

    public ChatUser addChatRoom(ChatRoom chatRoom) {
        this.chatRooms.add(chatRoom);
        chatRoom.setChatUser(this);
        return this;
    }

    public ChatUser removeChatRoom(ChatRoom chatRoom) {
        this.chatRooms.remove(chatRoom);
        chatRoom.setChatUser(null);
        return this;
    }

    public void setChatRooms(Set<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }

    public Set<ChatInvitation> getSenders() {
        return senders;
    }

    public ChatUser senders(Set<ChatInvitation> chatInvitations) {
        this.senders = chatInvitations;
        return this;
    }

    public ChatUser addSender(ChatInvitation chatInvitation) {
        this.senders.add(chatInvitation);
        chatInvitation.setSender(this);
        return this;
    }

    public ChatUser removeSender(ChatInvitation chatInvitation) {
        this.senders.remove(chatInvitation);
        chatInvitation.setSender(null);
        return this;
    }

    public void setSenders(Set<ChatInvitation> chatInvitations) {
        this.senders = chatInvitations;
    }

    public Set<ChatInvitation> getReceivers() {
        return receivers;
    }

    public ChatUser receivers(Set<ChatInvitation> chatInvitations) {
        this.receivers = chatInvitations;
        return this;
    }

    public ChatUser addReceiver(ChatInvitation chatInvitation) {
        this.receivers.add(chatInvitation);
        chatInvitation.setReceiver(this);
        return this;
    }

    public ChatUser removeReceiver(ChatInvitation chatInvitation) {
        this.receivers.remove(chatInvitation);
        chatInvitation.setReceiver(null);
        return this;
    }

    public void setReceivers(Set<ChatInvitation> chatInvitations) {
        this.receivers = chatInvitations;
    }

    public Set<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public ChatUser chatMessages(Set<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
        return this;
    }

    public ChatUser addChatMessage(ChatMessage chatMessage) {
        this.chatMessages.add(chatMessage);
        chatMessage.setChatUser(this);
        return this;
    }

    public ChatUser removeChatMessage(ChatMessage chatMessage) {
        this.chatMessages.remove(chatMessage);
        chatMessage.setChatUser(null);
        return this;
    }

    public void setChatMessages(Set<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public Set<ChatRoomAllowedUser> getChatRoomAllowedUsers() {
        return chatRoomAllowedUsers;
    }

    public ChatUser chatRoomAllowedUsers(Set<ChatRoomAllowedUser> chatRoomAllowedUsers) {
        this.chatRoomAllowedUsers = chatRoomAllowedUsers;
        return this;
    }

    public ChatUser addChatRoomAllowedUser(ChatRoomAllowedUser chatRoomAllowedUser) {
        this.chatRoomAllowedUsers.add(chatRoomAllowedUser);
        chatRoomAllowedUser.setChatUser(this);
        return this;
    }

    public ChatUser removeChatRoomAllowedUser(ChatRoomAllowedUser chatRoomAllowedUser) {
        this.chatRoomAllowedUsers.remove(chatRoomAllowedUser);
        chatRoomAllowedUser.setChatUser(null);
        return this;
    }

    public void setChatRoomAllowedUsers(Set<ChatRoomAllowedUser> chatRoomAllowedUsers) {
        this.chatRoomAllowedUsers = chatRoomAllowedUsers;
    }

    public Set<ChatOffensiveMessage> getChatOffensiveMessages() {
        return chatOffensiveMessages;
    }

    public ChatUser chatOffensiveMessages(Set<ChatOffensiveMessage> chatOffensiveMessages) {
        this.chatOffensiveMessages = chatOffensiveMessages;
        return this;
    }

    public ChatUser addChatOffensiveMessage(ChatOffensiveMessage chatOffensiveMessage) {
        this.chatOffensiveMessages.add(chatOffensiveMessage);
        chatOffensiveMessage.setChatUser(this);
        return this;
    }

    public ChatUser removeChatOffensiveMessage(ChatOffensiveMessage chatOffensiveMessage) {
        this.chatOffensiveMessages.remove(chatOffensiveMessage);
        chatOffensiveMessage.setChatUser(null);
        return this;
    }

    public void setChatOffensiveMessages(Set<ChatOffensiveMessage> chatOffensiveMessages) {
        this.chatOffensiveMessages = chatOffensiveMessages;
    }

    public Set<ChatNotification> getChatNotifications() {
        return chatNotifications;
    }

    public ChatUser chatNotifications(Set<ChatNotification> chatNotifications) {
        this.chatNotifications = chatNotifications;
        return this;
    }

    public ChatUser addChatNotification(ChatNotification chatNotification) {
        this.chatNotifications.add(chatNotification);
        chatNotification.setChatUser(this);
        return this;
    }

    public ChatUser removeChatNotification(ChatNotification chatNotification) {
        this.chatNotifications.remove(chatNotification);
        chatNotification.setChatUser(null);
        return this;
    }

    public void setChatNotifications(Set<ChatNotification> chatNotifications) {
        this.chatNotifications = chatNotifications;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatUser)) {
            return false;
        }
        return id != null && id.equals(((ChatUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ChatUser{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", bannedUser='" + isBannedUser() + "'" +
            "}";
    }
}
