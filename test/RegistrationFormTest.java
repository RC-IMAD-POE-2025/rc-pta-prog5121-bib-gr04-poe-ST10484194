import org.junit.Test;
import static org.junit.Assert.*;

public class RegistrationFormTest {

    @Test
    public void testUsernameCorrectlyFormatted() {
        String username = "kyl_1";
        boolean result = username.contains("_") && username.length() <= 5;
        assertTrue(result);
    }
    


    @Test
    public void testPasswordMeetsComplexity() {
        String password = "Ch&&sec@Keg9!";
        boolean hasCapital = password.matches(".*[A-Z].*");
        boolean hasSpecial = password.matches(".*[^a-zA-Z0-9].*");
        boolean hasLength = password.length() >= 8;
        boolean result = hasCapital && hasSpecial && hasLength;
        assertTrue(result);
       
    }
    
   

    @Test
    public void testCellNumberCorrectFormat() {
        String cell = "+27839669876";
        boolean result = cell.matches("^\\+27\\d{9}$");
        assertTrue(result);
    }

    @Test
    public void testCellNumberIncorrectFormat() {
        String cell = "08966533";
        boolean result = cell.matches("^\\+27\\d{9}$");
        assertFalse(result);
    }
}
