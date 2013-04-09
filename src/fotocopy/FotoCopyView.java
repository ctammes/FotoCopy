package fotocopy;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 2-4-13
 * Time: 20:49
 * To change this template use File | Settings | File Templates.
 */
public class FotoCopyView {
    private JTextField txtBron;
    private JTextField txtDoel;
    private JButton btnSelecteerBronFiles;
    private JButton btnSelecteerDoel;
    private JList lstBronfiles;
    private JTextArea txtStatusTekst;
    private JList lstHoogste;
    private JPanel mainPanel;
    private JButton btnUitvoeren;

    final static String COPYCMD = "cp";

    private static String bronFiles[];      // te kopieren bestanden
    private static String bronDir;          // brondirectory
    private static String doelDir;          // doeldirectory
    private static Map max;                 // overzicht van soorten en hoogste nummer in doeldir (dscf, afb enz.)

    public static void main(String[] args) {
        JFrame frame = new JFrame("FotoCopyView");
        frame.setContentPane(new FotoCopyView().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(200, 200);
        frame.pack();
        frame.setVisible(true);
    }

    public FotoCopyView() {
        txtBron.setText("/home/chris/chris1/chris/Foto´s");
        txtDoel.setText("/home/chris/tmp");

        btnSelecteerBronFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String args[] = {txtBron.getText()};
                SelecteerBronfiles.main(args);
                txtBron.setText(bronDir);
                DefaultListModel mod = new DefaultListModel();
                for (String file: bronFiles) {
                    mod.addElement(file);
                }
                lstBronfiles.setModel(mod);
                lstBronfiles.setVisible(true);
                txtBron.setVisible(true);
            }
        });

        btnSelecteerDoel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String args[] = {txtDoel.getText()};
                SelecteerDoel.main(args);
                txtDoel.setText(doelDir);

                String files[] = null;
                FilenameFilter filter = new FotoFilter();
                files = leesBestanden(txtDoel.getText(), filter);
                for (int i=0; i<files.length; i++){
                    System.out.print(files[i] + "\n");
                }

                max = bepaalHoogste(files);
                Object keys[] = max.keySet().toArray();
                String items[] = new String[keys.length];
//        for (Object key : keys) {
                for (int i=0; i<keys.length; i++) {
                    int nieuw = (Integer) max.get(keys[i].toString()) + 1;
                    String tekst = String.format("%5s -> %04d",keys[i].toString(), nieuw);
                    items[i] = tekst;
                    //            System.out.printf("Hoogste: %s -> %s  eerste vrije: %04d\n", key.toString(), max.get(key.toString()), nieuw);
                }
                lstHoogste.setListData(items);
            }
        });

        btnUitvoeren.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String cmds[] = kopieerBestanden(txtBron.getText(), txtDoel.getText());
                // nu nog in textarea, later zijn dit de copy-commando's
                if (cmds.length>0) {
                    JFrame frame = new JFrame("FotoCopyToonActies");
                    frame.setContentPane(new FotoCopyToonActies().mainPanel);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setVisible(true);

                    FotoCopyToonActies acties = new FotoCopyToonActies();
                    acties.clearTxtLogtekst();
                    String tekst = new String();
                    for (String cmd: cmds) {
                        if (cmd != null) {
                            tekst += cmd;
                            acties.setTxtLogtekst(cmd);
                            frame.repaint();
                        }
                    }
                    txtStatusTekst.setText(tekst);
                } else {
                    txtStatusTekst.setText("Tekst is leeg!");
                }

            }
        });
    }



    /*
 * Lees alle bestanden die met het filter overeenkomen uit doelDir
 * @param doelDir directory waarheen de bestanden geschreven worden
 * @param filter filenaam filter
 * @return array met bestandsnamen
 */
    public static String[] leesBestanden(String doelDir, FilenameFilter filter) {
        File dir = new File(doelDir);

        String files[] = dir.list(filter);
        Arrays.sort(files);

        return files;

    }


    /*
     * Bepaal het hoogste nummer van de bestanden S
     * @param files[] lijst met bestanden
     * @return hashmap met type en hoogste nummer
     */
    public static Map bepaalHoogste(String[] files) {

        //
        Pattern p = Pattern.compile("(\\D+)(\\d+)(\\D+)?\\.(.+)");

        Map max = new HashMap();
        for (int i = 0; i < files.length; i++) {
            Matcher m = p.matcher(files[i].toLowerCase());

            boolean found = m.find();
            if (found) {
                // filetype niet nodig, want nummering loopt toch door (vnl. jpg/avi)
//                String type = m.group(1) + "." + m.group(4);
                String type = m.group(1);
                int n = Integer.valueOf(m.group(2));
                if (!max.containsKey(type)) {
                    max.put(type, n);
                } else {
                    if (n > (Integer) max.get(type)) {
                        max.put(type, n);
                    }
                }
            }
        }
        return max;

    }

    /*
     * Voorbereiden van de kopieercommado's
     * @param bronDir brondirectory
     * @param doelDir doeldirectory
     * @return cmds[] uit te voeren commando's
     */
    public static String[] kopieerBestanden(String bronDir, String doelDir) {
        // lees te kopieren bestanden in
        String files[] = bronFiles;
        String cmds[] = new String[files.length];
        StringBuffer tekst = new StringBuffer();
        // bestandsnaam pattern (nog aanpassen voor namen met alleen cijfers!!)
        Pattern p = Pattern.compile("(\\D+)(\\d+)(\\D+)?\\.(.+)");
        for (int i=0; i<files.length; i++) {
            if (files[i] != null) {
                Matcher m = p.matcher(files[i].toLowerCase());
                boolean found = m.find();
                if (found) {
                    String type = m.group(1);
                    String cmd = new String();
                    if (max.containsKey(type)) {        // bestandsnaam komt ook in doel voor
                        int nieuw = (Integer) max.get(type) + 1;
                        String format = "%0" + String.format("%d", m.group(2).length()) + "d";
                        String newfile = files[i].replaceAll("(\\d+)", String.format(format, nieuw));
                        if (newfile.length()>0 ) {
                            max.put(type, nieuw);
                            cmd = String.format("%s %s/%s %s/%s\n", COPYCMD, bronDir, files[i], doelDir, newfile);
                            runCmd(cmd);
                        }
                    } else {                            // bestandsnaam komt niet in doel voor
                        cmd = String.format("%s %s/%s %s/%s\n", COPYCMD, bronDir, files[i], doelDir, files[i]);
                        runCmd(cmd);
                    }
                    cmds[i] = cmd;
                }
            }
        }

        return cmds;
    }


    /**
     * @return the doelDir test
     */
    public static String getDoelDir() {
        return doelDir;
    }

    /**
     * @param aDoelDir the doelDir to set
     */
    public static void setDoelDir(String aDoelDir) {
        doelDir = aDoelDir;
    }

    public static void setBronFiles(String files[]) {
        bronFiles = files;
    }

    public static String[] getBronFiles() {
        return bronFiles;
    }

    /**
     * @return the max
     */
    public static Map getMax() {
        return max;
    }

    /**
     * @param aMax the max to set
     */
    public static void setMax(Map aMax) {
        max = aMax;
    }

    /**
     * @return the bronDir
     */
    public static String getBronDir() {
        return bronDir;
    }

    /**
     * @param aBronDir the bronDir to set
     */
    public static void setBronDir(String aBronDir) {
        bronDir = aBronDir;
    }

    private static void runCmd(String cmd)
    {
        try {
            Runtime run = Runtime.getRuntime() ;
            Process pr = run.exec(cmd) ;
            pr.waitFor() ;
            BufferedReader buf = new BufferedReader( new InputStreamReader( pr.getInputStream() ) ) ;

            String line;
            while ( ( line = buf.readLine() ) != null )
            {
                System.out.println(line) ;
            }

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

class FotoFilter implements FilenameFilter {

    public boolean accept(File dir, String name) {
        return (name.toLowerCase().endsWith(".jpg")
                || name.toLowerCase().endsWith(".bmp")
                || name.toLowerCase().endsWith(".avi"));
    }
}

