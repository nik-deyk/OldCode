package com.velikiyprikalel.demo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.velikiyprikalel.demo.pojo.Task;

public class DBConnection {
    public static List<Task> getAll() {
        String query = "SELECT * from tasks_table";

        List<Task> list = new ArrayList<>();

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Task task = new Task(rs.getInt("id"), 
                    rs.getString("task_name"),
                    rs.getString("owner"),
                    rs.getInt("priority"));

                list.add(task);
            }

            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException|IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static Connection getConnection() throws FileNotFoundException, IOException, SQLException {
        Properties properties = new Properties();
        try (InputStream stream = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(stream);
        }

        String url = properties.getProperty("url");
        String user = properties.getProperty("username");
        String password = properties.getProperty("password");

        return DriverManager.getConnection(url, user, password);
    }
}
