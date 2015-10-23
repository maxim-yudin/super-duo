package barqsoft.footballscores;

import android.content.Context;

class Utilities {
    private static final int SERIE_A = 357;
    private static final int PREMIER_LEGAUE = 354;
    private static final int CHAMPIONS_LEAGUE = 362;
    private static final int PRIMERA_DIVISION = 358;
    private static final int BUNDESLIGA = 351;

    public static String getLeague(Context context, int league_num) {
        switch (league_num) {
            case SERIE_A:
                return context.getString(R.string.league_seria_a);
            case PREMIER_LEGAUE:
                return context.getString(R.string.league_premier);
            case CHAMPIONS_LEAGUE:
                return context.getString(R.string.league_uefa);
            case PRIMERA_DIVISION:
                return context.getString(R.string.league_primera_division);
            case BUNDESLIGA:
                return context.getString(R.string.league_bundesliga);
            default:
                return context.getString(R.string.league_unknown);
        }
    }

    public static String getMatchDay(Context context, int match_day, int league_num) {
        if (league_num == CHAMPIONS_LEAGUE) {
            if (match_day <= 6) {
                return context.getString(R.string.match_type_group_stages);
            } else if (match_day == 7 || match_day == 8) {
                return context.getString(R.string.match_type_first_knockout);
            } else if (match_day == 9 || match_day == 10) {
                return context.getString(R.string.match_type_quarter_final);
            } else if (match_day == 11 || match_day == 12) {
                return context.getString(R.string.match_type_semi_final);
            } else {
                return context.getString(R.string.match_type_final);
            }
        } else {
            return context.getString(R.string.match_type_unknown, String.valueOf(match_day));
        }
    }

    public static String getScores(Context context, int home_goals, int awaygoals) {
        if (home_goals < 0 || awaygoals < 0) {
            return context.getString(R.string.scores_empty);
        } else {
            return context.getString(R.string.scores,
                    String.valueOf(home_goals), String.valueOf(awaygoals));
        }
    }

    public static int getTeamCrestByTeamName(Context context, String teamname) {
        if (teamname == null) {
            return R.drawable.no_icon;
        }

        if (isSameTeam(teamname, context.getString(R.string.team_arsenal_london_fc))) {
            return R.drawable.arsenal;
        } else if (isSameTeam(teamname, context.getString(R.string.team_manchester_united_fc))) {
            return R.drawable.manchester_united;
        } else if (isSameTeam(teamname, context.getString(R.string.team_swansea_city))) {
            return R.drawable.swansea_city_afc;
        } else if (isSameTeam(teamname, context.getString(R.string.team_leicester_city))) {
            return R.drawable.leicester_city_fc_hd_logo;
        } else if (isSameTeam(teamname, context.getString(R.string.team_everton_fc))) {
            return R.drawable.everton_fc_logo1;
        } else if (isSameTeam(teamname, context.getString(R.string.team_west_ham_united_fc))) {
            return R.drawable.west_ham;
        } else if (isSameTeam(teamname, context.getString(R.string.team_tottenham_hotspur_fc))) {
            return R.drawable.tottenham_hotspur;
        } else if (isSameTeam(teamname, context.getString(R.string.team_west_bromwich_albion))) {
            return R.drawable.west_bromwich_albion_hd_logo;
        } else if (isSameTeam(teamname, context.getString(R.string.team_sunderland_afc))) {
            return R.drawable.sunderland;
        } else if (isSameTeam(teamname, context.getString(R.string.team_stoke_city_fc))) {
            return R.drawable.stoke_city;
        } else {
            return R.drawable.no_icon;
        }
    }

    private static boolean isSameTeam(String teamOriginal, String knownTeam) {
        return teamOriginal.equalsIgnoreCase(knownTeam);
    }
}
