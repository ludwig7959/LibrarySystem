package xyz.ludwicz.librarysystem.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import xyz.ludwicz.librarysystem.Config;
import xyz.ludwicz.librarysystem.data.Book;
import xyz.ludwicz.librarysystem.data.BookInfo;
import xyz.ludwicz.librarysystem.data.Category;
import xyz.ludwicz.librarysystem.data.Checkout;
import xyz.ludwicz.librarysystem.data.Member;
import xyz.ludwicz.librarysystem.data.Publisher;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {

    private final HikariDataSource hikariDataSource;
    private final SessionFactory sessionFactory;

    private final SQLTaskProcessor taskProcessor;

    public DatabaseManager() throws HikariPool.PoolInitializationException {
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

        Configuration configuration = new Configuration();

        Properties settings = new Properties();
        settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        settings.put(Environment.URL, dsn);
        settings.put(Environment.USER, Config.DB_USERNAME);
        settings.put(Environment.PASS, Config.DB_PASSWORD);
        settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        settings.put(Environment.SHOW_SQL, "false");
        settings.put(Environment.FORMAT_SQL, "true");
        settings.put(Environment.HBM2DDL_AUTO, "update");
        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

        settings.put("hibernate.hikari.dataSourceClassName", "com.zaxxer.hikari.HikariDataSource");
        settings.put("hibernate.hikari.maximumPoolSize", String.valueOf(Config.DB_POOLING_MAX_POOL_SIZE));
        settings.put("hibernate.hikari.idleTimeout", String.valueOf(Config.DB_POOLING_MAX_LIFETIME));
        settings.put("hibernate.hikari.connectionTimeout", String.valueOf(Config.DB_POOLING_CONNECTION_TIMEOUT));

        configuration.setProperties(settings);

        configuration.addAnnotatedClass(Category.class);
        configuration.addAnnotatedClass(Publisher.class);
        configuration.addAnnotatedClass(Member.class);
        configuration.addAnnotatedClass(BookInfo.class);
        configuration.addAnnotatedClass(Book.class);
        configuration.addAnnotatedClass(Checkout.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        this.sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public Connection getConnection() throws SQLException {
        return this.hikariDataSource.getConnection();
    }

    public SQLTaskProcessor getTaskProcessor() {
        return taskProcessor;
    }
}
