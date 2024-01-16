package com.rio.MessengerService.Repository;

import com.rio.MessengerService.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    @Query(value = "SELECT * FROM messages", nativeQuery = true)
//    WHERE (m.userFrom =?1 and m.status = 0) or (m.userTo =?1)  order by m.createdTime DESC
    List<Message> findUnreadMessagesForUser(String username);
}
