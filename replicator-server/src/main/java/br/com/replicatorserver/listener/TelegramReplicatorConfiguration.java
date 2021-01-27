package br.com.replicatorserver.listener;

import br.com.replicatorserver.els.ElsConnector;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import static br.com.replicatorserver.utils.KafkaTopics.*;
import static br.com.replicatorserver.utils.KafkaTopics.TELEGRAM_MESSAGE_SENT;

@Slf4j
@Configuration
public class TelegramReplicatorConfiguration {

    @Autowired
    private ElsConnector elsConnector;

    @KafkaListener(topics = TELEGRAM_MESSAGE, groupId = REPLICATOR_GROUP_ID)
    public void processTelegramMessage(@Payload final JsonNode msg){
        log.debug("replicating "+TELEGRAM_MESSAGE+" msg");
        elsConnector.addMsg(TELEGRAM_MESSAGE, msg);
    }

    @KafkaListener(topics = TELEGRAM_MESSAGE_SENT, groupId = REPLICATOR_GROUP_ID)
    public void processTelegramMessageSent(@Payload final JsonNode msg){
        log.debug("replicating "+TELEGRAM_MESSAGE_SENT+" msg");
        elsConnector.addMsg(TELEGRAM_MESSAGE_SENT, msg);
    }

}
