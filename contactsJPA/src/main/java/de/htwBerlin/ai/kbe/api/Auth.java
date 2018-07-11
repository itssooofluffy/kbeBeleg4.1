package de.htwBerlin.ai.kbe.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.htwBerlin.ai.kbe.storage.UsersDAO;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Path("/auth")
public class Auth {

    private UsersDAO UsersDao;

    @Inject
    public Auth(UsersDAO userDao) {
        this.UsersDao = userDao;
    }

    // GET http://localhost:8080/SongsRX/rest/auth
    // Returns a token if user is inside contacts
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAuth(@QueryParam("userId") String userId) {
        String token = UsersDao.getToken();
        if (UsersDao.checkContact(userId)) {
            return Response.ok(token).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("There is no User with the userId " + userId).build();
        }
    }


}
