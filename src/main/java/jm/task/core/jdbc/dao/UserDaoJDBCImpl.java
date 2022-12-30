package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private Connection connection;
    private boolean findTableUser;

//    static {
//        try {
//            connection = Util.getConnection();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public UserDaoJDBCImpl() {
        try {
            connection = Util.getConnection();
            findTableUser = connection.getMetaData().getTables("work4dao",null,"user",null).next();
        } catch (SQLException e) {
            System.out.println("Error connection((");
        }
    }

    public void createUsersTable() {
        if (findTableUser) {return;}
        String sqlQuarryCreateUsersTable= "CREATE TABLE user (\n" +
                "  id BIGINT(20) NOT NULL AUTO_INCREMENT,\n" +
                "  name VARCHAR(45) NOT NULL,\n" +
                "  lastName VARCHAR(45) NOT NULL,\n" +
                "  age TINYINT NOT NULL,\n" +
                "  PRIMARY KEY (`id`))";

        try (Statement statement = connection.createStatement()){
            statement.executeUpdate(sqlQuarryCreateUsersTable);
            findTableUser = true;
        } catch (SQLException e) {
            System.out.println("Ошибка при создании таблицы");
        }

    }

    public void dropUsersTable() {
        if (!findTableUser) {return;}
        String sqlQuarrydropUsersTable= "drop table user";
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate(sqlQuarrydropUsersTable);
            findTableUser = false;
        } catch (SQLException e) {
            System.out.println("Ошибка при удаление таблицы");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        if (!findTableUser) {return;}
        String sqlQuarryAddNewUser = "INSERT INTO user (name, lastName, age) values (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuarryAddNewUser)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка при записи пользователя");
        }

    }

    public void removeUserById(long id) {
        if (!findTableUser) {return;}
        String sqlQuarryDelete = "delete from user where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuarryDelete)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении пользователя");
        }
    }

    public List<User> getAllUsers() {
        if (!findTableUser) {return new ArrayList<User>();}
        List<User> userList = new ArrayList<>();
        String sqlQuarrySelectAll = "select * from user";
        try (Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(sqlQuarrySelectAll);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении пользователей");
        }
        return userList;
    }

    public void cleanUsersTable() {
        if (!findTableUser) {return;}
        String sqlQuarryDeleteAll = "delete from user";
        try (Statement statement = connection.createStatement()) {

            statement.executeUpdate(sqlQuarryDeleteAll);
        } catch (SQLException e) {
            System.out.println("Ошибка при очистки таблицы");
        }
    }

}
