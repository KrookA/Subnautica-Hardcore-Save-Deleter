import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HelpFrame extends JFrame {

    private Container content_pane;
    private Frame parent;

    /**
     * Help window that pops up when you click "help"
     */
    public HelpFrame(Frame aParent) {
        super("Hardcore Save Deleter - Help");
        parent = aParent;
        setSize(600, 900);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        content_pane = getContentPane();

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                parent.HelpWindowHasClosed();
            }
        });
    }

    /**
     * Whether to show or hide the frame
     * @param doesShow true is visible, false is hidden
     */
    public void Show(boolean doesShow) {
        setVisible(doesShow);
        Populate();
    }

    /**
     * Populates the frame with the text contents
     */
    private void Populate() {
        JPanel main_content_panel = new JPanel(new BorderLayout());

        JTextPane main_content = new JTextPane();
        main_content.setContentType("text/html");
        main_content.setEditable(false);
        String main_content_html = "<html>" +
                "<h1 style='font-family: Verdana; text-align: center; text-decoration: underlined'>How To Use:</h1>" +
                "<p style='font-family: sans-serif'>Press the 'Open' button in the top right, this opens a folder select dialog. You want to select a folder called 'Subnautica', which annoyingly is located in a different location depending on which version of the game you have installed (Steam or Epic Games). The following instructions are for Windows users:</p>" +
                "<p style='font-family: sans-serif'>If you have the Steam edition, this folder is located in the directory where the game is installed (most likely C:/Program Files (or Program Files (x86))/Steam/steamapps/common/Subnautica)</p>" +
                "<p style='font-family: sans-serif'>If you have the Epic Games edition, this folder is almost certainly in your AppData folder (AppData/LocalLow/Unknown Worlds/Subnautica)</p>" +
                "<p style='font-family: sans-serif'>If you are a Mac user, this folder will be somewhere else (don't worry it does exist). This app should be compatible with Mac devices, so if you need help finding this folder then ask one of the friendly people in the speedrunning discord (<a href='https://discord.gg/VCySSTfaAM'>https://discord.gg/VCySSTfaAM</a>) and I'm sure they'd be happy to help with anything.</p>" +
                "<p style='font-family: sans-serif'>Once you select a valid folder you'll know it worked because the main display will show 'Running :)', the app will continue to work as long as it is open. The app stops working once you close it down. When you reopen the app the savefile directory should be remembered, so you probably won't have to set it ever again. You will still have to manually delete one leftover savefile at the beginning of every session.</p>" +
                "<h2 style='font-family: Verdana;'>FAQs:</h2>" +
                "<ul>" +
                "<li style='font-family: sans-serif'><b>I'm not a speedrunner, do I need this app?</b> - No.</li>" +
                "<li style='font-family: sans-serif'><b>Is this app required to speedrun a hardcore category</b> - No, its for convenience only.</li>" +
                "<li style='font-family: sans-serif'><b>Will this delete my casual saves?</b> - No, any folders that exist before running the app are preserved.</li>" +
                "<li style='font-family: sans-serif'><b>Does this work with Below Zero?</b> - Yes, just select the equivalent 'Subnautica Below Zero' (Epic Games) or 'SubnauticaZero' (Steam) folder.</li>" +
                "<li style='font-family: sans-serif'><b>I need other help, how can I reach you?</b> - I'm usually in the official speedrunning discord, but if you can't reach me there you can join my personal discord and ask anything you want, it's a pretty chill place to hang out (plenty of memes) - <a href=''>https://discord.gg/V4R9rjGuSW</a></li>" +
                "</ul>" +
                "<br />" +
                "<br />" +
                "<br />" +
                "<br />" +
                "<p style='text-align: right; font-family: sans-serif; color: gray'><i><u>App made by KrookA</u></i></p>" +
                "</html>";
        main_content.setText(main_content_html);
        main_content_panel.add(main_content, BorderLayout.CENTER);


        content_pane.add(main_content_panel);
    }


}
