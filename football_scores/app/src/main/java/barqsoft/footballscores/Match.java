package barqsoft.footballscores;

/**
 * Created by maximyudin on 27.10.15.
 */
public class Match {
    private String homeTeam;

    private String awayTeam;

    private int homeGoals;

    private int awayGoals;

    private String date;

    public Match(String homeTeam, String awayTeam, int homeGoals, int awayGoals, String date) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.date = date;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public int getAwayGoals() {
        return awayGoals;
    }

    public String getDate() {
        return date;
    }
}
