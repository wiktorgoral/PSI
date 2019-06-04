package model.action;

import java.time.LocalDateTime;

public class ChangeState extends Action {
    private String listBefore;
    private String listBeforeId;
    private String listAfter;
    private String listAfterId;


    public ChangeState(String id, String idCreator, LocalDateTime time, String idCard, String listBefore, String listBeforeId, String listAfter, String listAfterId) {
        super(id, idCreator, time, idCard);
        this.listBefore = listBefore;
        this.listBeforeId = listBeforeId;
        this.listAfter = listAfter;
        this.listAfterId = listAfterId;
    }

    public String getListBefore() {
        return listBefore;
    }

    public void setListBefore(String listBefore) {
        this.listBefore = listBefore;
    }

    public String getListBeforeId() {
        return listBeforeId;
    }

    public void setListBeforeId(String listBeforeId) {
        this.listBeforeId = listBeforeId;
    }

    public String getListAfter() {
        return listAfter;
    }

    public void setListAfter(String listAfter) {
        this.listAfter = listAfter;
    }

    public String getListAfterId() {
        return listAfterId;
    }

    public void setListAfterId(String listAfterId) {
        this.listAfterId = listAfterId;
    }
}
