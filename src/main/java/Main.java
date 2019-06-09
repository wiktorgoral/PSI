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
import java.text.DecimalFormat;
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
            Vector<Comment> oceny = new Vector<>();
            for (int i = 0; i < comments.size(); i++) {
                if (comments.elementAt(i).getText().contains("2")) oceny.add(comments.elementAt(i));
            }
            Vector<ChangeState> states = builder.getState(json);
            Vector<CompleteTask> tasks = builder.getComplete(json);
            Vector<Member> addMembers = builder.getAdd(json);
            Vector<Member> removeMembers = builder.getRemove(json);
            cards = builder.updateCardStart(cards, addMembers);
            cards = builder.updateCardEnd(cards, states);
            cards = builder.updateCardMarked(cards, states);
            cards = builder.updateMemberDone(cards, tasks);
            users = builder.updateUser(users, cards);
            CheckService checker = new CheckService();
            int fast = 0;
            for (int i = 0; i < cards.length; i++) {
                fast += checker.fastComment(cards[i], comments);
            }
            int did = 0;
            for (int i = 0; i < cards.length; i++) {
                if (!checker.didWork(cards[i], tasks)) did++;
            }
            int con = 0;
            for (int i = 0; i < users.length; i++) {
                con += checker.concurrency(users[i]);
            }
            int[] link = checker.hasLink();
            System.out.println("----------------------------------------------------------------------------------------");
            DecimalFormat df2 = new DecimalFormat("##.##");
            double r = ((double) fast / (double) oceny.size()) * 100;
            System.out.println("Users disobeyed comment rule " + fast + " times. That is " + df2.format(r) + " % of all comments with mark 2");
            r = ((double) did / (double) cards.length) * 100;
            System.out.println("One of users have not done anything or did more than two tasks in " + did + " cards. That is " + df2.format(r) + " % of all comments with mark 2");
            System.out.println("Users disobeyed concurrency rule " + con + " times ");
            System.out.println("All pages have " + link[0] + " internal links and " + link[1] + " external links. Total " + (link[0] + link[1]) + " links in 260 pages");
            r = (double) (link[2] / (double) 260) * 100;
            System.out.println(link[2] + " out of 260 pages has links. That is " + df2.format(r) + " % of all pages have links");

        }

    }
}
