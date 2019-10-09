package cdc.demo.temperatures;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class StaticData {

    private final TemperatureRepository repo;

    @PostConstruct
    public void init() {
        if (repo.count() != 0) {
            return;
        }

        repo.save(new Temperature("Bern", 20.0));
        repo.save(new Temperature("Basel", 20.1));
        repo.save(new Temperature("Zurich", 19.9));
    }

}
