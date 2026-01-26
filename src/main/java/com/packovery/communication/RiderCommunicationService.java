package com.packovery.communication;

import com.packovery.auth.EmailService;
import com.packovery.user.User;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.bson.types.ObjectId;
import org.jboss.logging.Logger;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class RiderCommunicationService {

    private static final Logger LOG = Logger.getLogger(RiderCommunicationService.class);

    @Inject
    EmailService emailService;

    @Transactional
    public void sendMessage(Long senderId, Long riderId, String content, String orderId) {
        RiderCommunication message = new RiderCommunication();
        message.senderId = senderId;
        message.riderId = riderId;
        message.messageContent = content;
        message.messageSentTime = Instant.now();
        message.messageReadStatus = false;
        message.persist();

        User sender = User.findById(senderId);

        if (sender != null && sender.getEmail() != null && !sender.getEmail().isEmpty()) {
            try {
                emailService.sendMessageConfirmation(
                        sender.getEmail(),
                        orderId,
                        riderId,
                        content
                );
            } catch (Exception e) {
                LOG.error("Errore durante l'invio dell'email di conferma per l'ordine " + orderId, e);
            }
        } else {
            LOG.warn("Impossibile inviare email: Utente sender non trovato o senza indirizzo email valido.");
        }
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