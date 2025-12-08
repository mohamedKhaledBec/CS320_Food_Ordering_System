package FOS_UI;
import javax.swing.*;
import java.awt.*;
// Being done by Umair Ahmad
public final class DialogUtils {
    private DialogUtils(){}

    public static void showError(Component parent, String message){
        JOptionPane.showMessageDialog(parent,message,"Error",JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfo(Component parent, String message){
        JOptionPane.showMessageDialog(parent,message,"Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public static int confirm(Component parent, String message){
        return JOptionPane.showConfirmDialog(
                parent,
                message,
                "Confirm",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
    }





}
