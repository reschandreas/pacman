package programm;

/**
 * Created by Andreas on 16.05.16.
 */
public class Score {

    private String name = null;
    private long score = 0;
    private int level = 1;

    public Score(String name, int level, long score) {
        if (name != null && !name.isEmpty())
            this.name = name;
        if (score > 0)
            this.score = score;
        if (level > 1)
            this.level = level;
    }

    public long getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Wenn der übergebene Score größer ist, wird eine positive Zahl zurückgegeben,
     * ansonsten eine negative Zahl. Sind die Punktestände gleich, werden die Namen mit
     * der compareTo Mehtode der Klasse String verglichen
     * @param score - der zu vergleichende String
     * @return positiv - wenn score größer ist
     *         negativ - wenn score kleiner ist
     */
    public long compareTo(Score score) {
        long ret = this.score - score.score;
        if (ret == 0) {
            ret = name.compareTo(score.name);
        }
        return ret;
    }

    public void reSet(Score score) {
        if (score.name != null && !score.name.isEmpty())
            this.name = score.name;
        if (score.score > 0)
            this.score = score.score;
    }

    @Override
    public String toString() {
        return name + ";" + level + ";" + score;
    }

    @Override
    protected Score clone() {
        return new Score(name, level, score);
    }
}
