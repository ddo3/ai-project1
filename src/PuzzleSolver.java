import java.io.*;

public class PuzzleSolver {
    private int maxNodes;

    private Puzzle puzzle;

    PuzzleSolver(){
        puzzle = new Puzzle();
    }

    public void maxNodes(int n){
        maxNodes = n;
    }

    //this is where the search methods will go

    public void printSuccess(boolean s){
        String addOn = " ";
        if(!s){
            addOn = " NOT ";
        }

        System.out.println("This action was"+ addOn+ "sucsessful,");
    }

    public static void print(String s){
        System.out.println(s);
    }

    public boolean performAction(String action, String param){
        switch(action){
            case "setState":
                PuzzleState newState = new PuzzleState(param);
                puzzle.setState(newState);
                return true;

            case "randomizeState" :
                puzzle.randmoizeState(Integer.parseInt(param));
                return true;

            case "printState":
                puzzle.printState();
                return true;

            case "move":
                return puzzle.move(param);

            case "solve A-star":
                return false;

            case "solve beam":
                return false;

            case "maxNodes":
                maxNodes(Integer.parseInt(param));
                return true;

            default :
                return false;
        }
    }

    public static void main(String args[]){
        PuzzleSolver ps = new PuzzleSolver();

        if(args[0].contains(".txt")){//parse instructions read from file
            File file = new File(args[0]);
            try (BufferedReader br = new BufferedReader(new FileReader(file))){
                String st;
                while ((st = br.readLine()) != null){
                    System.out.println(st);
                }
            }catch (Exception e){
              System.out.println("Unable to read from file : " + args[0]);
            }

        }else{//need to perform single request
            String action = args[0];
            print(action);
            var successful = false;
            if(action.equals("solve")){
                action = action + " "+ args[1];
                successful = ps.performAction(action, args[2]);
            }else if(args.length == 1){
                successful = ps.performAction(action,"");
            }else{
                successful = ps.performAction(action,args[1]);
            }

            ps.printSuccess(successful);
        }

    }
}
