import java.util.*;

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
    private int fValue;

    public RubikState(String state){

        this.state = new ArrayList<PuzzleFace>();
        var states = state.split(" ");
        this.front = new PuzzleFace(new Grid(states[0]));
        this.back = new PuzzleFace(new Grid(states[1]));
        this.left = new PuzzleFace(new Grid(states[2]));
        this.right = new PuzzleFace(new Grid(states[3]));
        this.top = new PuzzleFace(new Grid(states[4]));
        this.bottom = new PuzzleFace(new Grid(states[5]));

        updatePuzzleState();

        setSideConnections();
        operation = new Operation(Direction.START, Face.START);
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

        operation = new Operation(Direction.START, Face.START);
        parent = null;

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
    public static RubikState rotateFrontFace(Direction d, RubikState prev){
        var rs = new RubikState(prev.toString());

        rs.setOperation(new Operation(d,Face.FRONT));
        rs.setParent(prev);


        if(d == Direction.RIGHT){
            rs.getFront().rotateRight();

        }else {
            rs.getFront().rotateLeft();
        }

        rs.updatePuzzleState();

        return rs;
    }

    public static RubikState rotateBackFace(Direction d, RubikState prev){

        var rs = new RubikState(prev.toString());
        rs.setOperation(new Operation(d,Face.BACK));
        rs.setParent(prev);

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

    public static RubikState rotateLeftFace(Direction d, RubikState prev){

        var rs = new RubikState(prev.toString());
        rs.setOperation(new Operation(d,Face.LEFT));
        rs.setParent(prev);

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

    public static RubikState rotateRightFace(Direction d, RubikState prev){
        var rs = new RubikState(prev.toString());
        rs.setOperation(new Operation(d,Face.RIGHT));
        rs.setParent(prev);

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

    public static RubikState rotateTopFace(Direction d, RubikState prev){

        var rs = new RubikState(prev.toString());
        rs.setOperation(new Operation(d,Face.TOP));
        rs.setParent(prev);

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

    public static RubikState rotateBottomFace(Direction d, RubikState prev){
        var rs = new RubikState(prev.toString());
        rs.setOperation(new Operation(d,Face.BOTTOM));
        rs.setParent(prev);

        //set up faces to be rotated
        rs.getBack().getGrid().rotateLeft();
        rs.getBack().getGrid().rotateLeft();
        rs.getLeft().getGrid().rotateLeft();
        rs.getRight().getGrid().rotateRight();

        if(d == Direction.RIGHT){
            rs.bottom.rotateRight();
        }else {
            rs.bottom.rotateLeft();
        }

        //rotate faces back
        rs.getBack().getGrid().rotateRight();
        rs.getBack().getGrid().rotateRight();
        rs.getLeft().getGrid().rotateRight();
        rs.getRight().getGrid().rotateLeft();

        return rs;
    }

    /*****************/

    public static int getNumberOfMisplacedColors(RubikState rs){
        int sum = 0;

        //left = white
        sum += Grid.numOfDifferentTiles(rs.getLeft().getGrid(),new Grid('w'));

        //right = green
        sum += Grid.numOfDifferentTiles(rs.getRight().getGrid(),new Grid('g'));

        //top = blue
        sum += Grid.numOfDifferentTiles(rs.getTop().getGrid(),new Grid('b'));

        //bottom = orange
        sum += Grid.numOfDifferentTiles(rs.getBottom().getGrid(),new Grid('o'));

        //front = red
        sum += Grid.numOfDifferentTiles(rs.getFront().getGrid(),new Grid('r'));

        //back = y
        sum += Grid.numOfDifferentTiles(rs.getBack().getGrid(),new Grid('y'));

        return sum;
    }

    public static List<RubikState> getAllSuccessors(RubikState rs){
        List<RubikState> newStates = new ArrayList<>();

        var m1 = RubikState.rotateBackFace(Direction.RIGHT, rs);
        m1.setFValue(RubikState.getNumberOfMisplacedColors(m1));

        var m2 = RubikState.rotateBackFace(Direction.LEFT, rs);
        m2.setFValue(RubikState.getNumberOfMisplacedColors(m2));

        var m3 = RubikState.rotateFrontFace(Direction.RIGHT, rs);
        m3.setFValue(RubikState.getNumberOfMisplacedColors(m3));

        var m4 = RubikState.rotateFrontFace(Direction.LEFT, rs);
        m4.setFValue(RubikState.getNumberOfMisplacedColors(m4));

        var m5 = RubikState.rotateLeftFace(Direction.RIGHT, rs);
        m5.setFValue(RubikState.getNumberOfMisplacedColors(m5));

        var m6 = RubikState.rotateLeftFace(Direction.LEFT, rs);
        m6.setFValue(RubikState.getNumberOfMisplacedColors(m6));

        var m7 = RubikState.rotateRightFace(Direction.RIGHT, rs);
        m7.setFValue(RubikState.getNumberOfMisplacedColors(m7));

        var m8 = RubikState.rotateRightFace(Direction.LEFT, rs);
        m8.setFValue(RubikState.getNumberOfMisplacedColors(m8));

        var m9 = RubikState.rotateTopFace(Direction.RIGHT, rs);
        m9.setFValue(RubikState.getNumberOfMisplacedColors(m9));

        var m10 = RubikState.rotateTopFace(Direction.LEFT, rs);
        m10.setFValue(RubikState.getNumberOfMisplacedColors(m10));

        var m11 = RubikState.rotateBottomFace(Direction.RIGHT, rs);
        m11.setFValue(RubikState.getNumberOfMisplacedColors(m11));

        var m12 = RubikState.rotateBottomFace(Direction.LEFT, rs);
        m12.setFValue(RubikState.getNumberOfMisplacedColors(m12));

        newStates.add(m1);
        newStates.add(m2);
        newStates.add(m3);
        newStates.add(m4);
        newStates.add(m5);
        newStates.add(m6);
        newStates.add(m7);
        newStates.add(m8);
        newStates.add(m9);
        newStates.add(m10);
        newStates.add(m11);
        newStates.add(m12);

        return newStates;
    }

    public void updatePuzzleState(){
        state.clear();

        state.add(front);
        state.add(back);
        state.add(left);
        state.add(right);
        state.add(top);
        state.add(bottom);
    }

    public static boolean equal(RubikState state1, RubikState state2){

        return state1.toString().equals(state2.toString());

        /*
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
                */
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

    public void setFValue(int fValue) {
        this.fValue = fValue;
    }

    public int getFValue() {
        return fValue;
    }

    public static RubikState getRandomState(RubikState state){
        Random generator = new Random(20);

        Direction[] movesArray = {Direction.LEFT,Direction.RIGHT};
        String[] facesArray = {"left", "right", "front", "back", "top", "bottom"};
        ArrayList<Direction> moves = new ArrayList<>(Arrays.asList(movesArray));
        ArrayList<String> faces = new ArrayList<>(Arrays.asList(facesArray));

        int randMove = generator.nextInt(2);
        int randFace = generator.nextInt(6);
        Direction moveChoice = moves.get(randMove);
        String faceChoice = faces.get(randFace);

        switch(faceChoice){
            case "left":
                return rotateLeftFace(moveChoice, state);
            case "right":
                return rotateRightFace(moveChoice, state);
            case "front":
                return rotateFrontFace(moveChoice, state);
            case "back":
                return rotateBackFace(moveChoice, state);
            case "top":
                return rotateTopFace(moveChoice, state);
            case "bottom":
                return rotateBottomFace(moveChoice, state);
            default:
                return state;
        }
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

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(PuzzleFace face : this.state){
            sb.append(face.getRowString(0));
            sb.append(face.getRowString(1));
            sb.append(" ");
        }
        return sb.toString();
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


