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

    public PuzzleState getState(){
        return currentState;
    }

    public void setState(PuzzleState p){
        this.currentState = p;
    }

    public boolean inGoalState(){
        return currentState.isGoalState();
    }

    public void setIsSolved(){
        isSolved = inGoalState();
    }

    public void printState(){
        currentState.print();
    }

    public void randomizeState(int n){
        int count = 0;
        String[] movesArray = {"up","down","left","right"};
        ArrayList<String> moves = new ArrayList<String>(Arrays.asList(movesArray));

        while(count != n) {
            int rand = (int) (Math.random() * moves.size());
            String choice = moves.get(rand);
            move(choice);
            count++;
        }
    }

    public void move(String direc){

        switch (direc){
            case "up" :
                currentState = PuzzleState.moveUp(currentState);
                setIsSolved();
                return;

            case "down" :
                currentState = PuzzleState.moveDown(currentState);
                setIsSolved();
                return;

            case "left" :
                currentState = PuzzleState.moveLeft(currentState);
                setIsSolved();
                return;

            case "right" :
                currentState = PuzzleState.moveRight(currentState);
                setIsSolved();
                return;

            default:
                System.out.println("This is an invalid move : " + direc);
                return;
        }
    }

    public static void main(String args[]){

    }
}
