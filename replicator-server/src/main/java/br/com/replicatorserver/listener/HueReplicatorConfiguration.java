package br.com.replicatorserver.listener;

import br.com.els.connector.ElsConnector;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import static br.com.kafka.utils.KafkaTopics.*;

@Configuration
@Slf4j
public class HueReplicatorConfiguration {

    @Autowired
    private ElsConnector elsConnector;

    @KafkaListener(topics = HUE_LIGHTS_MESSAGE, groupId = REPLICATOR_GROUP_ID)
    public void processHueLigths(@Payload final JsonNode msg){
        log.debug("replicating "+HUE_LIGHTS_MESSAGE+" msg");
        elsConnector.addMsg(HUE_LIGHTS_MESSAGE, msg);
    }

    @KafkaListener(topics = LIGHT_STATE_CHANGE, groupId = REPLICATOR_GROUP_ID)
    public void processLightStateChange(@Payload final JsonNode msg){
        log.debug("replicating "+LIGHT_STATE_CHANGE+" msg");
        elsConnector.addMsg(LIGHT_STATE_CHANGE, msg);
    }

    @KafkaListener(topics = SENSOR_STATE_CHANGE, groupId = REPLICATOR_GROUP_ID)
    public void processSensorStateChange(@Payload final JsonNode msg){
        log.debug("replicating "+SENSOR_STATE_CHANGE+" msg");
        elsConnector.addMsg(SENSOR_STATE_CHANGE, msg);
    }

    @KafkaListener(topics = TEMPERATURE_STATUS, groupId = REPLICATOR_GROUP_ID)
    public void processTemperatureStatus(@Payload final JsonNode msg){
        log.debug("replicating "+TEMPERATURE_STATUS+" msg");
        elsConnector.addMsg(TEMPERATURE_STATUS, msg);
    }

}
