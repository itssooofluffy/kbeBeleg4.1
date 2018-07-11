package de.htwBerlin.ai.kbe.api;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import de.htwBerlin.ai.kbe.data.Songlist;
import de.htwBerlin.ai.kbe.data.User;
import de.htwBerlin.ai.kbe.storage.SonglistDAO;
import de.htwBerlin.ai.kbe.storage.UsersDAO;
import java.io.IOException;
import javax.ws.rs.HeaderParam;

@Path("/UserId")
public class SonglistEndpoint {

    private UsersDAO UsersDao;
    private SonglistDAO SonglistDao;

    @Inject
    public SonglistEndpoint(UsersDAO userDao, SonglistDAO songlistDao) {
        this.UsersDao = userDao;
        this.SonglistDao = songlistDao;
    }

    @GET
    @Path("/{id}/songlists")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Collection<Songlist> getSonglists(@PathParam("id") String userId, @HeaderParam("authorization") String authString) throws IOException {
    	Collection<Songlist> songlist = null;
    	User user = UsersDao.findUserById(userId);
    	if (userId != null && isUserAuth(authString)) {
    		songlist = SonglistDao.findAllSonglistsByUserId(userId);
    	} else {
        songlist = SonglistDao.findPublicSonglistsByUserId(userId);
      }
    	return songlist;
    }

    @GET
    @Path("/{id}/songlists/{listId}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Songlist getSonglists(@PathParam("id") String userId, @PathParam("listId") String listId, @HeaderParam("authorization") String authString) throws IOException {
      User user = UsersDao.findUserById(userId);
      Songlist songlist = SonglistDao.findSonglistBySonglistId(listId);
      if (user != null && isUserAuth(authString) && songlist != null && songlist.getUser() == user) {
        return songlist;
      } else if (songlist != null && user !=null) {
            return SonglistDao.getPublicSonglistBySonglistId(listId);
      } else {
          return Response.status(Response.Status.NOT_FOUND).entity("No songlist found with id " + listId).build();
      }
    }

    @Context
    private UriInfo uriInfo;

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{id}/songlists/{listName}")
    public Response createSonglist(Songlist songlist, @PathParam("id") String userId, @PathParam("listName") String listName,
            @HeaderParam("authorization") String authString) throws IOException {
        User user = UsersDao.findUserById(userId);
        if (user != null && isUserAuth(authString)) {
            int newId = SonglistDao.saveSonglist(songlist);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            uriBuilder.path(Integer.toString(newId));
            return Response.created(uriBuilder.build()).build(); 
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("User is not authenticated!").build();
    }

    @PUT
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("/{id}")
    public Response updateSonglist(@PathParam("id") Integer id, Songlist songlist) {
        return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity("PUT not implemented").build();
    }

    @DELETE
    @Path("/{id}/songlists/{listId}")
    public Response delete(@PathParam("id") String userId,@PathParam("listId") Integer id,
            @HeaderParam("authorization") String authString) throws IOException {
        User user = UsersDao.findUserById(userId);
        if (user != null && isUserAuth(authString)) {
           SonglistDao.deleteSonglist(id);  
        } 
        return Response.status(Response.Status.NO_CONTENT).build();
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
