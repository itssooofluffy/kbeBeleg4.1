package de.htwBerlin.ai.kbe.api;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.htwBerlin.ai.kbe.data.Song;
import de.htwBerlin.ai.kbe.storage.SongsDAO;
import de.htwBerlin.ai.kbe.storage.UsersDAO;
import java.io.IOException;
import java.util.Objects;
import javax.inject.Inject;
import javax.ws.rs.HeaderParam;

@Path("/songs")
public class SongsEndpoint {

    private UsersDAO UsersDao;
    private SongsDAO SongsDao;

    @Inject
    public SongsEndpoint(UsersDAO userDao, SongsDAO songsDao) {
        this.UsersDao = userDao;
        this.SongsDao = songsDao;
    }

    // GET http://localhost:8080/songsRX/rest/songs
    // Returns all songs
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAllSongs(@HeaderParam("authorization") String authString) throws IOException {
        Collection<Song> songs;
        if (!isUserAuth(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User is not authenticated!").build();
        }
        songs = SongsDao.findAllSongs();
        return Response.ok(songs).build();
    }

    // GET http://localhost:8080/songsRX/rest/songs/10
    // Returns: 200 and song with id 10
    // Returns: 404 on provided id not found
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getSongs(@PathParam("id") String id,
            @HeaderParam("authorization") String authString) throws IOException {
        Song song = SongsDao.findSongById(id);
        if (!isUserAuth(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User is not authenticated!").build();
        }
        if (song != null) {
            return Response.ok(song).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("No song found with id " + id).build();
        }
    }

    // POST http://localhost:8080/songsRX/rest/songs with song in payload
    // Returns: Status Code 201 and the new id of the song in the payload
    // (temp. solution)
    //
    // Besser: Status Code 201 und URI fuer den neuen Eintrag im http-header
    // 'Location' zurueckschicken, also:
    // return Response.created(uriInfo.getAbsolutePath()+<newId>).build();
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.TEXT_PLAIN)
    public Response createSong(@HeaderParam("authorization") String authString,
            Song song) throws IOException {
        if (!isUserAuth(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User is not authenticated!").build();
        }
        return Response.status(Response.Status.CREATED).entity(SongsDao.saveSong(song)).build();
    }

    // PUT http://localhost:8080/songsRX/rest/song/10 with updated song in
    // payload
    // Returns 204 on successful update
    // Returns 404 on song with provided id not found
    // Returns 400 on id in URL does not match id in song
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/{id}")
    public Response updateSong(@PathParam("id") String id,
            @HeaderParam("authorization") String authString, Song song) throws IOException {
        boolean done = SongsDao.updateSong(song, id);
        if (!isUserAuth(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User is not authenticated!").build();
        }
        if (!Objects.equals(song.getId(), id)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID in URL " + id + " does not match Song ID " + song.getId()).build();
        } else if (done) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else if (!done) {
            return Response.status(Response.Status.NOT_FOUND).entity("No song found with id " + id).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    // DELETE http://localhost:8080/songsRX/rest/contacts/1
    // Returns 204 on successful delete
    // Returns 404 on provided id not found
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id,
            @HeaderParam("authorization") String authString) throws IOException {
        Song song = SongsDao.findSongById(id);
        SongsDao.deleteSong(id);
        if (!isUserAuth(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User is not authenticated!").build();
        }
        if (song != null) {
            return Response.status(Response.Status.NO_CONTENT).entity("Song was deleted " + id).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("No song found with id " + id).build();
        }
    }

    private boolean isUserAuth(String authString) throws IOException {
        if (authString != null) {
            Collection<String> tokenList = UsersDao.getTokenList();
            if (tokenList.stream().anyMatch((token) -> (token.equals(authString)))) {
                return true;
            }
        }
        return false;
    }
}
