package sphynx;

import javax.swing.UIManager;
import sphynx.gui.Editor;

/**
 * @author Israel-ICM
 */
public class MyEditor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        UIManager.put("ScrollBarUI", "sphynx.ui.components.UIScrollBar");
        new Editor().setVisible(true);
    }
}
