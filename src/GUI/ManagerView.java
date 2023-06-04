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
import java.util.HashMap;
import java.util.regex.*;
import java.awt.*;

public class ManagerView extends JFrame{
    private Main main;
    private StartMachine startMachine;

    private JTabbedPane tabs;       // 화면 분리
    private CardLayout StockCardLayout;
    private JPanel StockCards;
    private CardLayout MoneyCardLayout;
    private JPanel MoneyCard;

    private JPanel Input_pw;        // 관리자 인증 화면
    private JPanel Sales;           // 매출 현황 화면

    private JPanel Stocks;          // 재고 파악, 음료 수정 화면
    private JPanel supplyStocks;    // 재고 보충 화면
    private JPanel modifyBeverage;  // 음료 속성 변경 화면

    private JPanel Money;           // 잔돈 & 수금 화면
    private JPanel supplyMoney;     // 잔돈 보충 화면

    private JPanel resetPW;         // 비밀번호 변경 화면

    private JPanel managerView;         // 관리자 화면

    public ManagerView() {
        setTitle("관리자 메뉴");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        managerView=new JPanel();
        managerView.setLayout(new BorderLayout());
        managerView.add(menuPanel(), BorderLayout.CENTER);
        Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        managerView.setBorder(border);
        setContentPane(managerView);

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
                    StartMachine.changeImage(); // 자판기 음료 이미지 변경
                    StartMachine.chagnePrice(); // 자판기 음료 가격 변경
                    
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
        StockCardLayout = new CardLayout();
        StockCards = new JPanel(StockCardLayout);
        StockCards.add(Stocks, "Stocks");
        StockCards.add(supplyStocks, "Supply");
        StockCards.add(modifyBeverage, "Modify");

        Supplement.addActionListener(e -> {
            StockCardLayout.show(StockCards, "Supply");
        });

        Modify.addActionListener(e -> {
            StockCardLayout.show(StockCards,"Modify");
        });

        return StockCards;
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
            StockCardLayout.show(StockCards,"Stocks");
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

            StockCardLayout.show(StockCards,"Stocks");
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

        // 사용자가 가격의 TextField를 선택한 경우 초기값 사라짐
        price.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JFormattedTextField textField = (JFormattedTextField) e.getSource();
                textField.setText("");
            }
        });

        // 음료 가격 입력 이벤트
        price.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // 숫자 BackSpace, Delete키만 입력 허용
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE)
                    e.consume();

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

        changeBeverage.setSelectedIndex(0);  // 음료 선택 값 초기화
        newBeverage.setText(StartMachine.vm.getBeverageName(0));    // 음료 이름 변경 값 초기화
        price.setValue(StartMachine.vm.getBeveragePrice(0));        // 음료 가격 초기화

        modifyBeverage.add(changeBeveragePanel);
        modifyBeverage.add(newBeveragePanel);
        modifyBeverage.add(pricePanel);
        modifyBeverage.add(buttonPanel);

        // Combobox에서 음료를 선택한 경우
        changeBeverage.addActionListener(e->{
            String bName = (String)changeBeverage.getSelectedItem();
            newBeverage.setText(bName);   // 선택한 음료를 변경할 음료 이름에 저장(초기화)
            price.setValue(StartMachine.vm.getBeveragePrice(changeBeverage.getSelectedIndex())); // 선택한 음료의 가격을 변경할 음료 가격에 저장
        });

        // 취소 버튼 누를시
        buttons[0].addActionListener(e -> {
            StockCardLayout.show(StockCards,"Stocks"); // 음료 재고 조회 화면으로 이동
            changeBeverage.setSelectedIndex(0);              // 음료 선택 값 초기화
            newBeverage.setText(StartMachine.vm.getBeverageName(0));    // 음료 이름 변경 값 초기화
            price.setValue(StartMachine.vm.getBeveragePrice(0));        // 음료 가격 초기화
        });

        // 확인 버튼 누를시
        buttons[1].addActionListener(e -> {
            // 현재 선택된 음료의 정보를 각각 저장
            int currentPrice =  StartMachine.vm.getBeveragePrice(changeBeverage.getSelectedIndex());    // 가격
            String currentName = StartMachine.vm.getBeverageName(changeBeverage.getSelectedIndex());    // 이름
            int currentIdx = changeBeverage.getSelectedIndex(); // 인덱스

            // 취소 버튼의 ActionListener를 활용해 재고 조회 화면으로 이동할 수 있는 변수
            ActionListener moveStockView = buttons[0].getActionListeners()[0];

            // 음료의 이름을 변경하지 않은 경우
            if(newBeverage.getText().equals(currentName)){
                Class<?> type = price.getValue().getClass();
                int priceValue =0;
                if (type == Long.class) {
                    long tmp = (Long) price.getValue();
                   priceValue= (int) tmp;
                }
                else if(type == Integer.class){
                    priceValue = (Integer) price.getValue();
                }
                // 음료의 가격이 변경된 경우
                if(priceValue != currentPrice){
                    StartMachine.vm.setBeveragePrice(currentIdx,priceValue); // 선택된 음료의 가격을 price로 변경
                 //   StartMachine.getInstance().getBeverageBtn(currentIdx).setText(Integer.toString(priceValue) + "원");
                }
            }

            // 음료의 이름을 변경한 경우
            else{

                // 콤보박스의 내용 변경
                SwingUtilities.invokeLater(() -> {
                    changeBeverage.setSelectedItem((String) newBeverage.getText());
                    changeBeverage.revalidate();
                    changeBeverage.repaint();
                });
                
                // 기존 음료의 이름을 새로 지정한 이름으로 변경
                StartMachine.vm.setBeverageName(currentIdx, (String)newBeverage.getText()); // 선택된 음료의 이름을 newBeverage로 변경

                // price의 타입에 따라 달리 cast
                Class<?> type = price.getValue().getClass();
                int priceValue =0;
                if (type == Long.class) {
                    long tmp = (Long) price.getValue();
                    priceValue= (int) tmp;
                }
                else if(type == Integer.class){
                    priceValue = (Integer) price.getValue();
                }

                // 음료의 가격이 변경된 경우
                if(priceValue != currentPrice){
                    StartMachine.vm.setBeveragePrice(currentIdx,priceValue); // 선택된 음료의 가격을 price로 변경
                }

                modifyBeverage.revalidate();
                modifyBeverage.repaint();
            }

            // 재고 조회 테이블 업데이트
            tableModel.setValueAt(StartMachine.vm.getBeverageName(currentIdx), currentIdx, 0);
            // 재고 조회 화면으로 이동
            moveStockView.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));

        });
        return modifyBeverage;
    }

    // 잔돈 확인 & 충전, 수금 Panel
    public JPanel MoneyView(){
        Money = new JPanel(new BorderLayout(10,20));
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.CYAN);
        JPanel buttonPanel = new JPanel();

        // 화폐 단위, 화폐별 남은 개수 저장
        Object[][] data = new Object[5][];
        for (int i = 0; i < data.length; i++) {
            data[i] = new Object[2];
            data[i][0] = StartMachine.vm.moneyValues()[i];
            data[i][1] = StartMachine.vm.getChangeStock().get(StartMachine.vm.moneyValues()[i]);
        }

        String[] columnNames = {"화폐 이름", "남은 개수"};

        // Table을 Read-Only로 설정
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);    // 스크롤 가능

        JLabel title = new JLabel("자판기 잔돈 현황");
        JButton Supplement = new JButton("잔돈 보충");
        JButton Collect= new JButton("수금");

        titlePanel.add(title);
        buttonPanel.add(Supplement);
        buttonPanel.add(Collect);

        Money.add(titlePanel, BorderLayout.NORTH);
        Money.add(scrollPane, BorderLayout.CENTER);
        Money.add(buttonPanel, BorderLayout.SOUTH);
        
        supplyMoney = supplyMoneyView(tableModel); // 잔돈 보충 패널

        // CardLayout 재고 확인 & 보충 화면 구성
        MoneyCardLayout = new CardLayout();
        MoneyCard = new JPanel(MoneyCardLayout);
        MoneyCard.add(Money, "Money");
        MoneyCard.add(supplyMoney, "addMoney");

        // 잔돈 보충 클릭시 화면 이동
        Supplement.addActionListener(e -> {
            MoneyCardLayout.show(MoneyCard, "addMoney");
        });

        // 수금 클릭시 수금된 금액 표시
        Collect.addActionListener(e -> {
            int returns =StartMachine.vm.MoneyToManager();

            StringBuilder message = new StringBuilder();    // 수금액 알림으로 표시
            message.append("총 수금액: "+returns+" 원");          // 총 수금된 금액 표시

            JOptionPane.showMessageDialog(null,
                    message.toString(),
                    "반환된 금액",
                    JOptionPane.INFORMATION_MESSAGE);

            HashMap<Integer,Integer> updateStock = StartMachine.vm.getChangeStock();
            // 재고 조회 테이블 업데이트
            for(int i=0;i<tableModel.getRowCount();i++) {
                int rest = updateStock.get(tableModel.getValueAt(i, 0));
                tableModel.setValueAt(rest, i, 1);
            }
        });

        return MoneyCard;
    }

    // 잔돈 보충 화면
    public JPanel supplyMoneyView(DefaultTableModel tableModel){
        supplyMoney = new JPanel();
        GridLayout grid = new GridLayout(6,2);
        grid.setHgap(30);
        grid.setVgap(50);
        supplyMoney.setLayout(grid);

        Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        supplyMoney.setBorder(border);

        JLabel [] name = new JLabel[5];
        JFormattedTextField [] add = new JFormattedTextField[5];    // 숫자만 받기 위해 JFormatted 사용

        // 화폐 이름 저장 및 가운데 정렬
        for(int i=0;i<add.length;i++){
            name[i] = new JLabel(Integer.toString(StartMachine.vm.moneyValues()[i])+"원");
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

                    // 잔돈 보충은 5개 이상 불가
                    else if (Character.isDigit(c)) {
                        JFormattedTextField textField = (JFormattedTextField) e.getSource();
                        String currentText = textField.getText();
                        int caretPosition = textField.getCaretPosition();
                        String newText = currentText.substring(0, caretPosition) + c + currentText.substring(caretPosition);
                        int value = Integer.parseInt(newText);
                        if (value > 10) {
                            JOptionPane.showMessageDialog(null,
                                    "5개를 초과하여 잔돈을 충전할 수 없습니다.",
                                    "잔돈 충전 불가",
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
            supplyMoney.add(name[i]);
            supplyMoney.add(add[i]);
        }

        JButton [] buttons = new JButton[2];
        buttons[0] = new JButton("취소");
        buttons[1] = new JButton("확인");

        buttons[0].addActionListener(e -> {
            MoneyCardLayout.show(MoneyCard,"Money");
            for(int i=0;i<add.length;i++)
                add[i].setValue(0);     // 추후 입력을 위해 다시 0으로 초기화
        });

        buttons[1].addActionListener(e -> {
            for(int i=0;i<add.length;i++) {
                Number value = (Number) add[i].getValue();
                int plus = value.intValue();
                
                // MoneyValue를 key로 하여 현재 화폐당 남은 재고 current에 저장
                int current = StartMachine.vm.getChangeStock().get(StartMachine.vm.moneyValues()[i]);
                StartMachine.vm.setChangeStock(i,current+plus); // plus만큼 화폐 보충하기

                // 잔돈 조회 테이블 업데이트
                tableModel.setValueAt(current+plus, i, 1);
                add[i].setValue(0);     // 추후 입력을 위해 다시 0으로 초기화
            }
            MoneyCardLayout.show(MoneyCard,"Money");
        });

        supplyMoney.add(buttons[0]);
        supplyMoney.add(buttons[1]);

        return supplyMoney;
    }

    // 비밀번호 변경 Panel
    public JPanel PasswordView(){
        resetPW = new JPanel();
        GridLayout grid = new GridLayout(6,1,15,10);
        resetPW.setLayout(grid);
        Border border = BorderFactory.createEmptyBorder(20, 10, 10, 10);
        resetPW.setBorder(border);

        JLabel [] title = new JLabel[5];
        JPasswordField currentPW = new JPasswordField();    // 현재 비밀번호 입력창
        JPasswordField newPW = new JPasswordField();     // 새 비밀번호 입력창
        JPasswordField againPW = new JPasswordField();     // 비밀번호 재입력창
        currentPW.setEchoChar('*');
        newPW.setEchoChar('*');
        againPW.setEchoChar('*');

        JButton button = new JButton("확인");   // 확인 버튼
        button.setHorizontalAlignment(SwingConstants.CENTER);

        title[0] = new JLabel("현재 비밀번호");
        title[1] = new JLabel("새 비밀번호");
        title[2] = new JLabel("비밀번호 재입력");
        title[3] = new JLabel("비밀번호 변경");
        title[4] = new JLabel("* 특수문자, 숫자 포함 8자리 이상");

        // 제목 및 부제목 폰트 크기 설정
        title[3].setFont(title[3].getFont().deriveFont(Font.PLAIN, 35));
        title[4].setFont(title[4].getFont().deriveFont(Font.PLAIN, 13));

        title[3].setHorizontalAlignment(SwingConstants.CENTER);
        title[4].setHorizontalAlignment(SwingConstants.CENTER);

        
        // 확인 버튼을 누른 경우
        button.addActionListener(e -> {
            String checkPw = StartMachine.manager.getPassword();
            // 현재 비밀번호 입력이 틀린 경우
            if(!checkPw.equals(currentPW.getText())){
                JOptionPane.showMessageDialog(null,
                        "현재 비밀번호가 다릅니다.",
                        "비밀번호 변경 불가",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 비밀번호 재입력이 틀린 경우
            if(!newPW.getText().equals(againPW.getText())){
                JOptionPane.showMessageDialog(null,
                        "새 비밀번호와 재입력한 비밀번호가 다릅니다.",
                        "비밀번호 변경 불가",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 비밀번호 재설정 함수 호출
            boolean result = StartMachine.manager.setPassword(newPW.getText());

            // 비밀번호 변경에 성공한 경우
            if(result){
                JOptionPane.showMessageDialog(null,
                        "비밀번호가 변경되었습니다.",
                        "비밀번호 변경",
                        JOptionPane.INFORMATION_MESSAGE);
                currentPW.setText("");
                newPW.setText("");
                againPW.setText("");
            }
            // 비밀번호 변경에 실패한 경우
            else{
                JOptionPane.showMessageDialog(null,
                        "비밀번호 조건에 맞게 설정해 주세요",
                        "비밀번호 변경 불가",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // 내부 Contents가 가운데 정렬이 되도록 구성
        FlowLayout flow = new FlowLayout(FlowLayout.CENTER);
        flow.setHgap(20);

        // 현재 비밀번호 입력 패널
        JPanel currentPwPanel = new JPanel(flow);
        currentPW.setPreferredSize(new Dimension(150,30));
        currentPwPanel.add(title[0]);
        currentPwPanel.add(currentPW);

        // 새 비밀번호 입력 패널
        JPanel newPwPanel = new JPanel(flow);
        newPW.setPreferredSize(new Dimension(150,30));
        newPwPanel.add(title[1]);
        newPwPanel.add(newPW);

        // 비밀번호 재입력 패널
        JPanel againPwPanel = new JPanel(flow);
        againPW.setPreferredSize(new Dimension(150,30));
        againPwPanel.add(title[2]);
        againPwPanel.add(againPW);


        resetPW.add(title[3]);
        resetPW.add(title[4]);
        resetPW.add(currentPwPanel);
        resetPW.add(newPwPanel);
        resetPW.add(againPwPanel);
        resetPW.add(button);
        
        return resetPW;
    }

    // 관리자 메뉴 이동시 화면 변화
    public void changeView(Main main){
        this.main = main;
    }
}
