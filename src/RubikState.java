import java.util.ArrayList;
import java.util.List;

public class RubikState {
                                    //               0      1     2     3     4     5
    private List<PuzzleFace> state; //LIST ORDER : front, back, left, right, top, bottom
    private PuzzleFace left;
    private PuzzleFace right;
    private PuzzleFace front;
    private PuzzleFace back;
    private PuzzleFace top;
    private PuzzleFace bottom;

    private Operation operation;
    private RubikState parent;

    public RubikState(List<PuzzleFace> faces){
        state = faces;
        operation = null;
        parent = null;

        front = state.get(0);
        back = state.get(1);
        left = state.get(2);
        right = state.get(3);
        top = state.get(4);
        bottom = state.get(5);

        setSideConnections();
    }

    public RubikState(PuzzleFace back,PuzzleFace front,PuzzleFace right,PuzzleFace left,PuzzleFace bottom,PuzzleFace top ){
        this.back = back;
        this.front = front;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;

        state = new ArrayList<>();
        updatePuzzleState();

        operation = null;
        parent = null;
    }

    public RubikState(){
        //white green blue orange red yellow
        left = new PuzzleFace(new Grid('w'));
        right = new PuzzleFace(new Grid('g'));
        top = new PuzzleFace(new Grid('b'));
        bottom = new PuzzleFace(new Grid('o'));
        front = new PuzzleFace(new Grid('r'));
        back = new PuzzleFace(new Grid('y'));

        state = new ArrayList<>();
        setSideConnections();

        updatePuzzleState();
    }

    public void print(){
        List<Grid> grids = new ArrayList<>();
        grids.add(left.getGrid());
        grids.add(front.getGrid());
        grids.add(right.getGrid());
        grids.add(back.getGrid());

        Grid.printGrid(top.getGrid());
        Grid.printGrids(grids);
        Grid.printGrid(bottom.getGrid());

        System.out.println();
    }

    public PuzzleFace getBack() {
        return back;
        //return state.get(1);
    }

    public PuzzleFace getBottom() {
        return bottom;
        //return state.get(5);
    }

    public PuzzleFace getFront() {
        return front;
        //return state.get(0);
    }

    public PuzzleFace getLeft() {
        return left;
        //return state.get(2);
    }

    public PuzzleFace getRight() {
        return right;
        //return state.get(3);
    }

    public PuzzleFace getTop() {
        return top;
        //return state.get(4);
    }

    /*******MOVES*****/
    public RubikState rotateFrontFace(Direction d){
        var rs = new RubikState(this.state);
        rs.setOperation(new Operation(d,Face.FRONT));
        rs.setParent(this);


        if(d == Direction.RIGHT){
            rs.getFront().rotateRight();

        }else {
            rs.getFront().rotateLeft();
        }

        rs.updatePuzzleState();

        return rs;
    }

    public RubikState rotateBackFace(Direction d){

        var rs = new RubikState(this.state);
        rs.setOperation(new Operation(d,Face.BACK));
        rs.setParent(this);

        //set up faces to be rotated
        rs.getTop().getGrid().rotateRight();
        rs.getTop().getGrid().rotateRight();
        rs.getBottom().getGrid().rotateLeft();
        rs.getBottom().getGrid().rotateLeft();

        if(d == Direction.RIGHT){
            rs.getBack().rotateRight();
        }else {

            rs.getBack().rotateLeft();
        }

        //rotate faces back
        rs.getTop().getGrid().rotateLeft();
        rs.getTop().getGrid().rotateLeft();
        rs.getBottom().getGrid().rotateRight();
        rs.getBottom().getGrid().rotateRight();

        return rs;
    }

    public RubikState rotateLeftFace(Direction d){

        RubikState rs = new RubikState(this.state);
        rs.setOperation(new Operation(d,Face.LEFT));
        rs.setParent(this);

        //set up faces to be rotated
        rs.getTop().getGrid().rotateLeft();
        rs.getBottom().getGrid().rotateRight();

        if(d == Direction.RIGHT){
            rs.getLeft().rotateRight();
        }else {
            rs.getLeft().rotateLeft();
        }

        //rotate faces back
        rs.getTop().getGrid().rotateRight();
        rs.getBottom().getGrid().rotateLeft();

        return rs;
    }

    public RubikState rotateRightFace(Direction d){
        RubikState rs = new RubikState(this.state);
        rs.setOperation(new Operation(d,Face.RIGHT));
        rs.setParent(this);

        //set up faces to be rotated
        rs.getTop().getGrid().rotateRight();
        rs.getBottom().getGrid().rotateLeft();

        if(d == Direction.RIGHT){
            //actually rotate face
            rs.getRight().rotateRight();
        }else {
            rs.getRight().rotateLeft();
        }

        //rotate faces back
        rs.getTop().getGrid().rotateLeft();
        rs.getBottom().getGrid().rotateRight();

        return rs;
    }

    public RubikState rotateTopFace(Direction d){

        var rs = new RubikState(this.state);
        rs.setOperation(new Operation(d,Face.TOP));
        rs.setParent(this);

        //set up faces to be rotated
        rs.getBack().getGrid().rotateLeft();
        rs.getBack().getGrid().rotateLeft();
        rs.getLeft().getGrid().rotateRight();
        rs.getRight().getGrid().rotateLeft();

        if(d == Direction.RIGHT){
            rs.getTop().rotateRight();
        }else {
            rs.getTop().rotateLeft();
        }

        //rotate faces back
        rs.getBack().getGrid().rotateRight();
        rs.getBack().getGrid().rotateRight();
        rs.getLeft().getGrid().rotateLeft();
        rs.getRight().getGrid().rotateRight();
        return rs;
    }

    public RubikState rotateBottomFace(Direction d){
        var rs = new RubikState(this.state);
        rs.setOperation(new Operation(d,Face.BOTTOM));
        rs.setParent(this);

        //set up faces to be rotated
        rs.getFront().getGrid().rotateLeft();
        rs.getFront().getGrid().rotateLeft();
        rs.getLeft().getGrid().rotateLeft();
        rs.getRight().getGrid().rotateRight();

        if(d == Direction.RIGHT){
            rs.bottom.rotateRight();
        }else {
            rs.bottom.rotateLeft();
        }

        //rotate faces back
        rs.getFront().getGrid().rotateRight();
        rs.getFront().getGrid().rotateRight();
        rs.getLeft().getGrid().rotateRight();
        rs.getRight().getGrid().rotateLeft();

        return rs;
    }

    /*******/


    public void updatePuzzleState(){
        state.clear();

        state.add(front);
        state.add(back);
        state.add(left);
        state.add(right);
        state.add(top);
        state.add(bottom);
    }

    public boolean equal(RubikState rs){
        var currentFront = state.get(0);
        var currentBack = state.get(1);
        var currentLeft = state.get(2);
        var currentRight = state.get(3);
        var currentTop = state.get(4);
        var currentBottom = state.get(5);

        var inState = rs.getState();

        var inFront = inState.get(0);
        var inBack = inState.get(1);
        var inLeft = inState.get(2);
        var inRight = inState.get(3);
        var inTop = inState.get(4);
        var inBottom = inState.get(5);

        return PuzzleFace.isEqual(currentFront, inFront) &&
                PuzzleFace.isEqual(currentBack, inBack)&&
                PuzzleFace.isEqual(currentLeft, inLeft)&&
                PuzzleFace.isEqual(currentTop, inTop)&&
                PuzzleFace.isEqual(currentRight, inRight)&&
                PuzzleFace.isEqual(currentBottom, inBottom);
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public void setParent(RubikState parent) {
        this.parent = parent;
    }

    public void setState(List<PuzzleFace> state) {
        this.state = state;
    }

    public Operation getOperation() {
        return operation;
    }

    public RubikState getParent() {
        return parent;
    }

    public List<PuzzleFace> getState() {
        return state;
    }

    public void printOperation(){
        operation.printOperation();
    }

    private void setSideConnections(){
        //front
        front.setTop(top);
        front.setBottom(bottom);
        front.setLeft(left);
        front.setRight(right);

        //left
        left.setRight(front);
        left.setTop(top);
        left.setLeft(back);
        left.setBottom(bottom);

        //right
        right.setBottom(bottom);
        right.setTop(top);
        right.setLeft(front);
        right.setRight(back);

        //back
        back.setTop(top);
        back.setBottom(bottom);
        back.setLeft(right);
        back.setRight(left);

        //top
        top.setRight(right);
        top.setBottom(front);
        top.setLeft(left);
        top.setTop(back);

        //bottom
        bottom.setLeft(left);
        bottom.setBottom(back);
        bottom.setTop(front);
        bottom.setRight(right);

    }
}


