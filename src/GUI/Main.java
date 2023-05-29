package GUI;

import javax.swing.*;
import GUI.StartMachine;

public class Main extends JFrame {

    public Main() {

    }

    public static void main(String[] args) {
        new StartMachine();
        System.out.println("hellod");
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                Main vendingMachine = new Main();
//                vendingMachine.setVisible(true);
//            }
//        });
    }
}
