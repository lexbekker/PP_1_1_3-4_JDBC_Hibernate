package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Connection con;

    {
        try {
            con = Util.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String SQL = "CREATE TABLE IF NOT EXISTS users(" +
                "id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL," +
                "name varchar(50) NOT NULL," +
                "lastname varchar(50) NOT NULL," +
                "age tinyint NOT NULL," +
                "PRIMARY KEY (id));";
        try (PreparedStatement statement = con.prepareStatement(SQL)) {
            con.setAutoCommit(false);
            statement.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void dropUsersTable() {
        try {
            con.setAutoCommit(false);
            con.createStatement().executeUpdate("drop table if exists users");
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        try (PreparedStatement preparedStatement = con
                .prepareStatement("INSERT INTO users(name, lastname, age) VALUES (?,?,?)")) {
            con.setAutoCommit(false);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setByte(3, user.getAge());
            preparedStatement.executeUpdate();
            con.commit();
            System.out.printf("User с именем - %s добавлен в базу данных\n", name);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = con
                .prepareStatement("delete from users where id = ?")) {
            con.setAutoCommit(false);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (PreparedStatement statement = con.prepareStatement("select id, name, lastname, age from users");
             ResultSet rs = statement.executeQuery()) {
            con.setAutoCommit(false);
            while (rs.next()) {
                list.add(getUserFromRs(rs));
            }
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private User getUserFromRs(ResultSet rs) throws SQLException {
        User result = new User();
        result.setId(rs.getLong(1));
        result.setName(rs.getString(2));
        result.setLastName(rs.getString(3));
        result.setAge(rs.getByte(4));

        return result;
    }

    public void cleanUsersTable() {
        try (PreparedStatement preparedStatement = con
                .prepareStatement("truncate table users")) {
            con.setAutoCommit(false);
            preparedStatement.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
