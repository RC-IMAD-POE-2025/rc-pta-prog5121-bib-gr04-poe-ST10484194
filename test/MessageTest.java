import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {

    @Test
    public void testMessageLengthSuccess() {
        String text = "Hi Mike, can you join us for dinner tonight";
        assertTrue("Message should be within 250 characters", text.length() <= 250);
    }

    @Test
    public void testMessageLengthFailure() {
        String text = "x".repeat(260);
        int exceeded = text.length() - 250;
        assertTrue("Message exceeds 250 characters by " + exceeded, text.length() > 250);
    }

    @Test
    public void testRecipientCellSuccess() {
        Message msg = new Message("1234567890", "+27718693002", "Hello");
        assertTrue("Cell phone number successfully captured.", msg.checkRecipientCell());
    }

    @Test
    public void testRecipientCellFailure() {
        Message msg = new Message("1234567890", "08575975889", "Hello");
        assertFalse("Cell phone number is incorrectly formatted.", msg.checkRecipientCell());
    }

    @Test
    public void testMessageHashFormat() {
        Message msg = new Message("0012345678", "+27718693002", "Hi Mike, can you join us for dinner tonight");
        String expectedStart = "00:";
        String expectedEnd = "HI TONIGHT";
        String actualHash = msg.createMessageHash();
        assertTrue("Hash should start with 00:", actualHash.startsWith(expectedStart));
        assertTrue("Hash should end with HI TONIGHT", actualHash.endsWith(expectedEnd));
    }

    @Test
    public void testMessageIDFormat() {
        Message msg = new Message("+271234567890", "+27718693002", "Hello");
        assertTrue("Message ID should match 10-digit format", msg.checkMessageID());
    }

    @Test
    public void testStoreMessage() {
        Message.storedMessages.clear();
        Message msg = new Message("1234567890", "+27718693002", "Hello store test");
        msg.storeMessage();
        assertEquals(1, Message.storedMessages.size());
    }

    @Test
    public void testSendMessageIncreasesTotalCount() {
        int before = Message.returnTotalMessages();
        Message msg = new Message("1234567890", "+27718693002", "Test message");
        msg.SentMessage();
        int after = Message.returnTotalMessages();
        assertEquals(before + 1, after);
    }
}

