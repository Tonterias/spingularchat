package com.spingular.chat.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ChatOffensiveMessage.
 */
@Entity
@Table(name = "chat_offensive_message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ChatOffensiveMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "is_offensive")
    private Boolean isOffensive;

    @ManyToOne
    @JsonIgnoreProperties("chatOffensiveMessages")
    private ChatUser chatUser;

    @ManyToOne
    @JsonIgnoreProperties("chatOffensiveMessages")
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

    public ChatOffensiveMessage creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean isIsOffensive() {
        return isOffensive;
    }

    public ChatOffensiveMessage isOffensive(Boolean isOffensive) {
        this.isOffensive = isOffensive;
        return this;
    }

    public void setIsOffensive(Boolean isOffensive) {
        this.isOffensive = isOffensive;
    }

    public ChatUser getChatUser() {
        return chatUser;
    }

    public ChatOffensiveMessage chatUser(ChatUser chatUser) {
        this.chatUser = chatUser;
        return this;
    }

    public void setChatUser(ChatUser chatUser) {
        this.chatUser = chatUser;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public ChatOffensiveMessage chatMessage(ChatMessage chatMessage) {
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
        if (!(o instanceof ChatOffensiveMessage)) {
            return false;
        }
        return id != null && id.equals(((ChatOffensiveMessage) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ChatOffensiveMessage{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", isOffensive='" + isIsOffensive() + "'" +
            "}";
    }
}
