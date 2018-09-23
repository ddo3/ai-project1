import java.io.*;
import java.util.*;

public class RubikSolver {
    private PuzzleCube puzzle;
    private int maxNodes;

    public RubikSolver(){
        puzzle = new PuzzleCube(new RubikState());// creates a normal solves cube
    }

    //TODO for A* huristic
    //2 possible heuristic functions that come to my mind:
    //(Total number of mismatches with the goal state) / (number of rows or columns or colors)
    //Average Manhattan distance of each cube with the goal state


    //we are going to do total number of mismatches with the goal state

    private boolean isGoalState(RubikState rs){
        //left = white
        var leftIsRightColor = rs.getLeft().allOneColor('w');

        //right = green
        var rightIsRightColor = rs.getRight().allOneColor('g');
        //top = blue
        var topIsRightColor = rs.getTop().allOneColor('b');

        //bottom = orange
        var bottomIsRightColor = rs.getBottom().allOneColor('o');
        //front = red
        var frontIsRightColor = rs.getFront().allOneColor('r');
        //back = y
        var backIsRightColor = rs.getBack().allOneColor('y');


        return leftIsRightColor && rightIsRightColor && topIsRightColor && bottomIsRightColor && frontIsRightColor && backIsRightColor;
    }

    private void setMaxNodes( int max){
        this.maxNodes = max;
    }

    private Set<RubikState> getAllReachableStatesFromState(Set<String> visited, RubikState rs){
        Set<RubikState> newStates = new HashSet<>();

        var m1 = RubikState.rotateBackFace(Direction.RIGHT, rs);
        var m2 = RubikState.rotateBackFace(Direction.LEFT, rs);
        var m3 = RubikState.rotateFrontFace(Direction.RIGHT, rs);
        var m4 = RubikState.rotateFrontFace(Direction.LEFT, rs);
        var m5 = RubikState.rotateLeftFace(Direction.RIGHT, rs);
        var m6 = RubikState.rotateLeftFace(Direction.LEFT, rs);
        var m7 = RubikState.rotateRightFace(Direction.RIGHT, rs);
        var m8 = RubikState.rotateRightFace(Direction.LEFT, rs);
        var m9 = RubikState.rotateTopFace(Direction.RIGHT, rs);
        var m10 = RubikState.rotateTopFace(Direction.LEFT, rs);
        var m11 = RubikState.rotateBottomFace(Direction.RIGHT, rs);
        var m12 = RubikState.rotateBottomFace(Direction.LEFT, rs);

        newStates.add(m1);
        newStates.add(m2);
        newStates.add(m3);
        newStates.add(m4);
        newStates.add(m5);
        newStates.add(m6);
        newStates.add(m7);
        newStates.add(m8);
        newStates.add(m9);
        newStates.add(m10);
        newStates.add(m11);
        newStates.add(m12);

        Set<RubikState> finalStates = new HashSet<>();
        //check if any visited states are in the set
        for (RubikState puz: newStates) {
            if(!visited.contains(puz.toString())){
                finalStates.add(puz);
            }
        }

        return finalStates;
    }

    private List<RubikState> getPath(RubikState initial){
        LinkedList<RubikState> path = new LinkedList<>();
        RubikState node = initial;
        while(node.getParent() != null){
            path.addFirst(node);
            node = node.getParent();
        }
        return path;
    }


    private static String printList(List<RubikState> list){
        StringBuilder sb = new StringBuilder();

        sb.append("BEGIN : ");
        for(RubikState rs : list){
            var op = rs.getOperation();
            sb.append("{ " + op.toString() + "} ");
        }

        sb.append(": END");
        return sb.toString();
    }

    /*********SEARCH METHODS*********/
    private void aStar(String heuristic){
        var depth = 0;
        boolean reachedMaxNodes = false;
        var state = puzzle.getState();

        Comparator<RubikState> byFValue = new Comparator<RubikState>() {
            @Override
            public int compare(RubikState o1, RubikState o2) {
                return o1.getFValue() - (o2.getFValue());
            }
        };

        PriorityQueue<RubikState> pq = new PriorityQueue<>(byFValue);

        Set<String> visited = new HashSet<>();

        //add first state to pq
        pq.add(state);

        while(!isGoalState(state)){
            //get top state from pq
            state = pq.poll();

            if(isGoalState(state)){
                break;
            }


            //get all states reachable from state
            Set<RubikState> reachableStates = getAllReachableStatesFromState( visited, state);

            depth++;

            if(pq.size() >= maxNodes){
                reachedMaxNodes = true;
                break;
            }

            //calculate fvalue for each state in list based on huristic
            for ( RubikState rs: reachableStates) {
                var hvalue = RubikState.getNumberOfMisplacedColors(rs);
                /*
                if(heuristic.equals("h1")){
                    hvalue = RubikState.getNumberOfMisplacedTiles(rs);
                }else{
                    hvalue = RubikState.getSumOfDistOfTilesFromGoal(rs);
                }
                */

                rs.setFValue(hvalue + depth);
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

    private void beam(int k){

    }
    /********************************/

    private void performAction(String action, String param){
        switch(action){
            case "randomizeState" ://works
                puzzle.randomizeState(Integer.parseInt(param));
                return;

            case "printState": //works
                puzzle.printState();
                return;
            case "rotateFace": //works
                var newparams = param.split(" ");
                puzzle.rotateFace( newparams[0], newparams[1]);
                return;

            case "solve A-star": //works
                this.aStar(param);
                return;

            case "solve beam":
                this.beam(Integer.parseInt(param));
                return;

            case "maxNodes"://works
                this.setMaxNodes(Integer.parseInt(param));
                return;

            default :
                System.out.println("That was not a valid command!");
                return;
        }
    }

    public void parseAndPerformAction(String line){ // no setState
        var params = line.split(" ");

        String action = params[0];
        if(action.equals("solve")) {
            action = action + " " + params[1];
            performAction(action, params[2]);

        }else if(action.equals("rotateFace")){

            performAction(action, params[1]+ " "+ params[2]);
        }else if(params.length == 1) {
            performAction(action, "");
        }else{
            performAction(action,params[1]);
        }
    }

    //todo make this command line input
    public static void main(String args[]){
        RubikSolver rs = new RubikSolver();
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
                        rs.parseAndPerformAction(st);
                    }
                }catch (FileNotFoundException ex){
                    System.out.println(ex);
                }catch (IOException ex){
                    System.out.println(ex);
                }
            }else{

                rs.parseAndPerformAction(line);
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
