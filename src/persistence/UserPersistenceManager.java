package persistence;

import entities.User;
import entities.ViewsCounter;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

/**
 * A singleton class which is used for working with the database.
 * The instance of FrontControllerHelper references the instance of this class.
 * Used to persist or search users and also to increase the views counter
 */
public class UserPersistenceManager {

    private static UserPersistenceManager SINGLETON_INSTANCE;
    private SessionFactory factory;

    private UserPersistenceManager(){
        factory = new Configuration().configure().buildSessionFactory();
    }

    public static UserPersistenceManager getInstance() {
        if(SINGLETON_INSTANCE == null){
            SINGLETON_INSTANCE = new UserPersistenceManager();
        }
        return SINGLETON_INSTANCE;
    }

    // Writes the specified user into the database and returns the generated id.
    public int persistUser(User user) {

        Session session = factory.openSession();
        Transaction tx = null;
        Integer id = null;

        try{
            tx = session.beginTransaction();
            id = (Integer) session.save(user);
            tx.commit();
        }
        catch(HibernateException hbe){
            System.out.println("Hibernate Exception! Message: " + hbe.getMessage());
            if(tx!=null) {tx.rollback();}
            hbe.printStackTrace();
        }
        finally{
            session.close();
        }
        return id;
    }

    // Finds the specified user in the database and updates its values accordingly.
    public void updatetUser(User user) {

        Session session = factory.openSession();
        Transaction tx = null;

        try{
            tx = session.beginTransaction();
            session.update(user);
            tx.commit();
        }
        catch(HibernateException hbe){
            System.out.println("Hibernate Exception! Message: " + hbe.getMessage());
            if(tx!=null) {tx.rollback();}
            hbe.printStackTrace();
        }
        finally{
            session.close();
        }
    }

    /**
     * Looks in the database for a user with the specified name and password and returns the (first) result
     * You can pass null as password to search only by the username, for example to check if a username already exists.
     */
    public User findUser(String name, String password){

        Session session = factory.openSession();
        Transaction tx = null;
        User user = null;

        try{
            tx = session.beginTransaction();

            Query query = null;
            if(password == null){
                query = session.createQuery("SELECT U FROM entities.User U WHERE U.username=?1");
            }
            else{
                query = session.createQuery("SELECT U FROM entities.User U WHERE U.username=?1 AND U.password=?2");
                query.setParameter(2, password);
            }
            query.setParameter(1, name);

            List results = query.list();
            if(!results.isEmpty()){
                user = (User) results.get(0);
            }

            tx.commit();
        }
        catch(HibernateException hbe){
            System.out.println("Hibernate Exception! Message: " + hbe.getMessage());
            if(tx!=null) {tx.rollback();}
            hbe.printStackTrace();
        }
        finally{
            session.close();
        }

        return user;
    }

    /**
     * Increases the count-value of the (one and only) ViewsCounter object in the database by one
     * and returns the changed object. If there is no ViewsCounter object in the database yet,
     * creates and persists a new one with the counter value 1.
     */
    public ViewsCounter increaseAndGetViewsCounter(){

        Session session = factory.openSession();
        Transaction tx = null;
        ViewsCounter counter = null;

        try{
            tx = session.beginTransaction();

            Query query = session.createQuery("SELECT V FROM entities.ViewsCounter V");

            List results = query.list();
            if(!results.isEmpty()){
                counter = (ViewsCounter) results.get(0);
                int value = counter.getCount();
                counter.setCount(value + 1);
                session.update(counter);
            }
            else{
                counter = new ViewsCounter(1);
                session.save(counter);
            }

            tx.commit();
        }
        catch(HibernateException hbe){
            System.out.println("Hibernate Exception! Message: " + hbe.getMessage());
            if(tx!=null) {tx.rollback();}
            hbe.printStackTrace();
        }
        finally{
            session.close();
        }

        return counter;
    }
}
