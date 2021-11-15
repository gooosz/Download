import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

//Exception handling
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.awt.datatransfer.UnsupportedFlavorException;

//Copy and Paste
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import java.util.HashMap;

public class Window
{
	private JFrame frame;
	private JPanel panel;
	private JLabel label;
	private JTextField linkTextField;
	private JButton downloadButton;
	private JButton clearButton;
	private JButton pasteButton;
	private JRadioButton mp3;
	private JRadioButton mp4;
	private ButtonGroup formatButtons;
	private String format;
	//Store all those flags in a Map
	private boolean mp3Clicked = true;
	private boolean mp4Clicked = false;
	private HashMap<String, JRadioButton> collectionOfFormats;

	/*
	 * first download file
	 * share file to connected device
	 * using shareKdeConnect.sh script
	 */
	private JButton shareButton;

	private String pwd = null;

	//store previous downloaded file name
	private String prevDownloaded;

	public Window()
	{
		frame = new JFrame("YoutubeToMp3");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(640, 200);

		//set position of window
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = frame.getSize().width;
		int h = frame.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		frame.setLocation(x, y);


		panel = new JPanel(new BorderLayout());
		label = new JLabel("Enter Link");

		//Textfield accepts upto 50 characters, inputLink length
		linkTextField = new JTextField(50);
		linkTextField.setBounds(270, 400, 50, 10);

		//Download after clicking downloadButton
		downloadButton = new JButton("Download");
		downloadButton.setBounds(50, 50, 100, 20);

		//clear the Textfield
		clearButton = new JButton("Clear");

		//paste system Clipboard into TextField
		pasteButton = new JButton("Paste");
		shareButton = new JButton("Share");

		//Checkbox if download mp3 or mp4
		mp3 = new JRadioButton("Mp3", true);
		mp4 = new JRadioButton("Mp4", false);
		formatButtons = new ButtonGroup();

		formatButtons.add(mp3);
		formatButtons.add(mp4);

		collectionOfFormats = new HashMap<>();
		collectionOfFormats.put("mp3", mp3);
		collectionOfFormats.put("mp4", mp4);

		/*mp3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					//Checkbox mp3 is checked
					mp3Clicked = true;
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					mp3Clicked = false;
				}
			}
		});
		mp4.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					//Checkbox mp4 is checked
					mp4Clicked = true;
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					mp4Clicked = false;
				}
			}
		});
		*/
		//if both are pressed make error message "Only one format can be chosen!"



		//add actionListener for important buttons
		downloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//check the format to download
				checkFormat();

				String link = linkTextField.getText();

				if (pwd == null || !pwd.endsWith("/src/download.sh"))
				{
					//get pwd
					ProcessBuilder pb = new ProcessBuilder("pwd");

					try
					{
						Process process = pb.start();
						BufferedReader br = new BufferedReader(
										new InputStreamReader(
											process.getInputStream()
										));
						StringBuilder builder = new StringBuilder();
						String line = null;
						while ((line = br.readLine()) != null)
						{
							builder.append(line);
						}

						pwd = builder.toString();
						pwd += "/src/download.sh";
					}
					catch (IOException ev)
					{
						ev.printStackTrace();
					}
				}

				//run download script
				System.out.println("format: " + format);
				ProcessBuilder pb = new ProcessBuilder(pwd, link, format);

				try {
					Process process = pb.start();

					//store output in string prevDownloaded
					BufferedReader reader = new BufferedReader(
									new InputStreamReader(
										process.getInputStream()
									));
					String filename = "";
					while ((prevDownloaded = reader.readLine()) != null)
					{
						/*
						 * get only the last line
						 * aka loop through the output
						 * until there is no more output
						 */
						 System.out.println(prevDownloaded);
						 filename = prevDownloaded;
						 /*
						  * TODO implement progressbar read data here,
						  * data is in filename just fetch the % from that
						  */
					}
					prevDownloaded = filename;
					prevDownloaded += ".";
					prevDownloaded += format;
					System.out.println("Filename: " + prevDownloaded);
				}
				catch (IOException ev) {
					ev.printStackTrace();
				}
			}
		});

		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				linkTextField.setText("");
			}
		});

		pasteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				Transferable content = clipboard.getContents(null);

				String pastedString = "";

				try
				{
					pastedString = (String) content.getTransferData(DataFlavor.stringFlavor);
					linkTextField.setText(pastedString);
				}
				catch (UnsupportedFlavorException | IOException ev)
				{
					ev.printStackTrace();
				}
			}
		});

		shareButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//download the song first
				downloadButton.doClick();

				//send to connected device using shareKdeConnect.sh
				if (pwd.endsWith("/src/download.sh"))
				{
					/*
					 * 16 is the length of the string that has to change
					 * to shareKdeConnect.sh
					 */
					pwd = pwd.substring(0, pwd.length()-16);
				}
				pwd += "/src/kdesend.sh";

				ProcessBuilder pb = new ProcessBuilder(pwd, prevDownloaded);

				try
				{
					Process process = pb.start();

					//print output of process
					BufferedReader br = new BufferedReader(
									new InputStreamReader(
										process.getInputStream()
									));
					String line = null;
					while ((line = br.readLine()) != null)
					{
						System.out.println(line);
					}

				}
				catch (IOException ev)
				{
					ev.printStackTrace();
				}

				//TODO: remove songs, so that they will be only saved on mobile
			}
		});

		//Layout
		JPanel titelRow = new JPanel();
		titelRow.add(label);

		JPanel textFieldRow = new JPanel();
		JPanel northRow = new JPanel();
		northRow.add(mp3, BorderLayout.EAST);
		northRow.add(mp4, BorderLayout.WEST);
		textFieldRow.add(northRow, BorderLayout.NORTH);
		textFieldRow.add(linkTextField, BorderLayout.EAST);
		textFieldRow.add(clearButton, BorderLayout.WEST);
		textFieldRow.add(pasteButton, BorderLayout.SOUTH);

		JPanel southStuff = new JPanel();
		southStuff.add(shareButton, BorderLayout.EAST);
		southStuff.add(downloadButton, BorderLayout.WEST);

		//add everything to JPanel
		panel.add(titelRow, BorderLayout.NORTH);
		panel.add(textFieldRow);
		panel.add(southStuff, BorderLayout.SOUTH);

		panel.setPreferredSize(new Dimension(480, 360));
		panel.setMaximumSize(panel.getPreferredSize());
		panel.setMinimumSize(panel.getPreferredSize());


		//add Components to JFrame
		frame.getContentPane().add(panel);

		frame.setVisible(true);
	}

	private String checkIfBothCheckBoxesClicked() {
		/*
		 * check which JRadioButton is checked
		 * or none at all
		 */
		for (String key: collectionOfFormats.keySet()) {
			JRadioButton temp = collectionOfFormats.get(key);
			if (temp.isSelected()) {
				return key;
			}
		}

		return null;
	}

	private void checkFormat() {
		String temp = checkIfBothCheckBoxesClicked();

		switch (temp) {
			case "mp3" -> format = temp;
			case "mp4" -> format = temp;
			default -> JOptionPane.showMessageDialog(frame, "Choose one of the given formats!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
