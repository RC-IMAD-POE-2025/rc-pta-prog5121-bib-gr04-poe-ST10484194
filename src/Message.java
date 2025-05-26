import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Message {
    private static int totalMessages = 0;
    private String messageId;
    private String recipient;
    private String text;
    private String hash;

    public static List<Message> storedMessages = new ArrayList<>();

    public Message(String messageId, String recipient, String text) {
        this.messageId = messageId;
        this.recipient = recipient;
        this.text = text;
        this.hash = createMessageHash();
    }

    // Message ID validation
    public boolean checkMessageID() {
        return messageId != null && messageId.matches("\\d{10}");
    }

    // Recipient cell validation
    public boolean checkRecipientCell() {
        return recipient != null && recipient.matches("\\+27\\d{9}");
    }

    public String createMessageHash() {
        if (text == null || text.trim().isEmpty()) return "INVALID";
        String[] words = text.trim().split("\\s+");
        String first = words[0];
        String last = words[words.length - 1];
        String prefix = messageId.length() >= 2 ? messageId.substring(0, 2) : "00";
        return prefix + ":" + (totalMessages + 1) + ":" + (first + " " + last).toUpperCase();
    }

    public void SentMessage() {
        totalMessages++;
        System.out.println("Message sent: " + this);
    }

    public static void printMessages() {
        for (Message msg : storedMessages) {
            System.out.println(msg);
        }
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    public void storeMessage() {
        storedMessages.add(this);
        saveMessagesToJSON();
    }

    @SuppressWarnings("unchecked")
    private void saveMessagesToJSON() {
        JSONArray messageArray = new JSONArray();
        for (Message msg : storedMessages) {
            JSONObject obj = new JSONObject();
            obj.put("messageId", msg.messageId);
            obj.put("recipient", msg.recipient);
            obj.put("text", msg.text);
            obj.put("hash", msg.hash);
            messageArray.add(obj);
        }

        try (FileWriter file = new FileWriter("messages.json")) {
            file.write(messageArray.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.out.println("Error writing JSON: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "To: " + recipient + "\nText: " + text + "\nHash: " + hash + "\n";
    }
}


