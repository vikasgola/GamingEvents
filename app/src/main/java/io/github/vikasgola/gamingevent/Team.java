package io.github.vikasgola.gamingevent;

public class Team {
    private String leader;
    private long numMember;
    private String teamName;
    private long id;
    private String logo;

    public Team(String leader, long numMember, String teamName, long id) {
        this.leader = leader;
        this.numMember = numMember;
        this.teamName = teamName;
        this.id = id;
    }

    public Team(){}

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public long getNumMember() {
        return numMember;
    }

    public void setNumMember(long numMember) {
        this.numMember = numMember;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
