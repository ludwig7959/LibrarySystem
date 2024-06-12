package xyz.ludwicz.librarysystem.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SQLTask {
    public enum TaskType {
        QUERY, UPDATE
    }

    private String sql;
    private Object[] parameters;
    private TaskType taskType;

    public SQLTask(TaskType taskType, String sql, Object... parameters) {
        this.sql = sql;
        this.parameters = parameters;
        this.taskType = taskType;
    }

    public ResultSet executeQuery(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < parameters.length; i++) {
            preparedStatement.setObject(i + 1, parameters[i]);
        }
        return preparedStatement.executeQuery();
    }

    public int executeUpdate(Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setObject(i + 1, parameters[i]);
            }
            return preparedStatement.executeUpdate();
        }
    }

    public String getSql() {
        return sql;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public TaskType getTaskType() {
        return taskType;
    }
}
