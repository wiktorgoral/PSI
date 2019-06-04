package model.action;

import java.time.LocalDateTime;

public class Member extends Action {
    private String memberId;
    private String memberName;
    private boolean add;//if 1 its added member, if 0 deleted member


    public Member(String id, String idCreator, LocalDateTime time, String idCard, String memberId, String memberName, boolean add) {
        super(id, idCreator, time, idCard);
        this.memberId = memberId;
        this.memberName = memberName;
        this.add = add;
    }


    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }
}
