package br.com.els.connector;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.springframework.http.HttpMethod.POST;

@Component
@Slf4j
public class ElsConnector {

    private RestTemplate restTemplate;

    private String url;
    private String elsToken;

    public ElsConnector(RestTemplate restTemplate,
                        @Value("${els.url}") String url,
                        @Value("${els.token}") String elsToken){
        this.restTemplate = restTemplate;
        this.elsToken = elsToken;
        this.url = url;
    }

    public void addMsg(String topic, JsonNode msg){
        try{
            ResponseEntity<String> result = restTemplate.exchange(buildUrl("kafka-"+topic+"/doc"), POST,
                    new HttpEntity<>(msg, getHeaders()), String.class);
            log.debug(result.getBody());
        }catch (HttpClientErrorException e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }

    public Optional<JsonNode> updateMessage(String topic, String msgId ,String msg){
        try{
            ResponseEntity<JsonNode> result = restTemplate
                    .exchange(buildUrl("kafka-"+topic+"/_update/"+msgId), POST,
                    new HttpEntity<>(msg, getHeaders()), JsonNode.class);
            if(result.getStatusCode().is2xxSuccessful()){
                return Optional.of(result.getBody());
            }
        }catch (HttpClientErrorException e) {
            log.warn(e.toString());
        }
        return Optional.empty();
    }

    public Optional<JsonNode> searchMessage(String topic, String query){
        try{
            ResponseEntity<JsonNode> result = restTemplate.exchange(buildUrl("kafka-"+topic+"/_search"), POST,
                    new HttpEntity<>(query, getHeaders()), JsonNode.class);
            if(result.getStatusCode().is2xxSuccessful()){
                return Optional.of(result.getBody());
            }
        }catch (HttpClientErrorException e) {
            log.warn(e.toString());
        }
        return Optional.empty();
    }

    private String buildUrl(String resourcePart){
        return url+"/"+resourcePart;
    }

    private HttpHeaders getHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("content-type", "application/json");
        httpHeaders.set("authorization", "Basic "+elsToken);
        return httpHeaders;
    }

}
