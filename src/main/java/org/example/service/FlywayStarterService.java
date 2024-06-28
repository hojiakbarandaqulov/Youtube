package org.example.service;

import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class FlywayStarterService implements CommandLineRunner {
    private final DataSource dataSource;

    public FlywayStarterService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
//        System.out.println("FlywayStarterService");
        Flyway.configure().baselineOnMigrate(true).dataSource(dataSource).load().migrate();
    }
}
