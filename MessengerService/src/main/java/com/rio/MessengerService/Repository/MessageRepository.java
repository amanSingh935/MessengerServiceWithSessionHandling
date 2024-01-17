package com.rio.MessengerService.Repository;

import com.rio.MessengerService.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    @Query(value = "SELECT * FROM messages as m WHERE (m.user_to =?1 AND m.is_read = 0) OR (m.user_from =?1)  ORDER BY m.created_time DESC", nativeQuery = true)
    List<Message> findUnreadMessagesForUser(String username);

    @Query(value = "SELECT * FROM messages as m WHERE m.user_to =?1 OR m.user_from =?1  ORDER BY m.created_time DESC", nativeQuery = true)
    List<Message> findAllMessagesForUser(String username);

}
