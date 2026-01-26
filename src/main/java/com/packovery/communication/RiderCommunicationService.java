package com.packovery.communication;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class RiderCommunicationService {

    @Transactional
    public void sendMessage(Long senderId, Long riderId, String content) {
        RiderCommunication message = new RiderCommunication();
        message.senderId = senderId;
        message.riderId = riderId;
        message.messageContent = content;
        message.messageSentTime = Instant.now();
        message.messageReadStatus = false;

        message.persist();
    }

    @Transactional
    public void markAsRead(String messageId) {
        try {
            ObjectId objectId = new ObjectId(messageId);
            RiderCommunication message = RiderCommunication.findById(objectId);

            if (message != null) {
                message.messageReadStatus = true;
                message.update();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("ID non valido ricevuto: " + messageId);
        }
    }

    public List<RiderCommunication> getMessagesForRider(Long riderId) {
        return RiderCommunication.list("riderId = ?1", Sort.descending("messageSentTime"), riderId);
    }
}