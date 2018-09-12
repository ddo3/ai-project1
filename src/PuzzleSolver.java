import java.io.*;
import java.text.RuleBasedCollator;
import java.util.*;

public class PuzzleSolver {
    private int maxNodes;

    private Puzzle puzzle;

    PuzzleSolver(){
        puzzle = new Puzzle();
    }

    public void maxNodes(int n){
        maxNodes = n;
    }

    //this is where the search methods will go

    public void printSuccess(boolean s){
        String addOn = " ";
        if(!s){
            addOn = " NOT ";
        }

        System.out.println("This action was"+ addOn+ "sucsessful");
    }

    public static void print(String s){
        System.out.println(s);
    }


    public void aStar(String heuristic){
        var depth = 0;
        var state = puzzle.getState();

        Comparator<PuzzleState> byFValue = new Comparator<PuzzleState>() {
            @Override
            public int compare(PuzzleState o1, PuzzleState o2) {
                return o1.getFValue() - (o2.getFValue());
            }
        };

        PriorityQueue<PuzzleState> pq = new PriorityQueue<>(byFValue);

        Set<PuzzleState> visited = new HashSet<>();

        while(!state.isGoalState()){
            //add state visited
            visited.add(state);
            //increase depth
            depth++;

            //get all states reachable from state
            Set<PuzzleState> reachableStates = state.getAllReachableStatesFromCurrentState( visited);

            //calculate fvalue for each state in list based on huristic and depth
            for ( PuzzleState ps: reachableStates) {
                var hvalue = 0;
                if(heuristic.equals("h1")){
                    hvalue = ps.getNumberOfMisplacedTiles(ps);
                }else{
                    hvalue = ps.getSumOfDistOfTilesFromGoal(ps);
                }

                ps.setFValue(hvalue + depth);
            }
            //add state's possible moves to pq based on depth and heuristic
            pq.addAll(reachableStates);

            //choses from top of prioroty queue
            state = pq.poll();
            //set chosen state as puzzles new state????
            puzzle.setState(state);

        }

        System.out.println("Found the goal state!");
        System.out.println("Depth : " + depth);

    }

    private void performAction(String action, String param){
        switch(action){
            case "setState": //works
                PuzzleState newState = new PuzzleState(param);
                puzzle.setState(newState);

            case "randomizeState" : //works
                puzzle.randomizeState(Integer.parseInt(param));

            case "printState": //works
                puzzle.printState();

            case "move": //works
                puzzle.move(param);

            case "solve A-star":
                this.aStar(param);

            case "solve beam":
                //return false;

            case "maxNodes":
                maxNodes(Integer.parseInt(param));


            default :
                System.out.print("That was not a valid command!");
        }
    }

    public void parseAndPerformAction(String line){
        //parse line
        var params = line.split(" ");

        String action = params[0];
        if(action.equals("solve")){
            action = action + " "+ params[1];
            performAction(action, params[2]);
        }else if(params.length == 1) {
            performAction(action, "");
        }else if(params.length == 4){
            String newState = params[1] + " " + params[2] + " " + params[3];
            performAction(action, newState);
        }else{
            performAction(action,params[1]);
        }
    }

    public static void main(String args[]){
        PuzzleSolver ps = new PuzzleSolver();
        System.out.println("Start entering commands");

        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        while (!line.equals("exit")){
            if(line.contains(".txt")){
                File file = new File(line);
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String st;
                    while ((st = br.readLine()) != null) {
                        //System.out.println(st);
                        ps.parseAndPerformAction(st);
                    }

                    br.close();
                }catch (FileNotFoundException ex){
                        System.out.println(ex);
                }catch (IOException ex){
                        System.out.println(ex);
                }
            }else{

                ps.parseAndPerformAction(line);
            }

            line = sc.nextLine();
        }

        System.out.println("GoodBye!");
        /*
        if(args[0].contains(".txt")){//parse instructions read from file
            File file = new File(args[0]);
            try (BufferedReader br = new BufferedReader(new FileReader(file))){
                String st;
                while ((st = br.readLine()) != null){
                    System.out.println(st);
                }
            }catch (Exception e){
              System.out.println("Unable to read from file : " + args[0]);
            }

        }else{//need to perform single request
            String action = args[0];
            print(action);
            var successful = false;
            if(action.equals("solve")){
                action = action + " "+ args[1];
                successful = ps.performAction(action, args[2]);
            }else if(args.length == 1){
                successful = ps.performAction(action,"");
            }else{
                successful = ps.performAction(action,args[1]);
            }

            ps.printSuccess(successful);
        }
        */
    }
}
