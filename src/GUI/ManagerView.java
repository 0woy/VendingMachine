package GUI;
import VendingMachine.Manager;
import VendingMachine.VendingMachine;
import GUI.StartMachine;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.*;
import java.awt.*;



public class ManagerView extends JFrame{
    private Main main;
    private JTabbedPane tabs;       // 화면 분리
    private CardLayout cardLayout;
    private JPanel cards;

    private JPanel Input_pw;        // 관리자 인증 화면
    private JPanel Sales;           // 매출 현황 화면
    private JPanel Stocks;          // 재고 파악, 음료 수정 화면
    private JPanel supplyStocks;    // 재고 보충 화면
    private JPanel modifyBeverage;  // 음료 속성 변경 화면
    private JPanel Money;           // 잔돈 & 수금 화면
    private JPanel resetPW;         // 비밀번호 변경 화면

    private JPanel manager;         // 관리자 화면

    public ManagerView() {
        setTitle("관리자 메뉴");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        manager=new JPanel();
        manager.setLayout(new BorderLayout());
        manager.add(menuPanel(), BorderLayout.CENTER);
        Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        manager.setBorder(border);
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

    // 음료 재고 확인 Panel
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
        
        String[] columnNames = {"음료 이름", "현재 재고"};

        // Table을 Read-Only로 설정
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);    // 스크롤 가능

        JLabel title = new JLabel("음료 재고 현황");
        JButton Supplement = new JButton("재고 보충");
        JButton Modify= new JButton("음료 & 가격 변경");

        titlePanel.add(title);
        buttonPanel.add(Supplement);
        buttonPanel.add(Modify);

        Stocks.add(titlePanel, BorderLayout.NORTH);
        Stocks.add(scrollPane, BorderLayout.CENTER);
        Stocks.add(buttonPanel, BorderLayout.SOUTH);
        
        // 재고 보충 패널
        supplyStocks = SupplyStocksView(tableModel);
        modifyBeverage = ModifyBeverage(tableModel);


        // CardLayout 재고 확인 & 보충 화면 구성
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.add(Stocks, "Stocks");
        cards.add(supplyStocks, "Supply");
        cards.add(modifyBeverage, "Modify");

        Supplement.addActionListener(e -> {
            cardLayout.show(cards, "Supply");
        });

        Modify.addActionListener(e -> {
            cardLayout.show(cards,"Modify");
        });

        return cards;
    }

    // 재고 보충 화면
    public JPanel SupplyStocksView(DefaultTableModel tableModel){
        supplyStocks = new JPanel();
        GridLayout grid = new GridLayout(6,2);
        grid.setHgap(30);
        grid.setVgap(50);
        supplyStocks.setLayout(grid);

        Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        supplyStocks.setBorder(border);

        JLabel [] name = new JLabel[5];

        // 숫자만 받기 위해 JFormatted 사용
        JFormattedTextField [] add = new JFormattedTextField[5];

        // 음료 이름 저장 및 가운데 정렬
        for(int i=0;i<add.length;i++){
            name[i] = new JLabel(StartMachine.vm.getBeverageName(i));
            name[i].setHorizontalAlignment(SwingConstants.CENTER);
            
            // 숫자만 받아을 수 있도록 format 설정
            NumberFormat format = NumberFormat.getInstance();
            format.setParseIntegerOnly(true);

            // 초기값 0으로 설정 & 가운데 정렬
            add[i] = new JFormattedTextField(format);
            add[i].setValue(0);
            add[i].setHorizontalAlignment(SwingConstants.CENTER);

            int finalI = i;
            //입력 종료 후 사용자의 입력값 검증 (예외 처리)
            add[i].addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                // 사용자가 TextField 선택한 경우 0 사라짐
                public void focusGained(FocusEvent e) {
                    JFormattedTextField textField = (JFormattedTextField) e.getSource();
                   textField.setText("");
                }
            });

            add[i].addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    // 숫자 BackSpace, Delete키만 입력 허용
                    char c = e.getKeyChar();
                    if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                        e.consume();
                    }

                    // 재고 보충은 10개를 초과할 수는 없음
                    else if (Character.isDigit(c)) {
                        JFormattedTextField textField = (JFormattedTextField) e.getSource();
                        String currentText = textField.getText();
                        int caretPosition = textField.getCaretPosition();
                        String newText = currentText.substring(0, caretPosition) + c + currentText.substring(caretPosition);
                        int value = Integer.parseInt(newText);
                        if (value > 10) {
                            JOptionPane.showMessageDialog(null,
                                    "10개를 초과하여 재고를 충전할 수 없습니다.",
                                    "재고 충전 불가",
                                    JOptionPane.ERROR_MESSAGE);
                            e.consume();
                        }
                    }
                }

                @Override
                public void keyPressed(KeyEvent e) {}
                @Override
                public void keyReleased(KeyEvent e) {}
            });
            supplyStocks.add(name[i]);
            supplyStocks.add(add[i]);
        }

        JButton [] buttons = new JButton[2];
        buttons[0] = new JButton("취소");
        buttons[1] = new JButton("확인");

        buttons[0].addActionListener(e -> {
            cardLayout.show(cards,"Stocks");
            for(int i=0;i<add.length;i++)
                add[i].setValue(0);     // 추후 입력을 위해 다시 0으로 초기화
        });

        buttons[1].addActionListener(e -> {
            for(int i=0;i<add.length;i++) {
                Number value = (Number) add[i].getValue();
                int plus = value.intValue();
                int current = StartMachine.vm.currentStock(i);
                StartMachine.vm.setBeverageStocks(i, plus);
                
                // 재고 조회 테이블 업데이트
                tableModel.setValueAt(current+plus, i, 1);
                add[i].setValue(0);     // 추후 입력을 위해 다시 0으로 초기화
            }

            cardLayout.show(cards,"Stocks");
        });

        supplyStocks.add(buttons[0]);
        supplyStocks.add(buttons[1]);


        return supplyStocks;
    }

    // 음료 속성 변경화면
    public JPanel ModifyBeverage(DefaultTableModel tableModel){
        modifyBeverage = new JPanel();
        GridLayout grid = new GridLayout(5,1,15,40);

        modifyBeverage.setLayout(grid);

        Border border = BorderFactory.createEmptyBorder(20, 10, 10, 10);
        modifyBeverage.setBorder(border);

        JLabel [] name = new JLabel[3];                       // Labels
        JComboBox<String>changeBeverage = new JComboBox<>();  // 변경할 음료 선택
        JTextField newBeverage = new JTextField();            // 추가할 음료 선택
        JFormattedTextField price;                            // 음료 가격 변경
        JButton [] buttons = new JButton[2];                  // 취소 & 확인 버튼

        // 숫자만 받아을 수 있도록 format 설정
        NumberFormat format = NumberFormat.getInstance();
        format.setParseIntegerOnly(true);
        price = new JFormattedTextField(format);
        price.setValue(0);

        name[0] = new JLabel("변경할 음료 이름");
        name[1] = new JLabel("추가할 음료 이름");
        name[2] = new JLabel("변경할 음료 가격");

        buttons[0] = new JButton("취소");
        buttons[1] = new JButton("확인");

        // Label 가운데 정렬 및 음료 선택 콤보박스 아이템 추가
        for(int i=0;i<StartMachine.vm.getBeverageCount();i++){
            if(i<=2)
                name[i].setPreferredSize(new Dimension(150,30));
            changeBeverage.addItem(StartMachine.vm.getBeverageName(i));
        }

        // 사용자가 TextField 선택한 경우 초기값 사라짐
        price.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JFormattedTextField textField = (JFormattedTextField) e.getSource();
                textField.setText("");
            }
        });

        price.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // 숫자 BackSpace, Delete키만 입력 허용
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                }

                // 음료 가격은 5000원을 초과할 수는 없음
                else if (Character.isDigit(c)) {
                    JFormattedTextField textField = (JFormattedTextField) e.getSource();
                    String currentText = textField.getText();
                    int caretPosition = textField.getCaretPosition();
                    String newText = currentText.substring(0, caretPosition) + c + currentText.substring(caretPosition);
                    int value = Integer.parseInt(newText);
                    if (value > 5000) {
                        JOptionPane.showMessageDialog(null,
                                "5000원을 초과한 금액으로 산정할 수 없습니다.",
                                "가격 변경 불가",
                                JOptionPane.ERROR_MESSAGE);
                        e.consume();
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        // 음료 이름 선택 패널(Combobox로 구성하여 오탈자가 없도록 함)
        JPanel changeBeveragePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        changeBeverage.setPreferredSize(new Dimension(150, 30));
        changeBeveragePanel.add(name[0]);
        changeBeveragePanel.add(changeBeverage);

        // 음료 이름 변경 패널 (TextField로 구성하여 음료 이름 변경)
        JPanel newBeveragePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        newBeverage.setPreferredSize(new Dimension(150, 30));
        newBeveragePanel.add(name[1]);
        newBeveragePanel.add(newBeverage);

        // 음료 가격 변경 패널 (양수만 입력 가능하도록 함)
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        price.setPreferredSize(new Dimension(150, 30));
        pricePanel.add(name[2]);
        pricePanel.add(price);

        // 확인 및 취소 버튼
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setPreferredSize(new Dimension(150, 30));
        buttonPanel.add(buttons[0]);
        buttonPanel.add(buttons[1]);

        modifyBeverage.add(changeBeveragePanel);
        modifyBeverage.add(newBeveragePanel);
        modifyBeverage.add(pricePanel);
        modifyBeverage.add(buttonPanel);



        // Combobox에서 음료를 선택한 경우
        changeBeverage.addActionListener(e->{
            String bName = (String)changeBeverage.getSelectedItem();
            // 선택한 음료를 음료 이름 변경값지정
            newBeverage.setText(bName);
            // 선택한 음료의 가격을 음료 가격 변경값으로 지정
            price.setValue(StartMachine.vm.getBeveragePrice(changeBeverage.getSelectedIndex()));
        });

        // 취소 버튼 누를시
        buttons[0].addActionListener(e -> {
            cardLayout.show(cards,"Stocks"); // 음료 재고 조회 화면으로 이동
            changeBeverage.setSelectedIndex(0);  // 음료 선택 값 초기화
            newBeverage.setText(StartMachine.vm.getBeverageName(0));              // 음료 이름 변경 값 초기화
            price.setValue(StartMachine.vm.getBeveragePrice(0));                    // 음료 가격 초기화
        });

        // 확인 버튼 누를시
        buttons[1].addActionListener(e -> {

            // 현재 선택된 음료의 정보를 각각 저장
            int currentPrice =  StartMachine.vm.getBeveragePrice(changeBeverage.getSelectedIndex());    // 가격
            String currentName = StartMachine.vm.getBeverageName(changeBeverage.getSelectedIndex());    // 이름
            int currentIdx = changeBeverage.getSelectedIndex(); // 인덱스
            // 취소 버튼의 ActionListener를 활용해 재고 조회 화면으로 이동할 수 있는 변수
            ActionListener moveStockView = buttons[0].getActionListeners()[0];

            // 아무런 입력 없이 확인을 누른 경우 음료 속성 변경 방지
            newBeverage.setText(currentName);
            price.setValue(currentPrice);

            // 음료의 이름을 변경하지 않은 경우
            if(newBeverage.getText().equals(currentName)){

                // 음료의 가격이 변경된 경우
                if((int)price.getValue() != currentPrice){
                    StartMachine.vm.setBeveragePrice(currentIdx,(int)price.getValue()); // 선택된 음료의 가격을 price로 변경
                }
            }
            // 음료의 이름을 변경한 경우
            else{
                StartMachine.vm.setBeverageName(currentIdx, (String)newBeverage.getText()); // 선택된 음료의 이름을 newBeverage로 변경
                changeBeverage.setSelectedItem(newBeverage.getText());

                // 음료의 가격이 변경된 경우
                if((Integer) price.getValue() != currentPrice){
                    StartMachine.vm.setBeveragePrice(currentIdx,(int)price.getValue()); // 선택된 음료의 가격을 price로 변경
                }
            }
            // 재고 조회 테이블 업데이트
            tableModel.setValueAt(StartMachine.vm.getBeverageName(currentIdx), currentIdx, 0);
            //재고 조회 화면으로 이동
            moveStockView.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));

        });

        for(int i=0;i<5;i++)
            System.out.println("음료 이름:"+StartMachine.vm.getBeverageName(i)+" 음료 가격:"+StartMachine.vm.getBeveragePrice(i));
        return modifyBeverage;
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
