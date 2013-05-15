import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Model3 implements IBouncingBallsModel {

	private final double areaWidth;
	private final double areaHeight;
	private ArrayList<Ball> myBalls;


	public Model3(double width, double height) {
		this.areaWidth = width;
		this.areaHeight = height;
		myBalls = new ArrayList<Ball>();

		myBalls.add(new Ball(6,3,2.3,5,2,1));
		myBalls.add(new Ball(2,7,2,2,2,2));
		myBalls.add(new Ball(3,2,-1,2,0.5,0.5));
	}

	@Override
	public void tick(double deltaT) {

		for (int i=0; i<this.myBalls.size(); i++) {
			Ball b = this.myBalls.get(i);



			for(int j=i+1; j<this.myBalls.size(); j++){ //Only checks with subsequent balls (eliminates redundant checks)
				Ball b2 = this.myBalls.get(j);
				if(b.collidesWith(b2)){
					handleCollision(b,b2);
					continue;
				}
			}

			if (b.x < b.r || b.x > areaWidth - b.r)
				b.vx *= -1;
			if (b.y < b.r || b.y > areaHeight - b.r)
				b.vy *= -1;
			b.x += b.vx * deltaT;
			b.y += b.vy * deltaT;
			b.vy += -9.82*deltaT;
		}
	}

	private void handleCollision(Ball b1, Ball b2) {
		//Find angle between original and collision coordinate system
		double distance = Math.sqrt((Math.pow(b1.x-b2.x, 2) + Math.pow(b1.y-b2.y, 2)));
		double angle = Math.asin(Math.abs(b1.y-b2.y)/distance); 

		//Translate velocity from rect to polar
		PolCord v1 = new PolCord(b1.vx, b1.vy);
		PolCord v2 = new PolCord(b2.vx, b2.vy);

		//The polar coordinate angle in a rotated coordinate system is the old angle minus the rotation angle
		v1.angle -= angle;
		v2.angle -= angle;

		//The y-component of the force should be conserved, so we save it away
		//("As with collision with a wall the velocity tangential to the surface of the ball is not affected")
		double v1y = v1.getY();
		double v2y = v2.getY();

		//Calculate the collision (the x-component is what changes)
		double I = (b1.m * v1.getX() + b2.m * v2.getX());
		double R = -(v2.getX()-v1.getX());
		v1 = new PolCord(((-I+b2.m*R)/(b1.m+b2.m)), v1.getY());
		v2 = new PolCord(( ( I/(b1.m+b2.m) ) + R-( (b1.m*R)/(b1.m+b2.m) ) ), v2.getY());


		//Rotate the coordinate system back to the original one
		v1.angle += angle;
		v2.angle += angle;


		//Translate velocity back from polar to rect
		b1.vx = v1.getX();
		b1.vy = v1.getY();
		b2.vx = v2.getX();
		b2.vy = v2.getY();
	}

	@Override
	public List<Ellipse2D> getBalls() {
		List<Ellipse2D> ballShapes = new ArrayList<Ellipse2D>();
		for (Ball b : this.myBalls)
			ballShapes.add(b.getShape());
		return ballShapes;
	}

	private class Ball{
		private double x, y, vx, vy, r, m;
		private List<Ball> collidingWith;
		/**
		 * 
		 * @param x x-coord
		 * @param y y-coord
		 * @param vx x-velocity
		 * @param vy y-velocity
		 * @param r radius
		 * @param m mass
		 */
		private Ball(double x, double y, double vx, double vy, double r, double m){
			this.x = x;
			this.y = y;
			this.vx = vx;
			this.vy = vy;
			this.r = r;
			this.m = m;
			this.collidingWith = new ArrayList<Ball>();
		}
		private Ellipse2D getShape(){
			return new Ellipse2D.Double(this.x - this.r, this.y - this.r, 2 * this.r, 2 * this.r);
		}
		private boolean collidesWith(Ball other){
			double distance = Math.sqrt((Math.pow(this.x-other.x, 2) + Math.pow(this.y-other.y, 2)));
			return distance <= (this.r + other.r);
		}
	}
	private class PolCord{
		private double length, angle;
		private PolCord(double x, double y){
			this.length = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
			this.angle = Math.atan(y/x);
		}
		private double getX(){
			return (Math.cos(angle))*length;
		}
		private double getY(){
			return (Math.sin(angle))*length;
		}
	}
}
