package io.github.vikasgola.gamingevent;


public class Schedule {
    public Schedule() {
    }

    private String teams;
    private String schedule;

    public Schedule(String teams, String schedule) {
        this.teams = teams;
        this.schedule = schedule;
    }

    public String getTeams() {
        return teams;
    }

    public void setTeams(String teams) {
        this.teams = teams;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
