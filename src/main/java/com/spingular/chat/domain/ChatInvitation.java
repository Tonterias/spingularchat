package com.spingular.chat.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A ChatInvitation.
 */
@Entity
@Table(name = "chat_invitation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ChatInvitation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "acceptance")
    private Boolean acceptance;

    @Column(name = "denial")
    private Boolean denial;

    @Column(name = "acceptance_denial_date")
    private Instant acceptanceDenialDate;

    @ManyToOne
    @JsonIgnoreProperties("senders")
    private ChatUser sender;

    @ManyToOne
    @JsonIgnoreProperties("receivers")
    private ChatUser receiver;

    @ManyToOne
    @JsonIgnoreProperties("chatInvitations")
    private ChatRoom chatRoom;

    @ManyToMany(mappedBy = "chatInvitations")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
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

    public ChatInvitation creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean isAcceptance() {
        return acceptance;
    }

    public ChatInvitation acceptance(Boolean acceptance) {
        this.acceptance = acceptance;
        return this;
    }

    public void setAcceptance(Boolean acceptance) {
        this.acceptance = acceptance;
    }

    public Boolean isDenial() {
        return denial;
    }

    public ChatInvitation denial(Boolean denial) {
        this.denial = denial;
        return this;
    }

    public void setDenial(Boolean denial) {
        this.denial = denial;
    }

    public Instant getAcceptanceDenialDate() {
        return acceptanceDenialDate;
    }

    public ChatInvitation acceptanceDenialDate(Instant acceptanceDenialDate) {
        this.acceptanceDenialDate = acceptanceDenialDate;
        return this;
    }

    public void setAcceptanceDenialDate(Instant acceptanceDenialDate) {
        this.acceptanceDenialDate = acceptanceDenialDate;
    }

    public ChatUser getSender() {
        return sender;
    }

    public ChatInvitation sender(ChatUser chatUser) {
        this.sender = chatUser;
        return this;
    }

    public void setSender(ChatUser chatUser) {
        this.sender = chatUser;
    }

    public ChatUser getReceiver() {
        return receiver;
    }

    public ChatInvitation receiver(ChatUser chatUser) {
        this.receiver = chatUser;
        return this;
    }

    public void setReceiver(ChatUser chatUser) {
        this.receiver = chatUser;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public ChatInvitation chatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        return this;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public Set<ChatNotification> getChatNotifications() {
        return chatNotifications;
    }

    public ChatInvitation chatNotifications(Set<ChatNotification> chatNotifications) {
        this.chatNotifications = chatNotifications;
        return this;
    }

    public ChatInvitation addChatNotification(ChatNotification chatNotification) {
        this.chatNotifications.add(chatNotification);
        chatNotification.getChatInvitations().add(this);
        return this;
    }

    public ChatInvitation removeChatNotification(ChatNotification chatNotification) {
        this.chatNotifications.remove(chatNotification);
        chatNotification.getChatInvitations().remove(this);
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
        if (!(o instanceof ChatInvitation)) {
            return false;
        }
        return id != null && id.equals(((ChatInvitation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ChatInvitation{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", acceptance='" + isAcceptance() + "'" +
            ", denial='" + isDenial() + "'" +
            ", acceptanceDenialDate='" + getAcceptanceDenialDate() + "'" +
            "}";
    }
}
