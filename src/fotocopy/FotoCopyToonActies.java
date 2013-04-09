package fotocopy;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 2-4-13
 * Time: 21:50
 * To change this template use File | Settings | File Templates.
 */
public class FotoCopyToonActies {
    private JTextArea txtLogtekst;
    private JButton btnSluit;
    protected JPanel mainPanel;

    public FotoCopyToonActies() {
        btnSluit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    /**
     * @param tekst the txtLogtekst to append
     */
    public void setTxtLogtekst(String tekst) {
        this.txtLogtekst.append(tekst);
        this.txtLogtekst.setCaretPosition(this.txtLogtekst.getDocument().getLength());
    }

    public void clearTxtLogtekst() {
        this.txtLogtekst.setText("");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("FotoCopyToonActies");
        frame.setContentPane(new FotoCopyToonActies().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
