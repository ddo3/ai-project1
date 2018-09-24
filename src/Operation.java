public class Operation {

    private Face face;
    private Direction direction;
    public Operation (Direction d, Face f){
        face = f;
        direction = d;
    }

    public Face getFace() {
        return face;
    }

    public Direction getDirection() {
        return direction;
    }

    public String toString(){
        return "Turn "+ face.toString() +" face "+ direction.toString();
    }
}
