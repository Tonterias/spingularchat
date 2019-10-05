package com.spingular.chat.repository;
import com.spingular.chat.domain.ChatRoomAllowedUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ChatRoomAllowedUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatRoomAllowedUserRepository extends JpaRepository<ChatRoomAllowedUser, Long>, JpaSpecificationExecutor<ChatRoomAllowedUser> {

}
