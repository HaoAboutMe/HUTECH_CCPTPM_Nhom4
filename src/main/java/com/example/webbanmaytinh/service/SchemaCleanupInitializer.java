package com.example.webbanmaytinh.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SchemaCleanupInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public SchemaCleanupInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        Integer roleColumnCount = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM information_schema.columns
                WHERE table_schema = DATABASE()
                  AND table_name = 'users'
                  AND column_name = 'role'
                """,
                Integer.class
        );

        if (roleColumnCount != null && roleColumnCount > 0) {
            jdbcTemplate.execute("ALTER TABLE users DROP COLUMN role");
        }
    }
}
