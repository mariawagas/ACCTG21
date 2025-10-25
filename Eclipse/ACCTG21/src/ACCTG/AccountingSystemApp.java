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

    // ✅ Shared transactions list
    private ArrayList<Transaction> transactions = new ArrayList<>();

    // Keep a reference to the transaction table model
    private DefaultTableModel transactionTableModel;

    public AccountingSystemApp() {
        setTitle("Accounting System");
        setSize(1300, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== NAVIGATION BAR =====
     // ===== NAVIGATION BAR =====
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        navBar.setBackground(new Color(0, 150, 150));

        String[] sections = {"New Transaction", "Transactions", "Accounts", "Balance Sheet", "General Journal", "General Ledger"};
        navButtons = new JButton[sections.length];

        // Define colors
        Color baseColor = new Color(0, 180, 180);
        Color hoverColor = new Color(0, 170, 170);
        Color activeColor = new Color(0, 160, 160);

        for (int i = 0; i < sections.length; i++) {
            JButton btn = new JButton(sections[i]);
            btn.setFocusPainted(false);
            btn.setBackground(baseColor);
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 180, 180), 2),
                BorderFactory.createEmptyBorder(5, 20, 5, 20)
            ));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            int index = i;
            btn.addActionListener(e -> {
                // Switch panel when clicked
                switchPanel(sections[index]);
                highlightActiveButton(index);
            });

            // 🔹 Hover effect
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (btn.getBackground() != activeColor)
                        btn.setBackground(hoverColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (btn.getBackground() != activeColor)
                        btn.setBackground(baseColor);
                    btn.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(0, 180, 180), 2),
                            BorderFactory.createEmptyBorder(5, 20, 5, 20)
                        ));
                }
            });

            navButtons[i] = btn;
            navBar.add(btn);
        }

        add(navBar, BorderLayout.NORTH);

        // Highlight first section by default
//        highlightActiveButton(0);


        // ===== MAIN CONTENT (Card Layout) =====
        mainPanel = new JPanel(new CardLayout());

        JPanel newTransactionPanel = createNewTransactionPanel();
        JPanel transactionsPanel = createTransactionPanel();

        mainPanel.add(newTransactionPanel, "New Transaction");
        mainPanel.add(transactionsPanel, "Transactions");

        add(mainPanel, BorderLayout.CENTER);

        switchPanel("New Transaction");
        setVisible(true);
    }

    // 🔄 Switch between panels
    private void switchPanel(String name) {
        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        cl.show(mainPanel, name);
    }
    
 // 🔘 Highlight which section is active
    private void highlightActiveButton(int activeIndex) {
        Color baseColor = new Color(0, 150, 150);
        Color activeColor = new Color(0, 140, 140);

        for (int i = 0; i < navButtons.length; i++) {
            if (i == activeIndex) {
                navButtons[i].setBackground(activeColor);
                navButtons[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 180, 180), 3),
                    BorderFactory.createEmptyBorder(5, 20, 5, 20)
                ));
            } else {
                navButtons[i].setBackground(baseColor);
                navButtons[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 130, 130), 2),
                    BorderFactory.createEmptyBorder(5, 20, 5, 20)
                ));
            }
        }
    }


    // 🧾 NEW TRANSACTION PANEL
    private JPanel createNewTransactionPanel() {
//    	private JPanel createNewTransactionPanel() {
    	    JPanel panel = new JPanel(new BorderLayout());
    	    panel.setBackground(new Color(255, 255, 255));

    	    // Inner panel to hold form fields at upper center
    	    JPanel formPanel = new JPanel(null);
    	    formPanel.setPreferredSize(new Dimension(600, 350));
    	    formPanel.setBackground(Color.WHITE);

    	    // CENTER CONTAINER to keep it top-center
    	    JPanel topCenterWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
    	    topCenterWrapper.setBackground(Color.WHITE);
    	    topCenterWrapper.add(formPanel);
    	    panel.add(topCenterWrapper, BorderLayout.NORTH);

    	    JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
    	    dateLabel.setForeground(Color.BLACK);
    	    dateLabel.setBounds(130, 20, 150, 25);
    	    formPanel.add(dateLabel);

    	    JTextField dateField = new JTextField(LocalDate.now().toString());
    	    dateField.setBounds(260, 20, 200, 25);
    	    formPanel.add(dateField);

    	    JLabel descLabel = new JLabel("Description:");
    	    descLabel.setForeground(Color.BLACK);
    	    descLabel.setBounds(130, 60, 150, 25);
    	    formPanel.add(descLabel);

    	    JTextField descField = new JTextField();
    	    descField.setBounds(260, 60, 200, 25);
    	    formPanel.add(descField);

    	    JLabel debitLabel = new JLabel("Debit Account:");
    	    debitLabel.setForeground(Color.BLACK);
    	    debitLabel.setBounds(130, 100, 150, 25);
    	    formPanel.add(debitLabel);

    	    JComboBox<String> debitAccount = new JComboBox<>(new String[]{
    	        "Cash [ASSET]", "Petty Cash [ASSET]", "Accounts Receivable [ASSET]",
    	        "Supplies [ASSET]", "Prepaid Rent [ASSET]", "Equipment [ASSET]",
    	        "Furniture [ASSET]", "Accumulated Depreciation [ASSET]",
    	        "Accounts Payable [LIABILITY]", "Salaries Payable [LIABILITY]",
    	        "Unearned Revenue [LIABILITY]", "Notes Payable [LIABILITY]",
    	        "Owner's Capital [EQUITY]", "Owner's Withdrawals [EQUITY]",
    	        "Service Revenue [INCOME]", "Interest Income [INCOME]",
    	        "Sales Revenue [INCOME]", "Rent Expense [EXPENSE]",
    	        "Salaries Expense [EXPENSE]", "Supplies Expense [EXPENSE]",
    	        "Utilities Expense [EXPENSE]", "Depreciation Expense [EXPENSE]",
    	        "Miscellaneous Expense [EXPENSE]"
    	    });
    	    debitAccount.setBounds(260, 100, 250, 25);
    	    formPanel.add(debitAccount);

    	    JLabel creditLabel = new JLabel("Credit Account:");
    	    creditLabel.setForeground(Color.BLACK);
    	    creditLabel.setBounds(130, 140, 150, 25);
    	    formPanel.add(creditLabel);

    	    JComboBox<String> creditAccount = new JComboBox<>(new String[]{
    	        "Cash [ASSET]", "Petty Cash [ASSET]", "Accounts Receivable [ASSET]",
    	        "Supplies [ASSET]", "Prepaid Rent [ASSET]", "Equipment [ASSET]",
    	        "Furniture [ASSET]", "Accumulated Depreciation [ASSET]",
    	        "Accounts Payable [LIABILITY]", "Salaries Payable [LIABILITY]",
    	        "Unearned Revenue [LIABILITY]", "Notes Payable [LIABILITY]",
    	        "Owner's Capital [EQUITY]", "Owner's Withdrawals [EQUITY]",
    	        "Service Revenue [INCOME]", "Interest Income [INCOME]",
    	        "Sales Revenue [INCOME]", "Rent Expense [EXPENSE]",
    	        "Salaries Expense [EXPENSE]", "Supplies Expense [EXPENSE]",
    	        "Utilities Expense [EXPENSE]", "Depreciation Expense [EXPENSE]",
    	        "Miscellaneous Expense [EXPENSE]"
    	    });
    	    creditAccount.setBounds(260, 140, 250, 25);
    	    formPanel.add(creditAccount);

    	    JLabel amountLabel = new JLabel("Amount:");
    	    amountLabel.setForeground(Color.BLACK);
    	    amountLabel.setBounds(130, 180, 150, 25);
    	    formPanel.add(amountLabel);

    	    JTextField amountField = new JTextField();
    	    amountField.setBounds(260, 180, 200, 25);
    	    formPanel.add(amountField);

    	    RoundedButton addBtn = new RoundedButton("Add Transaction");
    	    addBtn.setBounds(260, 230, 120, 24);
    	    formPanel.add(addBtn);

    	    RoundedButton clearBtn = new RoundedButton("Clear");
    	    clearBtn.setBounds(390, 230, 100, 24);
    	    formPanel.add(clearBtn);

    	    statusLabel = new JLabel("");
    	    statusLabel.setForeground(new Color(0, 255, 0));
    	    statusLabel.setBounds(260, 270, 300, 25);
    	    formPanel.add(statusLabel);

        // Keep all your existing button logic unchanged 👇
        addBtn.addActionListener(e -> {
            try {
                String date = dateField.getText().trim();
                String desc = descField.getText().trim();
                String debit = debitAccount.getSelectedItem().toString();
                String credit = creditAccount.getSelectedItem().toString();
                double amount = Double.parseDouble(amountField.getText().trim());

                Transaction t = new Transaction(date, desc, debit, credit, amount);
                transactions.add(t);

                if (transactionTableModel != null) {
                    transactionTableModel.addRow(new Object[]{
                        t.getDate(), t.getDescription(), t.getDebitAccount(), t.getCreditAccount(), t.getAmount()
                    });
                }
                statusLabel.setForeground( new Color (0, 170, 170));
                statusLabel.setText("✅ Transaction Saved Successfully");
                Timer timer = new Timer(3000, ev -> statusLabel.setText(""));
                timer.setRepeats(false);
                timer.start();

                descField.setText("");
                amountField.setText("");
            } catch (Exception ex) {
                statusLabel.setForeground(Color.RED);
                statusLabel.setText("❌ Invalid input. Please check all fields.");
                Timer timer = new Timer(3000, ev -> statusLabel.setText(""));
                timer.setRepeats(false);
                timer.start();
            }
        });

        clearBtn.addActionListener(e -> {
            dateField.setText(LocalDate.now().toString());
            descField.setText("");
            amountField.setText("");
            debitAccount.setSelectedIndex(0);
            creditAccount.setSelectedIndex(0);
            statusLabel.setText("");
        });

        // ⬇ Add formPanel centered in the parent panel
//        panel.add(formPanel);
        return panel;
    }

    // 📋 TRANSACTIONS PANEL
    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        // 🔍 Search bar section
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel searchLabel = new JLabel("Search:");
        JTextField searchField = new JTextField(20);
        JButton clearButton = new JButton("Clear");
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(clearButton);
        panel.add(searchPanel, BorderLayout.NORTH);

        // 🧾 Table setup
        String[] columnNames = {"Date", "Description", "Debit Account", "Credit Account", "Amount"};
        transactionTableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(transactionTableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 🔍 Search logic
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String search = searchField.getText().toLowerCase();
                transactionTableModel.setRowCount(0);
                for (Transaction t : transactions) {
                    if (t.getDescription().toLowerCase().contains(search) ||
                        t.getDebitAccount().toLowerCase().contains(search) ||
                        t.getCreditAccount().toLowerCase().contains(search)) {
                        transactionTableModel.addRow(new Object[]{
                            t.getDate(),
                            t.getDescription(),
                            t.getDebitAccount(),
                            t.getCreditAccount(),
                            t.getAmount()
                        });
                    }
                }
            }
        });

        // 🧹 Clear search
        clearButton.addActionListener(e -> {
            searchField.setText("");
            refreshTransactionTable();
        });

        return panel;
    }

    // 🔁 Helper to refresh the table
    private void refreshTransactionTable() {
        transactionTableModel.setRowCount(0);
        for (Transaction t : transactions) {
            transactionTableModel.addRow(new Object[]{
                t.getDate(),
                t.getDescription(),
                t.getDebitAccount(),
                t.getCreditAccount(),
                t.getAmount()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AccountingSystemApp::new);
    }
}

