package cdc.demo.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    @Value("${temperatures.url}")
    private String temperaturesUrl;

    @GetMapping(path = "/weather/{city}")
    public Map<String, String> getWeather(@PathVariable String city) {
        Map<String, String> result = new HashMap<>();

        final ResponseEntity<Map> response = getRestTemplate()
            .getForEntity(temperaturesUrl + "/temperatures?location={city}", Map.class, city);
        if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            result.put("message", String.format("There's no weather information about %s available", city));
        } else {
            result.put("message",
                String.format("The weather in %s is fine and the temperature is %s °.",
                    city,
                    response.getBody().get("temperature")
                )
            );
        }
        return result;
    }

    private RestTemplate getRestTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            protected boolean hasError(HttpStatus statusCode) {
                return false;
            }
        });
        return restTemplate;
    }
}
