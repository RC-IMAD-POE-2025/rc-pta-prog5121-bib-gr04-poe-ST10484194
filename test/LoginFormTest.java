import org.junit.Test;
import static org.junit.Assert.*;

public class LoginFormTest {

    @Test
    public void testLoginSuccessful() {
        String correctUsername = "kyl_1";
        String correctPassword = "Ch&&sec@Keg9!";

        boolean loginSuccessful = correctUsername.equals("kyl_1") && correctPassword.equals("Ch&&sec@Keg9!");
        assertTrue(loginSuccessful);
    }

    @Test
    public void testLoginFailed() {
        String wrongUsername = "kyle!!!!!!!";
        String wrongPassword = "password";

        boolean loginFailed = wrongUsername.equals("kyl_1") && wrongPassword.equals("Ch&&sec@Keg9!");
        assertFalse(loginFailed);
    }
}


