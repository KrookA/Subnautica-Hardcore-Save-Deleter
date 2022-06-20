import java.io.*;

public final class AppdataHandler {

    private static String appdata_path;
    private static String persistent_file_path;

    /**
     * Initiates the AppdataHandler at the beginning of the process.
     */
    public static void Init() {
        appdata_path = System.getenv("AppData"); //points to the "roaming" folder
        persistent_file_path = appdata_path + "/KrookA/Hardcore Save Deleter/subnauticapath.txt";
    }

    /**
     * Verifies that the appdata structure is correct, if not it creates it
     * @return true if the operation was successful and it is safe to continue, else false
     */
    public static boolean VerifyState() {
        File application_folder = new File(appdata_path + "/KrookA/Hardcore Save Deleter");
        File settings_file = new File(persistent_file_path);
        System.out.println("Settings file path: " + settings_file.toURI());
        try {
            if (!application_folder.exists()) { //create it
                if (!application_folder.mkdirs()) { //failed
                    return false;
                }
            }
            if (!settings_file.exists()) { //create it
                if (!settings_file.createNewFile()) { //failed
                    return false;
                }
            }
            return true; //structure successfully validated
        }
        catch (IOException e) {
            return false;
        }
    }

    /**
     * Gets the path held in the appdata folder
     * @return the path held in the appdata folder
     */
    public static String getPersistentPath() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(appdata_path + "/KrookA/Hardcore Save Deleter/subnauticapath.txt"));
            return reader.readLine();
        }
        catch (IOException e) {
            return null;
        }
    }

    /**
     * Changes the path held in the appdata folder
     * @param newURI The new path to be saved
     * @return true if the operation was successful, else false
     */
    public static boolean setPersistentPath(String newURI) {
        try {
            FileWriter writer = new FileWriter(persistent_file_path);
            writer.write(newURI);
            writer.close();
            return true;
        }
        catch (IOException e) {
            System.out.println(e.toString());
            return false;
        }
    }
}
