package barqsoft.footballscores;

public class Match {
    public final String homeTeam;
    public final String awayTeam;
    public final int homeGoals;
    public final int awayGoals;
    public final String date;

    public Match(String homeTeam, String awayTeam, int homeGoals, int awayGoals, String date) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.date = date;
    }
}
