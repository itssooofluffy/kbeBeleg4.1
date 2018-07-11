/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htwBerlin.ai.kbe.storage;

import de.htwBerlin.ai.kbe.data.Song;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.NoResultException;

@Singleton
public class SongsDAOIml implements SongsDAO {

    private EntityManagerFactory emf;
    static List<String> tokenList = new ArrayList<>();
    private static String token = null;

    @Inject
    public SongsDAOIml(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Song findSongById(String songId) {
         EntityManager em = emf.createEntityManager(); 
        try { 
            TypedQuery<Song> query = em.createQuery("SELECT u FROM Song u where id = :songId", Song.class);
        query.setParameter("songId", songId); 
        Song song = query.getSingleResult(); 
        return song;
       } catch (NoResultException nre) { 
           return null; 
       } 
         finally { 
            em.close(); 
        } 
    }

    @Override
    public Collection<Song> findAllSongs() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Song> query = em.createQuery("SELECT c FROM User c", Song.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public int saveSong(Song Song) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(Song);
            transaction.commit();
            return Song.getId();
        } catch (Exception e) {
            transaction.rollback();
            throw new PersistenceException("Could not persist entity: " + e.toString());
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteSong(String id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        Song Song = null;
        try {
            Song = em.find(Song.class, id);
            if (Song != null) {
                transaction.begin();
                em.remove(Song);
                transaction.commit();
            }
        } catch (Exception e) {
            transaction.rollback();
            throw new PersistenceException("Could not remove entity: " + e.toString());
        } finally {
            em.close();
        }
    }

    @Override
    public boolean updateSong(Song song, String songId) {
        if (findSongById(songId) != null) {
            deleteSong(songId);
            saveSong(song);
            return true;
        } 
        return false;
    }

}
