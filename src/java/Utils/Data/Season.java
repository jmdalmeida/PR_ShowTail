
package Utils.Data;

import java.util.ArrayList;
import java.util.List;

public class Season {
    private final int id;
    private final int numberSeason;
    private List<Episode> episodes;

    public Season(int id, int numberSeason) {
        this.id = id;
        this.numberSeason = numberSeason;
    }

    public int getId() {
        return id;
    }

    public int getNumberSeason() {
        return numberSeason;
    }
    
    public void addEpisode(Episode e){
        if(episodes == null)
            episodes = new ArrayList<Episode>();
        episodes.add(e);
    }
    
    public List<Episode> getEpisodes(){
        return episodes;
    }
}
