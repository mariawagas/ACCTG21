package ACCTG;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.net.URL;
import javax.swing.table.JTableHeader;


public class AccountingSystemApp extends JFrame {
    private JPanel mainPanel;
    private JLabel statusLabel; 
    private JButton[] navButtons;
    private CardLayout cardLayout;
    private Accounts accountsPanel;
    private JPanel newTransactionPanel;
    private JPanel transactionsPanel;

    // ✅ Shared transactions list
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private ArrayList<Accounts> accounts = new ArrayList<>();
    private ArrayList<BalanceSheet> balanceSheet = new ArrayList<>();
    private ArrayList<GeneralJournal> generalJournal = new ArrayList<>();
    private ArrayList<GeneralLedger> generalLedger = new ArrayList<>();




    private void switchPanel(String panelName) {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, panelName);
    }



    // Keep a reference to the transaction table model
    private DefaultTableModel transactionTableModel;

    public AccountingSystemApp() {
        setTitle("Accounting System");
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] sections = {
            "New Transaction", "Transactions", "Accounts",
            "Balance Sheet", "General Journal", "General Ledger"
        };
        navButtons = new JButton[sections.length];

        JPanel navBar = new JPanel(new GridBagLayout());
        navBar.setBackground(new Color(240, 240, 240));
        navBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 180, 180)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        Color tabDefault = new Color(240, 240, 240);
        Color tabHover = new Color(250, 250, 250);
        Color tabActive = new Color(255, 255, 255);
        Color borderActive = new Color(0, 170, 170);
        Color shadow = new Color(200, 200, 200);

        for (int i = 0; i < sections.length; i++) {
            JButton btn = new JButton(sections[i]) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (getClientProperty("active") == Boolean.TRUE) {
                        g2.setColor(shadow);
                        g2.fillRoundRect(2, getHeight() - 4, getWidth() - 4, 4, 8, 8);
                    }
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight() - 2, 12, 12);
                    if (getClientProperty("active") == Boolean.TRUE) {
                        g2.setColor(borderActive);
                        g2.fillRect(0, getHeight() - 2, getWidth(), 2);
                    }
                    g2.dispose();
                    super.paintComponent(g);
                }
            };

            btn.setFocusPainted(false);
            btn.setContentAreaFilled(false);
            btn.setOpaque(false);
            btn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));// borders of the navi
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setForeground(Color.BLACK);
            btn.setBackground(tabDefault);

            int index = i;
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (btn.getClientProperty("active") != Boolean.TRUE) {
                        btn.setBackground(tabHover);
                        btn.repaint();
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    if (btn.getClientProperty("active") != Boolean.TRUE) {
                        btn.setBackground(tabDefault);
                        btn.repaint();
                    }
                }
            });

            btn.addActionListener(e -> {
                for (JButton b : navButtons) {
                    b.putClientProperty("active", false);
                    b.setBackground(tabDefault);
                    b.repaint();
                }
                btn.putClientProperty("active", true);
                btn.setBackground(tabActive);
                btn.repaint();
                switchPanel(sections[index]);
            });

            navButtons[i] = btn;

            gbc.gridx = i;
            gbc.weightx = 0.0; // each button keeps its natural width
            navBar.add(btn, gbc);
        }

        // 🔹 Add a filler to push everything to the left
        gbc.gridx = sections.length;
        gbc.weightx = 1.0;
        navBar.add(Box.createHorizontalGlue(), gbc);

        // Default active
        navButtons[0].putClientProperty("active", true);
        navButtons[0].setBackground(tabActive);
        navButtons[0].repaint();

        add(navBar, BorderLayout.NORTH);


     // ===== Create panels (use the class fields, do NOT redeclare locals) =====
//        accountsPanel = new Accounts();
        Accounts accountsPanel = new Accounts();
        BalanceSheet balanceSheetPanel = new BalanceSheet(accountsPanel);
        GeneralJournal generalJournalPanel = new GeneralJournal();
        GeneralLedger generalLedgerPanel = new GeneralLedger();

        // link them together
        accountsPanel.setBalanceSheetListener(balanceSheetPanel);
        accountsPanel.setGeneralJournalListener(generalJournalPanel);
        accountsPanel.setGeneralLedgerListener(generalLedgerPanel);

        // Create New Transaction panel (pass all panels that need updates)
        newTransactionPanel = createNewTransactionPanel(accountsPanel, balanceSheetPanel, generalJournalPanel, generalLedgerPanel);

        // Transactions panel (if you have one)
        transactionsPanel = createTransactionPanel();

        // ===== MAIN CONTENT (CardLayout) =====
        mainPanel = new JPanel(new CardLayout());
        mainPanel.add(newTransactionPanel, "New Transaction");
        mainPanel.add(transactionsPanel, "Transactions");
        mainPanel.add(accountsPanel, "Accounts");
        mainPanel.add(balanceSheetPanel, "Balance Sheet");
        mainPanel.add(generalJournalPanel, "General Journal");
        mainPanel.add(generalLedgerPanel, "General Ledger");

        add(mainPanel, BorderLayout.CENTER);

        // Default panel
        switchPanel("New Transaction");
        setVisible(true);
    }

    
 // 🔘 Highlight which section is active
    private void highlightActiveButton(int activeIndex) {
        Color baseColor = new Color(0, 130, 130);
        Color activeColor = new Color(0, 180, 180);

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
    private JPanel createNewTransactionPanel(Accounts accountsPanel,
            BalanceSheet balanceSheetPanel,
            GeneralJournal generalJournalPanel,
            GeneralLedger generalLedgerPanel) {

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

        JLabel titleLabel = new JLabel("Add a New Transaction");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 150, 150));
        titleLabel.setBounds(107, 20, 250, 25);
        formPanel.add(titleLabel);

        // --- Date field ---
        ImageIcon dateIcon = new ImageIcon(getClass().getResource("/icons/date.png"));
        Image scaledDateImage = dateIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):", new ImageIcon(scaledDateImage), JLabel.LEFT);
        dateLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        dateLabel.setIconTextGap(5);
        dateLabel.setForeground(Color.BLACK);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateLabel.setBounds(107, 60, 250, 25);
        formPanel.add(dateLabel);

        JTextField dateField = new JTextField((" ") + LocalDate.now().toString());
        dateField.setBounds(260, 60, 230, 25);
        formPanel.add(dateField);

        // --- Description field ---
        ImageIcon descIcon = new ImageIcon(getClass().getResource("/icons/description.png"));
        Image scaledDescImage = descIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JLabel descLabel = new JLabel("Description:", new ImageIcon(scaledDescImage), JLabel.LEFT);
        descLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        descLabel.setIconTextGap(5);
        descLabel.setForeground(Color.BLACK);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setBounds(107, 100, 250, 25);
        formPanel.add(descLabel);

        JTextField descField = new JTextField(" ");
        descField.setBounds(260, 100, 230, 25);
        formPanel.add(descField);

        // --- Debit account ---
        ImageIcon debitIcon = new ImageIcon(getClass().getResource("/icons/debit.jpg"));
        Image scaledDebitImage = debitIcon.getImage().getScaledInstance(23, 20, Image.SCALE_SMOOTH);
        JLabel debitLabel = new JLabel("Debit Account:", new ImageIcon(scaledDebitImage), JLabel.LEFT);
        debitLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        debitLabel.setIconTextGap(5);
        debitLabel.setForeground(Color.BLACK);
        debitLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        debitLabel.setBounds(107, 140, 150, 25);
        formPanel.add(debitLabel);

        JComboBox<String> debitAccount = new JComboBox<>(new String[]{
        		" Cash [ASSET]", 
                " Accounts Receivable [ASSET] ",
    		    " Inventory [ASSET]",
    		    " Prepaid Expenses [ASSET] ",
    		    " Equipment [ASSET] ",
    		    " Accumulated Depreciation [ASSET] ",
    		    " Accounts Payable [LIABILITY] ",
    		    " Notes Payable [LIABILITY] ",
    		    " Owner's Capital [EQUITY] ", 
    		    " Service Revenue [INCOME] ", 
    		    " Sales Revenue [INCOME] ",
    		    " Rent Expense [EXPENSE] ",
    		    " Salaries Expense [EXPENSE] ",
    		    " Supplies Expense [EXPENSE] ",
    		    " Utilities Expense [EXPENSE] ",
    		    " Cost of Good Sold [EXPENSE]",
    		    " Rent Expense [EXPENSE]",
        });
        debitAccount.setBounds(260, 140, 230, 25);
        debitAccount.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(debitAccount);

        // --- Credit account ---
        ImageIcon creditIcon = new ImageIcon(getClass().getResource("/icons/credit.jpg"));
        Image scaledCreditImage = creditIcon.getImage().getScaledInstance(23, 20, Image.SCALE_SMOOTH);
        JLabel creditLabel = new JLabel("Credit Account:", new ImageIcon(scaledCreditImage), JLabel.LEFT);
        creditLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        creditLabel.setIconTextGap(5);
        creditLabel.setForeground(Color.BLACK);
        creditLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        creditLabel.setBounds(107, 180, 150, 25);
        formPanel.add(creditLabel);

        JComboBox<String> creditAccount = new JComboBox<>(new String[]{
            " Cash [ASSET]", 
            " Accounts Receivable [ASSET] ",
		    " Inventory [ASSET]",
		    " Prepaid Expenses [ASSET] ",
		    " Equipment [ASSET] ",
		    " Accumulated Depreciation [ASSET] ",
		    " Accounts Payable [LIABILITY] ",
		    " Notes Payable [LIABILITY] ",
		    " Owner's Capital [EQUITY] ", 
		    " Service Revenue [INCOME] ", 
		    " Sales Revenue [INCOME] ",
		    " Rent Expense [EXPENSE] ",
		    " Salaries Expense [EXPENSE] ",
		    " Supplies Expense [EXPENSE] ",
		    " Utilities Expense [EXPENSE] ",
		    " Cost of Good Sold [EXPENSE]",
		    " Rent Expense [EXPENSE]",
        });
        creditAccount.setBounds(260, 180, 230, 25);
        creditAccount.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(creditAccount);

        // --- Amount field ---
        ImageIcon amountIcon = new ImageIcon(getClass().getResource("/icons/amount.png"));
        Image scaledAmountImage = amountIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JLabel amountLabel = new JLabel("Amount:", new ImageIcon(scaledAmountImage), JLabel.LEFT);
        amountLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        amountLabel.setIconTextGap(5);
        amountLabel.setForeground(Color.BLACK);
        amountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        amountLabel.setBounds(107, 220, 150, 25);
        formPanel.add(amountLabel);

        JTextField amountField = new JTextField(" ");
        amountField.setBounds(260, 220, 230, 25);
        formPanel.add(amountField);

        // --- Buttons ---
        RoundedButton addBtn = new RoundedButton("Add Transaction");
//        addBtn.setForeground(Color.DARK_GRAY);
        addBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        addBtn.setBounds(260, 260, 120, 24);
        formPanel.add(addBtn);

        RoundedButton clearBtn = new RoundedButton("Clear");
//        clearBtn.setForeground(Color.DARK_GRAY);
        clearBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        clearBtn.setBounds(390, 260, 100, 24);
        formPanel.add(clearBtn);

        JLabel statusLabel = new JLabel(" ");
        statusLabel.setForeground(new Color(0, 255, 0));
        statusLabel.setBounds(260, 290, 300, 25);
        formPanel.add(statusLabel);

        // 🧩 Add Button Logic
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

                // 🔹 Connect to Accounts panel
                if (accountsPanel != null) {
                    accountsPanel.updateFromTransaction(debit, credit, amount);
                }
                
                if (balanceSheetPanel != null) {
                    balanceSheetPanel.refreshData();
                }


                statusLabel.setForeground(new Color(0, 170, 170));
                statusLabel.setText("✅ Transaction Saved Successfully");

                Timer timer = new Timer(3000, ev -> statusLabel.setText(" "));
                timer.setRepeats(false);
                timer.start();

                descField.setText(" ");
                amountField.setText(" ");
            } catch (Exception ex) {
                statusLabel.setForeground(Color.RED);
                statusLabel.setText("❌ Invalid input. Please check all fields.");
                Timer timer = new Timer(3000, ev -> statusLabel.setText(" "));
                timer.setRepeats(false);
                timer.start();
            }
        });

        // 🔄 Clear button logic
        clearBtn.addActionListener(e -> {
            dateField.setText(LocalDate.now().toString());
            descField.setText(" ");
            amountField.setText(" ");
            debitAccount.setSelectedIndex(0);
            creditAccount.setSelectedIndex(0);
            statusLabel.setText(" ");
        });

        return panel;
    }


 // 📋 TRANSACTIONS PANEL
    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(Color.WHITE);

        // ===== UPPER BAR (Title + Search) =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 150, 150)));

        // 🔹 Left side: Title
        JLabel title = new JLabel("Transactions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(0, 150, 150));
        title.setBorder(BorderFactory.createEmptyBorder(0, 13, 0, 0));
        topPanel.add(title, BorderLayout.WEST);
        
        JLabel subtitle = new JLabel("Complete record of business transactions");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 13, 4, 0));
        topPanel.add(subtitle, BorderLayout.SOUTH);


        // 🔍 Right side: Search bar + Clear button
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        searchPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("Search:");
        JTextField searchField = new JTextField(25);
        RoundedButton clearButton = new RoundedButton("Clear");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(clearButton);

        topPanel.add(searchPanel, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        // ===== TABLE SECTION =====
        String[] columnNames = {"Date", "Description", "Debit Account", "Credit Account", "Amount"};
        transactionTableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(transactionTableModel);

        // 🔹 Table appearance
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);

        // 🔹 Cell renderer: add full teal grid + left edge
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                cell.setBackground(Color.WHITE);
                cell.setForeground(Color.BLACK);
                cell.setHorizontalAlignment(SwingConstants.LEFT);

                // Add borders — left border for first column
                if (column == 0) {
                    cell.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(0, 150, 150)));
                } else {
                    cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(0, 150, 150)));
                }

                if (isSelected) {
                    cell.setBackground(new Color(220, 250, 250));
                }
                return cell;
            }
        });

        // 🧩 Header styling — teal with vertical lines + left border
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0, 150, 150));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setOpaque(true);

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel headerLabel = new JLabel(value.toString(), SwingConstants.CENTER);
                headerLabel.setOpaque(true);
                headerLabel.setBackground(new Color(0, 170, 170));
                headerLabel.setForeground(Color.WHITE);
                headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

                // Left border on first column header
                if (column == 0) {
                    headerLabel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(0, 150, 150)));
                } else {
                    headerLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(0, 150, 150)));
                }
                return headerLabel;
            }
        });

        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 150, 150)));

        // ===== ScrollPane — white margin around table =====
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16)); // ⬅ white margin from all sides
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.setOpaque(true);

        panel.add(scrollPane, BorderLayout.CENTER);

        // ===== SEARCH LOGIC =====
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
