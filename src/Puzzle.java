import java.util.*;
import java.lang.*;

public class Puzzle {
    private PuzzleState currentState;
    private String goalState = "b12 345 678";
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

        //set currentState to goal state
        currentState = new PuzzleState(goalState);

        int count = 0;
        int seed  = 10;
        Random generator = new Random(seed);
        String[] movesArray = {"up","down","left","right"};
        ArrayList<String> moves = new ArrayList<String>(Arrays.asList(movesArray));

        while(count != n) {

            int rand = generator.nextInt((3 - 0) + 1);
            String choice = moves.get(rand);
            move(choice);
            count++;
        }

        currentState.setParent(null);
        currentState.setDirection(Direction.START);
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
