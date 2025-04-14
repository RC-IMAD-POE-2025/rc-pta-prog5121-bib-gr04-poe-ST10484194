import org.json.simple.JSONObject;

public class Message {
    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;
    
    public Message(String messageID, String recipient, String messageText, String messageHash) {
        this.messageID = messageID;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = messageHash;      
    }
    
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("message_id", messageID);
        obj.put("recipient", recipient);
        obj.put("message_text", messageText);
        obj.put("message_hash", messageHash);
        return obj;
    }
    
}
