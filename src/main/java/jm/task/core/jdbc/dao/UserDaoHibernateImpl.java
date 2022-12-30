package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {

        sessionFactory = Util.getSessionFactory();

    }


    @Override
    public void createUsersTable() {

        String sqlQuarryCreateUsersTable = "CREATE TABLE IF NOT exists maxim_sch.users (\n" +
                "  id BIGINT(20) NOT NULL AUTO_INCREMENT,\n" +
                "  name VARCHAR(45) NOT NULL,\n" +
                "  lastName VARCHAR(45) NOT NULL,\n" +
                "  age TINYINT NOT NULL,\n" +
                "  PRIMARY KEY (`id`))";

        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlQuarryCreateUsersTable);

        } catch (SQLException e) {
            System.out.println("Ошибка при создании таблицы");
        }

    }

    @Override
    public void dropUsersTable() {

        String sqlQuarrydropUsersTable = "drop table IF exists maxim_sch.users";
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlQuarrydropUsersTable);

        } catch (SQLException e) {
            System.out.println("Ошибка при удаление таблицы");
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();

            User user = new User(name, lastName, age);
            session.save(user);
            transaction.commit();

        } catch (Exception e) {
            System.out.println("____________________________________123123");
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            transaction.commit();

        } catch (Exception e) {
            System.out.println("____________________________________123123");
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {

        List<User> userList = new ArrayList<>();
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            userList = session.createQuery("from User").getResultList();

            transaction.commit();

        } catch (Exception e) {
            System.out.println("____________________________________123123");
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.createQuery("delete User").executeUpdate();
            //User user = session.get(User.class, id);
            transaction.commit();

        } catch (Exception e) {
            System.out.println("____________________________________123123");
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}
