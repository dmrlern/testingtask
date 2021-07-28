package apitests;

import java.io.IOException;

public enum UseBearerToken {

    INSTANCE;

    public String getBearerToken() throws IOException {
        Authenticate authenticate = new Authenticate();
        return authenticate.getBearerToken();
    }
}
