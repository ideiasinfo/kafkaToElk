package br.com.replicatorserver.listener;

import br.com.replicatorserver.els.ElsConnector;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import static br.com.replicatorserver.utils.KafkaTopics.*;

@Slf4j
@Configuration
public class StockReplicatorConfiguration {

    @Autowired
    private ElsConnector elsConnector;

    @KafkaListener(topics = STOCK_UPDATE_MSG, groupId = REPLICATOR_GROUP_ID)
    public void processStockUpdate(@Payload final JsonNode msg){
        log.debug("replicating "+STOCK_UPDATE_MSG+" msg");
        elsConnector.addMsg(STOCK_UPDATE_MSG, msg);
    }

    @KafkaListener(topics = STOCK_BULK_MSG, groupId = REPLICATOR_GROUP_ID)
    public void processBulkMsg(@Payload final JsonNode msg){
        log.debug("replicating "+STOCK_BULK_MSG+" msg");
        elsConnector.addMsg(STOCK_BULK_MSG, msg);
    }

}
