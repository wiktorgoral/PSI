import model.Card;
import model.User;
import model.action.ChangeState;
import model.action.Comment;
import model.action.CompleteTask;
import model.action.Member;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Vector;

public class Main {
    public static void main(String[] args) throws Exception {
        File f = new File("C:\\Users\\wikto\\Desktop\\REST+File Upload+HttpClient\\PSI\\src\\main\\resources\\kart.json");
        if (f.exists()) {
            InputStream is = new FileInputStream("C:\\Users\\wikto\\Desktop\\REST+File Upload+HttpClient\\PSI\\src\\main\\resources\\kart.json");
            String jsonTxt = IOUtils.toString(is, "UTF-8");
            JSONObject json = new JSONObject(jsonTxt);
            BuildService builder = new BuildService();
            User[] users = builder.getUser(json);
            Card[] cards = builder.getCard(json).toArray(new Card[0]);
            Vector<Comment> comments = builder.getComment(json);
            Vector<ChangeState> states = builder.getState(json);
            Vector<CompleteTask> tasks = builder.getComplete(json);
            Vector<Member> addMembers = builder.getAdd(json);
            Vector<Member> removeMembers = builder.getRemove(json);
            cards = builder.updateCardStart(cards, addMembers);
            cards = builder.updateCardEnd(cards, states);
            cards = builder.updateCardMarked(cards,states);
            cards = builder.updateMemberDone(cards,tasks);
            users = builder.updateUser(users, cards);
            CheckService checker = new CheckService();
            for (int i=0;i<cards.length;i++) {
                if (cards[i].getName().equals("Transforming Paintings and Photos Into Animations With AI [2 osoby]")) {
                    System.out.println(cards[i].getMemberDone());
                    System.out.println("aaaaa");
                }
            }
            for(int i=0;i<cards.length;i++) {
                checker.fastComment(cards[i], comments);
            }
            for(int i=0;i<cards.length;i++) {
                checker.didWork(cards[i],tasks);
            }
            for(int i=0;i<users.length;i++) {
                checker.concurrency(users[i]);
            }
        }

    }
}
