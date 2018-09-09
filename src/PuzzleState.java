import java.util.*;
import java.lang.*;

public class PuzzleState {

    private String state;

    PuzzleState(String state){
        this.state = state;
    }

    PuzzleState(){
        state = "b12345678";
        this.randomize();
    }

    private int blankIndex(){
        return state.indexOf('b');
    }

    public void setState(String state){
        this.state = state;
    }

    public String getState(){
        return state;
    }

    public void randomize(){
        StringBuilder sb = new StringBuilder();
        List<Character> characters = new ArrayList<Character>();
        for(char c : state.toCharArray()){
            characters.add(c);
        }

        while(characters.size() != 0){
            int rand = (int)(Math.random() * characters.size());
            sb.append(characters.remove(rand));
        }

        this.state = sb.toString();
    }

    private void swap(int blankIndex, int index2){
        var stateArray = state.toCharArray();

        char blank = stateArray[blankIndex];
        char otherTile = stateArray[index2];

        stateArray[blankIndex] = otherTile;
        stateArray[index2] = blank;

        state = stateArray.toString();
    }

    public boolean moveUp(){
        int indexOfBlank = blankIndex();
        if( indexOfBlank - 3 >= 0 ){
            swap(indexOfBlank, indexOfBlank-3);
            return true;
        }
        return false;
    }

    public boolean moveDown(){
        int indexOfBlank = blankIndex();
        if( indexOfBlank + 3 <= 8 ){
            swap(indexOfBlank, indexOfBlank-3);
            return true;
        }
        return false;
    }

    public boolean moveLeft(){
        int indexOfBlank = blankIndex();
        ArrayList<Integer> invalidIndexNums = new ArrayList<>();
        invalidIndexNums.add(-1);
        invalidIndexNums.add(2);
        invalidIndexNums.add(5);

        if(invalidIndexNums.contains(indexOfBlank - 1 )){
            swap(indexOfBlank, indexOfBlank - 1);
            return true;
        }
        return false;
    }

    public boolean moveRight(){
        int indexOfBlank = blankIndex();
        ArrayList<Integer> invalidIndexNums = new ArrayList<>();
        invalidIndexNums.add(3);
        invalidIndexNums.add(6);
        invalidIndexNums.add(9);
        if(invalidIndexNums.contains(indexOfBlank + 1 )){
            swap(indexOfBlank, indexOfBlank + 1);
            return true;
        }
        return false;
    }

    public void print(){
        System.out.println(state);
    }

    public static void main(String args[]){
        PuzzleState p = new PuzzleState();
        p.print();
    }
}
