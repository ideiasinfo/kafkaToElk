package br.com.replicatorserver.listener;

import br.com.els.connector.ElsConnector;
import br.com.stock.processor.StockProcessor;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import static br.com.kafka.utils.KafkaTopics.*;

@Slf4j
@Configuration
public class StockReplicatorConfiguration {

    @Autowired
    private ElsConnector elsConnector;

    @Autowired
    private StockProcessor stockProcessor;

    @KafkaListener(topics = STOCK_UPDATE_MSG, groupId = REPLICATOR_GROUP_ID)
    public void processStockUpdate(@Payload final JsonNode msg){
        stockProcessor.processStockMessage(STOCK_UPDATE_MSG, msg);
    }

    @KafkaListener(topics = STOCK_BULK_MSG, groupId = REPLICATOR_GROUP_ID)
    public void processBulkMsg(@Payload final JsonNode msg){
        log.debug("replicating "+STOCK_BULK_MSG+" msg");
        elsConnector.addMsg(STOCK_BULK_MSG, msg);
    }

    @KafkaListener(topics = STOCK_UPDATE_MSG_DEV, groupId = REPLICATOR_GROUP_ID)
    public void processStockUpdateDev(@Payload final JsonNode msg){
        stockProcessor.processStockMessage(STOCK_UPDATE_MSG_DEV, msg);
    }

    @KafkaListener(topics = STOCK_BULK_MSG_DEV, groupId = REPLICATOR_GROUP_ID)
    public void processBulkMsgDev(@Payload final JsonNode msg){
        log.debug("replicating "+STOCK_BULK_MSG_DEV+" msg");
        elsConnector.addMsg(STOCK_BULK_MSG_DEV, msg);
    }

    @KafkaListener(topics = STOCK_UPDATE_DAILY_MSG, groupId = REPLICATOR_GROUP_ID)
    public void processStockDailyUpdate(@Payload final JsonNode msg){
        stockProcessor.processStockMessage(STOCK_UPDATE_DAILY_MSG, msg);
    }

    @KafkaListener(topics = STOCK_UPDATE_MSG_DAILY_DEV, groupId = REPLICATOR_GROUP_ID)
    public void processStockDailyUpdateDev(@Payload final JsonNode msg){
        stockProcessor.processStockMessage(STOCK_UPDATE_MSG_DAILY_DEV, msg);
    }

}
