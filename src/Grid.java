import java.util.List;

public class Grid {

    private char[] row1;
    private char[] row2;

    public Grid(char[] row1, char[] row2){
        this.row1 = row1;
        this.row2 = row2;
    }

    public Grid(String face){
        char[] newRow1 = {face.charAt(0), face.charAt(1)};
        char[] newRow2 = {face.charAt(2), face.charAt(3)};
        row1 = newRow1;
        row2 = newRow2;
    }

    public Grid(char c){
        char[] newRow1 = {c, c};
        char[] newRow2 = {c, c};
        row1 = newRow1;
        row2 = newRow2;
    }

    public void setRow(int index, char[] row){
        if(index == 0){
            row1 = row;
        }else{
            row2 = row;
        }
    }

    public void setCol(int index, char[] col){
        row1[index] = col[0];
        row2[index] = col[1];
    }

    public char[] getRow(int index){
        if(index == 0){
            return row1;
        }else{
            return row2;
        }
    }

    public char[] getCol(int index){
        char[] col = {row1[index], row2[index]};
        return col;
    }

    public void rotateLeft(){
        char[] copyRow1 = {row1[1], row1[0]};
        char[] copyRow2 = {row2[1], row2[0]};
        setCol(0,copyRow1);
        setCol(1,copyRow2);
    }

    public void rotateRight(){
        char[] copyRow1 = {row1[0], row1[1]};
        char[] copyRow2 = {row2[0], row2[1]};
        setCol(0,copyRow2);
        setCol(1,copyRow1);
    }

    public static boolean isEqual(Grid g1, Grid g2){
        return g1.row1[0] == g2.row1[0] &&
                g1.row2[0] == g2.row2[0] &&
                g1.row1[1] == g2.row1[1] &&
                g1.row2[1] == g2.row2[1];
    }

    public static void printGrid(Grid g){
        System.out.println("     "+g.row1[0]+ " "+ g.row1[1]);
        System.out.println("     "+g.row2[0]+ " "+ g.row2[1]);
    }

    public static void printGrids(List<Grid> grids){
        StringBuilder rowOne = new StringBuilder();
        StringBuilder rowTwo = new StringBuilder();

        for(Grid g : grids){
            rowOne.append(g.row1[0]+ " "+ g.row1[1] + "  ");
            rowTwo.append(g.row2[0]+ " "+ g.row2[1] + "  ");
        }

        System.out.println(rowOne.toString());
        System.out.println(rowTwo.toString());
    }


    public static void main (String[] args){

        var grid = new Grid("rgby");

        Grid.printGrid(grid);
        System.out.println();

        grid.rotateLeft();
        Grid.printGrid(grid);
        System.out.println();

        grid.rotateLeft();
        Grid.printGrid(grid);
    }
}
