package de.htwBerlin.ai.kbe.storage;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import de.htwBerlin.ai.kbe.data.Songlist;
import de.htwBerlin.ai.kbe.data.User;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DBUsersDAO implements UsersDAO {

    private EntityManagerFactory emf;
    static List<String> tokenList = new ArrayList<>();
    private static String token = null;

    @Inject
    public DBUsersDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public User findUserById(String id) {
        EntityManager em = emf.createEntityManager();
        User entity = null;
        try {
            entity = em.find(User.class, id);
        } finally {
            em.close();
        }
        return entity;
    }

    public Collection<User> findAllUsers() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT c FROM User c", User.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public int saveUser(User User) throws PersistenceException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            // MUST set the User in every address
            for (Songlist a : User.getSonglistSet()) {
                a.setUser(User);
            }
            em.persist(User);
            transaction.commit();
            return User.getId();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error adding User: " + e.getMessage());
            transaction.rollback();
            throw new PersistenceException("Could not persist entity: " + e.toString());
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteUser(String bobId) throws PersistenceException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        User User = null;
        try {
            User = em.find(User.class, bobId);
            if (User != null) {
                System.out.println("Deleting: " + User.getId() + " with firstName: " + User.getFirstName());
                transaction.begin();
                em.remove(User);
                transaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error removing User: " + e.getMessage());
            transaction.rollback();
            throw new PersistenceException("Could not remove entity: " + e.toString());
        } finally {
            em.close();
        }
    }

    @Override
    public boolean checkContact(String userId) {
        Collection<User> listOfContacts = new ArrayList<>();
        listOfContacts = findAllUsers();
        for (User contactIterator : listOfContacts) {
            if (contactIterator.getUserId().equals(userId)) {
                createToken();
                return true;
            }
        }
        return false;
    }

    private String createToken() {
        SecureRandom random = new SecureRandom();
        long longToken = Math.abs(random.nextLong());
        String createdToken = Long.toString(longToken, 16);
        this.token = createdToken;
        tokenList.add("testToken");
        tokenList.add(token);
        return createdToken;
    }

    @Override
    public List<String> getTokenList() {
        return tokenList;
    }

    public static void addTokenList(String token) {
        tokenList.add(token);
    }

    @Override
    public String getToken() {
        return token;
    }

}
