import model.Card;
import model.User;
import model.action.ChangeState;
import model.action.Comment;
import model.action.CompleteTask;
import model.action.Member;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.Vector;


public class BuildService {
    public User[] getUser(JSONObject x){
        JSONArray members = x.getJSONArray("members");
        User[] out = new User[members.length()];
        for (int i = 0; i < members.length(); i++) {
            JSONObject currentMember = members.getJSONObject(i);
            out[i]=new User(
                    currentMember.getString("id"),
                    currentMember.getString("fullName"),
                    currentMember.getString("url"),
                    currentMember.getString("username"));
        }
        return out;
    }
    public Vector<Card> getCard(JSONObject x){
        JSONArray cards = x.getJSONArray("cards");
        Vector<Card> out = new Vector<Card>();
        for (int i = 0; i < cards.length(); i++) {
            JSONObject currentCard = cards.getJSONObject(i);
            JSONArray mem = currentCard.getJSONArray("idMembers");
            if (mem.length()==0) continue;
            String[] f = new String[mem.length()];
            for (int j=0; j<mem.length();j++){
                f[j]=mem.getString(j);
            }
            if (currentCard.getJSONArray("idChecklists").length()>0 && mem.length()>0) {
                out.add( new Card(currentCard.getString("id"),
                        currentCard.getString("name"),
                        f,
                        currentCard.getJSONArray("idChecklists").getString(0)));
            }
            else if(currentCard.getJSONArray("idChecklists").length()==0 && mem.length()>0){
                out.add( new Card(currentCard.getString("id"),
                        currentCard.getString("name"),
                        f,
                        null));
            }
        }
        return out;
    }
    public Vector<Comment> getComment(JSONObject x){
        JSONArray actions = x.getJSONArray("actions");
        Vector<Comment> out = new Vector<Comment>();
        for (int i = 0; i < actions.length(); i++) {
            JSONObject currentAction = actions.getJSONObject(i);
            if (currentAction.getString("type").equals("commentCard")&&!currentAction.getJSONObject("data").has("dateLastEdited")) {
                out.add(new Comment(currentAction.getString("id"),
                        currentAction.getString("idMemberCreator"),
                        LocalDateTime.parse(currentAction.getString("date").substring(0, 23)),
                        currentAction.getJSONObject("data").getJSONObject("card").getString("id"),
                        currentAction.getJSONObject("data").getString("text")));
            }
            else if (currentAction.getString("type").equals("commentCard")&&currentAction.getJSONObject("data").has("dateLastEdited")) {
                out.add(new Comment(currentAction.getString("id"),
                        currentAction.getString("idMemberCreator"),
                        LocalDateTime.parse(currentAction.getJSONObject("data").getString("dateLastEdited").substring(0, 23)),
                        currentAction.getJSONObject("data").getJSONObject("card").getString("id"),
                        currentAction.getJSONObject("data").getString("text")));
            }
        }
        return out;
    }

    public Vector<ChangeState> getState(JSONObject x){
        JSONArray actions = x.getJSONArray("actions");
        Vector<ChangeState> out = new Vector<ChangeState>();
        for (int i = 0; i < actions.length(); i++) {
            JSONObject currentAction = actions.getJSONObject(i);
            if (currentAction.getString("type").equals("updateCard") && !currentAction.getJSONObject("data").isNull("listAfter")) {
                out.add(new ChangeState(
                        currentAction.getString("id"),
                        currentAction.getString("idMemberCreator"),
                        LocalDateTime.parse(currentAction.getString("date").substring(0, 23)),
                        currentAction.getJSONObject("data").getJSONObject("card").getString("id"),
                        currentAction.getJSONObject("data").getJSONObject("listBefore").getString("name"),
                        currentAction.getJSONObject("data").getJSONObject("listBefore").getString("id"),
                        currentAction.getJSONObject("data").getJSONObject("listAfter").getString("name"),
                        currentAction.getJSONObject("data").getJSONObject("listAfter").getString("id")));
            }
        }
        return out;
    }

    public Vector<CompleteTask> getComplete(JSONObject x) {
        JSONArray actions = x.getJSONArray("actions");
        Vector<CompleteTask> out = new Vector<CompleteTask>();
        for (int i = 0; i < actions.length(); i++) {
            JSONObject currentAction = actions.getJSONObject(i);
            if (currentAction.getString("type").equals("updateCheckItemStateOnCard") && currentAction.getJSONObject("data").getJSONObject("checkItem").getString("state").equals("complete")) {
                out.add( new CompleteTask(
                        currentAction.getString("id"),
                        currentAction.getString("idMemberCreator"),
                        LocalDateTime.parse(currentAction.getString("date").substring(0, 23)),
                        currentAction.getJSONObject("data").getJSONObject("card").getString("id")));
            }
        }
        return out;
    }

    public Vector<Member> getAdd(JSONObject x) {
        JSONArray actions = x.getJSONArray("actions");
        Vector<Member> out = new Vector<Member>();
        for (int i = 0; i < actions.length(); i++) {
            JSONObject currentAction = actions.getJSONObject(i);
            if (currentAction.getString("type").equals("addMemberToCard")) {
                out.add( new Member(
                        currentAction.getString("id"),
                        currentAction.getString("idMemberCreator"),
                        LocalDateTime.parse(currentAction.getString("date").substring(0, 23)),
                        currentAction.getJSONObject("data").getJSONObject("card").getString("id"),
                        currentAction.getJSONObject("data").getJSONObject("member").getString("id"),
                        currentAction.getJSONObject("data").getJSONObject("member").getString("name"),
                        true));
            }
        }
        return out;
    }

    public Vector<Member> getRemove(JSONObject x) {
        JSONArray actions = x.getJSONArray("actions");
        Vector<Member> out = new Vector<Member>();
        for (int i = 0; i < actions.length(); i++) {
            JSONObject currentAction = actions.getJSONObject(i);
            if (currentAction.getString("type").equals("removeMemberFromCard")) {
                out.add(new Member(
                        currentAction.getString("id"),
                        currentAction.getString("idMemberCreator"),
                        LocalDateTime.parse(currentAction.getString("date").substring(0, 23)),
                        currentAction.getJSONObject("data").getJSONObject("card").getString("id"),
                        currentAction.getJSONObject("data").getJSONObject("member").getString("id"),
                        currentAction.getJSONObject("data").getJSONObject("member").getString("name"),
                        false));
            }
        }
        return out;
    }

    public Card[] updateCardStart(Card[] x, Vector<Member> y){
        for (int i=0; i<x.length;i++){
            for (int j=0;j<x[i].getIdMembers().length;j++){
                for (int k=0;k<y.size();k++){
                    if (y.elementAt(k).getMemberId().equals(x[i].getIdMembers()[j])&& y.elementAt(k).getIdCard().equals(x[i].getId())) {
                        Vector<LocalDateTime> v =x[i].getStart();
                        v.add(y.elementAt(k).getTime());
                        x[i].setStart(v);
                        break;
                    }
                }
            }
        }
        return x;
    }

    public Card[] updateCardEnd(Card[] x, Vector<ChangeState> y){
        for(int i=0; i<x.length;i++){
            for (int j =0;j<y.size();j++){
                if (x[i].getId().equals(y.elementAt(j).getIdCard())&& y.elementAt(j).getListAfter().equals("Ocenione")) {
                    x[i].setEnd(y.elementAt(j).getTime());
                    break;
                }
            }
        }
        return x;
    }

    public Card[] updateCardMarked(Card[] x, Vector<ChangeState> y){
        for(int i=0; i<x.length;i++){
            for (int j =0;j<y.size();j++){
                if (x[i].getId().equals(y.elementAt(j).getIdCard())&& y.elementAt(j).getListAfter().equals("Zrobione")) {
                    x[i].setMarked(y.elementAt(j).getTime());
                }
            }
        }
        return x;
    }

    public User[] updateUser(User[] x, Card[] y){
        for (int i=0;i<y.length;i++){
            for(int j =0; j<y[i].getIdMembers().length;j++){
                for (int k=0;k<x.length;k++){
                    if (y[i].getIdMembers()[j].equals(x[k].getId())) {
                        Vector<Card> v = x[k].getCards();
                        v.add(y[i]);
                        x[k].setCards(v);
                    }
                }
            }
        }
        return x;
    }

    public Card[] updateMemberDone(Card[] x, Vector<CompleteTask> y){
        for(int i=0;i<x.length;i++){
            if (x[i].getIdMembers().length==1) {
                Vector<LocalDateTime> p = new Vector<LocalDateTime>();
                p.add(x[i].getMarked());
                x[i].setMemberDone(p);
                continue;
            }
            else if (x[i].getIdMembers().length==0) continue;
            for(int j =0; j<x[i].getIdMembers().length;j++) {
                Vector<LocalDateTime> ini = x[i].getMemberDone();
                ini.add( LocalDateTime.of(2020,1,1,1,1));
                x[i].setMemberDone(ini);
                for (int k = 0; k < y.size(); k++) {
                    if (y.get(k).getIdCreator().equals(x[i].getIdMembers()[j]) && x[i].getId().equals(y.get(k).getIdCard()) && y.get(k).getTime().isBefore(x[i].getMemberDone().elementAt(j))){
                        Vector<LocalDateTime> a = x[i].getMemberDone();
                        a.set(j,y.get(k).getTime());
                        x[i].setMemberDone(a);
                    }
                }
                if (x[i].getMemberDone().elementAt(j).equals(LocalDateTime.of(2020,1,1,1,1))) {
                    Vector<LocalDateTime> se = x[i].getMemberDone();
                    se.set(j,x[i].getMarked());
                    x[i].setMemberDone(se);
                }
            }
        }
        return x;
    }

}
//https://api.trello.com/1/boards/fBs9PkEz?board_action=addMemberToCard&key=5c86b0f36f8278b844f45debd98ca879&token=d1bcfa9991f9d4508546c2b4b9ea1eb93ec21694ced5e252aec444e3265234a8
        //https://api.trello.com/1/boards/fBs9PkEz/actions?filter=removeMemberFromCard&limit=1000&before=2019-03-06T23:58:15.946Z&key=5c86b0f36f8278b844f45debd98ca879&token=d1bcfa9991f9d4508546c2b4b9ea1eb93ec21694ced5e252aec444e3265234a8