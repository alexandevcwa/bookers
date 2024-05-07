package org.alexandevcwa.application.model.database;

import lombok.Getter;
import org.alexandevcwa.application.util.AppConfiguration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PostgreSQL {

    private String url;

    private String username;

    private String password;

    @Getter
    private Connection connection;

    private static PostgreSQL postgreSQL;

    /**
     * Método constructor de clase
     */
    private PostgreSQL(){
        try {
            this.url = AppConfiguration.getInstance().getProperty("postgresql.config.url");
            this.username = AppConfiguration.getInstance().getProperty("postgresql.config.username");
            this.password = AppConfiguration.getInstance().getProperty("postgresql.config.password");
        } catch (IOException e) {
            throw new RuntimeException("Error al configurar propiedades de conexión con base de datos\n " + e.getMessage());
        }
    }

    public static PostgreSQL getInstance(){
        if (postgreSQL == null){
            postgreSQL = new PostgreSQL();
            try {
                postgreSQL.openConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return postgreSQL;
    }

    private void openConnection() throws SQLException {
        Properties properties =new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        properties.setProperty("ssl", "false");
        this.connection = DriverManager.getConnection(url,properties);
    }
}
