package br.com.replicatorserver.listener;

import br.com.els.connector.ElsConnector;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import static br.com.kafka.utils.KafkaTopics.PUSHOVER_MESSAGE;
import static br.com.kafka.utils.KafkaTopics.REPLICATOR_GROUP_ID;

@Slf4j
@Configuration
public class PushOverReplicatorConfiguration {

    @Autowired
    private ElsConnector elsConnector;

    @KafkaListener(topics = PUSHOVER_MESSAGE, groupId = REPLICATOR_GROUP_ID)
    public void processPushOverMessage(@Payload final JsonNode msg){
        log.debug("replicating "+PUSHOVER_MESSAGE+" msg");
        elsConnector.addMsg(PUSHOVER_MESSAGE, msg);
    }

}
