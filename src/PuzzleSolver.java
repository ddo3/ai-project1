import java.io.*;
import java.util.Scanner;

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

        System.out.println("This action was"+ addOn+ "sucsessful");
    }

    public static void print(String s){
        System.out.println(s);
    }

    private boolean performAction(String action, String param){
        switch(action){
            case "setState": //works
                PuzzleState newState = new PuzzleState(param);
                puzzle.setState(newState);
                return true;

            case "randomizeState" : //works
                puzzle.randomizeState(Integer.parseInt(param));
                return true;

            case "printState": //works
                puzzle.printState();
                return true;

            case "move": //works
                return puzzle.move(param);

            case "solve A-star":
                return false;

            case "solve beam":
                return false;

            case "maxNodes":
                maxNodes(Integer.parseInt(param));
                return true;

            default :
                System.out.print("That was not a valid command!");
                return false;
        }
    }

    public void parseAndPerformAction(String line){
        //parse line
        var params = line.split(" ");

        String action = params[0];
        var successful = false;
        if(action.equals("solve")){
            action = action + " "+ params[1];
            successful = performAction(action, params[2]);
        }else if(params.length == 1) {
            successful = performAction(action, "");
        }else if(params.length == 4){
            String newState = params[1] + " " + params[2] + " " + params[3];
            successful = performAction(action, newState);
        }else{
            successful = performAction(action,params[1]);
        }

        printSuccess(successful);
    }

    public static void main(String args[]){
        PuzzleSolver ps = new PuzzleSolver();
        System.out.println("Start entering commands");

        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        while (!line.equals("exit")){
            if(line.contains(".txt")){
                File file = new File(line);
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String st;
                    while ((st = br.readLine()) != null) {
                        //System.out.println(st);
                        ps.parseAndPerformAction(st);
                    }

                    br.close();
                }catch (FileNotFoundException ex){
                        System.out.println(ex);
                }catch (IOException ex){
                        System.out.println(ex);
                }
            }else{

                ps.parseAndPerformAction(line);
            }

            line = sc.nextLine();
        }

        System.out.println("GoodBye!");
        /*
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
        */
    }
}
