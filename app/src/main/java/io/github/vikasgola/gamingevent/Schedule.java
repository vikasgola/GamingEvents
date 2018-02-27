package io.github.vikasgola.gamingevent;


public class Schedule {
    public Schedule() {
    }

    private String team1;
    private String team2;
    private String schedule;

    public Schedule(String team1, String team2, String schedule) {
        this.team1 = team1;
        this.team2 = team2;
        this.schedule = schedule;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
