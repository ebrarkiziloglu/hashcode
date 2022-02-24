import java.util.HashMap;

public class Contributor {

    public HashMap<String, Integer> skills;
    String name;
    boolean isAvailable;
    Contributor(String name){
        skills = new HashMap<>();
        this.name = name;
        this.isAvailable = true;
    }

    public void skillAdd(String name, int level){
        skills.put(name, level);
    }

    public void matchToRole(){
        isAvailable = true;
    }

    public void fillContributor(){
        isAvailable = false;
    }

    public boolean doesSkillExist(String name){
        if(skills.containsKey(name)) return true;
        return false;
    }

    public int getLevel(String role){
        return skills.get(role);
    }

}
