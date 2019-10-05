package com.spingular.chat.repository;
import com.spingular.chat.domain.ChatUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ChatUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long>, JpaSpecificationExecutor<ChatUser> {

}
