package com.spingular.chat.repository;
import com.spingular.chat.domain.ChatNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ChatNotification entity.
 */
@Repository
public interface ChatNotificationRepository extends JpaRepository<ChatNotification, Long>, JpaSpecificationExecutor<ChatNotification> {

    @Query(value = "select distinct chatNotification from ChatNotification chatNotification left join fetch chatNotification.chatInvitations",
        countQuery = "select count(distinct chatNotification) from ChatNotification chatNotification")
    Page<ChatNotification> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct chatNotification from ChatNotification chatNotification left join fetch chatNotification.chatInvitations")
    List<ChatNotification> findAllWithEagerRelationships();

    @Query("select chatNotification from ChatNotification chatNotification left join fetch chatNotification.chatInvitations where chatNotification.id =:id")
    Optional<ChatNotification> findOneWithEagerRelationships(@Param("id") Long id);

}
