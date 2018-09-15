import java.io.*;
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

    public static void print(String s){
        System.out.println(s);
    }

    private Set<PuzzleState> getAllReachableStatesFromState(Set<String> visited, PuzzleState ps){
        Set<PuzzleState> newStates = new HashSet<>();

        var m1 = PuzzleState.moveDown(ps);
        var m2 = PuzzleState.moveLeft(ps);
        var m3 = PuzzleState.moveRight(ps);
        var m4 = PuzzleState.moveUp(ps);

        newStates.add(m1);
        newStates.add(m2);
        newStates.add(m3);
        newStates.add(m4);

        //check if this state is in the set
        if(newStates.contains(ps)){
            newStates.remove(ps);
        }

        //
        Set<PuzzleState> finalStates = new HashSet<>();
        //check if any visited states are in the set
        for (PuzzleState puz: newStates) {
            if(!visited.contains(puz.toString())){
                finalStates.add(puz);
            }
        }
        return finalStates;
    }

    public List<PuzzleState> getPath(PuzzleState initial){
        List<PuzzleState> path = new ArrayList<>();
        PuzzleState node = initial;
        while(node.getParent() != null){
            path.add(node);
            node = node.getParent();
        }

        return path;
    }

    //this is where the search methods will go
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

        Set<String> visited = new HashSet<>();

        //add first state to pq
        pq.add(state);

        while(!state.isGoalState()){
            //get top state from pq
            state = pq.poll();

            //get all states reachable from state
            Set<PuzzleState> reachableStates = getAllReachableStatesFromState( visited, state);

            //increase depth
            depth++;

            //calculate fvalue for each state in list based on huristic
            for ( PuzzleState ps: reachableStates) {
                var hvalue = 0;
                if(heuristic.equals("h1")){
                    hvalue = PuzzleState.getNumberOfMisplacedTiles(ps);
                }else{
                    hvalue = PuzzleState.getSumOfDistOfTilesFromGoal(ps);
                }
                ps.setFValue(hvalue + depth);
            }

            //add state's possible moves to pq based on depth and heuristic
            pq.addAll(reachableStates);

            //add state visited
            visited.add(state.toString());

            print("state : " + state.toString()+" depth : " +depth);
        }

        System.out.println("Found the goal state!");
        System.out.println("Depth : " + depth);
    }

    //todo implement this
    public void beam(int numOfStates){

    }

    private void performAction(String action, String param){
        switch(action){
            case "setState": //works
                PuzzleState newState = new PuzzleState(param);
                puzzle.setState(newState);
                return;

            case "randomizeState" : //works
                puzzle.randomizeState(Integer.parseInt(param));
                return;

            case "printState": //works
                puzzle.printState();
                return;
            case "move": //works
                puzzle.move(param);
                return;

            case "solve A-star": //works!!!!!
                this.aStar(param);
                return;

            case "solve beam":
                this.beam(Integer.parseInt(param));
                return;

            case "maxNodes":
                maxNodes(Integer.parseInt(param));
                return;

            default :
                System.out.println("That was not a valid command!");
                return;
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

    //todo make this command line input
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
