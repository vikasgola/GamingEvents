package io.github.vikasgola.gamingevent;

public class Event {
    private String image;
    private String time;
    private String status,id;
    private Long totalTeams;

    public Event(){}

    public Event(String image, String time, Long totalTeams,String status) {
        this.image = image;
        this.time = time;
        this.totalTeams = totalTeams;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getTotalTeams() {
        return totalTeams;
    }

    public void setTotalTeams(Long totalTeams) {
        this.totalTeams = totalTeams;
    }
}
