package uk.ac.ncl.openlab.intake24.client.api.auth;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import org.fusesource.restygwt.client.Dispatcher;
import org.fusesource.restygwt.client.Method;
import uk.ac.ncl.openlab.intake24.client.ui.AuthTokenUIAdapter;
import uk.ac.ncl.openlab.intake24.client.ui.GenUserUIAdapter;
import uk.ac.ncl.openlab.intake24.client.ui.LoginUIAdapter;

import java.util.logging.Logger;

public class RefreshDispatcher implements Dispatcher {

    public static final RefreshDispatcher INSTANCE = new RefreshDispatcher();
    private static final Logger logger = Logger.getLogger(RefreshDispatcher.class.getName());

    private static final LoginUIAdapter loginUIAdapter = new LoginUIAdapter();
    private static final GenUserUIAdapter genUserUIAdapter = new GenUserUIAdapter();
    private static final AuthTokenUIAdapter authTokenUIAdapter = new AuthTokenUIAdapter();

    @Override
    public Request send(Method method, RequestBuilder builder) throws RequestException {
        logger.fine("Sending refresh request: " + builder.getHTTPMethod() + " " + builder.getUrl());

        String cachedRefreshToken = AuthCache.getCachedRefreshToken();

        if (cachedRefreshToken != null)
            builder.setHeader(AuthCache.AUTH_TOKEN_HEADER, cachedRefreshToken);

        RequestCallback userCallback = builder.getCallback();

        builder.setCallback(new RefreshCallback(method, userCallback, loginUIAdapter, genUserUIAdapter, authTokenUIAdapter));

        return builder.send();
    }
}