package zombiecrushsaga.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import properties_manager.PropertiesManager;
import zombiecrushsaga.ZombieCrushSaga.ZombieCrushSagaPropertyType;
import zombiecrushsaga.data.ZombieCrushDataModel;
import zombiecrushsaga.data.ZombieCrushLevelRecord;
import zombiecrushsaga.data.ZombieCrushRecord;
import zombiecrushsaga.ui.ZombieCrushMiniGame;

/**
 * This class provides services for efficiently loading and saving binary files
 * for the Mahjong game application.
 *
 * @author Richard McKenna
 * @coauthor Longcheng Ni
 */
public class ZombieCrushFileManager {
    // WE'LL LET THE GAME KNOW WHEN DATA LOADING IS COMPLETE

    private ZombieCrushMiniGame miniGame;

    /**
     * Constructor for initializing this file manager, it simply keeps the game
     * for later.
     *
     * @param initMiniGame The game for which this class loads data.
     */
    public ZombieCrushFileManager(ZombieCrushMiniGame initMiniGame) {
        // KEEP IT FOR LATER
        miniGame = initMiniGame;
    }

    /**
     * This method loads the contents of the levelFile argument so that the
     * player may then play that level.
     *
     * @param levelFile Level to load.
     */
    public int[][] loadLevel(String levelFile) {
        // LOAD THE RAW DATA SO WE CAN USE IT
        // OUR LEVEL FILES WILL HAVE THE DIMENSIONS FIRST,
        // FOLLOWED BY THE GRID VALUES
        int[][] newGrid = null;
        try {
            File fileToOpen = new File(levelFile);

            // LET'S USE A FAST LOADING TECHNIQUE. WE'LL LOAD ALL OF THE
            // BYTES AT ONCE INTO A BYTE ARRAY, AND THEN PICK THAT APART.
            // THIS IS FAST BECAUSE IT ONLY HAS TO DO FILE READING ONCE
            byte[] bytes = new byte[Long.valueOf(fileToOpen.length()).intValue()];
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            FileInputStream fis = new FileInputStream(fileToOpen);
            BufferedInputStream bis = new BufferedInputStream(fis);

            // HERE IT IS, THE ONLY READY REQUEST WE NEED
            bis.read(bytes);
            bis.close();

            // NOW WE NEED TO LOAD THE DATA FROM THE BYTE ARRAY
            DataInputStream dis = new DataInputStream(bais);

            // NOTE THAT WE NEED TO LOAD THE DATA IN THE SAME
            // ORDER AND FORMAT AS WE SAVED IT

            // FIRST READ THE GRID DIMENSIONS
            int initGridColumns = dis.readInt();
            int initGridRows = dis.readInt();
            newGrid = new int[initGridColumns][initGridRows];

            // AND NOW ALL THE CELL VALUES
            for (int i = 0; i < initGridColumns; i++) {
                for (int j = 0; j < initGridRows; j++) {
                    newGrid[i][j] = dis.readInt();
                }
            }

            // EVERYTHING WENT AS PLANNED SO LET'S MAKE IT PERMANENT


//            miniGame.updateBoundaries();
        } catch (Exception e) {
//            // LEVEL LOADING ERROR
//            miniGame.getErrorHandler().processError(ZombieCrushSagaPropertyType.LOAD_LEVEL_ERROR);
        }
        return newGrid;
    }

    /**
     * This method loads the player record from the records file so that the
     * user may view stats.
     *
     * @return The fully loaded record from the player record file.
     */
    public ZombieCrushRecord loadRecord() {
        ZombieCrushRecord recordToLoad = new ZombieCrushRecord(miniGame);
        for (int i = 0; i < 10; i++) {
            ZombieCrushLevelRecord rec = new ZombieCrushLevelRecord(i + 1);
            recordToLoad.addLevelRcord(rec.getLevelNumber(), rec);
        }

        // LOAD THE RAW DATA SO WE CAN USE IT
        // OUR LEVEL FILES WILL HAVE THE DIMENSIONS FIRST,
        // FOLLOWED BY THE GRID VALUES
        try {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String dataPath = props.getProperty(ZombieCrushSagaPropertyType.DATA_PATH);
            String recordPath = dataPath + props.getProperty(ZombieCrushSagaPropertyType.RECORD_FILE_NAME);
            File fileToOpen = new File(recordPath);

            // LET'S USE A FAST LOADING TECHNIQUE. WE'LL LOAD ALL OF THE
            // BYTES AT ONCE INTO A BYTE ARRAY, AND THEN PICK THAT APART.
            // THIS IS FAST BECAUSE IT ONLY HAS TO DO FILE READING ONCE
            byte[] bytes = new byte[Long.valueOf(fileToOpen.length()).intValue()];
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            FileInputStream fis = new FileInputStream(fileToOpen);
            BufferedInputStream bis = new BufferedInputStream(fis);

            // HERE IT IS, THE ONLY READY REQUEST WE NEED
            bis.read(bytes);
            bis.close();

            // NOW WE NEED TO LOAD THE DATA FROM THE BYTE ARRAY
            DataInputStream dis = new DataInputStream(bais);

            // NOTE THAT WE NEED TO LOAD THE DATA IN THE SAME
            // ORDER AND FORMAT AS WE SAVED IT
            // FIRST READ THE MAX LEVEL
            int maxLevel = dis.readInt();
            //SET MAXLEVEL
            ZombieCrushDataModel data = (ZombieCrushDataModel) miniGame.getDataModel();
            data.setMaxLevel(maxLevel);
   
            //LOAD EACH LEVEL RECORD
            for (int i = 0; i < 10; i++) {
                int levelNumber = dis.readInt();
                int targetScore = dis.readInt();
                int starNumber = dis.readInt();
                recordToLoad.getLevelRcord(levelNumber).setStarsNumber(starNumber);
                recordToLoad.getLevelRcord(levelNumber).setTargetScore(targetScore);
            }
            dis.close();
        } catch (Exception e) {
        }
        return recordToLoad;
    }

    /**
     * save the record
     *
     * @param record the current record that is going to be saved
     */
    public void save(ZombieCrushRecord record) {
        //get the file
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String dataPath = props.getProperty(ZombieCrushSagaPropertyType.DATA_PATH);
        String recordPath = dataPath + props.getProperty(ZombieCrushSagaPropertyType.RECORD_FILE_NAME);
        File fileToSave = new File(recordPath);
        //got the outputStream
        try {
            FileOutputStream fos = new FileOutputStream(fileToSave);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            //get byte array from record object
            byte[] bytes = record.toByteArray();
            //write into the file
            bos.write(bytes);
            bos.close();
        } catch (Exception e) {
        }
    }
}