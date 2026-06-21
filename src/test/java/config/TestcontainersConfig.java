package config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.wait.strategy.Wait;

public class TestcontainersConfig {

    public static final MySQLContainer<?> MY_SQL_CONTAINER;

    static {
        MY_SQL_CONTAINER = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.33"))
                .withDatabaseName("whatsapp-api-clean-test")
                .withUsername("root")
                .withPassword("Dioge1997")
                .withEnv("MYSQL_ROOT_PASSWORD", "Dioge1997")
                .withStartupTimeout(Duration.ofMinutes(5))
                .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofMinutes(5)));
        MY_SQL_CONTAINER.start();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurationApplication) {
            Map<String, Object> props = new HashMap<>();
            props.put("spring.datasource.url", MY_SQL_CONTAINER.getJdbcUrl());
            props.put("spring.datasource.username", MY_SQL_CONTAINER.getUsername());
            props.put("spring.datasource.password", MY_SQL_CONTAINER.getPassword());
            configurationApplication.getEnvironment().getPropertySources()
                    .addFirst(new MapPropertySource("testcontainers", props));
        }
    }
}
