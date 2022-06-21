import java.awt.*;
import java.util.*;
import java.io.*;
import java.util.concurrent.*;

public class Cleaner {

    private String folder_uri;
    private ArrayList<String> initial_folder_names;
    private Queue<String> created_saves;
    private ScheduledExecutorService loop;

    /**
     * Process that runs on a specified folder to clear any new folders created since initiation.
     * Also keeps the newest folder created since initialization, so if a 2nd new folder is created the second newest
     * is deleted
     */
    public Cleaner(String savegame_folder_uri) {
        folder_uri = savegame_folder_uri;
        initial_folder_names = new ArrayList<String>();
        created_saves = new LinkedList<String>();
        File savegame_folder_file = new File(folder_uri);
        File[] subdirectories = savegame_folder_file.listFiles();
        for (File subdirectory : subdirectories) {
            initial_folder_names.add(subdirectory.getName());
        }
        System.out.println("Cleaner process found these initial folders: " + initial_folder_names);
    }

    /**
     * Starts the cleaner process
     */
    public void Start() {
        Runnable task = () -> RunCleanTask();
        loop = Executors.newScheduledThreadPool(1);
        loop.scheduleAtFixedRate(task, 5, 5, TimeUnit.SECONDS);
        Index.SendActionMessage("Process started", Color.green);
    }

    /**
     * Terminates the cleaner process
     */
    public void Stop() {
        loop.shutdown();
        Index.SendActionMessage("Process aborted", Color.RED);
    }

    /**
     * Gets called on an interval, performs the task of identifying saves that need to be culled
     */
    private void RunCleanTask() {
        File folder = new File(folder_uri);
        File[] subdirectories = folder.listFiles();
        for (File subdirectory : subdirectories) { //for every folder in the SavedGames folder:
            //Is it one of the original folders?
            if (initial_folder_names.indexOf(subdirectory.getName()) >= 0) { //yes, leave it be
                continue;
            }
            //is it in the created saves queue?
            else if (created_saves.contains(subdirectory.getName())) { //yes, leave it be
                continue;
            }
            else { //must be a new save, add it to the queue
                created_saves.add(subdirectory.getName());
            }
        }

        //is the queue now bigger than 1?
        if (created_saves.size() > 1) { //delete the folder at the front of the queue
            String save_game_name = created_saves.remove();
            System.out.println("Directory to be deleted: "+ folder_uri + "\\" + save_game_name);
            DeleteSaveGame(new File(folder_uri + "\\" + save_game_name));
        }
        System.out.println("Clean task executed");
    }

    /**
     * Deletes a savefile
     * @param save_game_folder a File object of the savegame folder that you wish to delete
     */
    private void DeleteSaveGame(File save_game_folder) {
        //Subnautica structure:
        //      Savefile
        //MiscFiles  CellsCache
        //            MiscFiles
        //
        //BZ structure:
        //      Savefile
        //MiscFiles  CellsCache  .BatchObjects
        //            MiscFiles    MiscFiles

        try {
            if (save_game_folder.exists()) {
                File[] base_level_files = save_game_folder.listFiles();
                //delete first level folder contents
                for (File file_or_folder : base_level_files) {
                    if (file_or_folder.isDirectory()) { //subdirectory
                        File[] sub_level_files = file_or_folder.listFiles();
                        for (File sub_level_file : sub_level_files) { //delete subfolder contents
                            sub_level_file.delete();
                        }
                        //delete subdirectory itself
                        file_or_folder.delete();
                    }
                    else { //base level file
                        file_or_folder.delete();
                    }
                }
                //finally delete the save folder
                save_game_folder.delete();
                Index.SendActionMessage("Deleted save: " + save_game_folder.getName(), Color.RED);
            }
        }
        catch (Exception e) {
            Index.StopCleanerProcess();
            Index.SendFrameError("An error occured while trying to delete a savegame :(");
        }
    }
}
