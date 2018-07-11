package de.htwBerlin.ai.kbe.storage;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import de.htwBerlin.ai.kbe.data.Song;
import de.htwBerlin.ai.kbe.data.Songlist;

@Singleton
public class SonglistDAOImpl implements SonglistDAO {

    private EntityManagerFactory emf;

    @Inject
    public SonglistDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Collection<Songlist> findPublicSonglistsByUserId(String userId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Songlist> query = em.createQuery("SELECT c FROM Songlist c WHERE ispublic=true", Songlist.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Collection<Songlist> findAllSonglistsByUserId(String userId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Songlist> query = em.createQuery("SELECT c FROM Songlist c", Songlist.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Songlist findSonglistBySonglistId(String songlistId) {
        EntityManager em = emf.createEntityManager();
        Songlist entity = null;
        try {
            entity = em.find(Songlist.class, songlistId);
        } finally {
            em.close();
        }
        return entity;
    }

    public Songlist getPublicSonglistBySonglistId(String songlistId) {
        EntityManager em = emf.createEntityManager();
        Songlist entity = null;
        try {
            TypedQuery<Songlist> query = em.createQuery("SELECT c FROM Songlist c WHERE ispublic=true AND songlistId= :songlistId", Songlist.class).setParameter("songlistId", songlistId);
            return (Songlist) query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Integer saveSonglist(Songlist songlist) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            for (Song a : songlist.getSongSet()) {
                a.setSongList(songlist);
            }
            em.persist(songlist);
            transaction.commit();
            return songlist.getId();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error adding Songlist: " + e.getMessage());
            transaction.rollback();
            throw new PersistenceException("Could not persist entity: " + e.toString());
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteSonglist(Integer id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        Songlist songlist = null;
        try {
            songlist = em.find(Songlist.class, id);
            if (songlist != null) {
                System.out.println("Deleting: " + songlist.getId() + " with listName: " + songlist.getListName());
                transaction.begin();
                em.remove(songlist);
                transaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error removing Songlist: " + e.getMessage());
            transaction.rollback();
            throw new PersistenceException("Could not remove entity: " + e.toString());
        } finally {
            em.close();
        }
    }

}
