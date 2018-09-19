public class Operation {

    private Face face;
    private Direction direction;
    public Operation (Direction d, Face f){
        face = f;
        direction = d;
    }

    public void printOperation(){
        System.out.println("Turn "+ face.toString() +" face "+ direction.toString());
    }
}
