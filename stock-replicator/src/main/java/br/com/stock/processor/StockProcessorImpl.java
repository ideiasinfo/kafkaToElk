package br.com.stock.processor;

import br.com.els.connector.ElsConnector;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StockProcessorImpl implements StockProcessor{

    private ElsConnector elsConnector;

    public StockProcessorImpl(ElsConnector elsConnector){
        this.elsConnector = elsConnector;
    }

    public void processStockMessage(String topic, JsonNode msg) {
        String stockSymbol = msg.get("symbol").asText();
        Long stockTimestamp = msg.get("timestamp").asLong();
        String query = "{\"query\":{\"bool\":{\"must\":[{\"match\":{\"symbol\":\""+stockSymbol+"\"}}],\"filter\":[{\"term\":{\"timestamp\":"+stockTimestamp+"}}]}}}";
        elsConnector.searchMessage(topic, query)
            .ifPresentOrElse(result -> processResult(topic, msg, result), () -> insert(topic, msg));
    }

    private void processResult(String topic, JsonNode msg, JsonNode result){
        Long resultsFound = result.get("hits").get("total").get("value").asLong();
        if(resultsFound >= 1){
            if(resultsFound == 1){
                update(topic, msg, result);
            }else{
                log.error("more than one result found");
            }
        }else{
            log.debug("no results find in elk");
            insert(topic, msg);
        }
    }

    private void update(String topic, JsonNode msg, JsonNode resultMessage){
        JsonNode result = resultMessage.get("hits").get("hits").get(0);
        String regId = result.get("_id").asText();
        JsonNode source = result.get("_source");
        Double high = source.get("high").asDouble();
        Double low = source.get("low").asDouble();
        Double close = source.get("close").asDouble();
        Double open = source.get("open").asDouble();

        Double highNew = msg.get("high").asDouble();
        Double lowNew = msg.get("low").asDouble();
        Double closeNew = msg.get("close").asDouble();
        Double openNew = msg.get("open").asDouble();

        if(high == highNew && low == lowNew && close == closeNew && open == openNew){
            log.debug("same msg top update - no update");
        }else{
            log.debug("updating registry "+topic+" msg");
            String msgToUpdate = "{\"doc\":{\"high\":"+highNew+",\"low\":"+lowNew+",\"close\":"+closeNew+",\"open\":"+openNew+"}}";
            elsConnector.updateMessage(topic, regId, msgToUpdate);
        }
    }

    private void insert(String topic, JsonNode msg){
        log.debug("insert registry "+topic+" msg");
        elsConnector.addMsg(topic, msg);
    }

}