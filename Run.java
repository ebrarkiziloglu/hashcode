import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.Scanner;

// hashcode/src/*.java -d hashcode/bin --release 16
// java -cp hashcode/bin Run /Users/gk/Downloads/input_data/a_an_example.in.txt myoutputa.txt
// java -cp hashcode/bin Run /Users/gk/Downloads/input_data/b_better_start_small.in.txt myoutputb.txt
// java -cp hashcode/bin Run /Users/gk/Downloads/input_data/c_collaboration.in.txt myoutputc.txt
// java -cp hashcode/bin Run /Users/gk/Downloads/input_data/d_dense_schedule.in.txt myoutputd.txt
// java -cp hashcode/bin Run /Users/gk/Downloads/input_data/e_exceptional_skills.in.txt myoutpute.txt
// java -cp hashcode/bin Run /Users/gk/Downloads/input_data/f_find_great_mentors.in.txt myoutputf.txt

public class Run {

    static PriorityQueue<Project> projectValue = new PriorityQueue<>();
    static ArrayList<Project> dumbProjects = new ArrayList<>();
    static Set<String> allRoles = new HashSet<>();
    static ArrayList<Contributor> staff = new ArrayList<>();
    static HashMap<String, PriorityQueue<Contributor>> roleList = new HashMap<>();

    public static void main(String[] args) throws FileNotFoundException {
//        String inputFileName=args[0];
//        File myInputFile = new File(inputFileName);
        File myInputFile = new File("/Users/gk/Downloads/input_data/a_an_example.in.txt");
        Scanner sc = new Scanner(myInputFile);
//        PrintStream outstream= new PrintStream(new File(args[1]));
        PrintStream outstream= new PrintStream(new File("myoutputb.txt"));
        int contributors = sc.nextInt();
        int projects = sc.nextInt();
        sc.nextLine();

        for(int i = 0; i < contributors; i++){
            String[] names = sc.nextLine().split(" ");
            String name = names[0];
            int number = Integer.parseInt(names[1]);
            Contributor c = new Contributor(name);
            for(int j = 0; j < number; j++){
                String[] skill = sc.nextLine().split(" ");
                String language = skill[0];
                allRoles.add(language);
                int level = Integer.parseInt(skill[1]);
                c.skillAdd(language, level);
            }
            staff.add(c);
        }

        for(int i = 0; i < projects; i++){
            String[] project = sc.nextLine().split(" ");
            String projectName = project[0];

            int duration = Integer.parseInt(project[1]);
            int score = Integer.parseInt(project[2]);
            int bestBefore = Integer.parseInt(project[3]);
            int numRoles = Integer.parseInt(project[4]);

            Project project1 = new Project(projectName, duration, bestBefore, score);
            for(int j = 0; j < numRoles; j++){
                String[] role = sc.nextLine().split(" ");
                project1.addRoles(role[0], Integer.parseInt(role[1]));
            }
            int current = 0;
            project1.calculateValue(current);
            projectValue.add(project1);
        }
        for (Contributor contributor : staff) {
            for (String s : allRoles) {
                if (!contributor.skills.containsKey(s)) {
                    contributor.skillAdd(s, 0);
                }
            }
        }
        int current = 0;
        createRoleQueues(contributors);
        int totalScore = 0;
        while(!projectValue.isEmpty()){
            Project project = projectValue.remove();
            HashMap<String, Contributor> matches = new HashMap<>();
            for(Entry<String, Integer> entry: project.roles.entrySet()){
                String role = entry.getKey();
                int level = entry.getValue();
                // PriorityQueue<Contributor> q = roleList.get(role);
                Contributor c = findContributor(level, role);
                if(c != null){
                    matches.put(role, c);
                    project.fillRole(role);
                    c.fillContributor();
                }
            }

            for(Entry<String, Integer> entry: project.roles.entrySet()){
                String role = entry.getKey();
                int level = entry.getValue();
                if(!project.isPlaced.get(role)){
                    // PriorityQueue<Contributor> q = roleList.get(role);
                    Contributor c = findContributor(level-1, role);
                    if(c != null){
                        for(Entry<String, Contributor> match : matches.entrySet()) {
                            Contributor mentor = match.getValue();
                            if(mentor.getLevel(role) >= level){
                                matches.put(role, c);
                                project.fillRole(role);
                                c.fillContributor();
                            }
                        }
                    }
                }
            }
            System.out.println("match:" + matches.size());
            System.out.println("roles: " + project.roles.size());
            if(matches.size() == project.roles.size()){
                System.out.println("girdik");
                totalScore += project.score - project.bestDay + current + project.duration;
                current += project.duration;
            }
            else{
                dumbProjects.add(project);
            }
            // totalScore
            // update current
        }
        System.out.println(totalScore);
        sc.close();
        outstream.close();
    }

    static Contributor findContributor(int level, String role){
        PriorityQueue<Contributor> q = roleList.get(role);
        if(q.isEmpty())
            return null;
        Contributor c = q.poll();
        if(c.getLevel(role) >= level && c.isAvailable) return c;
        else{
            Contributor c2 = findContributor(level, role);
            q.add(c);
            return c2;
        }
    }

    static void createRoleQueues(int contributors){
        int numOfRoles = allRoles.size();
        Iterator<String> it = allRoles.iterator();
        for(int i = 0; i < numOfRoles; i++){
            String role = it.next();

            PriorityQueue<Contributor> queue = new PriorityQueue<>(new Comparator<>() {
                @Override
                public int compare(Contributor a, Contributor b) {
                    return a.getLevel(role) - b.getLevel(role);
                }
            });
            for(int k = 0; k < contributors; k++){
                Contributor contributor = staff.get(k);
                if(contributor.doesSkillExist(role)) queue.add(contributor);
            }
//            System.out.println("Role: " + role);
//            while(!queue.isEmpty()){
//                Contributor cont = queue.poll();
//                System.out.println(cont.name + " " + cont.getLevel(role));
//            }
            roleList.put(role, queue);
        }
    }


}
