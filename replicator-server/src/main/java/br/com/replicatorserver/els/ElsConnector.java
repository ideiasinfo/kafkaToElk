package br.com.replicatorserver.els;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.POST;

@Component
public class ElsConnector {

    private static final Logger logger = LoggerFactory.getLogger(ElsConnector.class);

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
            logger.debug(result.getBody());
        }catch (HttpClientErrorException e) {
            e.printStackTrace();
            logger.warn(e.toString());
        }
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
