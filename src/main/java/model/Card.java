package model;

import java.time.LocalDateTime;
import java.util.Vector;

public class Card {
    private String id;
    private String name;
    private String[] idMembers;
    private String idChecklist;
    private Vector<LocalDateTime> memberDone = new Vector<LocalDateTime>();
    private LocalDateTime marked =  LocalDateTime.of(2000,1,1,1,1);
    private LocalDateTime end = LocalDateTime.of(2000,1,1,1,1);
    private Vector<LocalDateTime> start=new Vector<LocalDateTime>();

    public Card(String id, String name, String[] idMembers, String idChecklist) {
        this.id = id;
        this.name = name;
        this.idMembers = idMembers;
        this.idChecklist = idChecklist;
    }

    public LocalDateTime getMarked() {
        return marked;
    }

    public void setMarked(LocalDateTime marked) {
        this.marked = marked;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Vector<LocalDateTime> getStart() {
        return start;
    }

    public void setStart(Vector<LocalDateTime> start) {
        this.start = start;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getIdMembers() {
        return idMembers;
    }

    public void setIdMembers(String[] idMembers) {
        this.idMembers = idMembers;
    }

    public String getIdChecklist() {
        return idChecklist;
    }

    public void setIdChecklist(String idChecklist) {
        this.idChecklist = idChecklist;
    }

    public Vector<LocalDateTime> getMemberDone() {
        return memberDone;
    }

    public void setMemberDone(Vector<LocalDateTime> memberDone) {
        this.memberDone = memberDone;
    }
}


