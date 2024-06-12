package xyz.ludwicz.librarysystem.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLSchema {

    private static final String[] schemas = {
            "CREATE TABLE IF NOT EXISTS Member(" +
                    "memberId VARCHAR(16) NOT NULL," +
                    "name VARCHAR(32) NOT NULL," +
                    "phone VARCHAR(16) NOT NULL UNIQUE," +
                    "type VARCHAR(10) NOT NULL," +
                    "PRIMARY KEY(memberId)" +
                    ");",
            "CREATE TABLE IF NOT EXISTS Librarian(" +
                    "librarianId VARCHAR(32) NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "name VARCHAR(32) NOT NULL," +
                    "phone VARCHAR(16) NOT NULL UNIQUE," +
                    "PRIMARY KEY(librarianId)" +
                    ");",
            "CREATE TABLE IF NOT EXISTS Publisher(" +
                    "publisherId INTEGER NOT NULL AUTO_INCREMENT," +
                    "name VARCHAR(32) NOT NULL UNIQUE," +
                    "address VARCHAR(255) NOT NULL," +
                    "PRIMARY KEY(publisherId)" +
                    ");",
            "CREATE TABLE IF NOT EXISTS Category(" +
                    "categoryId INTEGER NOT NULL AUTO_INCREMENT," +
                    "name VARCHAR(16) NOT NULL UNIQUE," +
                    "PRIMARY KEY(categoryId)" +
                    ");",
            "CREATE TABLE IF NOT EXISTS BookInfo(" +
                    "bookInfoId INTEGER NOT NULL AUTO_INCREMENT," +
                    "title VARCHAR(255) NOT NULL," +
                    "author VARCHAR(32) NOT NULL," +
                    "publisherId INTEGER NOT NULL," +
                    "categoryId INTEGER DEFAULT NULL," +
                    "PRIMARY KEY(bookInfoId)," +
                    "FOREIGN KEY(publisherId) REFERENCES Publisher(publisherId)" +
                    "ON DELETE CASCADE," +
                    "FOREIGN KEY(categoryId) REFERENCES Category(categoryId)" +
                    "ON DELETE SET NULL" +
                    ");",
            "CREATE TABLE IF NOT EXISTS Book(" +
                    "bookId INTEGER NOT NULL AUTO_INCREMENT," +
                    "bookInfoId INTEGER NOT NULL," +
                    "PRIMARY KEY(bookId)," +
                    "FOREIGN KEY(bookInfoId) REFERENCES BookInfo(bookInfoId)" +
                    "ON DELETE CASCADE" +
                    ");",
            "CREATE TABLE IF NOT EXISTS Checkout(" +
                    "checkoutId INTEGER NOT NULL AUTO_INCREMENT," +
                    "bookId INTEGER NOT NULL," +
                    "memberId VARCHAR(16) NOT NULL," +
                    "librarianId VARCHAR(32)," +
                    "dueDate DATE NOT NULL," +
                    "returnDate DATE DEFAULT NULL," +
                    "PRIMARY KEY(checkoutId)," +
                    "FOREIGN KEY(bookId) REFERENCES Book(bookId)" +
                    "ON DELETE CASCADE," +
                    "FOREIGN KEY(memberId) REFERENCES Member(memberId)" +
                    "ON DELETE CASCADE," +
                    "FOREIGN KEY(librarianId) REFERENCES Librarian(librarianId)" +
                    "ON DELETE SET NULL" +
                    ");"
    };

    public static void initTables(Connection connection) {
        for(String schema : schemas) {
            initTable(connection, schema);
        }
    }

    private static void initTable(Connection connection, String schema) {
        try (Statement s = connection.createStatement()) {
            s.executeUpdate(schema);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
