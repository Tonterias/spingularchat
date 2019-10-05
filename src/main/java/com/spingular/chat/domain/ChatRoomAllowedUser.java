package com.spingular.chat.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ChatRoomAllowedUser.
 */
@Entity
@Table(name = "chat_room_allowed_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ChatRoomAllowedUser implements Serializable {

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

    @Column(name = "banned_date")
    private Instant bannedDate;

    @ManyToOne
    @JsonIgnoreProperties("chatRoomAllowedUsers")
    private ChatRoom chatRoom;

    @ManyToOne
    @JsonIgnoreProperties("chatRoomAllowedUsers")
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

    public ChatRoomAllowedUser creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean isBannedUser() {
        return bannedUser;
    }

    public ChatRoomAllowedUser bannedUser(Boolean bannedUser) {
        this.bannedUser = bannedUser;
        return this;
    }

    public void setBannedUser(Boolean bannedUser) {
        this.bannedUser = bannedUser;
    }

    public Instant getBannedDate() {
        return bannedDate;
    }

    public ChatRoomAllowedUser bannedDate(Instant bannedDate) {
        this.bannedDate = bannedDate;
        return this;
    }

    public void setBannedDate(Instant bannedDate) {
        this.bannedDate = bannedDate;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public ChatRoomAllowedUser chatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        return this;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public ChatUser getChatUser() {
        return chatUser;
    }

    public ChatRoomAllowedUser chatUser(ChatUser chatUser) {
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
        if (!(o instanceof ChatRoomAllowedUser)) {
            return false;
        }
        return id != null && id.equals(((ChatRoomAllowedUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ChatRoomAllowedUser{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", bannedUser='" + isBannedUser() + "'" +
            ", bannedDate='" + getBannedDate() + "'" +
            "}";
    }
}
