import java.util.*;
import java.lang.*;

public class PuzzleState {

    private String state;

    private PuzzleState parent = null;

    private Direction direction = Direction.START;

    private int fValue = 0;

    PuzzleState(String state){
        this.state = state;
    }

    PuzzleState(){
        state = "b12 345 678";
        this.randomize();
    }

    public int getFValue(){
        return fValue;
    }

    public void setFValue(int v){
        this.fValue = v;
    }

    public void setDirection(Direction d){
        direction = d;
    }

    public Direction getDirection(){
        return direction;
    }

    public PuzzleState getParent(){
        return parent;
    }

    public void setParent(PuzzleState parent) {
        this.parent = parent;
    }

    private int blankIndex(){
        return state.indexOf('b');
    }

    public void setState(String state){
        this.state = state;
    }

    public String toString(){
        return state;
    }

    private void randomize(){

        StringBuilder sb = new StringBuilder();
        List<Character> characters = new ArrayList<Character>();
        for(char c : state.toCharArray()){
            if(c != ' '){
                characters.add(c);
            }
        }

        while(characters.size() != 0){
            int rand = (int)(Math.random() * characters.size());
            if(sb.length() == 3 || sb.length() == 7){
                sb.append(" ");
            }
            sb.append(characters.remove(rand));
        }

        this.state = sb.toString();
    }

    public boolean isGoalState(){
        return state.equals("b12 345 678");
    }

    private static String swap(int blankIndex, int index2, String s){
        var array = s.toCharArray();

        char blank = array[blankIndex];
        char otherTile = array[index2];

        array[blankIndex] = otherTile;
        array[index2] = blank;

        return new String(array);
    }

    public static Set<PuzzleState> removeStateFromSet(Set<PuzzleState> set, PuzzleState ps){
        Set<PuzzleState> newSet = new HashSet<>();
        for(PuzzleState state : set){
            if (!state.toString().equals(ps.toString()))
                newSet.add(state);
        }
        return newSet;
    }

    public static boolean setHasState(Set<PuzzleState> states, String state){
        for (PuzzleState s: states) {
            if(s.state.equals(state)){
                return true;
            }
        }
        return false;
    }


    public static Set<String> getAllStateStrings(Set<PuzzleState> states){
        Set<String> stateStrings = new HashSet<>();

        for (PuzzleState ps: states) {
            stateStrings.add(ps.toString());
        }

        return stateStrings;
    }

    public static int getNumberOfMisplacedTiles(PuzzleState ps){
        int count = 0;
        for (int i = 0; i < ps.toString().length() ; i++){
            if(ps.state.charAt(i) != "b12 345 678".charAt(i)){
                count++;
            }
        }
        return count;
    }

    public static int getSumOfDistOfTilesFromGoal(PuzzleState ps){
        var noSpaceState = removeSpaces(ps.toString());

        var sum = 0;

        for (int i = 0; i < noSpaceState.length(); i++){
            //turn index to x and y
            int y = (i / 3);
            int x = i - (y * 3);

            sum = sum + getDistOfSingleTileFromGoal(noSpaceState.charAt(i), x, y);
        }
        return sum;
    }

    private static int getDistOfSingleTileFromGoal(char tile, int x, int y){
        var goalX = 0;
        var goalY = 0;
        switch(tile){
            case 'b':
                goalX = 0;
                goalY = 0;
                return manhattanDistance(x,y,goalX, goalY);
            case '1':
                goalX = 1;
                goalY = 0;
                return manhattanDistance(x,y,goalX, goalY);
            case '2':
                goalX = 2;
                goalY = 0;
                return manhattanDistance(x,y,goalX, goalY);
            case '3':
                goalX = 0;
                goalY = 1;
                return manhattanDistance(x,y,goalX, goalY);
            case '4':
                goalX = 1;
                goalY = 1;
                return manhattanDistance(x,y,goalX, goalY);
            case '5':
                goalX = 1;
                goalY = 2;
                return manhattanDistance(x,y,goalX, goalY);
            case '6':
                goalX = 2;
                goalY = 0;
                return manhattanDistance(x,y,goalX, goalY);
            case '7':
                goalX = 2;
                goalY = 1;
                return manhattanDistance(x,y,goalX, goalY);
            case '8':
                goalX = 2;
                goalY = 2;
                return manhattanDistance(x,y,goalX, goalY);
            default:
                return 0;
        }
    }

    private static int manhattanDistance(int x1, int y1, int x2, int y2){
        return Math.abs(x1-x2) + Math.abs(y1-y2);
    }

    private static String removeSpaces(String state){
        return state.replaceAll(" ", "");
    }

    /********/
    //these methods return the state after the swap, does not change current state
    public static PuzzleState moveUp(PuzzleState workingState){
        String stateString = workingState.toString();
        int indexOfBlank = workingState.blankIndex();
        if( indexOfBlank - 4 >= 0 ){
            //swap the string string specifically to get a new string
            String newStateString = swap(indexOfBlank, indexOfBlank - 4, stateString);
            //return an new state and set all appropriate variables
            var newState = new PuzzleState(newStateString);
            newState.setDirection(Direction.UP);
            newState.setParent(workingState);
            return newState;
        }
        return workingState;
    }

    public static PuzzleState moveDown(PuzzleState workingState){
        String stateString = workingState.toString();
        int indexOfBlank = workingState.blankIndex();
        if( indexOfBlank + 4 < workingState.toString().length()){
            //swap the string string specifically to get a new string
            String newStateString = swap(indexOfBlank, indexOfBlank + 4, stateString);
            //return an new state and set all appropriate variables
            var newState = new PuzzleState(newStateString);
            newState.setDirection(Direction.DOWN);
            newState.setParent(workingState);
            return newState;
        }
        return workingState;
    }

    public static PuzzleState moveLeft(PuzzleState workingState){
        String stateString = workingState.toString();
        int indexOfBlank = workingState.blankIndex();
        if((indexOfBlank - 1 >= 0) && workingState.toString().charAt(indexOfBlank - 1 ) != ' ' ){
            //swap the string string specifically to get a new string
            String newStateString = swap(indexOfBlank, indexOfBlank - 1 , stateString);
            //return an new state and set all appropriate variables
            var newState = new PuzzleState(newStateString);
            newState.setDirection(Direction.LEFT);
            newState.setParent(workingState);
            return newState;
        }
        return workingState;
    }

    public static PuzzleState moveRight(PuzzleState workingState){
        String stateString = workingState.toString();
        int indexOfBlank = workingState.blankIndex();
        if((indexOfBlank + 1 < workingState.toString().length()) && workingState.toString().charAt(indexOfBlank + 1 ) != ' ' ){
            //swap the string string specifically to get a new string
            String newStateString = swap(indexOfBlank, indexOfBlank + 1 , stateString);
            //return an new state and set all appropriate variables
            var newState = new PuzzleState(newStateString);
            newState.setDirection(Direction.RIGHT);
            newState.setParent(workingState);
            return newState;
        }
        return workingState;
    }

    /**********/

    public void print(){
        System.out.println(state);
    }

    public static void main(String args[]){
        PuzzleState p = new PuzzleState("147 258 36b");
        p.print();

        var test = getSumOfDistOfTilesFromGoal(p);

        System.out.println("total: "+test);
    }
}
