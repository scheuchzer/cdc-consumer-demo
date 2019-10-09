package cdc.demo.temperatures.boundary;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.StateChangeAction;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.loader.PactBrokerAuth;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import au.com.dius.pact.provider.spring.target.SpringBootHttpTarget;
import cdc.demo.temperatures.Temperature;
import cdc.demo.temperatures.TemperatureRepository;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRestPactRunner.class)
@Provider("provider-demo-temperatures")
@PactBroker(scheme = "https",
    host = "${pact.broker.url}",
    port = "443",
    authentication = @PactBrokerAuth(username = "${pact.broker.username}", password = "${pact.broker.password}"))
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherProviderIT {

    @MockBean
    private TemperatureRepository repo;

    @TestTarget
    public final Target target = new SpringBootHttpTarget();

    @State(value = "temperature for city exists", action = StateChangeAction.SETUP)
    public void toTemperatureForCityExists(final Map<String, String> params) {
        final String city = params.get("city");
        doReturn(Optional.of(new Temperature(city, 30))).when(repo).findById(eq(city));
    }

    @State(value = {
        "temperature for city exists",
        "temperature for city doesn't exists"},
        action = StateChangeAction.TEARDOWN)
    public void toTemperatureForXExistsTeardown() {
        Mockito.reset(repo);
    }

    @State("temperature for city doesn't exists")
    public void toTemperatureForCityDoesNotExist() {
        doReturn(Optional.empty()).when(repo).findById(anyString());
    }

}