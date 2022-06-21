import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Frame extends JFrame {
    private Container content_pane;
    private JTextField url_display;
    private JLabel status_display;
    private JLabel action_display;

    public Frame(String initial_persistent_uri) {
        super("Hardcore Save Deleter");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        content_pane = getContentPane();

        addWindowListener(new WindowAdapter() { //overrides default form closing to terminate cleaner process if it is running.
            @Override
            public void windowClosing(WindowEvent e) {
                Index.StopCleanerProcess();
                System.exit(0);
            }
        });
    }


    /**
     * Sets the display to visible or not
     * @param doesShow should the display be visible
     */
    public void Show(boolean doesShow) {
        setVisible(doesShow);
    }

    /**
     * Populates the frame with its contents
     */
    public void Populate() {
        //The top panel
        JPanel top_panel = new JPanel(new BorderLayout());
        //The center panel
        JPanel center_panel = new JPanel((new BorderLayout()));
        //The bottom panel
        JPanel bottom_panel = new JPanel((new BorderLayout()));
        bottom_panel.setBackground(Color.white);
        bottom_panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));

        //URL display text box
        url_display = new JTextField();
        url_display.setEditable(false);
        url_display.setBackground(Color.white);
        url_display.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));

        //Open file dialog button
        JButton open_dialog_btn = new JButton();
        open_dialog_btn.setText("Open");
        open_dialog_btn.addActionListener(e -> { //open dialog button clicked
            String chosen_uri = Show_Folder_Dialog();
            if (Index.VerifySubnauticaDirectory(chosen_uri)) { //valid subnautica directory selected
                ChangeDisplayedURI(chosen_uri);
                AppdataHandler.setPersistentPath(chosen_uri);
                UpdateStatus("Running :)", Color.green);
                Index.StopCleanerProcess();
                Index.InitCleanerProcess();
            }
            else if (chosen_uri != null) { //directory selected was not a valid subnautica directory
                UpdateStatus("Invalid Subnautica directory selected :(", Color.red);
                Index.StopCleanerProcess();
            }
        });

        top_panel.add(url_display, BorderLayout.CENTER);
        top_panel.add(open_dialog_btn, BorderLayout.LINE_END);

        //status display
        status_display = new JLabel();
        status_display.setHorizontalAlignment(SwingConstants.CENTER);

        center_panel.add(status_display, BorderLayout.CENTER);

        //action display
        action_display = new JLabel();
        action_display.setText(" ");

        bottom_panel.add(action_display);

        content_pane.add(top_panel, BorderLayout.PAGE_START);
        content_pane.add(center_panel, BorderLayout.CENTER);
        content_pane.add(bottom_panel, BorderLayout.PAGE_END);
    }

    /**
     * Shows the "open folder dialog"
     * @return the path to the folder chosen
     */
    public String Show_Folder_Dialog() {
        JFileChooser file_chooser = new JFileChooser();
        file_chooser.setCurrentDirectory(new java.io.File(System.getProperty("user.home")));
        file_chooser.setDialogTitle("Select Subnautica Directory");
        file_chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        file_chooser.setAcceptAllFileFilterUsed(false);
        if (file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            System.out.println("Directory chosen: " + file_chooser.getSelectedFile());
            return file_chooser.getSelectedFile().toString();
        }

        return null;
    }

    /**
     * Updates the directory display box
     * @param new_text the new text to display in the box
     */
    public void ChangeDisplayedURI(String new_text) {
        url_display.setText(new_text);
    }

    /**
     * Updates the status display of the app
     * @param status_text the new text to display
     * @param text_color the color of the text to display
     */
    public void UpdateStatus(String status_text, Color text_color) {
        status_display.setText("<html>" + status_text + "</html>");
        status_display.setForeground(text_color);
    }

    /**
     * Updates the text displayed at the bottom of the app for the last action executed by the process
     * @param action_text the text to display
     * @param text_color the color of the text to display
     */
    public void UpdateLastAction(String action_text, Color text_color) {
        action_display.setText(action_text);
        action_display.setForeground(text_color);
    }

    public void InitializeState() {
        //set initial action message
        UpdateLastAction("Waiting...", Color.gray);
        //check if initial subnautica path is fine
        if (Index.VerifySubnauticaDirectory(AppdataHandler.getPersistentPath())) {
            ChangeDisplayedURI(AppdataHandler.getPersistentPath());
            UpdateStatus("Running :)", Color.green);
            Index.InitCleanerProcess();
        }
        else {
            ChangeDisplayedURI("");
            UpdateStatus("Select the directory that contains \"Subnautica.exe\" or \"SubnauticaZero.exe\"", Color.black);
        }
    }
}
