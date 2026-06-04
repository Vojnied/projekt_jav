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
                diffRepo.save(new DifficultyLevel(i));
            }
        }
    }
}
