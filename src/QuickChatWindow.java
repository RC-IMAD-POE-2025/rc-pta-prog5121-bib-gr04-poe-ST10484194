import javax.swing.*;

public class QuickChatWindow {

    public QuickChatWindow() {
        showMenu();
    }

    private void showMenu() {
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat!", "Welcome", JOptionPane.INFORMATION_MESSAGE);

        boolean running = true;

        while (running) {
            String[] options = {
                "1) Send Messages", 
                "2) View Sent/Stored/Discarded Messages",
                "3) Reports",
                "4) Quit"
            };
            String menuChoice = (String) JOptionPane.showInputDialog(
                    null,
                    "Select an option:",
                    "QuickChat Menu",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (menuChoice == null || menuChoice.contains("4)")) {
                running = false;
                break;
            }

            switch (menuChoice) {
                case "1) Send Messages":
                    handleSendMessages();
                    break;

                case "2) View Sent/Stored/Discarded Messages":
                    viewAllMessages();
                    break;

                case "3) Reports":
                    displayReports();
                    break;
            }
        }

        JOptionPane.showMessageDialog(null, "Thanks for using QuickChat!");
    }

    private void handleSendMessages() {
        String countStr = JOptionPane.showInputDialog("How many messages would you like to send?");
        if (countStr == null) return;

        int count;
        try {
            count = Integer.parseInt(countStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number.");
            return;
        }

        for (int i = 0; i < count; i++) {
            String messageId = JOptionPane.showInputDialog("Enter 10-digit message ID:");
            if (messageId == null) return;

            String recipient = JOptionPane.showInputDialog("Enter recipient number (+27 format):");
            if (recipient == null) return;

            String text = JOptionPane.showInputDialog("Enter message text (max 250 characters):");
            if (text == null) return;

            if (text.length() > 250) {
                JOptionPane.showMessageDialog(null, "Message too long. Max 250 characters.");
                i--;
                continue;
            }

            Message msg = new Message(messageId, recipient, text);

            if (!msg.checkMessageID()) {
                JOptionPane.showMessageDialog(null, "Invalid Message ID.");
                i--;
                continue;
            }

            if (!msg.checkRecipientCell()) {
                JOptionPane.showMessageDialog(null, "Invalid Recipient Number. Must start with +27.");
                i--;
                continue;
            }

            String hash = msg.createMessageHash();

            String[] choices = {"Send", "Store", "Discard"};
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Message:\n" + text + "\nHash: " + hash,
                    "Choose Action",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    choices,
                    choices[0]);

            switch (choice) {
                case 0: // Send
                    msg.SentMessage();
                    MessageManager.sentMessages.add(msg);
                    MessageManager.messageHashes.add(hash);
                    MessageManager.messageIds.add(messageId);
                    JOptionPane.showMessageDialog(null, "Message Sent:\n" + msg.toString());
                    break;

                case 1: // Store
                    msg.storeMessage();
                    MessageManager.storedMessages.add(msg);
                    MessageManager.messageHashes.add(hash);
                    MessageManager.messageIds.add(messageId);
                    JOptionPane.showMessageDialog(null, "Message Stored:\n" + msg.toString());
                    break;

                case 2: // Discard
                    MessageManager.disregardedMessages.add(msg);
                    JOptionPane.showMessageDialog(null, "Message Discarded.");
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "No valid option selected. Discarding.");
            }
        }
    }

    private void viewAllMessages() {
        StringBuilder messageLog = new StringBuilder("=== Sent Messages ===\n");
        for (Message m : MessageManager.sentMessages) {
            messageLog.append(m.toString()).append("\n");
        }

        messageLog.append("\n=== Stored Messages ===\n");
        for (Message m : MessageManager.storedMessages) {
            messageLog.append(m.toString()).append("\n");
        }

        messageLog.append("\n=== Discarded Messages ===\n");
        for (Message m : MessageManager.disregardedMessages) {
            messageLog.append(m.toString()).append("\n");
        }

        messageLog.append("\nTotal Messages Sent: ").append(Message.returnTotalMessages());

        JOptionPane.showMessageDialog(null, messageLog.toString());
    }

    private void displayReports() {
        String[] reportOptions = {
            "1) Show all senders and recipients",
            "2) Show longest message",
            "3) Search by Message ID",
            "4) Search messages by recipient",
            "5) Delete a message using message hash",
            "6) Full sent message report"
        };

        String choice = (String) JOptionPane.showInputDialog(
                null,
                "Choose a report option:",
                "Reports",
                JOptionPane.PLAIN_MESSAGE,
                null,
                reportOptions,
                reportOptions[0]);

        if (choice == null) return;

        switch (choice) {
            case "1) Show all senders and recipients":
                StringBuilder sr = new StringBuilder("Senders & Recipients:\n");
                for (Message m : MessageManager.sentMessages) {
                    sr.append("Recipient: ").append(m.getRecipient()).append("\n");
                }
                JOptionPane.showMessageDialog(null, sr.toString());
                break;

            case "2) Show longest message":
                Message longest = null;
                for (Message m : MessageManager.sentMessages) {
                    if (longest == null || m.getText().length() > longest.getText().length()) {
                        longest = m;
                    }
                }
                if (longest != null) {
                    JOptionPane.showMessageDialog(null, "Longest Message:\n" + longest.getText());
                } else {
                    JOptionPane.showMessageDialog(null, "No sent messages.");
                }
                break;

            case "3) Search by Message ID":
                String searchId = JOptionPane.showInputDialog("Enter Message ID to search:");
                for (Message m : MessageManager.sentMessages) {
                    if (m.getMessageId().equals(searchId)) {
                        JOptionPane.showMessageDialog(null, "Recipient: " + m.getRecipient() + "\nMessage: " + m.getText());
                        return;
                    }
                }
                JOptionPane.showMessageDialog(null, "Message not found.");
                break;

            case "4) Search messages by recipient":
                String recipient = JOptionPane.showInputDialog("Enter recipient to search:");
                StringBuilder found = new StringBuilder("Messages sent/stored to " + recipient + ":\n");

                for (Message m : MessageManager.sentMessages) {
                    if (m.getRecipient().equals(recipient)) {
                        found.append("- ").append(m.getText()).append("\n");
                    }
                }
                for (Message m : MessageManager.storedMessages) {
                    if (m.getRecipient().equals(recipient)) {
                        found.append("- ").append(m.getText()).append("\n");
                    }
                }

                JOptionPane.showMessageDialog(null, found.length() > 0 ? found.toString() : "No messages found.");
                break;

            case "5) Delete a message using message hash":
                String hashToDelete = JOptionPane.showInputDialog("Enter message hash to delete:");
                boolean removed = false;
                for (int i = 0; i < MessageManager.sentMessages.size(); i++) {
                    if (MessageManager.sentMessages.get(i).getHash().equals(hashToDelete)) {
                        MessageManager.sentMessages.remove(i);
                        removed = true;
                        break;
                    }
                }
                if (removed) {
                    JOptionPane.showMessageDialog(null, "Message successfully deleted.");
                } else {
                    JOptionPane.showMessageDialog(null, "Message not found.");
                }
                break;

            case "6) Full sent message report":
                StringBuilder report = new StringBuilder("=== Sent Message Report ===\n");
                for (Message m : MessageManager.sentMessages) {
                    report.append("ID: ").append(m.getMessageId())
                          .append("\nRecipient: ").append(m.getRecipient())
                          .append("\nMessage: ").append(m.getText())
                          .append("\nHash: ").append(m.getHash()).append("\n\n");
                }
                JOptionPane.showMessageDialog(null, report.toString());
                break;
        }
    }
}
