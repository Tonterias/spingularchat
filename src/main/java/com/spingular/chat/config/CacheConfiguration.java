package com.spingular.chat.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.spingular.chat.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.spingular.chat.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.spingular.chat.domain.User.class.getName());
            createCache(cm, com.spingular.chat.domain.Authority.class.getName());
            createCache(cm, com.spingular.chat.domain.User.class.getName() + ".authorities");
            createCache(cm, com.spingular.chat.domain.ChatUser.class.getName());
            createCache(cm, com.spingular.chat.domain.ChatUser.class.getName() + ".chatRooms");
            createCache(cm, com.spingular.chat.domain.ChatUser.class.getName() + ".senders");
            createCache(cm, com.spingular.chat.domain.ChatUser.class.getName() + ".receivers");
            createCache(cm, com.spingular.chat.domain.ChatUser.class.getName() + ".chatMessages");
            createCache(cm, com.spingular.chat.domain.ChatUser.class.getName() + ".chatRoomAllowedUsers");
            createCache(cm, com.spingular.chat.domain.ChatUser.class.getName() + ".chatOffensiveMessages");
            createCache(cm, com.spingular.chat.domain.ChatUser.class.getName() + ".chatNotifications");
            createCache(cm, com.spingular.chat.domain.ChatRoom.class.getName());
            createCache(cm, com.spingular.chat.domain.ChatRoom.class.getName() + ".chatMessages");
            createCache(cm, com.spingular.chat.domain.ChatRoom.class.getName() + ".chatRoomAllowedUsers");
            createCache(cm, com.spingular.chat.domain.ChatRoom.class.getName() + ".chatNotifications");
            createCache(cm, com.spingular.chat.domain.ChatRoom.class.getName() + ".chatInvitations");
            createCache(cm, com.spingular.chat.domain.ChatRoomAllowedUser.class.getName());
            createCache(cm, com.spingular.chat.domain.ChatMessage.class.getName());
            createCache(cm, com.spingular.chat.domain.ChatMessage.class.getName() + ".chatNotifications");
            createCache(cm, com.spingular.chat.domain.ChatMessage.class.getName() + ".chatOffensiveMessages");
            createCache(cm, com.spingular.chat.domain.ChatOffensiveMessage.class.getName());
            createCache(cm, com.spingular.chat.domain.ChatNotification.class.getName());
            createCache(cm, com.spingular.chat.domain.ChatNotification.class.getName() + ".chatInvitations");
            createCache(cm, com.spingular.chat.domain.ChatInvitation.class.getName());
            createCache(cm, com.spingular.chat.domain.ChatInvitation.class.getName() + ".chatNotifications");
            createCache(cm, com.spingular.chat.domain.ChatPhoto.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}
