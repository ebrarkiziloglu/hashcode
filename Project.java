import java.util.*;

public class Project implements Comparable<Project> {
    String name;
    HashMap<String, Boolean> isPlaced;
    HashMap<String, Integer> roles;
    int duration;
    int bestDay;
    int score;
    int estScore;

    public Project(String name, int duration, int bestDay, int score){
        this.name = name;
        this.duration = duration;
        this.bestDay = bestDay;
        this.score = score;
        this.roles = new HashMap<>();
        this.isPlaced = new HashMap<>();
    }

    void addRoles(String skillName, int level){
        roles.put(skillName, level);
        isPlaced.put(skillName, false);
    }

    // score - bestDay + current + duration
    // end = current + duration
    void calculateValue(int current){
        // int numOfRoles = roles.size();
        estScore = score - bestDay + current + duration;
    }

    void fillRole(String role){
        isPlaced.replace(role, true);
    }

    @Override
    public int compareTo(Project o) {
        if(this.estScore > o.estScore) return -1;
        return 1;
    }
}
