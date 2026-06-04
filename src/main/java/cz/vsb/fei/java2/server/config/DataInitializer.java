package cz.vsb.fei.java2.server.config;

import cz.vsb.fei.java2.server.entity.DifficultyLevel;
import cz.vsb.fei.java2.server.repository.DifficultyLevelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final DifficultyLevelRepository diffRepo;

    @Override
    public void run(String... args) {
        if (diffRepo.count() == 0) {
            for (int i = 1; i <= 10; i++) {
                int length = 2 * i + 3;
                double speed = 0.4 * Math.pow(0.8, i - 1);
                diffRepo.save(new DifficultyLevel(i, length, speed));
            }
        }
    }
}
