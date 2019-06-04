package model.action;

import java.time.LocalDateTime;

public class CompleteTask extends Action {
    public CompleteTask(String id, String idCreator, LocalDateTime time, String idCard) {
        super(id, idCreator, time, idCard);
    }
}
