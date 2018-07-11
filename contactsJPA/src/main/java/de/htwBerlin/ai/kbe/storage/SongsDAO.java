package de.htwBerlin.ai.kbe.storage;

import java.util.Collection;

import de.htwBerlin.ai.kbe.data.Song;

public interface SongsDAO {

    /**
     * Retrieves a Song
     * 
     * @param SongId
     * @return
     */
    public Song findSongById(String SongId);

    /**
     * Retrieves all Songs
     * 
     * @return
     */
    public Collection<Song> findAllSongs();

    /**
     * Save a new Song
     * 
     * @param Song
     * @return the Id of the new Songs
     */
    public int saveSong(Song Song);
    
    /**
     * Deletes the Song for the provided id
     * @param id
     */
    public void deleteSong(String id);

    public boolean updateSong(Song song, String id);
}
