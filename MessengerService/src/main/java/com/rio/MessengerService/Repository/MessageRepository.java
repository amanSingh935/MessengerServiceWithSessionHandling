package com.rio.MessengerService.Repository;

import com.rio.MessengerService.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    @Query(value = "select * from messages m where (m.userFrom = :username or  m.userTo = :username) and m.status = 0 order by timestamp DESC")
    List<Message> findAllUnreadMessagesForUser(String username);
}
