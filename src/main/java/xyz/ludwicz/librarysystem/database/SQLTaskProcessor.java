package xyz.ludwicz.librarysystem.database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class SQLTaskProcessor {

    private DataSource dataSource;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public SQLTaskProcessor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Future<Object> submitTask(SQLTask task) {
        return executorService.submit(new Callable<Object>() {
            @Override
            public Object call() {
                try (Connection connection = dataSource.getConnection()) {
                    switch (task.getTaskType()) {
                        case QUERY:
                            return task.executeQuery(connection);
                        case UPDATE:
                            task.executeUpdate(connection);
                            return true;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }
}
