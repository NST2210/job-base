package com.gtel.api.infrastracture.rabbitmq.publisher;

import com.gtel.api.domains.dto.SendMailRquest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class EmailPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange.mail}")
    private String exchangeMail;

    @Value("${spring.rabbitmq.routing-key.mail}")
    private String routingKeySendMail;

    public void sendMail(SendMailRquest sendMailRquest) {
        log.info("Sending mail {} ", sendMailRquest);
        rabbitTemplate.convertAndSend(exchangeMail, routingKeySendMail, sendMailRquest);
    }

}
