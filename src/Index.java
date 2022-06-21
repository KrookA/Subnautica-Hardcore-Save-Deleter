import java.awt.*;
import java.io.File;

public class Index {

    private static Frame GUI;
    private static Cleaner cleaner;

    public static void main(String[] args) {
        AppdataHandler.Init(); //initialises the appdata handler
        Start();
    }

    public static void Start() {
        if (AppdataHandler.VerifyState()) { //the correct appdata structure is correct from this point on
            //create the gui
            GUI = new Frame(AppdataHandler.getPersistentPath());
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
     * @param test_uri the path to the folder that contains Subnautica.exe
     * @return true if the folder is a valid subnautica instance, else false
     */
    public static boolean VerifySubnauticaDirectory(String test_uri) {
        File proposed_savegame_folder = new File(test_uri + "/SNAppData/SavedGames");
        if (proposed_savegame_folder.exists()) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Starts the cleaner process on the current appdata defined subnautica instance
     */
    public static void InitCleanerProcess() {
        if (cleaner == null) {
            cleaner = new Cleaner(AppdataHandler.getPersistentPath() + "\\SNAppData\\SavedGames");
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
