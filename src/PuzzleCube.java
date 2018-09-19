import java.util.List;

public class PuzzleCube {

    private RubikState state;

    public PuzzleCube(RubikState rs){ //LIST ORDER : front, back, left, right, top, bottom
         state = rs;
    }

    public void print(){
        state.print();
    }

    public void rotate(Face f, Direction d){
        switch (f){
            case TOP:
                state = state.rotateTopFace(d);
                return;
            case BACK:
                state = state.rotateBackFace(d);
                return;
            case LEFT:
                state = state.rotateLeftFace(d);
                return;
            case FRONT:
                state = state.rotateFrontFace(d);
                return;
            case RIGHT:
                state = state.rotateRightFace(d);
                return;
            case BOTTOM:
                state = state.rotateBottomFace(d);
                return;
        }
    }

    public static void main(String[] args){
        //TODO test all rotations!!!
        PuzzleCube ps = new PuzzleCube(new RubikState());

        ps.print();

        ps.rotate(Face.TOP, Direction.RIGHT); //TODO everything but front rotation not working

        ps.print();

        ps.rotate(Face.TOP, Direction.LEFT);

        ps.print();
    }

}


