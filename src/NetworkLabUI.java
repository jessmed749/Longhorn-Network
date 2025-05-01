//** NOTE: this is about a bit less than 50% of the lab grade covered, there will be a a bit of a stretch to get a 100.
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class NetworkLabUI extends JFrame {
    private JComboBox<String> testCaseSelector;
    private JButton runTestsButton;
    private JTextArea testOutputArea;
    private GraphPanel graphPanel;
    private JTextArea roommateArea;
    private JComboBox<String> startStudentSelector;
    private JTextField targetCompanyField;
    private JTextArea referralArea;
    private List<List<UniversityStudent>> testCases;

    private static final Color UT_ORANGE = new Color(0xbf5700);

    public NetworkLabUI() {
        super("Longhorn Network Lab UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("icons/logo.png").getImage());

        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 13));

        testCases = Arrays.asList(
                Main.generateTestCase1(),
                Main.generateTestCase2(),
                Main.generateTestCase3()
        );

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Test Runner", new ImageIcon("icons/test.png"), createTestRunnerPanel());
        tabs.addTab("Graph Viewer", new ImageIcon("icons/graph.png"), createGraphViewerPanel());
        tabs.addTab("Roommate Pairs", new ImageIcon("icons/roommates.png"), createRoommatePanel());
        tabs.addTab("Referral Path", new ImageIcon("icons/path.png"), createReferralPanel());
        tabs.addTab("Friends & Chats", new ImageIcon("icons/chat.png"), createFriendChatPanel());

        add(tabs);
    }

    private JPanel createTestRunnerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel top = new JPanel();
        top.setBorder(new EmptyBorder(10, 10, 10, 10));

        testCaseSelector = new JComboBox<>(new String[]{"Test Case 1", "Test Case 2", "Test Case 3", "All Test Cases"});
        runTestsButton = new JButton("Run Tests");
        runTestsButton.setToolTipText("Run the selected test case(s)");
        runTestsButton.setBackground(UT_ORANGE);
        runTestsButton.setForeground(Color.WHITE);

        runTestsButton.addActionListener(e -> onRunTests());
        top.add(new JLabel("Select Test Case:"));
        top.add(testCaseSelector);
        top.add(runTestsButton);

        testOutputArea = new JTextArea();
        testOutputArea.setEditable(false);
        testOutputArea.setBackground(Color.WHITE);
        testOutputArea.setForeground(Color.BLACK);
        testOutputArea.setFont(new Font("Consolas", Font.PLAIN, 13));

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(testOutputArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createGraphViewerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel controls = new JPanel();
        controls.setBorder(new EmptyBorder(10, 10, 10, 10));

        JComboBox<String> graphCaseSelector = new JComboBox<>(new String[]{"Test Case 1", "Test Case 2", "Test Case 3"});
        JButton loadGraphButton = new JButton("Load Graph");
        loadGraphButton.setToolTipText("Render student graph for selected test case");
        loadGraphButton.setBackground(UT_ORANGE);
        loadGraphButton.setForeground(Color.WHITE);

        loadGraphButton.addActionListener(e -> {
            int idx = graphCaseSelector.getSelectedIndex();
            List<UniversityStudent> data = testCases.get(idx);
            StudentGraph graph = new StudentGraph(data);
            graphPanel.setGraph(graph, data);
        });

        controls.add(new JLabel("Select Data:"));
        controls.add(graphCaseSelector);
        controls.add(loadGraphButton);

        graphPanel = new GraphPanel();
        panel.add(controls, BorderLayout.NORTH);
        panel.add(graphPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createRoommatePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel controls = new JPanel();
        controls.setBorder(new EmptyBorder(10, 10, 10, 10));

        JComboBox<String> rmCaseSelector = new JComboBox<>(new String[]{"Test Case 1", "Test Case 2", "Test Case 3"});
        JButton computeButton = new JButton("Compute Roommates");
        computeButton.setToolTipText("Match students using Gale-Shapley");
        computeButton.setBackground(UT_ORANGE);
        computeButton.setForeground(Color.WHITE);

        computeButton.addActionListener(e -> {
            int idx = rmCaseSelector.getSelectedIndex();
            List<UniversityStudent> data = testCases.get(idx);
            data.forEach(s -> s.setRoommate(null));
            GaleShapley.assignRoommates(data);
            StringBuilder sb = new StringBuilder();
            for (UniversityStudent s : data) {
                if (s.getRoommate() != null && s.getName().compareTo(s.getRoommate().getName()) < 0) {
                    sb.append(s.getName()).append(" → ").append(s.getRoommate().getName()).append("\n");
                }
            }
            roommateArea.setText(sb.toString());
        });

        controls.add(new JLabel("Select Data:"));
        controls.add(rmCaseSelector);
        controls.add(computeButton);

        roommateArea = new JTextArea();
        roommateArea.setEditable(false);
        roommateArea.setBackground(Color.WHITE);
        roommateArea.setForeground(Color.BLACK);

        panel.add(controls, BorderLayout.NORTH);
        panel.add(new JScrollPane(roommateArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createReferralPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel controls = new JPanel();
        controls.setBorder(new EmptyBorder(10, 10, 10, 10));

        JComboBox<String> refCaseSelector = new JComboBox<>(new String[]{"Test Case 1", "Test Case 2", "Test Case 3"});
        startStudentSelector = new JComboBox<>();
        targetCompanyField = new JTextField(10);
        JButton findButton = new JButton("Find Path");
        findButton.setToolTipText("Use Dijkstra to find referral path");
        findButton.setBackground(UT_ORANGE);
        findButton.setForeground(Color.WHITE);

        findButton.addActionListener(e -> {
            int idx = refCaseSelector.getSelectedIndex();
            List<UniversityStudent> data = testCases.get(idx);
            String selectedName = (String) startStudentSelector.getSelectedItem();
            UniversityStudent start = data.stream().filter(s -> s.getName().equals(selectedName)).findFirst().orElse(null);
            String target = targetCompanyField.getText().trim();
            if (start != null && !target.isEmpty()) {
                StudentGraph graph = new StudentGraph(data);
                ReferralPathFinder finder = new ReferralPathFinder(graph);
                List<UniversityStudent> path = finder.findReferralPath(start, target);
                StringBuilder sb = new StringBuilder();
                path.forEach(s -> sb.append(s.getName()).append(" -> "));
                if (!path.isEmpty()) sb.setLength(sb.length() - 4);
                referralArea.setText(sb.toString());
            }
        });

        refCaseSelector.addActionListener(e -> {
            int idx = refCaseSelector.getSelectedIndex();
            List<UniversityStudent> data = testCases.get(idx);
            startStudentSelector.removeAllItems();
            data.forEach(s -> startStudentSelector.addItem(s.getName()));
        });
        refCaseSelector.setSelectedIndex(0);

        controls.add(new JLabel("Data:"));
        controls.add(refCaseSelector);
        controls.add(new JLabel("Start:"));
        controls.add(startStudentSelector);
        controls.add(new JLabel("Target Company:"));
        controls.add(targetCompanyField);
        controls.add(findButton);

        referralArea = new JTextArea();
        referralArea.setEditable(false);
        referralArea.setBackground(Color.WHITE);
        referralArea.setForeground(Color.BLACK);

        panel.add(controls, BorderLayout.NORTH);
        panel.add(new JScrollPane(referralArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFriendChatPanel() {
        JPanel panel = new JPanel(new BorderLayout());
    
        JComboBox<String> caseSelector = new JComboBox<>(new String[]{"Test Case 1", "Test Case 2", "Test Case 3"});
        JComboBox<String> studentSelector = new JComboBox<>();
    
        JButton showButton = new JButton("Show");
        showButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        showButton.setBackground(UT_ORANGE);
        showButton.setForeground(Color.WHITE);
        showButton.setFocusPainted(false);
        showButton.setToolTipText("Show friends and chat history for selected student");
    
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Color.WHITE);
        JScrollPane chatScroll = new JScrollPane(chatPanel);
        chatScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    
        Font emojiFont = new Font("Segoe UI Emoji", Font.PLAIN, 13);
    
        caseSelector.addActionListener(e -> {
            int idx = caseSelector.getSelectedIndex();
            List<UniversityStudent> data = testCases.get(idx);
            studentSelector.removeAllItems();
            for (UniversityStudent s : data) {
                studentSelector.addItem(s.getName());
            }
        });
        caseSelector.setSelectedIndex(0);
    
        showButton.addActionListener(e -> {
            int idx = caseSelector.getSelectedIndex();
            List<UniversityStudent> data = testCases.get(idx);
            String selected = (String) studentSelector.getSelectedItem();
            UniversityStudent u = data.stream().filter(s -> s.getName().equals(selected)).findFirst().orElse(null);
            if (u == null) return;
    
            chatPanel.removeAll();
    
            // Friends Label
            JLabel friendsLabel = new JLabel("👥 Friends:");
            friendsLabel.setFont(emojiFont.deriveFont(Font.BOLD, 14));
            chatPanel.add(friendsLabel);
    
            if (u.getFriends().isEmpty()) {
                JLabel none = new JLabel("   ❌ None");
                none.setFont(emojiFont);
                chatPanel.add(none);
            } else {
                for (UniversityStudent f : u.getFriends()) {
                    JLabel item = new JLabel("   • " + f.getName());
                    item.setFont(emojiFont);
                    chatPanel.add(item);
                }
            }
    
            // Chat History Label
            JLabel historyLabel = new JLabel("💬 Chat History:");
            historyLabel.setFont(emojiFont.deriveFont(Font.BOLD, 14));
            historyLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
            chatPanel.add(historyLabel);
    
            if (u.getAllChats().isEmpty()) {
                JLabel none = new JLabel("   ❌ None");
                none.setFont(emojiFont);
                chatPanel.add(none);
            } else {
                for (Map.Entry<UniversityStudent, List<String>> entry : u.getAllChats().entrySet()) {
                    JLabel withLabel = new JLabel("🔸 With " + entry.getKey().getName() + ":");
                    withLabel.setFont(emojiFont.deriveFont(Font.ITALIC, 13));
                    chatPanel.add(withLabel);
    
                    for (String m : entry.getValue()) {
                        JLabel bubble = new JLabel("<html><div style='padding:6px; max-width: 500px;'>" + m + "</div></html>");
                        bubble.setOpaque(true);
                        bubble.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                        bubble.setFont(emojiFont);
                        bubble.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
    
                        JPanel wrapper = new JPanel(new FlowLayout(m.startsWith("You") ? FlowLayout.RIGHT : FlowLayout.LEFT));
                        wrapper.setBackground(Color.WHITE);
    
                        if (m.startsWith("You")) {
                            bubble.setBackground(UT_ORANGE);
                            bubble.setForeground(Color.WHITE);
                        } else {
                            bubble.setBackground(new Color(230, 230, 230));
                            bubble.setForeground(Color.BLACK);
                        }
    
                        wrapper.add(bubble);
                        chatPanel.add(wrapper);
                    }
                }
            }
    
            chatPanel.revalidate();
            chatPanel.repaint();
        });
    
        JPanel top = new JPanel();
        top.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        top.setBackground(new Color(245, 245, 245));
        top.add(new JLabel("Test Case:")); top.add(caseSelector);
        top.add(new JLabel("Student:")); top.add(studentSelector);
        top.add(showButton);
    
        panel.add(top, BorderLayout.NORTH);
        panel.add(chatScroll, BorderLayout.CENTER);
    
        return panel;
    }
    
     

    private void onRunTests() {
        testOutputArea.setText("");
        String sel = (String) testCaseSelector.getSelectedItem();
        if (sel.equals("All Test Cases")) {
            for (int i = 1; i <= testCases.size(); i++) runTests(i);
        } else {
            int num = Integer.parseInt(sel.split(" ")[2]);
            runTests(num);
        }
    }

    private void runTests(int caseNum) {
        testOutputArea.append("=== Test Case " + caseNum + " ===\n");
        List<UniversityStudent> data = testCases.get(caseNum - 1);
        data.forEach(s -> testOutputArea.append(s + "\n"));
        testOutputArea.append("\n");
        int score = Main.gradeLab(data, caseNum);
        testOutputArea.append("Test Case " + caseNum + " Score: " + score + "\n\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NetworkLabUI().setVisible(true));
    }

    private static class GraphPanel extends JPanel {
        private StudentGraph graph;
        private List<UniversityStudent> nodes;
        private Map<UniversityStudent, Point> coords = new HashMap<>();

        public GraphPanel() {
            ToolTipManager.sharedInstance().registerComponent(this);
        }

        void setGraph(StudentGraph g, List<UniversityStudent> data) {
            this.graph = g;
            this.nodes = data;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (graph == null || nodes == null) return;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int width = getWidth(), height = getHeight();
            int r = Math.min(width, height) / 3;
            int cx = width / 2, cy = height / 2;
            coords.clear();
            int n = nodes.size();
            for (int i = 0; i < n; i++) {
                double angle = 2 * Math.PI * i / n;
                int x = cx + (int) (r * Math.cos(angle));
                int y = cy + (int) (r * Math.sin(angle));
                coords.put(nodes.get(i), new Point(x, y));
            }

            for (UniversityStudent s : nodes) {
                for (StudentGraph.Edge e : graph.getNeighbors(s)) {
                    UniversityStudent t = e.neighbor;
                    if (nodes.indexOf(t) <= nodes.indexOf(s)) continue;
                    Point p1 = coords.get(s), p2 = coords.get(t);
                    g2.setColor(Color.GRAY);
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                    g2.drawString(String.valueOf(e.weight), (p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
                }
            }

            for (UniversityStudent s : nodes) {
                Point p = coords.get(s);
                g2.setColor(UT_ORANGE);
                g2.fillOval(p.x - 15, p.y - 15, 30, 30);
                g2.setColor(Color.WHITE);
                g2.drawString(s.getName(), p.x - 12, p.y + 4);
            }
        }

        @Override
        public String getToolTipText(MouseEvent e) {
            for (Map.Entry<UniversityStudent, Point> entry : coords.entrySet()) {
                if (e.getPoint().distance(entry.getValue()) <= 15) {
                    UniversityStudent s = entry.getKey();
                    return s.getName() + " | Major: " + s.getMajor() + " | GPA: " + s.getGPA();
                }
            }
            return null;
        }
    }
}
