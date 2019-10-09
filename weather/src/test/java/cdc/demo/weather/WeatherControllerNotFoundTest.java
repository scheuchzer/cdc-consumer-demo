package cdc.demo.weather;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactHttpsProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class WeatherControllerNotFoundTest {

    @Rule
    public PactHttpsProviderRule temperaturesMock = new PactHttpsProviderRule("provider-demo-temperatures",
        "localhost",
        8081, this);

    @Pact(provider = "provider-demo-temperatures", consumer = "consumer-demo-weather")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        return builder
            .given("temperature for city doesn't exists", "city", "Sydney")
            .uponReceiving("a request for a city that has no data")
            .path("/temperatures")
            .query("location=Sydney")
            .method("GET")
            .willRespondWith()
            .status(404)
            .toPact();
    }

    @Autowired
    private MockMvc mockMvc;


    @Test
    @PactVerification
    public void testWeatherForSydney() throws Exception {
        mockMvc.perform(get("/weather/Sydney").accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("There's no weather information about Sydney available")));
    }

}