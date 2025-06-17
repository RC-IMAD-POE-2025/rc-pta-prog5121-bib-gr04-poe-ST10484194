import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {

    @Test
    public void testCheckMessageID_Valid() {
        Message msg = new Message("1234567890", "+27831234567", "Hello world");
        assertTrue(msg.checkMessageID());
    }

    @Test
    public void testCheckMessageID_Invalid() {
        Message msg = new Message("123", "+27831234567", "Hello world");
        assertFalse(msg.checkMessageID());
    }

    @Test
    public void testCheckRecipientCell_Valid() {
        Message msg = new Message("1234567890", "+27831234567", "Hi there");
        assertTrue(msg.checkRecipientCell());
    }

    @Test
    public void testCheckRecipientCell_Invalid() {
        Message msg = new Message("1234567890", "0831234567", "Hi there");
        assertFalse(msg.checkRecipientCell());
    }

    @Test
    public void testCreateMessageHash() {
        Message msg = new Message("1234567890", "+27831234567", "Hello world this is a test");
        String hash = msg.createMessageHash();
        assertTrue(hash.startsWith("12:"));
        assertTrue(hash.contains("HELLO"));
        assertTrue(hash.contains("TEST"));
    }

    @Test
    public void testMessageLength() {
        String longText = "a".repeat(251);
        Message msg = new Message("1234567890", "+27831234567", longText);
        assertTrue(msg.getText().length() > 250);
    }
}


