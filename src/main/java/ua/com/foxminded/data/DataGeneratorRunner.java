package ua.com.foxminded.data;

import org.junit.jupiter.api.Order;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import ua.com.foxminded.data.structure.DataStructureGenerator;

@Component
@Profile("dev")
@Order(1)
public class DataGeneratorRunner implements CommandLineRunner {

    private final DataStructureGenerator generator;

    public DataGeneratorRunner(DataStructureGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void run(String... args) throws Exception {
        generator.generateAll();
    }

}
