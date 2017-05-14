package addon;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 * Klasa wyświetlająca okno umożliwiające wybór pliku tekstowego.
 * @author Tobiasz Rumian
 */
public class FileChooser extends JFrame {
    private String path=null;

    public FileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Pliki tekstowe", "txt"));
        chooser.setCurrentDirectory(new File("."));
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            this.path = chooser.getSelectedFile().getPath();
            this.toFront();
            this.repaint();
            pack();
        }
    }

    /**
     * Zwraca wybraną przez użytkownika ścieżkę
     * @return Wybrana ścieżka
     */
    public String getPath() {
        return path;
    }
}
