package skkunion.union2024.auth.config;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationConfig {

    private static final List<String> authMatchers = new ArrayList<>();

    public static String[] getAuthMatchers() {
        return authMatchers.toArray(new String[0]);
    }

    static {
        authMatchers.add("/login");
        authMatchers.add("/logout");
        authMatchers.add("/reissue");
    }
}
