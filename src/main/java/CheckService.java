import model.Card;
import model.User;
import model.action.Comment;
import model.action.CompleteTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.time.LocalDateTime;
import java.util.Vector;

public class CheckService {
    public int concurrency(User x) {
        int h = 0;
        boolean didIt = false;
        for (int i = 0; i < x.getCards().size(); i++) {
            LocalDateTime start = null;
            LocalDateTime end = null;

            for (int j = 0; j < x.getCards().get(i).getIdMembers().length; j++) {
                if (x.getId().equals(x.getCards().get(i).getIdMembers()[j])) {
                    start = x.getCards().get(i).getStart().get(j);
                    end = x.getCards().get(i).getMemberDone().get(j);
                }
            }
            for (int j = 0; j < x.getCards().size(); j++) {
                LocalDateTime start1 = null;
                LocalDateTime end1 = null;
                for (int k = 0; k < x.getCards().get(j).getIdMembers().length; k++) {
                    if (x.getId().equals(x.getCards().get(j).getIdMembers()[k])) {
                        start1 = x.getCards().get(j).getStart().get(k);
                        end1 = x.getCards().get(j).getMemberDone().get(k);
                    }
                }
                if (i == j) continue;
                else if (start1.isAfter(end)) continue;
                else if (start.isAfter(end1)) continue;
                else {
                    System.out.println("User " + x.getName() + " did card " + x.getCards().get(i).getName() + " with card " + x.getCards().get(j).getName());
                    didIt = true;
                }
            }
            if (didIt) h++;
        }
        if (didIt) System.out.println("User " + x.getName() + " did at least 2 cards simultaneously " + h + " times");
        return h;
    }

    public int fastComment(Card x, Vector<Comment> y) {
        int out = 0;
        for (int i = 0; i < y.size(); i++) {
            if (y.get(i).getIdCard().equals(x.getId()) && y.get(i).getText().contains("2") && y.get(i).getTime().isAfter(x.getMarked())) {
                if (x.getMarked().isAfter(y.elementAt(i).getTime().minusMinutes(3))) {
                    System.out.println("User with id " + y.get(i).getIdCreator() + " commented card " + x.getName() + " in less than 3 minutes after it was posted");
                    out++;
                }
            }
        }
        return out;
    }

    public boolean didWork(Card x, Vector<CompleteTask> y) {
        boolean out = true;
        if (x.getMarked().equals(LocalDateTime.of(2000, 1, 1, 1, 1))) return true;
        if (x.getIdMembers().length < 2) return true;
        int[] tasksDone = new int[x.getIdMembers().length];
        for (int i = 0; i < x.getIdMembers().length; i++) {
            for (int j = 0; j < y.size(); j++) {
                if (x.getIdMembers()[i].equals(y.get(j).getIdCreator()) && x.getId().equals(y.elementAt(j).getIdCard()))
                    tasksDone[i]++;
            }
        }
        for (int i = 0; i < tasksDone.length; i++) {
            if (tasksDone[i] == 0) {
                System.out.println("User with id " + x.getIdMembers()[i] + " had not done any task in card " + x.getName());
                out = false;
            } else if (tasksDone[i] > 1) {
                System.out.println("User with id " + x.getIdMembers()[i] + " had done " + tasksDone[i] + " tasks in card " + x.getName());
                out = false;
            }
        }
        return out;
    }

    static class MyAuthenticator extends Authenticator {
        private String username, password;

        public MyAuthenticator(String user, String pass) {
            username = user;
            password = pass;
        }
    }

    public int[] hasLink() throws Exception {
        String url = "https://krzysztof.kutt.pl/psi-wiki/index.php?title=Specjalna:Zaloguj&returnto=Specjalna%3AZaloguj";
        Auth http = new Auth();
        CookieHandler.setDefault(new CookieManager());
        String page = http.GetPageContent(url);
        String postParams = http.getFormParams(page, "wgoral", "Wojownik1");
        http.sendPost(url, postParams);

        int inside = 0;
        int outside = 0;
        int hasLinks = 0;
        int[] out = new int[3];
        String text = http.GetPageContent("https://krzysztof.kutt.pl/psi-wiki/index.php/Specjalna:Wszystkie_strony");
        Document doc = Jsoup.parse(text);
        Element ul = doc.getElementsByClass("mw-allpages-body").first();
        Elements li = ul.getElementsByTag("li");
        Vector<String> strony = new Vector<>();
        for (Element lii : li) {
            Element link = lii.getElementsByTag("a").first();
            String linkHref = link.attr("href");
            strony.add(linkHref);
        }

        for (int i = 0; i < strony.size(); i++) {
            int stanOut = outside;
            int stanIn = inside;
            String textStrony = http.GetPageContent("https://krzysztof.kutt.pl" + strony.elementAt(i));
            doc = Jsoup.parse(textStrony);
            Element body = doc.getElementById("bodyContent");
            Elements ps = body.getElementsByTag("p");
            for (Element p : ps) {
                Elements elements = p.select("a");
                if (!elements.isEmpty()) {
                    for (Element link : elements) {
                        String linkHref = link.attr("href");
                        if (linkHref.startsWith("/")) {
                            inside++;
                        } else if (linkHref.startsWith("h")) {
                            outside++;
                        }
                    }
                }
            }
            if (stanIn != inside || stanOut != outside) {
                System.out.println("Site https://krzysztof.kutt.pl" + strony.elementAt(i) + " has links");
                hasLinks++;
            }
        }

        out[0] = inside;
        out[1] = outside;
        out[2] = hasLinks;
        return out;
    }
}
