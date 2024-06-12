package xyz.ludwicz.librarysystem.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import xyz.ludwicz.librarysystem.Config;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static DatabaseManager instance;

    synchronized public static DatabaseManager getInstance() {
        if(instance == null) {
            instance = new DatabaseManager();
        }

        return instance;
    }


    private final HikariDataSource hikariDataSource;

    private final SQLTaskProcessor taskProcessor;

    private DatabaseManager() throws HikariPool.PoolInitializationException {
        String dsn = "jdbc:mysql://" + Config.DB_HOST + ":" + Config.DB_PORT + "/" + Config.DB_NAME + Config.DB_FLAGS;
        HikariConfig config = new HikariConfig();

        config.setPoolName("LibrarySystem MySQL");
        config.setJdbcUrl(dsn);

        config.setUsername(Config.DB_USERNAME);
        config.setPassword(Config.DB_PASSWORD);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");
        config.addDataSourceProperty("cacheCallableStmts", "true");

        config.setMaximumPoolSize(Config.DB_POOLING_MAX_POOL_SIZE);
        config.setMaxLifetime(Config.DB_POOLING_MAX_LIFETIME);
        config.setConnectionTimeout(Config.DB_POOLING_CONNECTION_TIMEOUT);

        try {
            Driver driver;
            try {
                driver = (Driver) Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException e) {
                driver = (Driver) Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
            }

            DriverManager.registerDriver(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.hikariDataSource = new HikariDataSource(config);

        try (Connection connection = getConnection()) {
            SQLSchema.initTables(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.taskProcessor = new SQLTaskProcessor(hikariDataSource);
    }

    public Connection getConnection() throws SQLException {
        return this.hikariDataSource.getConnection();
    }

    public SQLTaskProcessor getTaskProcessor() {
        return taskProcessor;
    }
}
