package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MiniStatement extends JFrame {

    private final String pinNumber;

    public MiniStatement(String pinNumber) {
        this.pinNumber = pinNumber;

        setTitle("Mini Statement");
        setLayout(null);
        setSize(400, 600);
        setLocation(20, 20);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Bank name
        JLabel bank = new JLabel("INDIAN BANK");
        bank.setBounds(100, 20, 200, 25);
        bank.setFont(new Font("Arial", Font.BOLD, 18));
        bank.setHorizontalAlignment(SwingConstants.CENTER);
        add(bank);

        // Card number
        JLabel card = new JLabel("Card Number: Loading...");
        card.setBounds(20, 65, 350, 25);
        card.setFont(new Font("Arial", Font.PLAIN, 12));
        add(card);

        // Transaction history heading
        JLabel heading = new JLabel("Date                 Type                    Amount");
        heading.setBounds(20, 105, 350, 25);
        heading.setFont(new Font("Monospaced", Font.BOLD, 12));
        add(heading);

        // Transaction history
        JTextArea statement = new JTextArea();
        statement.setEditable(false);
        statement.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statement.setLineWrap(false);
        statement.setText("Loading transactions...");

        JScrollPane scrollPane = new JScrollPane(statement);
        scrollPane.setBounds(20, 135, 345, 250);
        add(scrollPane);

        // Current balance
        JLabel balance = new JLabel("Current Balance: Loading...");
        balance.setBounds(20, 405, 350, 25);
        balance.setFont(new Font("Arial", Font.BOLD, 13));
        add(balance);

        getContentPane().setBackground(Color.WHITE);

        // Load data from database
        loadMiniStatement(card, statement, balance);

        setVisible(true);
    }

    private void loadMiniStatement(
            JLabel card,
            JTextArea statement,
            JLabel balance) {

        if (pinNumber == null || pinNumber.trim().isEmpty()) {
            card.setText("Card Number: Unavailable");
            statement.setText("A valid PIN is required.");
            balance.setText("Current Balance: Unavailable");
            return;
        }

        String cardNumber = null;

        // Fetch card number
        String cardQuery = "SELECT cardNumber FROM login WHERE pin = ?";

        try (Connection con = new Conn().c;
             PreparedStatement ps = con.prepareStatement(cardQuery)) {

            ps.setString(1, pinNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cardNumber = rs.getString("cardNumber");
                }
            }

            if (cardNumber != null && !cardNumber.isEmpty()) {
                if (cardNumber.length() >= 16) {
                    card.setText("Card Number: "
                            + cardNumber.substring(0, 4)
                            + "XXXXXXXX"
                            + cardNumber.substring(12));
                } else {
                    card.setText("Card Number: " + cardNumber);
                }
            } else {
                card.setText("Card Number: Unavailable");
            }

        } catch (SQLException e) {
            card.setText("Card Number: Could not load");
            System.err.println("Error fetching card number:");
            e.printStackTrace();
        }

        // Fetch transactions and calculate balance
        String transactionQuery =
                "SELECT date, type, amount FROM bank WHERE pin = ?";

        StringBuilder history = new StringBuilder();
        double currentBalance = 0.0;
        boolean hasTransactions = false;

        try (Connection con = new Conn().c;
             PreparedStatement ps = con.prepareStatement(transactionQuery)) {

            ps.setString(1, pinNumber);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    hasTransactions = true;

                    String date = rs.getString("date");
                    String type = rs.getString("type");
                    double amount = rs.getDouble("amount");

                    if (rs.wasNull()) {
                        history.append("Invalid transaction amount\n");
                        continue;
                    }

                    history.append(String.format(
                            "%-12s %-12s Rs. %.2f%n",
                            date == null ? "" : date,
                            type == null ? "" : type,
                            amount
                    ));

                    if ("Deposit".equalsIgnoreCase(type)) {
                        currentBalance += amount;
                    } else if ("Withdrawal".equalsIgnoreCase(type)
                            || "Withdraw".equalsIgnoreCase(type)
                            || "Fast Cash".equalsIgnoreCase(type)) {
                        currentBalance -= amount;
                    }
                }
            }

            if (hasTransactions) {
                statement.setText(history.toString());
                statement.setCaretPosition(0);
                balance.setText(String.format(
                        "Current Balance: Rs. %.2f", currentBalance));
            } else {
                statement.setText("No transactions found.");
                balance.setText("Current Balance: Rs. 0.00");
            }

        } catch (SQLException e) {
            statement.setText("Unable to load transaction history.");
            balance.setText("Current Balance: Unavailable");
            System.err.println("Error fetching transactions:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new MiniStatement("1234")
        );
    }
}
