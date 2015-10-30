package barqsoft.footballscores;

public class Match {
    public final int id;
    public final String homeTeam;
    public final String awayTeam;
    public final int homeGoals;
    public final int awayGoals;
    public final String time;

    public Match(int id, String homeTeam, String awayTeam, int homeGoals, int awayGoals, String time) {
        this.id = id;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.time = time;
    }
}
