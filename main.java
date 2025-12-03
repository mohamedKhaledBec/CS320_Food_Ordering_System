import FOS_UI.LoginWindow;

import javax.swing.*;

public class main {
        public static void main(String[] args) {
            SwingUtilities.invokeLater(()->{new LoginWindow().setVisible(true);} );
            System.out.println("Hello World!");
        }
}
