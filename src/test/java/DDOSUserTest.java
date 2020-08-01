import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DDOSUserTest {
    private static long startTests;
    private long startTestTime;

    @BeforeAll
    static void init() {
        System.out.println("Starting Tests");
        startTests = System.currentTimeMillis();
    }

    @AfterAll
    static void completeTests() {
        System.out.println("Tests complete " + (System.currentTimeMillis() - startTests) + " milliseconds");
    }

    @BeforeEach
    void initTest() {
        System.out.println("Start new test");
        startTestTime = System.currentTimeMillis();
    }

    @AfterEach
    void completeTest() {
        System.out.println("Test complete " + (System.currentTimeMillis() - startTestTime) + " milliseconds");
    }

    @DisplayName("fileExist")
    @Test
    void fileExist() {
        File usersFile = new File("src\\main\\resources\\users.db");
        File serverLog = new File("src\\main\\resources\\server.log");
        assertTrue(usersFile.exists());
        assertTrue(serverLog.exists());
        System.out.println("users.db exists\n" + "server.log exists");
    }

    //В этом тесте я продублировал код поскольку строк много вручную считать глупо
    @Test
    void maxConnections() throws IOException {
        String serverLog = "src\\main\\resources\\server.log";
        Map<String, Integer> connectsMap = Main.readServersLog(serverLog, new UsersMaps());
        int max = 0;
        for (Map.Entry<String, Integer> entry : connectsMap.entrySet()) {
            if (max < entry.getValue()) {
                max = entry.getValue();
            }
        }
        assertEquals(210, max);
    }

    @Test
    void userMapNotEmpty() throws FileNotFoundException {
        String usersDb = "src\\main\\resources\\users.db";
        UsersMaps usersMaps = new UsersMaps();
        Main.readUsersFromFile(usersDb, usersMaps);
        assertNotNull(usersMaps);
    }
}
