import java.util.*;
import java.lang.*;

public class PuzzleState {

    private String state;

    PuzzleState(String state){
        this.state = state;
    }

    PuzzleState(){
        state = "b12 345 678";
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

    private void swap(int blankIndex, int index2){
        var stateArray = state.toCharArray();

        char blank = stateArray[blankIndex];
        char otherTile = stateArray[index2];

        stateArray[blankIndex] = otherTile;
        stateArray[index2] = blank;

        state = new String(stateArray);
    }

    public boolean moveUp(){
        int indexOfBlank = blankIndex();
        if( indexOfBlank - 4 >= 0 ){
            swap(indexOfBlank, indexOfBlank - 4);
            print();
            return true;
        }
        return false;
    }

    public boolean moveDown(){
        int indexOfBlank = blankIndex();
        if( indexOfBlank + 4 < state.length()){
            swap(indexOfBlank, indexOfBlank + 4);
            print();
            return true;
        }
        return false;
    }

    public boolean moveLeft(){
        int indexOfBlank = blankIndex();
        if(state.charAt(indexOfBlank - 1 ) != ' ' && (indexOfBlank - 1 >= 0) ){
            swap(indexOfBlank, indexOfBlank - 1);
            print();
            return true;
        }
        return false;
    }

    public boolean moveRight(){
        int indexOfBlank = blankIndex();
        if(state.charAt(indexOfBlank + 1 ) != ' ' && (indexOfBlank + 1 < state.length())){
            swap(indexOfBlank, indexOfBlank + 1);
            print();
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
