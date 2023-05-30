package GUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.regex.*;
import java.awt.*;

public class ManagerView extends JFrame {
    private Main main;

    private JPanel Input_pw;    // 관리자 인증 화면
    private JPanel Sales;       // 매출 현황 화면
    private JPanel Stocks;      // 재고 파악, 음료 수정 화면
    private JPanel Money;       // 잔돈 & 수금 화면
    private JPanel resetPW;     // 비밀번호 변경 화면

    private JPanel mainPanel;
    private JPanel chartPanel;
    private JPanel menuPanel;




    public ManagerView() {
        mainPanel=new JPanel();
        mainPanel.setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(400,300);
        setSize(800, 400);
        setTitle("���Ǳ� ���α׷�");

        mainPanel.add(getMenuPanel(),BorderLayout.EAST);
        mainPanel.add(getChartPanel(),BorderLayout.CENTER);
        setContentPane(mainPanel);

        setVisible(true);

    }

    public JPanel getChartPanel() {
        if(chartPanel==null) {
            chartPanel=new JPanel();
            chartPanel.setBackground(Color.RED);

        }

        return chartPanel;
    }

    public JPanel getMenuPanel() {
        if(chartPanel==null) {
            menuPanel=new JPanel();
            menuPanel.setBorder(new EmptyBorder(5,5,5,5));
            menuPanel.setPreferredSize(new Dimension(300,300));
            menuPanel.setLayout(new GridLayout(8,1));
           // chartPanel.setBackground(Color.blue);
        }
        return menuPanel;
    }

    // 비밀번호 재설정
    public boolean resetPw(String password){
        String pattern = "^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$";

        // 비밀번호와 패턴을 비교
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(password);

        return matcher.matches();
    }

    public void setMain(Main main) {
        this.main=main;
    }

}
