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

    @KafkaListener(topics = HUE_LIGHTS_MESSAGE, groupId = REPLICATOR_GROUP_ID)
    public void processHueLigths(@Payload final JsonNode msg){
        logger.debug("replicating "+HUE_LIGHTS_MESSAGE+" msg");
        elsConnector.addMsg(HUE_LIGHTS_MESSAGE, msg);
    }

    @KafkaListener(topics = LIGHT_STATE_CHANGE, groupId = REPLICATOR_GROUP_ID)
    public void processLightStateChange(@Payload final JsonNode msg){
        logger.debug("replicating "+LIGHT_STATE_CHANGE+" msg");
        elsConnector.addMsg(LIGHT_STATE_CHANGE, msg);
    }

    @KafkaListener(topics = PUSHOVER_MESSAGE, groupId = REPLICATOR_GROUP_ID)
    public void processPushOverMessage(@Payload final JsonNode msg){
        logger.debug("replicating "+PUSHOVER_MESSAGE+" msg");
        elsConnector.addMsg(PUSHOVER_MESSAGE, msg);
    }

    @KafkaListener(topics = SENSOR_STATE_CHANGE, groupId = REPLICATOR_GROUP_ID)
    public void processSensorStateChange(@Payload final JsonNode msg){
        logger.debug("replicating "+SENSOR_STATE_CHANGE+" msg");
        elsConnector.addMsg(SENSOR_STATE_CHANGE, msg);
    }

    @KafkaListener(topics = TELEGRAM_MESSAGE, groupId = REPLICATOR_GROUP_ID)
    public void processTelegramMessage(@Payload final JsonNode msg){
        logger.debug("replicating "+TELEGRAM_MESSAGE+" msg");
        elsConnector.addMsg(TELEGRAM_MESSAGE, msg);
    }

    @KafkaListener(topics = TELEGRAM_MESSAGE_SENT, groupId = REPLICATOR_GROUP_ID)
    public void processTelegramMessageSent(@Payload final JsonNode msg){
        logger.debug("replicating "+TELEGRAM_MESSAGE_SENT+" msg");
        elsConnector.addMsg(TELEGRAM_MESSAGE_SENT, msg);
    }

    @KafkaListener(topics = TEMPERATURE_STATUS, groupId = REPLICATOR_GROUP_ID)
    public void processTemperatureStatus(@Payload final JsonNode msg){
        logger.debug("replicating "+TEMPERATURE_STATUS+" msg");
        elsConnector.addMsg(TEMPERATURE_STATUS, msg);
    }

}
