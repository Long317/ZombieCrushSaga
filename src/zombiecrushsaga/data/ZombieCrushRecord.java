/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.data;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import zombiecrushsaga.ui.ZombieCrushMiniGame;

/**
 *
 * @author Longcheng Ni
 */
public class ZombieCrushRecord {
    //LEVEL RECORDS

    private ArrayList<ZombieCrushLevelRecord> levelRecords;
    //MINI GAME
    private ZombieCrushMiniGame game;

    //CONSTRUCTOR
    public ZombieCrushRecord(ZombieCrushMiniGame game) {
        this.game = game;
        levelRecords = new ArrayList<ZombieCrushLevelRecord>();
    }

    /**
     * ADD LEVEL RECORD
     */
    public void addLevelRcord(int level, ZombieCrushLevelRecord rec) {
        this.levelRecords.add(level - 1, rec);
    }

    /**
     * GET LEVEL RECORD
     */
    public ZombieCrushLevelRecord getLevelRcord(int level) {
        if (level < 0 || level > 10) {
            System.out.println("LEVEL IS OUT OF 10");
            return null;
        }
        return this.levelRecords.get(level);
    }

    /**
     * toByteArray
     */
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        ZombieCrushDataModel data = (ZombieCrushDataModel) game.getDataModel();
        dos.writeInt(data.getMaxLevel());
        for (int i = 0; i < levelRecords.size(); i++) {
            dos.writeInt(i);
            ZombieCrushLevelRecord rec = levelRecords.get(i);
            dos.writeInt(rec.getTargetScore());
            dos.writeInt(rec.getStarsNumber());

        }
        // AND THEN RETURN IT
        return baos.toByteArray();
    }
}
