package br.com.replicatorserver.config;

import br.com.replicatorserver.els.ElsConnector;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import static br.com.replicatorserver.utils.KafkaTopics.*;

@Configuration
public class ReplicatorConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ReplicatorConfiguration.class);

    @Autowired
    private ElsConnector elsConnector;

    @KafkaListener(topics = HUE_LIGHT_MSG, groupId = REPLICATOR_GROUP_ID)
    public void processHueLigths(@Payload final JsonNode msg){
        logger.debug("replicating "+HUE_LIGHT_MSG+" msg");
        elsConnector.addMsg(HUE_LIGHT_MSG, msg);
    }

    @KafkaListener(topics = SENSOR_STATE_CHANGE_MSG, groupId = REPLICATOR_GROUP_ID)
    public void processSensorStateChange(@Payload final JsonNode msg){
        logger.debug("replicating "+SENSOR_STATE_CHANGE_MSG+" msg");
        elsConnector.addMsg(SENSOR_STATE_CHANGE_MSG, msg);
    }

    @KafkaListener(topics = LIGHT_STATE_CHANGE_MSG, groupId = REPLICATOR_GROUP_ID)
    public void processLightStateChange(@Payload final JsonNode msg){
        logger.debug("replicating "+LIGHT_STATE_CHANGE_MSG+" msg");
        elsConnector.addMsg(LIGHT_STATE_CHANGE_MSG, msg);
    }

    @KafkaListener(topics = TELEGRAM_MSG, groupId = REPLICATOR_GROUP_ID)
    public void processTelegramMessage(@Payload final JsonNode msg){
        logger.debug("replicating "+TELEGRAM_MSG+" msg");
        elsConnector.addMsg(TELEGRAM_MSG, msg);
    }

}
