import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        UsersMaps usersMaps = new UsersMaps();
        readUsersFromFile("src\\main\\resources\\users.db", usersMaps);
        findDDOSUser(readServersLog("src\\main\\resources\\server.log", usersMaps), usersMaps);
    }

    static void readUsersFromFile(String fileName, UsersMaps usersMaps) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        scanner.nextLine();
        while (scanner.hasNext()) {
            String[] userData = scanner.nextLine().split(";");
            User user = new User(userData[1], userData[3], userData[2]);
            usersMaps.getUsersMap().put(user.getId(), user);
            if (usersMaps.getUsersIPs().get(user.getId()) != null) {
                Set<String> usersIpsSet = usersMaps.getUsersIPs().get(user.getId());
                usersIpsSet.add(userData[0]);
            } else {
                Set<String> usersIpsSet = new HashSet<>();
                usersIpsSet.add(userData[0]);
                usersMaps.getUsersIPs().put(user.getId(), usersIpsSet);
            }
        }
        scanner.close();
        System.out.printf("Файл %s прочитан.\n", fileName);
    }

    static Map<String, Integer> readServersLog(String fileName, UsersMaps usersMaps) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        Map<String, Integer> ipCounter = new HashMap<>();
        while (scanner.hasNext()) {
            String[] input = scanner.nextLine().split(":");
            if (ipCounter.get(input[0]) != null) {
                int i = ipCounter.get(input[0]);
                ipCounter.put(input[0], i + 1);
            } else {
                ipCounter.put(input[0], 1);
            }
        }

        System.out.printf("Файл %s прочитан.\n", fileName);
        return ipCounter;
    }


    static void findDDOSUser(Map<String, Integer> serverLog, UsersMaps usersMaps) {
        String ipWithMaxConnects = null;
        int max = 0;
        for (Map.Entry<String, Integer> entry : serverLog.entrySet()) {
            if (max < entry.getValue()) {
                max = entry.getValue();
            }
        }

        for (Map.Entry<String, Integer> entry : serverLog.entrySet()) {
            if (entry.getValue() == max) {
                ipWithMaxConnects = entry.getKey();
            }
        }

        String DDOSUser = null;
        for (Map.Entry<String, Set<String>> entry : usersMaps.getUsersIPs().entrySet()) {
            for (String ip : entry.getValue()) {
                assert ipWithMaxConnects != null;
                if (ipWithMaxConnects.equals(ip)) {
                    DDOSUser = entry.getKey();
                    break;
                }
            }
        }
        System.out.printf("ФИО: %s\nАдресс: %s\nКолличество входов: %d\nIP: %s",
                usersMaps.getUsersMap().get(DDOSUser).getFullName(),
                usersMaps.getUsersMap().get(DDOSUser).getAddress(),
                max, ipWithMaxConnects);
    }

    static void printMaps(UsersMaps usersMaps) {
        System.out.println("users");
        for (Map.Entry<String, User> entry : usersMaps.getUsersMap().entrySet()) {
            System.out.println(entry.getKey() + " | " + entry.getValue().toString());
        }
        System.out.println("----------------------------------------------------------------------------");
        for (Map.Entry<String, Set<String>> entry : usersMaps.getUsersIPs().entrySet()) {
            System.out.println(entry.getKey() + " | " + entry.getValue().toString());
        }
    }
}
