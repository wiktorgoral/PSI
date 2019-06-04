import model.Card;
import model.User;
import model.action.Comment;
import model.action.CompleteTask;

import java.time.LocalDateTime;
import java.util.Vector;

public class CheckService {
    public void concurrency(User x){
        for(int i=0;i<x.getCards().size();i++){
            LocalDateTime start= null;
            LocalDateTime end = null;
            for (int j=0;j<x.getCards().get(i).getIdMembers().length;j++){
                if (x.getId().equals(x.getCards().get(i).getIdMembers()[j])){
                    start=x.getCards().get(i).getStart().get(j);
                    end = x.getCards().get(i).getMemberDone().get(j);
                }
            }
            for(int j=0;j<x.getCards().size();j++){
                LocalDateTime start1 = null;
                LocalDateTime end1 = null;
                for (int k=0;k<x.getCards().get(j).getIdMembers().length;k++) {
                    if (x.getId().equals(x.getCards().get(j).getIdMembers()[k])) {
                        start1 = x.getCards().get(j).getStart().get(k);
                        end1= x.getCards().get(j).getMemberDone().get(k);
                    }
                }
                if (i==j) continue;
                else if (start1.isAfter(start.plusMinutes(15)) && start1.isBefore(end)){
                    System.out.println("User "+x.getName()+" did card "+ x.getCards().get(i).getName()+ " with card "+x.getCards().get(j).getName());
                }
                else if (start1.isBefore(start.plusMinutes(15)) && end1.isAfter(start)){
                    System.out.println("User "+x.getName()+" did card "+ x.getCards().get(i).getName()+ " with card "+x.getCards().get(j).getName());
                }
                else if (start.isBefore(start1) && end1.isBefore(end)){
                    System.out.println("User "+x.getName()+" did card "+ x.getCards().get(i).getName()+ " with card "+x.getCards().get(j).getName());
                }
                else if (start1.isBefore(start) && end.isBefore(end1)){
                    System.out.println("User "+x.getName()+" did card "+ x.getCards().get(i).getName()+ " with card "+x.getCards().get(j).getName());
                }
            }
        }
    }

    public void fastComment(Card x, Vector<Comment> y){
        for (int i=0; i<y.size();i++){
            if (y.get(i).getIdCard().equals(x.getId()) && y.get(i).getText().contains("2") && y.get(i).getTime().isAfter(x.getMarked())){
                if (y.get(i).getTime().isBefore(x.getMarked().plusMinutes(3)))
                    System.out.println("User with id "+y.get(i).getIdCreator()+" commented card "+x.getName()+" in less than 3 minutes after it was posted");
            }
        }
    }

    public void didWork(Card x, Vector<CompleteTask> y){
        if (x.getMarked().equals(LocalDateTime.of(2000,1,1,1,1))) return;
        if (x.getIdMembers().length<2) return;
        int[] tasksDone = new int[x.getIdMembers().length];
        for(int i=0;i<x.getIdMembers().length;i++){
            for(int j=0;j<y.size();j++){
                if (x.getIdMembers()[i].equals(y.get(j).getIdCreator()))
                    tasksDone[i]++;
            }
        }
        for (int i=0;i<tasksDone.length;i++){
            if (tasksDone[i]==0){
                System.out.println("User with id "+x.getIdMembers()[i]+" had not done any task in card "+x.getName());
            }
            else if(tasksDone[i]>1){
                //System.out.println("User with id "+x.getIdMembers()[i]+" had done "+tasksDone[i]+" tasks in card "+x.getName());
                //
            }
        }
    }
}
