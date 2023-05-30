package GUI;

import javax.swing.*;

import java.awt.*;

public class Main extends JFrame {
    static ManagerView managerPage;
    static StartMachine startPage;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Main main = new Main();
                    startPage = new StartMachine();
                    startPage.setMain(main);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
}
