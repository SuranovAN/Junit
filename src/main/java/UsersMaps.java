

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class UsersMaps {
    private final Map<String, User> usersMap;
    private final Map<String, Set<String>> usersIPs;

    public UsersMaps() {
        usersMap = new TreeMap<>();
        usersIPs = new HashMap<>();
    }

    public Map<String, User> getUsersMap() {
        return usersMap;
    }

    public Map<String, Set<String>> getUsersIPs() {
        return usersIPs;
    }
}
