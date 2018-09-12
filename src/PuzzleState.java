import java.util.*;
import java.lang.*;

public class PuzzleState {

    private String state;

    private String goalState = "b12 345 678";

    private int fValue = 0;

    PuzzleState(String state){
        this.state = state;
    }

    PuzzleState(){
        state = goalState;
        this.randomize();
    }

    public int getFValue(){
        return fValue;
    }

    public void setFValue(int v){
        this.fValue = v;
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
        return state.equals(goalState);
    }

    private void swap(int blankIndex, int index2){
        var stateArray = state.toCharArray();

        char blank = stateArray[blankIndex];
        char otherTile = stateArray[index2];

        stateArray[blankIndex] = otherTile;
        stateArray[index2] = blank;

        state = new String(stateArray);
    }

    public int getNumberOfMisplacedTiles(PuzzleState ps){
        int count = 0;
        for (int i = 0; i < state.length() ; i++){
            if(ps.state.charAt(i) != goalState.charAt(i)){
                count++;
            }
        }
        return count;
    }

    public int getSumOfDistOfTilesFromGoal(PuzzleState ps){
        return 0;
    }

    public Set<PuzzleState> getAllReachableStatesFromCurrentState(Set<PuzzleState> visited){
        Set<PuzzleState> newStates = new HashSet<>();

        newStates.add(this.moveDown());
        newStates.add(this.moveLeft());
        newStates.add(this.moveRight());
        newStates.add(this.moveUp());

        //check if this state is in the set
        if(newStates.contains(this)){
            newStates.remove(this);
        }

        //check if any visited states are in the set
        for (PuzzleState ps: newStates) {
            if(visited.contains(ps)){
                newStates.remove(ps);
            }
        }

        return newStates;
    }

    public PuzzleState moveUp(){
        int indexOfBlank = blankIndex();
        if( indexOfBlank - 4 >= 0 ){
            swap(indexOfBlank, indexOfBlank - 4);
        }
        return new PuzzleState(state);
    }

    public PuzzleState moveDown(){
        int indexOfBlank = blankIndex();
        if( indexOfBlank + 4 < state.length()){
            swap(indexOfBlank, indexOfBlank + 4);
        }
        return new PuzzleState(state);
    }

    public PuzzleState moveLeft(){
        int indexOfBlank = blankIndex();
        if(state.charAt(indexOfBlank - 1 ) != ' ' && (indexOfBlank - 1 >= 0) ){
            swap(indexOfBlank, indexOfBlank - 1);
        }
        return new PuzzleState(state);
    }

    public PuzzleState moveRight(){
        int indexOfBlank = blankIndex();
        if(state.charAt(indexOfBlank + 1 ) != ' ' && (indexOfBlank + 1 < state.length())){
            swap(indexOfBlank, indexOfBlank + 1);
        }

        return new PuzzleState(state);
    }

    public void print(){
        System.out.println(state);
    }

    public static void main(String args[]){
        PuzzleState p = new PuzzleState();
        p.print();
    }
}
