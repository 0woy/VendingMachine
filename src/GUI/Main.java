package GUI;
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    static ManagerView managerPage;
    static StartMachine startPage;

    public StartMachine getStartInstance() {
        return startPage;
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Main main = new Main();
                    startPage = new StartMachine();
                    startPage.changeView(main);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


    }
}
