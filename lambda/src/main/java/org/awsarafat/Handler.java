package org.awsarafat;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class Handler implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> event, Context context) {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> responseBody = Map.of(
                "message", "Hi from RestAPI"
        );
        Map<String, Object> response = new java.util.HashMap<>(Map.of(
                "statusCode", "200",
                "headers", Map.of(
                        "Content-Type", "application/json"
                )
        ));

        try {
            response.put("body", mapper.writeValueAsString(responseBody));
        } catch (Exception e) {
            response.put("body", "{\"message\": \"Jackson not working\"}");
        }

        return response;
    }
}

