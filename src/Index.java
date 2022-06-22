import java.awt.*;
import java.io.File;

public final class Index {

    private static Frame GUI;
    private static Cleaner cleaner;
    public static String OS;

    /**
     * App entrypoint
     * @param args
     */
    public static void main(String[] args) {
        AppdataHandler.Init(); //initialises the appdata handler
        Start();
    }

    /**
     * Initiates the main part of the application
     */
    public static void Start() {
        if (AppdataHandler.VerifyState()) { //the correct appdata structure is correct from this point on
            //create the gui
            GUI = new Frame();
            GUI.Populate();
            GUI.Show(true);
            GUI.InitializeState();

        }
        else {
            System.out.println("Appdata state could not be verified");
            System.exit(0);
        }
    }

    /**
     * Checks if the given URI is a valid subnautica instance folder
     * @param test_uri the path to the "Subnautica" folder
     * @param selected_by_user true if selected by user, false if read from appdata
     * @return string path to the savegame folder if valid, else null
     */
    public static String VerifySubnauticaDirectory(String test_uri, boolean selected_by_user) {
        if (test_uri != null) {
            //should be selected by user
            if (selected_by_user) {
                File proposed_savegame_folder_steam = new File(test_uri + "/SNAppData/SavedGames");
                File proposed_savegame_folder_epic = new File(test_uri + "/Subnautica/SavedGames");
                File proposed_savegame_folder_epic_bz = new File(test_uri + "/SubnauticaZero/SavedGames");

                if (proposed_savegame_folder_steam.exists()) { //steam installation
                    return test_uri + "\\SNAppData\\SavedGames";
                }
                else if (proposed_savegame_folder_epic.exists()) { //epic installation
                    return test_uri + "\\Subnautica\\SavedGames";
                }
                else if (proposed_savegame_folder_epic_bz.exists()) { //epic bz installation
                    return test_uri + "\\SubnauticaZero\\SavedGames";
                }
                else { //invalid subnautica directory
                    return null;
                }
            }
            else { //read from appdata
                File appdata_savegame_folder = new File(test_uri);
                if (appdata_savegame_folder.exists()) {
                    return test_uri;
                }
                else {
                    return null;
                }
            }
        }
        else {
            return null;
        }
    }

    /**
     * Starts the cleaner process on the current appdata defined subnautica instance
     */
    public static void InitCleanerProcess() {
        if (cleaner == null) {
            cleaner = new Cleaner(AppdataHandler.getPersistentPath());
            cleaner.Start();
        }
        else {
            System.out.println("Tried to create a new cleaner process but one was already running!");
        }
    }

    /**
     * Stops the running cleaner process
     */
    public static void StopCleanerProcess() {
        if (cleaner != null) {
            cleaner.Stop();
            cleaner = null;
        }
    }

    /**
     * Sends an error message to be displayed by the frame
     * @param msg the error message to be displayed
     */
    public static void SendFrameError(String msg) {
        GUI.UpdateStatus(msg, Color.red);
    }

    /**
     * Sends an action message to be displayed by the frame
     * @param msg the action message to be displayed
     * @param color the color of text of the message
     */
    public static void SendActionMessage(String msg, Color color) { GUI.UpdateLastAction(msg, color); }
}
