package com.spingular.chat.repository;
import com.spingular.chat.domain.ChatInvitation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ChatInvitation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatInvitationRepository extends JpaRepository<ChatInvitation, Long>, JpaSpecificationExecutor<ChatInvitation> {

}
