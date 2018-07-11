package de.htwBerlin.ai.kbe.example;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.htwBerlin.ai.kbe.data.Songlist;
import de.htwBerlin.ai.kbe.data.User;

public class UsersTesterMain {


    public static void main(String[] args) {
        
        // Datei persistence.xml wird automatisch eingelesen, beim Start der Applikation
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("UsersDB-PU");

        // EntityManager bietet Zugriff auf Datenbank
        EntityManager em = factory.createEntityManager();
        
        try {
            em.getTransaction().begin();
            // Create: neuen User anlegen
            
            User User = new User("3", "Bobby", "Junior");
            Songlist Songlist1 = new Songlist("Bobby Str 1",true, User);
            Songlist Songlist2 = new Songlist("New Str 2",false, User);
            Set<Songlist> SonglistSet = new HashSet<>();
            SonglistSet.add(Songlist1);
            SonglistSet.add(Songlist2);
            User.setSonglistSet(SonglistSet);
            // Wir persistieren nur User, 
            // wegen cascade=CascadeType.ALL werden auch Songlist1, Songlist 2 persistiert
            em.persist(User);
            
            // commit transaction
            em.getTransaction().commit();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // EntityManager nach Datenbankaktionen wieder freigeben
            em.close();
            // Freigabe am Ende der Applikation
            factory.close();
        }
    }


}