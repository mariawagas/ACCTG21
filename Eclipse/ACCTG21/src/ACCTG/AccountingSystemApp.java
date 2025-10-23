package ACCTG;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AccountingSystemApp extends JFrame {
    private JPanel mainPanel;
    private JLabel statusLabel; 
    private JButton[] navButtons;

    // Store all transactions
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private DefaultTableModel transactionTableModel;

    public AccountingSystemApp() {
        setTitle("Accounting System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== NAVIGATION BAR =====
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        navBar.setBackground(new Color(0, 150, 150));

        String[] sections = {"New Transaction", "Transactions", "Accounts", "Balance Sheet", "General Journal", "General Ledger"};
        navButtons = new JButton[sections.length];

        for (int i = 0; i < sections.length; i++) {
            navButtons[i] = new JButton(sections[i]);
            navButtons[i].setFocusPainted(false);
            navButtons[i].setBackground(new Color(0, 150, 150));
            navButtons[i].setForeground(Color.WHITE);
            navButtons[i].setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            int index = i;
            navButtons[i].addActionListener(e -> switchPanel(sections[index]));
            navBar.add(navButtons[i]);
        }
        add(navBar, BorderLayout.NORTH);

        // ===== MAIN CONTENT (Card Layout) =====
        mainPanel = new JPanel(new CardLayout());
        mainPanel.add(createNewTransactionPanel(), "New Transaction");
        mainPanel.add(createTransactionsPanel(), "Transactions");
        mainPanel.add(new Accounts(), "Accounts");
//        mainPanel.add(new BalanceSheetPanel(), "Balance Sheet");
//        mainPanel.add(new GeneralJournalPanel(), "General Journal");
//        mainPanel.add(new GeneralLedgerPanel(), "General Ledger");


        add(mainPanel, BorderLayout.CENTER);

        switchPanel("New Transaction");
        setVisible(true);
    }

    // 🧾 NEW TRANSACTION PANEL
    private JPanel createNewTransactionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.DARK_GRAY);

        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setBounds(100, 50, 150, 25);
        panel.add(dateLabel);

        JTextField dateField = new JTextField(LocalDate.now().toString());
        dateField.setBounds(260, 50, 200, 25);
        panel.add(dateField);

        JLabel descLabel = new JLabel("Description:");
        descLabel.setForeground(Color.WHITE);
        descLabel.setBounds(100, 90, 150, 25);
        panel.add(descLabel);

        JTextField descField = new JTextField();
        descField.setBounds(260, 90, 200, 25);
        panel.add(descField);

        JLabel debitLabel = new JLabel("Debit Account:");
        debitLabel.setForeground(Color.WHITE);
        debitLabel.setBounds(100, 130, 150, 25);
        panel.add(debitLabel);

        JComboBox<String> debitAccount = new JComboBox<>(new String[]{"Cash [ASSET]", "Accounts Receivable [ASSET]", "Supplies [ASSET]"});
        debitAccount.setBounds(260, 130, 200, 25);
        panel.add(debitAccount);

        JLabel creditLabel = new JLabel("Credit Account:");
        creditLabel.setForeground(Color.WHITE);
        creditLabel.setBounds(100, 170, 150, 25);
        panel.add(creditLabel);

        JComboBox<String> creditAccount = new JComboBox<>(new String[]{"Cash [ASSET]", "Revenue [INCOME]", "Accounts Payable [LIABILITY]"});
        creditAccount.setBounds(260, 170, 200, 25);
        panel.add(creditAccount);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setForeground(Color.WHITE);
        amountLabel.setBounds(100, 210, 150, 25);
        panel.add(amountLabel);

        JTextField amountField = new JTextField();
        amountField.setBounds(260, 210, 200, 25);
        panel.add(amountField);

        JButton addBtn = new JButton("Add Transaction");
        addBtn.setBounds(260, 260, 150, 30);
        panel.add(addBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setBounds(420, 260, 100, 30);
        panel.add(clearBtn);

        statusLabel = new JLabel("");
        statusLabel.setForeground(new Color(0, 255, 0));
        statusLabel.setBounds(260, 300, 300, 25);
        panel.add(statusLabel);

        // 🟢 ADD BUTTON FUNCTIONALITY
        addBtn.addActionListener(e -> {
            try {
                String date = dateField.getText().trim();
                String desc = descField.getText().trim();
                String debit = debitAccount.getSelectedItem().toString();
                String credit = creditAccount.getSelectedItem().toString();
                double amount = Double.parseDouble(amountField.getText().trim());

                // Create Transaction and add to list
                Transaction t = new Transaction(date, desc, debit, credit, amount);
                transactions.add(t);

                // Add to table
                transactionTableModel.addRow(new Object[]{t.getDate(), t.getDescription(), t.getDebitAccount(), t.getCreditAccount(), t.getAmount()});

                statusLabel.setText("✅ Transaction Saved Successfully");
                Timer timer = new Timer(3000, ev -> statusLabel.setText(""));
                timer.setRepeats(false);
                timer.start();

                // Clear fields
                descField.setText("");
                amountField.setText("");
            } catch (Exception ex) {
                statusLabel.setForeground(Color.RED);
                statusLabel.setText("❌ Invalid input. Please check all fields.");
            }
        });

        // 🧹 CLEAR BUTTON FUNCTIONALITY
        clearBtn.addActionListener(e -> {
            dateField.setText(LocalDate.now().toString());
            descField.setText("");
            amountField.setText("");
            debitAccount.setSelectedIndex(0);
            creditAccount.setSelectedIndex(0);
            statusLabel.setText("");
        });

        return panel;
    }

    // 📋 TRANSACTIONS PANEL (Table)
    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel header = new JLabel("Transactions");
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(header, BorderLayout.NORTH);

        transactionTableModel = new DefaultTableModel(new Object[]{"Date", "Description", "Debit Account", "Credit Account", "Amount"}, 0);
        JTable table = new JTable(transactionTableModel);
        table.setFillsViewportHeight(true);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    // 🔁 SWITCH PANELS
    private void switchPanel(String name) {
        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        cl.show(mainPanel, name);

        // Highlight active nav button
        for (JButton btn : navButtons) {
            if (btn.getText().equals(name)) {
                btn.setBackground(new Color(0, 200, 200));
            } else {
                btn.setBackground(new Color(0, 150, 150));
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AccountingSystemApp::new);
    }
}
