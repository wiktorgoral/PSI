package model.action;

import java.time.LocalDateTime;

public class Action implements ActionInter {
    private String id;
    private String idCreator;
    private LocalDateTime time;
    private String idCard;

    public Action() {
    }

    public Action(String id, String idCreator, LocalDateTime time, String idCard) {
        this.id = id;
        this.idCreator = idCreator;
        this.time = time;
        this.idCard = idCard;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(String idCreator) {
        this.idCreator = idCreator;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}
