package addon;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Klasa wyświetlająca okno umożliwiające wybór pliku tekstowego.
 *
 * @author Tobiasz Rumian
 */
public class FileChooser extends JFrame {

	private String path = null;

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
	 *
	 * @return Wybrana ścieżka
	 */
	public String getPath() {
		return path;
	}
}
