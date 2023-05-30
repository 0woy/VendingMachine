package GUI;
import VendingMachine.Manager;
import VendingMachine.VendingMachine;
import GUI.StartMachine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.util.regex.*;
import java.awt.*;



public class ManagerView extends JFrame{
    private Main main;
    private JTabbedPane tabs;   // 화면 분리
    
    private JPanel Input_pw;    // 관리자 인증 화면
    private JPanel Sales;       // 매출 현황 화면
    private JPanel Stocks;      // 재고 파악, 음료 수정 화면
    private JPanel Money;       // 잔돈 & 수금 화면
    private JPanel resetPW;     // 비밀번호 변경 화면

    private JPanel manager;       // 관리자 화면
    private JPanel viewMenuPanel;   // 선택한 메뉴에 따라 화면 보이기
    private JPanel menuPanel;       // 확인할 메뉴 선택

    public ManagerView() {
        setTitle("관리자 메뉴");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        manager=new JPanel();
        manager.setLayout(new BorderLayout());

        manager.add(menuPanel(), BorderLayout.CENTER);
        setContentPane(manager);

        setLocation(600,100);
        setSize(850, 650);
        setVisible(true);

    }

    // 메뉴 선택 Panel
    public JTabbedPane menuPanel() {
        tabs= new JTabbedPane();

        tabs.add("매출 조회",SalesView());
        tabs.add("재고 조회",StocksView());
        tabs.add("잔돈 확인",MoneyView());
        tabs.add("비밀번호 변경",PasswordView());
        tabs.add("로그아웃",new JPanel());

        // 로그아웃 선택할 경우 자판기 화면으로 되돌아감
        tabs.addChangeListener(e -> {
            int selectedIndex = tabs.getSelectedIndex();

            if (selectedIndex == tabs.indexOfTab("로그아웃")) {
                int option = JOptionPane.showConfirmDialog(null, "로그아웃 하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    JFrame managerView=this;
                    managerView.dispose();
                } else {
                    tabs.setSelectedIndex(tabs.indexOfTab("매출 조회"));
                }
            }
        });
        return tabs;
    }

    // 전체&음료별 일/월 매출 현황 테이블 Panel
    public JPanel SalesView(){
        Sales = new JPanel();
        Sales.add(new JLabel("매출조회~~"));
        return Sales;
    }

    // 음료 재고 확인 및 보충, 가격 조정 Panel
    public JPanel StocksView() {
        Stocks = new JPanel(new BorderLayout(10,20));
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.CYAN);
        JPanel buttonPanel = new JPanel();

        Object[][] data = new Object[5][];
        for (int i = 0; i < data.length; i++) {
            data[i] = new Object[2];
            data[i][0] = StartMachine.vm.getBeverageName(i);
            data[i][1] = StartMachine.vm.getBeverageStocks(i);
        }

        // Column names
        String[] columnNames = {"음료 이름", "현재 재고"};

        // Read-only table model
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JLabel title = new JLabel("음료 재고 현황");

        JButton Supplement = new JButton("재고 보충");
        JButton Modify= new JButton("음료 & 가격 변경");

        titlePanel.add(title);
        buttonPanel.add(Supplement);
        buttonPanel.add(Modify);

        Stocks.add(titlePanel, BorderLayout.NORTH);
        Stocks.add(scrollPane, BorderLayout.CENTER);
        Stocks.add(buttonPanel, BorderLayout.SOUTH);
        // Supply panel
        JPanel supply = new JPanel();
        JButton tmp = new JButton("우힣");
        supply.add(tmp);

        // CardLayout to switch between Stocks and supply panels
        CardLayout cardLayout = new CardLayout();
        JPanel cards = new JPanel(cardLayout);
        cards.add(Stocks, "Stocks");
        cards.add(supply, "Supply");

        Supplement.addActionListener(e -> {
            cardLayout.show(cards, "Supply");
        });
        tmp.addActionListener(e -> {
            cardLayout.show(cards,"Stocks");
        });



        // Create a wrapper panel to hold the cards panel
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.add(cards, BorderLayout.CENTER);

        return wrapperPanel;
    }

    // 잔돈 확인 & 충전, 수금 Panel
    public JPanel MoneyView(){
        Money = new JPanel();
        return Money;
    }

    // 비밀번호 변경 Panel
    public JPanel PasswordView(){
        resetPW = new JPanel();
        resetPW.add(new JLabel("비번 변경"));
        return resetPW;
    }

    // 관리자 메뉴 이동시 화면 변화
    public void changeView(Main main){
        this.main = main;
    }
}
