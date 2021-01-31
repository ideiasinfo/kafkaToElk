package br.com.stock.processor;

import com.fasterxml.jackson.databind.JsonNode;

public interface StockProcessor {

    void processStockMessage(String topic, JsonNode msg);

}
