package download.UI;

import download.Logic.Main;

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

public class Window extends JFrame{
        private final JPanel panel;
        private final JLabel label;
        private final JTextField linkTextField;
        private final JButton downloadButton;
        private final JButton shareButton;
        private final JButton clearButton;
        private final JButton pasteButton;
        private final JRadioButton mp3;
        private final JRadioButton mp4;
        private final ButtonGroup formatButtons;
        private final JProgressBar progressBar;

        private final Main main;

        public Window()
        {
                main = new Main(this);

                this.setTitle("YoutubeToMp3");
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                this.setSize(640, 200);

                //set position of window
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                int w = this.getSize().width;
                int h = this.getSize().height;
                int x = (dim.width - w) / 2;
                int y = (dim.height - h) / 2;
                this.setLocation(x, y);

                panel = new JPanel(new BorderLayout());
                label = new JLabel("Enter Link");
                linkTextField = new JTextField(50);
                downloadButton = new JButton("Download");
                shareButton = new JButton("Share");
                pasteButton = new JButton("Paste");
                clearButton = new JButton("Clear");
                mp3 = new JRadioButton("Mp3", true);
                mp4 = new JRadioButton("Mp4", false);
                formatButtons = new ButtonGroup();
                progressBar = new JProgressBar(0, 100);

                downloadButton.setBounds(50, 50, 100, 20);
                formatButtons.add(mp3);
                formatButtons.add(mp4);
                progressBar.setValue(0);
                progressBar.setStringPainted(true);

                downloadButton.setActionCommand("download");
                shareButton.setActionCommand("share");
                pasteButton.setActionCommand("paste");
                clearButton.setActionCommand("clear");
                mp3.setActionCommand("mp3");
                mp4.setActionCommand("mp4");

                downloadButton.addActionListener(main);
                shareButton.addActionListener(main);
                pasteButton.addActionListener(main);
                clearButton.addActionListener(main);

                setLayout();
        }

        public String getSelectedFormat()
        {
                return formatButtons.getSelection().getActionCommand();
        }

        public String getTextFieldString()
        {
                return linkTextField.getText();
        }

        public void displayErrorMessage(String error)
        {
                JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
        }

        public void setLinkTextField(String text)
        {
                linkTextField.setText(text);
        }

        public void doDownload()
        {
                downloadButton.doClick();
        }

        private void setLayout()
        {
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
                GridBagLayout gridLayout = new GridBagLayout();
                GridBagConstraints gbc = new GridBagConstraints();
                JPanel southsouthStuff = new JPanel();
                southStuff.setLayout(gridLayout);
                southsouthStuff.add(shareButton, BorderLayout.EAST);
                southsouthStuff.add(downloadButton, BorderLayout.WEST);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.gridx = 0; gbc.gridy = 0; southStuff.add(progressBar, gbc);
                gbc.gridx = 0; gbc.gridy = 1; southStuff.add(southsouthStuff, gbc);

                //add everything to main panel
                panel.add(titelRow, BorderLayout.NORTH);
                panel.add(textFieldRow);
                panel.add(southStuff, BorderLayout.SOUTH);

                panel.setPreferredSize(new Dimension(480, 360));
                panel.setMaximumSize(panel.getPreferredSize());
                panel.setMinimumSize(panel.getPreferredSize());

                this.getContentPane().add(panel);
                this.setVisible(true);
        }
}
