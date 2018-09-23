public class Operation {

    private Face face;
    private Direction direction;
    public Operation (Direction d, Face f){
        face = f;
        direction = d;
    }

    public String toString(){
        return "Turn "+ face.toString() +" face "+ direction.toString();
    }
}
