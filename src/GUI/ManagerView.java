package GUI;
import VendingMachine.Manager;
import VendingMachine.VendingMachine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.regex.*;
import java.awt.*;

public class ManagerView extends StartMachine {
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

        manager.add( menuPanel(),BorderLayout.CENTER);
        setContentPane(manager);

        setLocation(600,100);
        setSize(850, 650);
        setVisible(true);

    }

    // 메뉴 선택 Panel
    public JTabbedPane menuPanel() {
        tabs= new JTabbedPane();
        tabs.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);

        tabs.add("매출조회",SalesView());
        tabs.add("재고조회",StocksView());
        tabs.add("비밀번호 변경",PasswordView());

        JPanel emptyPanel = new JPanel();
        tabs.setTabComponentAt(tabs.getTabCount() - 1, emptyPanel);

//        menuPanel=new JPanel();
//        menuPanel.setBorder(new EmptyBorder(5,5,5,5));
//        menuPanel.setPreferredSize(new Dimension(100,50));
//        menuPanel.setLayout(tabs);
//         menuPanel.setLayout(new GridLayout(1,5));
//        menuPanel.setBackground(Color.green);

        return tabs;
    }

    // 전체&음료별 일/월 매출 현황 테이블 Panel
    public JPanel SalesView(){
        Sales = new JPanel();
        Sales.add(new JLabel("매출조회~~"));
        return Sales;
    }

    // 음료 재고 확인 및 보충, 가격 조정 Panel
    public JPanel StocksView(){
        Stocks = new JPanel();
        Stocks.add(new JLabel("재고조회~~"));
        return Stocks;
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

    // 로그아웃 Panel
    public void Logout(){

    }

}
