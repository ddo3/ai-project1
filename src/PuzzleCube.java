import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class PuzzleCube {

    private RubikState state;

    public PuzzleCube(RubikState rs){ //LIST ORDER : front, back, left, right, top, bottom
         state = rs;
    }

    public void printState(){
        state.print();
    }

    private void rotate(Face f, Direction d){
        switch (f){
            case TOP:
                state = RubikState.rotateTopFace(d, state);
                return;
            case BACK:
                state = RubikState.rotateBackFace(d, state);
                return;
            case LEFT:
                state = RubikState.rotateLeftFace(d, state);
                return;
            case FRONT:
                state = RubikState.rotateFrontFace(d, state);
                return;
            case RIGHT:
                state = RubikState.rotateRightFace(d, state);
                return;
            case BOTTOM:
                state = RubikState.rotateBottomFace(d,state);
                return;
        }
    }

    public RubikState getState() {
        return state;
    }

    private Face getFace(String face){
        switch(face){
            case "left":
                return Face.LEFT;
            case "right":
                return Face.RIGHT;
            case "top":
                return Face.TOP;
            case "bottom":
                return Face.BOTTOM;
            case "front":
                return Face.FRONT;
            case "back":
                return Face.BACK;
            default:
                return Face.ERROR;
        }
    }

    private Direction getDirection(String d){
        switch (d){
            case "left":
                return Direction.LEFT;
            case "right":
                return Direction.RIGHT;
            default:
                return Direction.START;
        }
    }

    public void rotateFace(String face, String direction){
        var rotatingFace = getFace(face);

        if(rotatingFace == Face.ERROR){
            System.out.println("Invalid Face name : "+ face);
        }else{
            var dir = getDirection(direction);

            if(dir == Direction.START ){
                System.out.println("Invalid Direction : "+ direction);
            }else{
                this.rotate(rotatingFace,dir);
            }
        }
    }

    public void randomizeState(int k){
        state = new RubikState();
        int count = 0;
        Random generator = new Random(20);

        String[] movesArray = {"left","right"};
        String[] facesArray = {"left", "right", "front", "back", "top", "bottom"};
        ArrayList<String> moves = new ArrayList<String>(Arrays.asList(movesArray));
        ArrayList<String> faces = new ArrayList<>(Arrays.asList(facesArray));

        while(count != k) {

            int randMove = generator.nextInt(2);
            int randFace = generator.nextInt(6);
            String moveChoice = moves.get(randMove);
            String faceChoice = faces.get(randFace);
            rotateFace(faceChoice, moveChoice);
            count++;
        }

        state.setOperation(new Operation(Direction.START, Face.START));
        state.setParent(null);
    }

    public static void main(String[] args){
        PuzzleCube ps = new PuzzleCube(new RubikState());

        ps.printState();

        ps.rotate(Face.TOP, Direction.RIGHT);

        ps.printState();

        ps.rotate(Face.TOP, Direction.LEFT);

        ps.printState();
    }

}


