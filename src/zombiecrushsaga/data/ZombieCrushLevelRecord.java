/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.data;

/**
 *
 * @author LongchengNi
 */
public class ZombieCrushLevelRecord {

    private int targetScore;
    private int levelNumber;
    private int starsNumber;

    /**
     * Constructor for level record
     *
     * @param levelNumber number of level
     * @param targetScore target score
     */
    public ZombieCrushLevelRecord(int levelNumber) {
        this.levelNumber = levelNumber;
        this.targetScore = getTargetScore(levelNumber);
        this.starsNumber = 0;
    }

    public int getTargetScore() {
        return targetScore;
    }

    public void setTargetScore(int TargetScore) {
        this.targetScore = TargetScore;
    }

    public int getStarsNumber() {
        return starsNumber;
    }

    public void setStarsNumber(int starsNumber) {
        this.starsNumber = starsNumber;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    private int getTargetScore(int levelNumber) {
        int[] TargetScore = new int[]{
            300, 1900, 4000, 4500, 5000, 9000, 60000, 20000, 22000, 40000};
        return TargetScore[levelNumber - 1];
    }
}
