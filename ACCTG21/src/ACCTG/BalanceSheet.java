package ACCTG;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.text.DecimalFormat;

public class BalanceSheet extends JPanel {

    private RoundedButton refreshButton;
    private JTable assetsTable;
    private JTable liabilitiesTable;
    private DefaultTableModel assetsModel;
    private DefaultTableModel liabilitiesModel;
    private Accounts accountsDataSource;


    private final Color LINE_COLOR = new Color(0, 150, 150);
    private final Color HEADER_COLOR = new Color(0, 170, 170);

    private DecimalFormat df = new DecimalFormat("#,##0.00");

    // Data structure to hold balances (shared with Transactions)
    private Map<String, Double> accountBalances;

//    public BalanceSheet(Map<String, Double> accountBalances) {
//        this.accountBalances = accountBalances;
        public BalanceSheet(Accounts accountsDataSource) {
            this.accountsDataSource = accountsDataSource;


        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);

        // ===== HEADER BAR =====
     // ===== HEADER BAR =====
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 150, 150)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // spacing from edges

        // 🔹 LEFT SIDE (Title + Subtitle)
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Balance Sheet");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(0, 150, 150));

        JLabel subtitle = new JLabel("Statement of assets, liabilities, and equity");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        titlePanel.add(title, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.SOUTH);

        // 🔹 RIGHT SIDE (Print Button)
        ImageIcon printIcon = new ImageIcon(getClass().getResource("/icons/printer.png"));
        Image scaledPrintImage = printIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        RoundedButton printButton = new RoundedButton(" Print", new ImageIcon(scaledPrintImage));
        printButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        printButton.setForeground(Color.WHITE);
        printButton.setBackground(new Color(0, 170, 170));
        printButton.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        printButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        printButton.setFocusable(false);

        // ===== ADD TO TOP PANEL =====

        // Left (title + subtitle)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        topPanel.add(titlePanel, gbc);

        // Right (print button, parallel aligned)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        topPanel.add(printButton, gbc);

        add(topPanel, BorderLayout.NORTH);


        // ===== MAIN CONTENT AREA =====
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 16, 0));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16)); 

        // ===== LEFT PANEL - ASSETS =====
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);

        JLabel assetLabel = new JLabel("Assets");
        assetLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        assetLabel.setForeground(LINE_COLOR);
        assetLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 0));
        leftPanel.add(assetLabel, BorderLayout.NORTH);

        String[] assetCols = {"Account", "Amount"};
        assetsModel = new DefaultTableModel(assetCols, 0);
        assetsTable = createStyledTable(assetsModel);
        JScrollPane assetScroll = new JScrollPane(assetsTable);
        assetScroll.setBorder(BorderFactory.createEmptyBorder());
        assetScroll.getViewport().setBackground(Color.WHITE);
        leftPanel.add(assetScroll, BorderLayout.CENTER);

        // ===== RIGHT PANEL - LIABILITIES & EQUITY =====
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);

        JLabel liabLabel = new JLabel("Liabilities & Equity");
        liabLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        liabLabel.setForeground(LINE_COLOR);
        liabLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 0));
        rightPanel.add(liabLabel, BorderLayout.NORTH);

        String[] liabCols = {"Account", "Amount"};
        liabilitiesModel = new DefaultTableModel(liabCols, 0);
        liabilitiesTable = createStyledTable(liabilitiesModel);
        JScrollPane liabScroll = new JScrollPane(liabilitiesTable);
        liabScroll.setBorder(BorderFactory.createEmptyBorder());
        liabScroll.getViewport().setBackground(Color.WHITE);
        rightPanel.add(liabScroll, BorderLayout.CENTER);

        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);
        add(contentPanel, BorderLayout.CENTER);

        refreshData();
    }

    // ===== Update the balance sheet with current balances =====
        public void refreshData() {
            if (accountsDataSource == null) return;

            // Clear existing rows
            assetsModel.setRowCount(0);
            liabilitiesModel.setRowCount(0);
         // === Custom renderer for bold TOTAL rows ===
            DefaultTableCellRenderer boldRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                                boolean hasFocus, int row, int column) {
                    JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    String firstCol = "";
                    try {
                        firstCol = table.getValueAt(row, 0).toString().toLowerCase();
                    } catch (Exception e) { }

                    if (firstCol.contains("total")) {
                        cell.setFont(cell.getFont().deriveFont(Font.PLAIN));
                        cell.setBackground(new Color(220, 250, 250));
                    } else {
                        cell.setFont(cell.getFont().deriveFont(Font.PLAIN));
                        cell.setBackground(Color.WHITE);
                    }

                    return cell;
                }
            };

            // Apply renderer to both tables
            assetsTable.setDefaultRenderer(Object.class, boldRenderer);
            liabilitiesTable.setDefaultRenderer(Object.class, boldRenderer);

            double totalAssets = 0.0;
            double totalLiabilities = 0.0;
            double totalEquity = 0.0;

            // ✅ Collect data from Accounts panel
            for (Object[] row : accountsDataSource.getAccountBalances()) {
                String account = (String) row[0];
                String type = (String) row[1];
                double balance = (double) row[2];

                switch (type.toUpperCase()) {
                    case "ASSET":
                        assetsModel.addRow(new Object[]{account, String.format("%.2f", balance)});
                        totalAssets += balance;
                        break;

                    case "LIABILITY":
                        liabilitiesModel.addRow(new Object[]{account, String.format("%.2f", balance)});
                        totalLiabilities += balance;
                        break;

                    case "EQUITY":
                        liabilitiesModel.addRow(new Object[]{account, String.format("%.2f", balance)});
                        totalEquity += balance;
                        break;
                }
            }

            // 🔹 Compute totals
            double totalLiabilitiesAndEquity = totalLiabilities + totalEquity;

            // 🔹 Add total rows (visually styled)
            assetsModel.addRow(new Object[]{"", ""});
            assetsModel.addRow(new Object[]{"Total Assets", String.format("%.2f", totalAssets)});

            liabilitiesModel.addRow(new Object[]{"", ""});
            liabilitiesModel.addRow(new Object[]{"Total Liabilities + Equity", String.format("%.2f", totalLiabilitiesAndEquity)});
        }



    // ===== Determine account type (simple keywords) =====
    private boolean isAsset(String name) {
        name = name.toLowerCase();
        return name.contains("cash") || name.contains("asset") || name.contains("receivable") || name.contains("equipment");
    }

    private boolean isLiability(String name) {
        name = name.toLowerCase();
        return name.contains("liability") || name.contains("payable") || name.contains("loan");
    }

    private boolean isEquity(String name) {
        name = name.toLowerCase();
        return name.contains("equity") || name.contains("capital") || name.contains("retained");
    }

    // ===== TABLE DESIGN =====
    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);

        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(26);
        table.setFillsViewportHeight(true);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel cell = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                cell.setHorizontalAlignment(SwingConstants.LEFT);
                cell.setOpaque(true);

                if (column == 0)
                    cell.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, LINE_COLOR));
                else
                    cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, LINE_COLOR));

                if (isSelected) cell.setBackground(new Color(220, 250, 250));
                else cell.setBackground(Color.WHITE);

                return cell;
            }
        });

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setOpaque(true);

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel headerLabel = new JLabel(value.toString(), SwingConstants.CENTER);
                headerLabel.setOpaque(true);
                headerLabel.setBackground(HEADER_COLOR);
                headerLabel.setForeground(Color.WHITE);
                headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

                if (column == 0)
                    headerLabel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, LINE_COLOR));
                else
                    headerLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, LINE_COLOR));

                return headerLabel;
            }
        });

        return table;
    }
}
