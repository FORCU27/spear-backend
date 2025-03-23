package im.spear.app;

import im.spear.core.SpearCoreConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(SpearCoreConfiguration.class)
@SpringBootApplication
public class SpearApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpearApiApplication.class, args);
    }
}
