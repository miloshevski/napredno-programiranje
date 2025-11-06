package lab1.lab1_2;


import java.util.Arrays;
import java.util.Scanner;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

interface Movable{
    public void moveUp() throws ObjectCanNotBeMovedException;
    public void moveDown() throws ObjectCanNotBeMovedException;
    public void moveLeft() throws ObjectCanNotBeMovedException;
    public void moveRight() throws ObjectCanNotBeMovedException;
    public int getCurrentXPosition();
    public int getCurrentYPosition();
    TYPE typeOf();
}

class MovablePoint implements Movable{

    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public MovablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getxSpeed() {
        return xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

    @Override
    public String toString() {
        return String.format("Movable point with coordinates (%d,%d)",x,y);
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException{
        if(y + ySpeed > MovablesCollection.Y_MAX || y + ySpeed < 0)
        {
            throw new ObjectCanNotBeMovedException(new MovablePoint(x, y + ySpeed));
        }
        y += ySpeed;
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if(y - ySpeed > MovablesCollection.Y_MAX || y - ySpeed < 0)
        {
            throw new ObjectCanNotBeMovedException(new MovablePoint(x, y - ySpeed));
        }
        y -= ySpeed;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if(x - xSpeed < 0 || x - xSpeed > MovablesCollection.X_MAX){
            throw new ObjectCanNotBeMovedException(new MovablePoint(x - xSpeed,y));
        }
        x -= xSpeed;
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if(x + xSpeed > MovablesCollection.X_MAX || x + xSpeed < 0){
            throw new ObjectCanNotBeMovedException(new MovablePoint(x + xSpeed, y));
        }
        x += xSpeed;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    @Override
    public TYPE typeOf() {
        return TYPE.POINT;
    }
}

class MovableCircle implements Movable{

    private int radius;
    private MovablePoint center;

    public MovableCircle(int radius, MovablePoint center) {
        this.radius = radius;
        this.center = center;
    }

    public int getRadius() {
        return radius;
    }

    public MovablePoint getCenter() {
        return center;
    }

    @Override
    public String toString() {
        return String.format("Movable circle with center coordinates (%d,%d) and radius %d",center.getCurrentXPosition(),center.getCurrentYPosition(),radius);
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        center.moveUp();
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        center.moveDown();
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        center.moveLeft();
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        center.moveRight();
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }

    @Override
    public TYPE typeOf() {
        return TYPE.CIRCLE;
    }
}

class ObjectCanNotBeMovedException extends Exception{
    Movable m;

    public ObjectCanNotBeMovedException(Movable m) {
        this.m = m;
    }

    @Override
    public String getMessage() {
        return "Point (" + m.getCurrentXPosition() + "," + m.getCurrentYPosition() + ") is out of bounds";
    }
}

class MovablesCollection{
    private Movable[] movables;
    static int X_MAX = 0;
    static int Y_MAX = 0;

    public MovablesCollection(int x_MAX,int y_MAX){
        X_MAX = x_MAX;
        Y_MAX = y_MAX;
        movables = new Movable[0];
    }

    public static void setxMax(int x){
        X_MAX = x;
    }
    public static void setyMax(int y){
        Y_MAX = y;
    }

    void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        if(m.typeOf() == TYPE.CIRCLE){
            MovableCircle c = (MovableCircle) m;
            if(m.getCurrentXPosition() + c.getRadius() > X_MAX || m.getCurrentXPosition() - c.getRadius() < 0 || m.getCurrentYPosition() - c.getRadius() < 0 || m.getCurrentYPosition() + c.getRadius() > Y_MAX){
                throw new MovableObjectNotFittableException(m);
            }

        }
        if(m.getCurrentXPosition() > X_MAX || m.getCurrentXPosition() < 0 || m.getCurrentYPosition() > Y_MAX || m.getCurrentYPosition() < 0)
        {
            throw new MovableObjectNotFittableException(m);
        }

        movables = Arrays.copyOf(movables, movables.length + 1);
        movables[movables.length - 1] = m;
    }
    void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction){
        for(Movable m : movables){
            if(m.typeOf() == type){
                try{
                    if(direction == DIRECTION.UP){
                        m.moveUp();
                    }else if(direction == DIRECTION.DOWN){
                        m.moveDown();
                    }else if(direction == DIRECTION.LEFT){
                        m.moveLeft();
                    }else {
                        m.moveRight();
                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Collection of movable objects with size " + movables.length + ":\n");
        for(Movable m : movables){
            sb.append(m.toString() + '\n');
        }
        return sb.toString();
    }
}

class MovableObjectNotFittableException extends Exception{
    Movable m;
    public MovableObjectNotFittableException(Movable m){
        this.m = m;
    }

    @Override
    public String getMessage() {
        return m.toString().replace("coordinates ", "") + " can not be fitted into the collection";
    }
}

public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            try{
                if (Integer.parseInt(parts[0]) == 0) { //point
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                } else { //circle
                    int radius = Integer.parseInt(parts[5]);
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());


    }


}
