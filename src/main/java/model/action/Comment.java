package model.action;

import java.time.LocalDateTime;

public class Comment extends Action {
    public String text;

    public Comment(String id, String idCreator, LocalDateTime time, String idCard, String text) {
        super(id, idCreator, time, idCard);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
