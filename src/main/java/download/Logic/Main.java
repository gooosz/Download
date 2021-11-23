package download.Logic;

import download.UI.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import java.util.concurrent.CyclicBarrier;

public class Main implements ActionListener {
        private final Window ui;

        private String pwd = null;
        private String prevDownloaded = null;

        public Main(Window window)
        {
                ui = window;
        }

        @Override
        public void actionPerformed(ActionEvent ev)
        {
                String actionCommand = ev.getActionCommand();
                switch (actionCommand)
                {
                        case "download" -> {
                                String format = getFormat();
                                String link = ui.getTextFieldString();

                                if (pwd == null || !pwd.endsWith("/src/main/java/download/Scripts/download.sh")) {
                                        //get pwd
                                        pwd = getPWD("/src/main/java/download/Scripts/download.sh");
                                        if (pwd == null) {
                                                ui.displayErrorMessage("Couldn't get PWD!");
                                        }
                                        //run download script
                                        ProcessBuilder pb = new ProcessBuilder(pwd, link, format);
                                        try {
                                                Process process = pb.start();
                                                //store output in string prevDownloaded
                                                BufferedReader reader = new BufferedReader(
                                                                new InputStreamReader(
                                                                        process.getInputStream())
                                                );
                                                String filename = "";
                                                while ((prevDownloaded = reader.readLine()) != null) {
                                                        /*
                                                         * get only the last line
                                                         * aka loop through the output
                                                         * until there is no more output
                                                         */
                                                        filename = prevDownloaded;
                                                        /*
                                                         * TODO implement progressbar read data here,
                                                         * data is in filename just fetch the % from that
                                                         */
                                                        //parse the output stored in filename for %
                                                        String parsed = filename;
                                                        int startIndex = 11;
                                                        int endIndex = parsed.indexOf("%");
                                                        if (parsed.length() <= startIndex || endIndex == -1 ||
                                                                parsed.charAt(endIndex-2) != '.') {
                                                                continue;
                                                        }
                                                        while (parsed.charAt(startIndex) ==  ' ') {
                                                                startIndex++;
                                                        }
                                                        parsed = parseString(parsed, startIndex, endIndex-2);
                                                        int valueOfProgressBar = Integer.parseInt(parsed);
                                                        //[download]   3.9% of 2.81MiB at 46.53KiB/s ETA 00:59
                                                        //[download]  13.9% of 2.81MiB at 46.53KiB/s ETA 00:59
                                                        //set value of progressBar to valueOfProgressBar
                                                        //TODO update the progressBar with valueOfProgressBar
                                                        System.out.println(filename);
                                                        System.out.println(valueOfProgressBar);
                                                }
                                                prevDownloaded = filename + "." + format;
                                                System.out.println("Filename: " + prevDownloaded);

                                        } catch (IOException e) {
                                                e.printStackTrace();
                                        }

                                }
                        }
                        case "share" -> {
                                ui.doDownload();
                                if (!pwd.endsWith("src/main/java/download/Scripts/share.sh")) {
                                        pwd = getPWD("/src/main/java/download/Scripts/share.sh");
                                }
                                ProcessBuilder pb = new ProcessBuilder(pwd, prevDownloaded);
                                try {
                                        Process process = pb.start();
                                        //print output to screen
                                        BufferedReader reader = new BufferedReader(
                                                        new InputStreamReader(
                                                                process.getInputStream())
                                        );
                                        String line = null;
                                        while ((line = reader.readLine()) != null) {
                                                System.out.println(line);
                                        }
                                } catch (IOException e) {
                                        e.printStackTrace();
                                }
                                //TODO remove songs, so that they will be only saved on mobile
                        }
                        case "paste" -> {
                                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                                Transferable content = clipboard.getContents(null);
                                String pastedString = "";
                                try {
                                        pastedString = (String)content.getTransferData(DataFlavor.stringFlavor);
                                        ui.setLinkTextField(pastedString);
                                } catch (UnsupportedFlavorException | IOException e) {
                                        e.printStackTrace();
                                }
                        }
                        case "clear" -> {
                                ui.setLinkTextField("");
                        }
                        default -> System.out.println("Error");
                }
        }

        private String parseString(String str, int start, int end)
        {
                if (str.length() <= start || str.length() < end || end < 0) {
                        return null;
                }
                return str.substring(start, end);
        }

        public String getFormat()
        {
                return ui.getSelectedFormat();
        }

        private String getPWD(String script)
        {
                String cwd = System.getProperty("user.dir");
                if (cwd == null) {
                        return null;
                }
                return cwd + script;
        }

        public static void main(String[] argv)
        {
                Main main = new Main(new Window());
        }


}
