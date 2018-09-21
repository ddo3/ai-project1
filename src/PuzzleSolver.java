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
        LinkedList<PuzzleState> path = new LinkedList<>();
        PuzzleState node = initial;
        while(node.getParent() != null){
            path.addFirst(node);
            node = node.getParent();
        }

        return path;
    }

    private static String printList(List<PuzzleState> list){
        StringBuilder sb = new StringBuilder();

        sb.append("BEGIN : ");
        for(PuzzleState ps : list){
            sb.append(ps.getDirection().toString() + " ");
        }
        sb.append(": END");
        return sb.toString();
    }

    private static  List<PuzzleState> getKRandomStates(int k, PuzzleState ps){
        List<PuzzleState> testStates = new ArrayList<>();

        while(testStates.size() != k){
            //get a random depth
            int depth = (int) (Math.random() * 10);
            int count = 0;
            var state = ps;
            //call getRandomState depth number of times
            while(count != depth){
                state = PuzzleState.getRandomState(state);
                count++;
            }

            //calutate the f valeu for the final result
            state.setFValue(PuzzleState.getSumOfDistOfTilesFromGoal(state));

            testStates.add(state);
        }

        return testStates;
    }

    /********SEARCH METHODS**********/

    public void aStar(String heuristic){
        var depth = 0;
        boolean reachedMaxNodes = false;
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

            if(state.isGoalState()){
                break;
            }


            //get all states reachable from state
            Set<PuzzleState> reachableStates = getAllReachableStatesFromState( visited, state);

            depth++;

            if(pq.size() >= maxNodes){
                reachedMaxNodes = true;
                break;
            }

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
        }

        if(reachedMaxNodes){
            System.out.println("ERROR : Reached to max nodes!");
        }else{
            System.out.println("Found the goal state!");

            var path = getPath(state);

            System.out.println("Number of Nodes : " + path.size());

            System.out.println(printList(path));
        }

    }

    public void beam(int numOfStates){
        boolean isGoalState = false;
        PuzzleState finishedState = null;
        boolean reachedMaxNodes = false;

        //create priority Q
        Comparator<PuzzleState> byFValue = new Comparator<PuzzleState>() {
            @Override
            public int compare(PuzzleState o1, PuzzleState o2) {
                return o1.getFValue() - (o2.getFValue());
            }
        };

        PriorityQueue<PuzzleState> pq = new PriorityQueue<>(byFValue);

        //generate k random states from start puzzle
        List<PuzzleState> nextStates = getKRandomStates(numOfStates, puzzle.getState());

        //check to see if any random states are in the are in the goal state
        for(PuzzleState ps : nextStates){
            if(ps.isGoalState()){
                isGoalState = true;
                finishedState = ps;
                break;
            }
        }

        while(!isGoalState){
            if(pq.size() >= maxNodes){
                reachedMaxNodes = true;
                break;
            }

            List<PuzzleState> successors = new ArrayList<>();
            //for each state get its successors
            for(PuzzleState ps : nextStates){
                successors.addAll(PuzzleState.getAllSuccessors(ps));
            }

            //check if any of the successors are the goal state, if they are quit
            for(PuzzleState ps : successors){
                if(ps.isGoalState()){
                    isGoalState = true;
                    finishedState = ps;
                    break;
                }
            }

            if(!isGoalState){
                //add all to Q
                pq.addAll(successors);

                nextStates.clear();
                //take top k nodes from Q
                for(int i = 0; i < numOfStates; i ++){
                    nextStates.add(pq.poll());
                }

                //clear Q
                pq.clear();
            }

        }

        System.out.println("Found the goal state!");

        var path = getPath(finishedState);

        System.out.println("Number of Nodes : " + path.size());

        System.out.println(printList(path));

    }

    /********************************/

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

            case "maxNodes": //works
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
