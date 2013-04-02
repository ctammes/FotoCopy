/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fotocopy;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 *
 * @author chris
 */
public class SelecteerBronfiles {

    public static void main(String[] args) {
        String home;
        if (args[0] == "") {
                home = ".";
        } else {
                home = args[0];
        }

        JFileChooser fileopen = new JFileChooser(home);

        fileopen.setMultiSelectionEnabled(true);

        FileFilter filter1 = new FileNameExtensionFilter("jpg files", "jpg");
        fileopen.addChoosableFileFilter(filter1);
        FileFilter filter2 = new FileNameExtensionFilter("bmp files", "bmp");
        fileopen.addChoosableFileFilter(filter2);
        FileFilter filter3 = new FileNameExtensionFilter("mov files", "mov");
        fileopen.addChoosableFileFilter(filter3);
        FileFilter filter4 = new FileNameExtensionFilter("alle plaatjes", "jpg", "bmp", "mov");
        fileopen.addChoosableFileFilter(filter4);

        int ret = fileopen.showDialog(null, "Open file");

        if (ret == JFileChooser.APPROVE_OPTION) {
            File[] files  = fileopen.getSelectedFiles();
            String bronFiles[] = new String[files.length];
            for(int i = 0; i < files.length; i++) {
                bronFiles[i] = files[i].getName();
            }
            FotoCopyView.setBronFiles(bronFiles);
            FotoCopyView.setBronDir(fileopen.getCurrentDirectory().getPath());
        } else if (ret == JFileChooser.CANCEL_OPTION) {
          JOptionPane.showMessageDialog(fileopen, "Actie afgebroken");
        }

    }

}