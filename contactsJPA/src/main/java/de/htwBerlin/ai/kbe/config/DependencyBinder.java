package de.htwBerlin.ai.kbe.config;



import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import de.htwBerlin.ai.kbe.storage.UsersDAO;
import de.htwBerlin.ai.kbe.storage.DBUsersDAO;
import de.htwBerlin.ai.kbe.storage.SonglistDAO;
import de.htwBerlin.ai.kbe.storage.SonglistDAOImpl;

public class DependencyBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind (Persistence.createEntityManagerFactory("songDB-PU")).to(EntityManagerFactory.class);
        bind(DBUsersDAO.class).to(UsersDAO.class).in(Singleton.class);
        bind(SonglistDAOImpl.class).to(SonglistDAO.class).in(Singleton.class);
        bind(SongsDAOIml.class).to(SongsDAO.class).in(Singleton.class);
    }
}
