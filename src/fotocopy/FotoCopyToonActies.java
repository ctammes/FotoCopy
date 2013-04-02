package fotocopy;

import javax.swing.*;

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

    /**
     * @param tekst the txtLogtekst to append
     */
    public void setTxtLogtekst(String tekst) {
        this.txtLogtekst.append(tekst);
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
