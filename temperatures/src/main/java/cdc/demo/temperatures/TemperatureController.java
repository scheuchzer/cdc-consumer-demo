package cdc.demo.temperatures;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unused")
@RestController
@RequiredArgsConstructor
public class TemperatureController {

    private final TemperatureRepository repo;

    @GetMapping(value = "/temperatures")
    public Temperature getTemparature(@RequestParam String location) {
        return repo.findById(location).orElseThrow(NotFoundException::new);
    }

}
