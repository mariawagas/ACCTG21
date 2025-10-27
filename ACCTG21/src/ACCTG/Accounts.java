package ACCTG;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

public class Accounts extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private ArrayList<Account> accountList;

    // 🔗 Linked panels (for communication)
    private BalanceSheet balanceSheet;
    private GeneralJournal generalJournal;
    private GeneralLedger generalLedger;

    // 🎨 Colors
    private final Color COLOR_ASSET = new Color(0, 200, 255, 40);
    private final Color COLOR_LIABILITY = new Color(255, 140, 0, 40);
    private final Color COLOR_EQUITY = new Color(153, 102, 255, 40);
    private final Color COLOR_INCOME = new Color(0, 200, 100, 40);
    private final Color COLOR_EXPENSE = new Color(255, 50, 50, 40);
    private final Color LINE_COLOR = new Color(0, 150, 150);

    // ===== Constructor =====
    public Accounts() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);
        accountList = new ArrayList<>();

        // ===== HEADER =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, LINE_COLOR));

        JLabel title = new JLabel("Accounts");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(LINE_COLOR);
        title.setBorder(BorderFactory.createEmptyBorder(3, 13, 3, 0));
        topPanel.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("List of all ledger accounts and current balances");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 13, 4, 0));
        topPanel.add(subtitle, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] columns = {"Account", "Type", "Balance"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);

        // Custom cell rendering
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel cell = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                cell.setHorizontalAlignment(SwingConstants.LEFT);
                cell.setOpaque(true);

                String type = table.getValueAt(row, 1).toString();
                Color bg = Color.WHITE;

                if (column == 0) {
                    switch (type.toUpperCase()) {
                        case "ASSET": bg = COLOR_ASSET; break;
                        case "LIABILITY": bg = COLOR_LIABILITY; break;
                        case "EQUITY": bg = COLOR_EQUITY; break;
                        case "INCOME": bg = COLOR_INCOME; break;
                        case "EXPENSE": bg = COLOR_EXPENSE; break;
                    }
                }

                cell.setBorder(BorderFactory.createMatteBorder(0, (column == 0) ? 1 : 0, 1, 1, LINE_COLOR));
                cell.setBackground(isSelected ? new Color(220, 250, 250) : bg);
                return cell;
            }
        });

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(0, 170, 170));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel marginPanel = new JPanel(new BorderLayout());
        marginPanel.setBackground(Color.WHITE);
        marginPanel.setBorder(BorderFactory.createEmptyBorder(9, 16, 16, 16));
        marginPanel.add(scrollPane, BorderLayout.CENTER);

        add(marginPanel, BorderLayout.CENTER);

        // Load initial accounts
        addAllAccounts();
        refreshTable();
    }
    
 // === Called when a new transaction occurs ===
    public void updateFromTransaction(String debit, String credit, double amount) {
        String cleanDebit = debit.split("\\[")[0].trim();
        String cleanCredit = credit.split("\\[")[0].trim();

        for (Account acc : accountList) {
            if (acc.getName().equalsIgnoreCase(cleanDebit)) acc.debit(amount);
            if (acc.getName().equalsIgnoreCase(cleanCredit)) acc.credit(amount);
        }

        refreshTable();

        // 🔄 Notify other panels
        if (balanceSheet != null) balanceSheet.refreshData();
        if (generalJournal != null) {
            generalJournal.addEntry(cleanDebit, amount);
            generalJournal.addEntry(cleanCredit, -amount);
        }
        if (generalLedger != null) {
            generalLedger.updateLedger(cleanDebit, amount);
            generalLedger.updateLedger(cleanCredit, -amount);
        }
    }
 // ===== Return all current account balances =====
    public java.util.ArrayList<Object[]> getAccountBalances() {
        java.util.ArrayList<Object[]> balances = new java.util.ArrayList<>();
        for (Account acc : accountList) {
            balances.add(new Object[]{acc.getName(), acc.getType(), acc.getBalance()});
        }
        return balances;
    }


    // ===== Linking Methods =====
    public void setBalanceSheetListener(BalanceSheet sheet) {
        this.balanceSheet = sheet;
    }

    public void setGeneralJournalListener(GeneralJournal journal) {
        this.generalJournal = journal;
    }

    public void setGeneralLedgerListener(GeneralLedger ledger) {
        this.generalLedger = ledger;
    }

    // ===== Called when transaction affects accounts =====
    public void updateAccounts(String accountName, double amount) {
        for (Account acc : accountList) {
            if (acc.getName().equalsIgnoreCase(accountName)) {
                if (acc.getType().equals("ASSET") || acc.getType().equals("EXPENSE"))
                    acc.debit(amount);
                else
                    acc.credit(amount);
            }
        }

        refreshTable();

        if (balanceSheet != null) balanceSheet.refreshData();
        if (generalJournal != null) generalJournal.addEntry(accountName, amount);
        if (generalLedger != null) generalLedger.updateLedger(accountName, amount);
    }

    // ===== Account Table Helpers =====
    private void addAllAccounts() {
        String[][] accounts = {
                {"Cash", "ASSET"},
                {"Accounts Receivable", "ASSET"},
                {"Inventory", "ASSET"},
                {"Prepaid Expenses", "ASSET"},
                {"Equipment", "ASSET"},
                {"Accumulated Depreciation", "ASSET"},
                {"Accounts Payable", "LIABILITY"},
                {"Notes Payable", "LIABILITY"},
                {"Owner's Capital", "EQUITY"},
                {"Service Revenue", "INCOME"},
                {"Sales Revenue", "INCOME"},
                {"Rent Expense", "EXPENSE"},
                {"Salaries Expense", "EXPENSE"},
                {"Supplies Expense", "EXPENSE"},
                {"Utilities Expense", "EXPENSE"},
                {"Cost of Goods Sold", "EXPENSE"}
        };

        for (String[] acc : accounts)
            accountList.add(new Account(acc[0], acc[1], 0.00));
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Account acc : accountList)
            model.addRow(new Object[]{acc.getName(), acc.getType(), String.format("%.2f", acc.getBalance())});
    }

    // ===== Account Inner Class =====
    private static class Account {
        private String name;
        private String type;
        private double balance;

        public Account(String name, String type, double balance) {
            this.name = name;
            this.type = type;
            this.balance = balance;
        }

        public String getName() { return name; }
        public String getType() { return type; }
        public double getBalance() { return balance; }

        public void debit(double amount) {
            if (type.equals("ASSET") || type.equals("EXPENSE")) balance += amount;
            else balance -= amount;
        }

        public void credit(double amount) {
            if (type.equals("ASSET") || type.equals("EXPENSE")) balance -= amount;
            else balance += amount;
        }
    }
}
