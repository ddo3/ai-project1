public class PuzzleFace {

    private Grid grid;

    private PuzzleFace top;
    private PuzzleFace bottom;
    private PuzzleFace left;
    private PuzzleFace right;


    public PuzzleFace (Grid grid){
        this.grid = grid;
        top = null;
        bottom = null;
        left = null;
        right = null;
    }

    public Grid getGrid(){
        return grid;
    }

    public void setLeft (PuzzleFace face){
        left = face;
    }

    public void setRight (PuzzleFace face){
        right = face;
    }
    public void setTop (PuzzleFace face){
        top = face;
    }
    public void setBottom (PuzzleFace face){
        bottom = face;
    }

    public void rotateLeft(){
        char[] topBottomRow = top.grid.getRow(1);
        char[] leftRightCol = left.grid.getCol(1);
        char[] bottomTopRow = bottom.grid.getRow(0);
        char[] rightLeftCol = right.grid.getCol(0);

        //set rows to their appropriate locations
        left.grid.setCol(1, topBottomRow);
        right.grid.setCol(0, bottomTopRow);
        top.grid.setRow(1, rightLeftCol);
        bottom.grid.setRow(0, leftRightCol);

        //rotate the current face
        grid.rotateLeft();
    }

    public void rotateRight(){
        char[] topBottomRow = top.grid.getRow(1);
        char[] leftRightCol = left.grid.getCol(1);
        char[] bottomTopRow = bottom.grid.getRow(0);
        char[] rightLeftCol = right.grid.getCol(0);

        //set rows to their appropriate locations
        left.grid.setCol(1, bottomTopRow);
        right.grid.setCol(0, topBottomRow);
        top.grid.setRow(1, leftRightCol);
        bottom.grid.setRow(0, rightLeftCol);

        //rotate the current face
        grid.rotateRight();
    }


    public static boolean isEqual( PuzzleFace p1, PuzzleFace p2){
        return Grid.isEqual(p1.grid, p2.grid);
    }
}
