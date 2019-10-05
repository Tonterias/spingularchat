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

import com.spingular.chat.domain.enumeration.ChatNotificationReason;

/**
 * A ChatNotification.
 */
@Entity
@Table(name = "chat_notification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ChatNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "chat_notification_reason")
    private ChatNotificationReason chatNotificationReason;

    @ManyToOne
    @JsonIgnoreProperties("chatNotifications")
    private ChatUser chatUser;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "chat_notification_chat_invitation",
               joinColumns = @JoinColumn(name = "chat_notification_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "chat_invitation_id", referencedColumnName = "id"))
    private Set<ChatInvitation> chatInvitations = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("chatNotifications")
    private ChatRoom chatRoom;

    @ManyToOne
    @JsonIgnoreProperties("chatNotifications")
    private ChatMessage chatMessage;

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

    public ChatNotification creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public ChatNotificationReason getChatNotificationReason() {
        return chatNotificationReason;
    }

    public ChatNotification chatNotificationReason(ChatNotificationReason chatNotificationReason) {
        this.chatNotificationReason = chatNotificationReason;
        return this;
    }

    public void setChatNotificationReason(ChatNotificationReason chatNotificationReason) {
        this.chatNotificationReason = chatNotificationReason;
    }

    public ChatUser getChatUser() {
        return chatUser;
    }

    public ChatNotification chatUser(ChatUser chatUser) {
        this.chatUser = chatUser;
        return this;
    }

    public void setChatUser(ChatUser chatUser) {
        this.chatUser = chatUser;
    }

    public Set<ChatInvitation> getChatInvitations() {
        return chatInvitations;
    }

    public ChatNotification chatInvitations(Set<ChatInvitation> chatInvitations) {
        this.chatInvitations = chatInvitations;
        return this;
    }

    public ChatNotification addChatInvitation(ChatInvitation chatInvitation) {
        this.chatInvitations.add(chatInvitation);
        chatInvitation.getChatNotifications().add(this);
        return this;
    }

    public ChatNotification removeChatInvitation(ChatInvitation chatInvitation) {
        this.chatInvitations.remove(chatInvitation);
        chatInvitation.getChatNotifications().remove(this);
        return this;
    }

    public void setChatInvitations(Set<ChatInvitation> chatInvitations) {
        this.chatInvitations = chatInvitations;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public ChatNotification chatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        return this;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public ChatNotification chatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
        return this;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatNotification)) {
            return false;
        }
        return id != null && id.equals(((ChatNotification) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ChatNotification{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", chatNotificationReason='" + getChatNotificationReason() + "'" +
            "}";
    }
}
