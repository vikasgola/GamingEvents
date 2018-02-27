package io.github.vikasgola.gamingevent;

public class Player {
    private String name;
    private Long score;

    public Player(String name, Long score) {
        this.name = name;
        this.score = score;
    }

    public Player() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }
}
