import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Message {

    private String messageId;
    private String recipient;
    private String text;
    private String hash;

    public Message(String messageId, String recipient, String text) {
        this.messageId = messageId;
        this.recipient = recipient;
        this.text = text;
        this.hash = createMessageHash();
    }

    public boolean checkMessageID() {
        return messageId.matches("\\d{10}");
    }

    public boolean checkRecipientCell() {
        return recipient.startsWith("+27") && recipient.length() == 12;
    }

    public String createMessageHash() {
        String[] words = text.split(" ");
        String first = words.length > 0 ? words[0] : "";
        String last = words.length > 1 ? words[words.length - 1] : first;
        return messageId.substring(0, 2) + ":" + messageId + ":" + first.toUpperCase() + "-" + last.toUpperCase();
    }

    public void SentMessage() {
        MessageManager.sentMessages.add(this);
        MessageManager.messageHashes.add(this.hash);
        MessageManager.messageIds.add(this.messageId);
    }

    public void storeMessage() {
        MessageManager.storedMessages.add(this);
        MessageManager.messageHashes.add(this.hash);
        MessageManager.messageIds.add(this.messageId);
        saveToJsonFile();
    }

    public void discardMessage() {
        MessageManager.disregardedMessages.add(this);
    }

    public static int returnTotalMessages() {
        return MessageManager.sentMessages.size();
    }

    public static void loadStoredMessagesFromJson() {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("storedMessages.json")) {
            JSONArray messageArray = (JSONArray) parser.parse(reader);
            for (Object obj : messageArray) {
                JSONObject msg = (JSONObject) obj;
                String id = (String) msg.get("messageId");
                String recipient = (String) msg.get("recipient");
                String text = (String) msg.get("text");
                Message m = new Message(id, recipient, text);
                MessageManager.storedMessages.add(m);
                MessageManager.messageHashes.add(m.getHash());
                MessageManager.messageIds.add(m.getMessageId());
            }
        } catch (Exception e) {
            System.out.println("Error loading stored messages: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void saveToJsonFile() {
        JSONArray messages = new JSONArray();

        // Load current JSON content
        try (FileReader reader = new FileReader("storedMessages.json")) {
            JSONParser parser = new JSONParser();
            messages = (JSONArray) parser.parse(reader);
        } catch (Exception ignored) {}

        // Add this message
        JSONObject obj = new JSONObject();
        obj.put("messageId", messageId);
        obj.put("recipient", recipient);
        obj.put("text", text);
        messages.add(obj);

        // Save
        try (FileWriter file = new FileWriter("storedMessages.json")) {
            file.write(messages.toJSONString());
        } catch (IOException e) {
            System.out.println("Error writing to JSON file: " + e.getMessage());
        }
    }

    public String getMessageId() {
        return messageId;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getText() {
        return text;
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return "Message ID: " + messageId +
                "\nRecipient: " + recipient +
                "\nText: " + text +
                "\nHash: " + hash;
    }
}
