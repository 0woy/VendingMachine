package GUI;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.swing.border.Border;

import FileIO.Datas;
import VendingMachine.VendingMachine;
import VendingMachine.Manager;

public class StartMachine extends JFrame{
    
    private Main main;              // GUI.Main 클래스
    static Manager manager;         // 관리자 클래스
    
    private JPanel beveragePanel;   // 음료 구매 화면
    private JPanel select;          // 음료 버튼 설정 화면
    private JPanel userPanel;       // 잔돈 반환 & 화폐 투입

    private JPanel outPanel;        // 음료 투출 패널
    private JLabel outLet;          // 음료 투출구

    private JPanel managePanel;     // 관리자 메뉴
    private JPanel start;           // 자판기 화면
    
    public static VendingMachine vm;     // 음료 속성을 가져오기 위한 변수
    private JLabel cMoney;                  // 현재 사용자가 투입 & 사용 금액
    private static JButton[] beverage;      // 자판기 음료 버튼

    public StartMachine(){
        manager = new Manager();
        vm = new VendingMachine();

        setTitle("음료 자판기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c =getContentPane();
        c.setLayout(new BorderLayout(0,10));
        cMoney =new JLabel("0원");
        beverage=new JButton[]{};

        start = new JPanel();
        start.setLayout(new BorderLayout(30,20));

        beveragePanel = BeveragePanel();
        userPanel = userPanel();
        outPanel = outPanel();
        managePanel = toManage();

        start.setPreferredSize(new Dimension(200,400));
        start.add(beveragePanel, BorderLayout.NORTH);
        start.add(userPanel, BorderLayout.CENTER);
        start.add(outPanel, BorderLayout.SOUTH);

        c.add(start, BorderLayout.CENTER);
        c.add(managePanel,BorderLayout.SOUTH);
        setLocation(50,0);
        setSize(700,800);
        setVisible(true);
    }

    // 음료 구매 패널
    public JPanel BeveragePanel(){
        JPanel beverage = new JPanel();
        beverage.setBackground(Color.lightGray);

        Border border = BorderFactory.createLineBorder(Color.blue,5);
        beverage.setBorder(border);
        select = selectBeverage();
        beverage.add(select);
        return beverage;
    }

    // 음료 재고 상태 & 사용자 투입 금액에 따른 음료 이미지 변환
    public static void changeImage(){
        for(int i=0;i<beverage.length;i++){

            // 재고는 있으나 투입 금액이 부족해 음료를 구매할 수 없는 경우
            if(vm.currentInput() < vm.getBeveragePrice(i) && vm.getBeverageStocks(i)>0){
                // 각 음료별 구매 불가능 이미지 파일 읽어오기
                String newPath = "src/Images/no_"+vm.getBeverageName(i)+".png";
                File imageFile = new File(newPath);
                
                // 음료 이미지 파일이 존재하는 경우, 개별 음료 사진 사용
                if(imageFile.exists()) {
                    ImageIcon newImage = new ImageIcon(newPath);
                    beverage[i].setIcon(newImage);
                }

                // 음료 이미지가 없는 경우
                else{
                    ImageIcon NoImage = new ImageIcon("src/Images/noImage.png");
                    beverage[i].setIcon(NoImage);
                }
            }

            // 음료의 재고가 부족한 경우
            else if(vm.getBeverageStocks(i) <=0){
                // 음료별 재고 부족 이미지 읽어오기
                String newPath = "src/Images/soldout_"+vm.getBeverageName(i)+".png";
                File imageFile = new File(newPath);

                // 음료 이미지 파일이 존재하는 경우
                if(imageFile.exists()) {
                    ImageIcon newImage = new ImageIcon(newPath);
                    beverage[i].setIcon(newImage);  // Set the new image for the button}
                }

                // 음료 이미지가 없는 경우
                else{
                    ImageIcon NoImage = new ImageIcon("src/Images/noImage.png");
                    beverage[i].setIcon(NoImage);
                }
            }

            // 투입액이 충분해 음료를 구매할 수 있는 경우
            else{
                String newPath = "src/Images/"+vm.getBeverageName(i)+".png";
                File imageFile = new File(newPath);

                // 음료 이미지 파일이 존재하는 경우
                if(imageFile.exists()) {
                    ImageIcon newImage = new ImageIcon(newPath);
                    beverage[i].setIcon(newImage);  // Set the new image for the button}
                }

                // 음료 이미지가 없는 경우
                else{
                    ImageIcon NoImage = new ImageIcon("src/Images/noImage.png");
                    beverage[i].setIcon(NoImage);
                }
            }
        }
    }

    // 음료 버튼 Text(가격) 변경
    public static void chagnePrice(){
        for(int i=0;i<beverage.length;i++)
            beverage[i].setText(vm.getBeveragePrice(i)+"원");
    }
    
    // 음료 구매 Panel
    public JPanel selectBeverage(){
        select = new JPanel();
        GridLayout grid = new GridLayout(2,3);
        grid.setHgap(10);
        grid.setVgap(10);
        select.setLayout(grid);
        select.setBackground(Color.lightGray);
        beverage = new JButton[5];              // 이미지 버튼
        ImageIcon [] images = new ImageIcon[5]; // 음료 이미지

        select.setSize(150,30);

        // 음료 구매 버튼 생성
        for (int i=0; i < beverage.length; i++) {
            int bIdx;
            String name;
            String path="";
            String price="";
            bIdx =i;
            name=vm.getBeverageName(i);
            path = "src/Images/no_"+name+".png";
            price =Integer.toString(vm.getBeveragePrice(i))+"원";
            images[i] = new ImageIcon(path);
            // 음료별 구매 버튼(음료 가격, 음료 이미지) 생성
            beverage[i] = new JButton(price,images[i]);
            beverage[i].setSize(60,80);
            beverage[i].setHorizontalTextPosition(SwingConstants.CENTER);
            beverage[i].setVerticalTextPosition(SwingConstants.BOTTOM);
            beverage[i].setFocusPainted(false);
                    
            // 음료를 구매 버튼을 선택했을 때
            beverage[i].addActionListener(e -> {

                // 투입 금액이 충분해 음료를 구매할 수 있는 경우
                if(vm.currentInput() >= vm.getBeveragePrice(bIdx)) {
                    // 재고가 없는 경우
                    if(vm.getBeverageStocks(bIdx)<=0){
                        JOptionPane.showMessageDialog(null,
                                "선택한 음료의 재고가 없습니다.\n관리자에게 문의하세요.",
                                "구매 불가",
                                JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    // 재고가 충분한 경우
                    else{
                        vm.buyBeverage(bIdx);   // 음료 구매 함수 호출
                        cMoney.setText(Integer.toString(vm.currentInput()) + "원");  // 현재 투입 금액 업데이트

                        // 투출구에 음료 이미지 표시
                        if(outLet != null){
                            String outPath = "src/Images/buy_"+vm.getBeverageName(bIdx)+".png";
                            File imageFile = new File(outPath);
                            // 해당 음료의 투출 이미지 파일이 존재하는 경우
                            if(imageFile.exists()) {
                                ImageIcon newImage = new ImageIcon(outPath);
                                outLet.setIcon(newImage);
                            }
                            // 음료의 투출 이미지가 없는 경우
                            else{
                                ImageIcon NoImage = new ImageIcon("src/Images/buy_noImage.png");
                                outLet.setIcon(NoImage);
                            }
                        }
                            // 구매 후 음료 재고가 없는 경우
                            if(vm.getBeverageStocks(bIdx)<=0)
                                Datas.writeSoldout(bIdx);   // 재고가 소진된 음료 정보 파일에 입력
                            }
                    }
                    // 투입 금액이 부족해 음료를 구매할 수 없는 경우
                    else{
                        JOptionPane.showMessageDialog(null,
                                "잔액이 부족합니다.",
                                "구매 불가",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    changeImage();  // 음료 재고에 따른 이미지 변경
                });

                select.add(beverage[i]);    //select 패널에 음료 버튼 추가
            }

        return select;
    }

    // 잔돈 반환 & 화폐 투입 Panel
    public JPanel userPanel(){
        JPanel user =new JPanel();
        user.setLayout(new FlowLayout(FlowLayout.RIGHT,20,0));

        JButton returnChange = new JButton("잔돈 반환");
        JButton inputMoney = new JButton("화폐투입");
        user.setPreferredSize(user.getLayout().preferredLayoutSize(user));
        
        JPanel coin = inputMoneyPanel();    // 화폐 선택 창

        // 화폐 투입 Action: 화폐 선택 화면 View
        inputMoney.addActionListener(e -> {
            user.remove(inputMoney);                // 화폐 투입 버튼 없애기
            user.add(coin);                         // 화폐 선택창 보이기
            JButton ok = new JButton("확인");   // coin 화면 닫는 버튼
            user.add(cMoney);                       // 화폐 선택창 옆에 현재 사용 가능한 액수 보이기
            user.add(ok);              
            user.revalidate();
            user.repaint();

            // 확인 버튼 누를 시 다시 화폐 투입 버튼 보이기
            ok.addActionListener(e2 -> {
                user.remove(coin);
                user.remove(ok);
                user.remove(cMoney);
                user.add(inputMoney);
                user.revalidate();
                user.repaint();
            });
        });

        // 잔돈 반환 Action
        returnChange.addActionListener(e-> {
            int[] returns = vm.getChanges();    // 화폐별 반환 개수 저장
            int[] values = vm.moneyValues();    // 화폐 단위 저장
            int sum =0;

            StringBuilder message = new StringBuilder();    // 사용자에게 반환된 잔돈 알림창으로 표시

            // 화폐별로 반환된 개수 표시
            for (int i = 0; i < returns.length; i++) {
                sum +=values[i]*returns[i];
                message.append(Integer.toString(values[i])).append("원: ")
                        .append(returns[i]).append("개\n");
            }
            message.append("총 반환액: "+sum+" 원");          // 총 반환된 금액 표시

            JOptionPane.showMessageDialog(null,
                    message.toString(),
                    "반환된 금액",
                    JOptionPane.INFORMATION_MESSAGE);
            cMoney.setText("0원");   // 잔돈 반환 후 사용 가능 금액 초기화
            vm.freeInput();         // input 동적 할당 해제
            vm.setInput();          // 반환 후에도 음료를 구매할 수 있도록 input 동적할당
            changeImage();
            outLet.setIcon(new ImageIcon("src/Images/투출구.png"));
        });

        user.add(returnChange);
        user.add(inputMoney);

        return user;
    }

    // 투입할 화폐 선택 Panel
    public JPanel inputMoneyPanel(){
        JPanel input =new JPanel();
        int [] value = vm.moneyValues();    // 화폐 단위 저장
        JButton [] btn = new JButton[5];    // 화폐 입력 버튼

        GridLayout grid = new GridLayout(1,6);
        input.setLayout(grid);
        
        // 화폐 이미지 버튼 클릭시 자판기에 돈 투입
        for(int i=0;i<5;i++){
            // 각 화폐별 금액 및 이미지 버튼 생성
            final int amount = value[i];    
            btn[i] = new JButton("", new ImageIcon("src/Images/" + value[i] + "won.png"));
            
            // 화폐를 누른 경우 발생하는 이벤트
            btn[i].addActionListener(e -> {
              int check = vm.insertMoney(amount);   // 자판기에 화폐 가치만큼 돈 투입(1000원이면 1000원 투입)
                
                // 상황에 따른 에러 메세지 출력
                switch (check){
                    case 0:
                        JOptionPane.showMessageDialog(null,
                                "5000원을 초과해 돈을 투입할 수 없습니다.",
                                "투입 불가",
                                JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null,
                                "1000원권을 4장 이상 넣을 수 없습니다.",
                                "투입 불가",
                                JOptionPane.INFORMATION_MESSAGE);
                        break;
                }
                // 현재 사용가능 금액 표시
                cMoney.setText(vm.currentInput()+"원");
                changeImage();
            });

            input.add(btn[i]);
        }
        return input;
    }

    // 관리자 메뉴 이동 Panel
    public JPanel toManage(){
        JPanel manage =new JPanel();
        manage.setLayout(new BorderLayout());

        JButton toManage = new JButton("관리자 메뉴");
        Border border = BorderFactory.createEmptyBorder(0, 0, 10, 10);
        manage.setBorder(border);

        manage.add(toManage,BorderLayout.EAST);

        // 관리자 메뉴 이동 버튼 클릭시
        toManage.addActionListener(e -> {
            String pw = JOptionPane.showInputDialog("비밀번호 입력:");    // 입력한 비밀번호 pw에 저장
            if(pw!=null) {
                
                // 비밀번호가 일치하는 경우 관리자 메뉴 Open
                if (pw.equals(manager.getPassword())) {
                    new ManagerView().changeView(main);
                }
                // 일치하지 않는 경우 에러메세지 출력
                else {
                    JOptionPane.showMessageDialog(null,
                            "비밀번호가 일치하지 않습니다.",
                            "관리자 인증 불가",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return manage;
    }

    // 투출구 Panel
    public JPanel outPanel() {
        JPanel out = new JPanel();
        ImageIcon image = new ImageIcon("src/Images/투출구.png");
        out.setLayout(new FlowLayout(FlowLayout.CENTER));

        // 음료 투출구 이미지 표시
        outLet = new JLabel(image);
        out.add(outLet);

        return out;
    }

    // 관리자 메뉴 이동시 화면 변화
    public void changeView(Main main){
        this.main = main;
    }

}
