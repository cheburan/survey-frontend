package uk.ac.ncl.openlab.intake24.client.api.auth;

import com.google.gwt.core.client.GWT;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Options(serviceRootKey = "intake24-api")
public interface GeneratedUsersService extends RestService {
    GeneratedUsersService INSTANCE = GWT.create(GeneratedUsersService.class);

    @POST
    @Path("/surveys/{surveyId}/generate-user")
    void generateUser(@PathParam("surveyId") String surveyId, MethodCallback<GeneratedCredentials> callback);
}
