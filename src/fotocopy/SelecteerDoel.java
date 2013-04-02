/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fotocopy;

import javax.swing.*;

/**
 *
 * @author chris
 */
public class SelecteerDoel {
  public static void main(String[] args) {
    String home;
    if (args[0] == "") {
            home = ".";
    } else {
            home = args[0];
    }

    JFileChooser fileopen = new JFileChooser(home);
    fileopen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//    FileFilter filter = new FileNameExtensionFilter("c files", "c");
//    fileopen.addChoosableFileFilter(filter);

    int ret = fileopen.showDialog(null, "Open file");

    if (ret == JFileChooser.APPROVE_OPTION) {
      FotoCopyView.setDoelDir(fileopen.getSelectedFile().getPath());
    } else if (ret == JFileChooser.CANCEL_OPTION) {
      JOptionPane.showMessageDialog(fileopen, "Actie afgebroken");
    }
  }
}


