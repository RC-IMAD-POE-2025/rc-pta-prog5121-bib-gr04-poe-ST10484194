import javax.swing.*;

public class QuickChatWindow {

    public QuickChatWindow() {
        showMenu();
    }

    private void showMenu() {
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat!", "Welcome", JOptionPane.INFORMATION_MESSAGE);

        boolean running = true;

        while (running) {
            String[] options = {"1) Send Messages", "2) Recently Sent Messages (Coming Soon)", "3) Quit"};
            String menuChoice = (String) JOptionPane.showInputDialog(
                    null,
                    "Select an option:",
                    "QuickChat Menu",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (menuChoice == null || menuChoice.contains("3)")) {
                running = false;
                break;
            }

            switch (menuChoice) {
                case "1) Send Messages":
                    handleSendMessages();
                    break;

                case "2) Recently Sent Messages (Coming Soon)":
                    JOptionPane.showMessageDialog(null, "Feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE);
                    break;
            }
        }

        JOptionPane.showMessageDialog(null, "Thanks for using QuickChat!");
    }

    private void handleSendMessages() {
        String countStr = JOptionPane.showInputDialog("How many messages would you like to send?");
        if (countStr == null) {
            JOptionPane.showMessageDialog(null, "Cancelled. Returning to main menu.");
            return;
        }

        int count;
        try {
            count = Integer.parseInt(countStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number.");
            return;
        }

        for (int i = 0; i < count; i++) {
            String messageId = JOptionPane.showInputDialog("Enter 10-digit message ID:");
            if (messageId == null) {
                JOptionPane.showMessageDialog(null, "Cancelled. Returning to main menu.");
                return;
            }

            String recipient = JOptionPane.showInputDialog("Enter recipient number (+27 format):");
            if (recipient == null) {
                JOptionPane.showMessageDialog(null, "Cancelled. Returning to main menu.");
                return;
            }

            String text = JOptionPane.showInputDialog("Enter message text (max 250 characters):");
            if (text == null) {
                JOptionPane.showMessageDialog(null, "Cancelled. Returning to main menu.");
                return;
            }

            if (text.length() > 250) {
                JOptionPane.showMessageDialog(null, "Message is too long. Max 250 characters.");
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
                    JOptionPane.showMessageDialog(null, "Message Sent:\n" + msg.toString());
                    break;
                case 1: // Store
                    msg.storeMessage();
                    JOptionPane.showMessageDialog(null, "Message Stored:\n" + msg.toString());
                    break;
                case 2: // Discard
                    JOptionPane.showMessageDialog(null, "Message discarded.");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "No valid option selected. Discarding.");
            }
        }

        JOptionPane.showMessageDialog(null, "Total messages sent: " + Message.returnTotalMessages());
    }
}

