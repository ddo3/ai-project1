import java.util.*;
import java.lang.*;

public class Puzzle {
    private PuzzleState currentState;
    private String goalState = "b12345678";
    private boolean isSolved;

    Puzzle(){
        currentState = new PuzzleState();
        isSolved = inGoalState();
    }

    Puzzle(PuzzleState state){
        currentState = state;
        isSolved = inGoalState();
    }

    public void setState(PuzzleState p){
        this.currentState = p;
    }

    public boolean inGoalState(){
        return currentState.equals(goalState);
    }

    public void setIsSolved(){
        isSolved = inGoalState();
    }

    public void printState(){
        currentState.print();
    }

    public void randmoizeState(int n){
        int count = 0;
        String[] movesArray = {"up","down","left","right"};
        ArrayList<String> moves = new ArrayList<String>(Arrays.asList(movesArray));

        while(count != n) {
            int rand = (int) (Math.random() * moves.size());
            String choice = moves.get(rand);
            boolean result = move(choice);

            if (result) {//we were able to perform that move given state
                count++;
            }
        }
    }

    public boolean move(String direc){
        boolean result = false;
        switch (direc){
            case "up" :
                result =  currentState.moveUp();
                setIsSolved();
                return result;

            case "down" :
                result = currentState.moveDown();
                setIsSolved();
                return result;

            case "left" :
                result = currentState.moveLeft();
                setIsSolved();
                return result;

            case "right" :
                result = currentState.moveRight();
                setIsSolved();
                return result;

            default:
                return false;
        }
    }

    public static void main(String args[]){

    }
}
