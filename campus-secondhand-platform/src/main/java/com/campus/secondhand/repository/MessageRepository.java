package com.campus.secondhand.repository;

import com.campus.secondhand.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    Page<Message> findBySenderIdAndReceiverId(Long senderId, Long receiverId, Pageable pageable);
    
    Page<Message> findByReceiverId(Long receiverId, Pageable pageable);
    
    List<Message> findBySenderIdAndReceiverIdAndStatus(Long senderId, Long receiverId, Integer status);
    
    Long countByReceiverIdAndStatus(Long receiverId, Integer status);
}
