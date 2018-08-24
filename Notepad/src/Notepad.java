import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.undo.UndoManager;

import java.awt.Font;

public class Notepad {

	String fileName = "";
	String filePath = "";
	String appName = "Notedpad";

	private JFrame frmNotepad;

	JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Notepad window = new Notepad();
					window.frmNotepad.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Notepad() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmNotepad = new JFrame();
		frmNotepad.setTitle("Notepad");
		frmNotepad.setBounds(100, 100, 640, 480);
		frmNotepad.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmNotepad.getContentPane().setLayout(null);

		/**
		 * Initialize UndoManager class to handle undo redo operation.
		 */
		UndoManager undoRedoManager = new UndoManager();

		/**
		 * Initialize user menu
		 */
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 640, 21);
		frmNotepad.getContentPane().add(menuBar);

		/**
		 * First tree File
		 */
		JMenu menuFile = new JMenu("Plik");
		menuBar.add(menuFile);

		JMenuItem menuItemNew = new JMenuItem("Nowy");
		menuItemNew.setActionCommand("new");
		menuItemNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				newFile();

			}
		});
		menuFile.add(menuItemNew);

		JMenuItem menuItemOpen = new JMenuItem("Otwórz");
		menuItemOpen.setActionCommand("open");
		menuItemOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				openFile();

			}
		});
		menuFile.add(menuItemOpen);

		JMenuItem menuItemSave = new JMenuItem("Zapisz");
		menuItemSave.setActionCommand("save");
		menuItemSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				saveFile(e);
			}
		});
		menuFile.add(menuItemSave);

		JMenuItem menuItemSaveAs = new JMenuItem("Zapisz jako");
		menuItemSaveAs.setActionCommand("saveAs");
		menuItemSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				saveFile(e);
			}
		});
		menuFile.add(menuItemSaveAs);

		JSeparator separator = new JSeparator();
		menuFile.add(separator);

		JMenuItem menuItemClose = new JMenuItem("Zamknij");
		menuItemClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int val = JOptionPane.showConfirmDialog(null, "Czy napewno chcesz wyjść z aplikacji?", "Ostrzeżenie",
						JOptionPane.WARNING_MESSAGE);
				if (val == 0) {
					closeApp();
				}

			}
		});
		menuFile.add(menuItemClose);

		/**
		 * Second tree Edit
		 */

		JMenu menuEdit = new JMenu("Edycja");
		menuBar.add(menuEdit);

		JMenuItem menuItemUndo = new JMenuItem("Cofnij");
		menuItemUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (undoRedoManager.canUndo())
					undoRedoManager.undo();
			}
		});
		menuEdit.add(menuItemUndo);

		JMenuItem menuItemRedo = new JMenuItem("Ponów");
		menuItemRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (undoRedoManager.canRedo())
					undoRedoManager.redo();
			}
		});
		menuEdit.add(menuItemRedo);

		JSeparator separator_1 = new JSeparator();
		menuEdit.add(separator_1);

		JMenuItem menuItemSearch = new JMenuItem("Szukaj");
		menuItemSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputText = JOptionPane.showInputDialog(null, "Wprowadź szukany ciąg", "Szukaj",
						JOptionPane.INFORMATION_MESSAGE);
				searchText(inputText);
			}
		});
		menuEdit.add(menuItemSearch);

		JMenuItem menuItemReplace = new JMenuItem("Zamień");
		menuItemReplace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				replaceText();
			}
		});
		menuEdit.add(menuItemReplace);

		/**
		 * Third tree About
		 */

		JMenu menuAbout = new JMenu("O programie");
		menuBar.add(menuAbout);

		JMenuItem menuItemAboutMe = new JMenuItem("O mnie");
		menuItemAboutMe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aboutMe();
			}
		});
		menuAbout.add(menuItemAboutMe);

		textArea = new JTextArea();
		textArea.setFont(new Font("Serif", Font.PLAIN, 10));
		textArea.setBounds(0, 22, 640, 459);
		textArea.setLineWrap(true);
		textArea.getDocument().addUndoableEditListener(undoRedoManager);
		frmNotepad.getContentPane().add(textArea);

	}

	/**
	 * Add-ons methods.
	 */

	// method to handle create new file
	private void newFile() {
		String text = textArea.getText();

		if (!text.isEmpty()) {
			int var = JOptionPane.showConfirmDialog(null,
					"Czy napewno chcesz otworzyć nowy plik i usunąć bieżącą zawartość?", "Ostrzeżenie",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (var == 0) {
				textArea.setText("");
				fileName = "";
				filePath = "";
				frmNotepad.setTitle(appName);
			}
		}
	}

	// method to handle open new file
	private void openFile() {
		try {

			String text = textArea.getText();

			if (!text.isEmpty()) {
				int var = JOptionPane.showConfirmDialog(null,
						"Czy napewno chcesz otworzyć nowy plik i usunąć bieżącą zawartość?", "Ostrzeżenie",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (var == 0) {
					textArea.setText("");
				} else
					return;
			}

			JFileChooser jFileChooser = new JFileChooser();
			jFileChooser.showOpenDialog(null);
			filePath = jFileChooser.getSelectedFile().getPath();
			fileName = jFileChooser.getSelectedFile().getName();
			frmNotepad.setTitle(appName + " " + fileName);
			File file = new File(filePath);
			BufferedReader buff = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = buff.readLine()) != null) {
				textArea.append(line + "\n");
			}
			buff.close();

		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, "Nie wybrano pliku!", "Ostrzeżenie", JOptionPane.WARNING_MESSAGE);
		}

		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Nie znalezniono wskazanego pliku!", "Ostrzeżenie",
					JOptionPane.WARNING_MESSAGE);
		}

		catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
		}

	}

	// method to handle save and saveAs
	private void saveFile(ActionEvent actionEvent) {

		try {

			String text = textArea.getText();
			System.out.println(actionEvent.getActionCommand());
			System.out.println(filePath);
			if (filePath.equals("") || actionEvent.getActionCommand().equals("saveAs")) {
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.showSaveDialog(null);
				filePath = jFileChooser.getSelectedFile().getPath();
				fileName = jFileChooser.getSelectedFile().getName();
				frmNotepad.setTitle(appName + " " + fileName);
			}
			FileWriter fileWriter = new FileWriter(filePath);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(text);
			bufferedWriter.close();
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, "Nie wskazano pliku do zapisania!", "Ostrzeżenie",
					JOptionPane.WARNING_MESSAGE);

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
		}

	}

	// method to handle close application
	private void closeApp() {
		frmNotepad.dispose();

	}

	// method to handle highligth searching text (param input text)
	private void searchText(String inputText) {

		try {
			String text = textArea.getText();
			int actualIndex = 0;
			int occurIndex = 0;
			int inputLength = inputText.length();
			Highlighter h = textArea.getHighlighter();
			h.removeAllHighlights();
			if (text.equals("") || inputText.equals("")) {
				occurIndex = -1;
			}
			while (occurIndex != -1) {
				occurIndex = text.indexOf(inputText, actualIndex);
				if (occurIndex != -1) {

					h.addHighlight(occurIndex, occurIndex + inputLength, DefaultHighlighter.DefaultPainter);
					actualIndex = occurIndex + inputLength;
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
		}
	}

	// method to handle replace text
	private void replaceText() {
		JTextField targetTextField = new JTextField();
		JTextField replaceTextField = new JTextField();
		Object[] message = { "Tekst:", targetTextField, "Zamień na:", replaceTextField };
		JOptionPane.showConfirmDialog(null, message, "Zamień na: ", JOptionPane.OK_OPTION);
		String targetText = targetTextField.getText();
		String replaceText = replaceTextField.getText();
		String text = textArea.getText();
		String newText = text.replaceAll(targetText, replaceText);
		textArea.setText(newText);

	}

	// method return information about author
	private void aboutMe() {

		String aboutAuthor = "";
		aboutAuthor += "Autor: Adam Nowakowski \n";
		aboutAuthor += "E-mail: adam.nowakowski@hotmail.com \n";
		aboutAuthor += "Adres: Poznań Polska \n";
		aboutAuthor += "Github: https://github.com/adamnPL \n";
		JOptionPane.showMessageDialog(null, aboutAuthor, "O autorze", JOptionPane.INFORMATION_MESSAGE);
	}

}
