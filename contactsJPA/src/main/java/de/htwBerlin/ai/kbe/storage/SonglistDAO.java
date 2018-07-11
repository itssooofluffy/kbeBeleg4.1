package de.htwBerlin.ai.kbe.storage;

import java.util.Collection;

import de.htwBerlin.ai.kbe.data.Songlist;

public interface SonglistDAO {

    /**
     * Retrieves all public Songlists
     *
     * @param userId
     * @return
     */
    public Collection<Songlist> findPublicSonglistsByUserId(String userId);

    /**
     * Retrieves all Songlists
     *
     * @param userId
     * @return
     */
    public Collection<Songlist> findAllSonglistsByUserId(String userId);

    /**
     * Retrieves a Songlist
     *
     * @param SonglistId
     * @return
     */
    public Songlist findSonglistBySonglistId(String SonglistId);

    public Songlist getPublicSonglistBySonglistId(String songlistId);
     /**
     * Save a new Songlist
     *
     * @param Songlist
     * @return the Id of the new Songlists
     */

    public Integer saveSonglist(Songlist Songlist);

    /**
     * Deletes the Songlist for the provided id
     *
     * @param id
     */
    public void deleteSonglist(Integer id);
}
