package GUI;
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    static StartMachine startMachinPage;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 모니터에 보이는 화면 설정
                try {
                    Main main = new Main();
                    startMachinPage = new StartMachine();
                    startMachinPage.changeView(main);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


    }
}
