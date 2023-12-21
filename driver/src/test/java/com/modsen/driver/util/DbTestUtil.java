package com.modsen.driver.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class DbTestUtil {

    public static void executeScript(String pathToScript, JdbcTemplate jdbcTemplate) {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            Resource resource = new ClassPathResource(pathToScript);
            ScriptUtils.executeSqlScript(connection, resource);
        } catch (SQLException e) {
            throw new RuntimeException("Error executing SQL script", e);
        }
    }

}
