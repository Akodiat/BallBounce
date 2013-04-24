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

		PolCord p = new PolCord(5,3);
		//JOptionPane.showInputDialog("x=5, y=3 -->\nangle="+p.angle+" \nlenght="+p.length+" --> \nx="+p.getX()+" y="+p.getY());
		myBalls.add(new Ball(2,3,2.3,1,1,1));
		myBalls.add(new Ball(2,5,2,2,1,1));
		myBalls.add(new Ball(3,2,-1,2,0.5,0.5));
	}

	@Override
	public void tick(double deltaT) { //y' = vy; vy' = -g GRAVITATION (tillståndet i y-led, (y, vy))

		for (int i=0; i<this.myBalls.size(); i++) {
			Ball b = this.myBalls.get(i);
			if (b.x < b.r || b.x > areaWidth - b.r)
				b.vx *= -1;
			else if (b.y < b.r || b.y > areaHeight - b.r)
				b.vy *= -1;

			else
				for(int j=i+1; j<this.myBalls.size(); j++){ //Only checks with subsequent balls (eliminates redundant checks)
					Ball b2 = this.myBalls.get(j);
					if(b.collidesWith(b2)){
						handleCollision(b,b2);
					}
				}

			b.x += b.vx * deltaT;
			b.y += b.vy * deltaT;
			b.vy += -9.82*deltaT;
		}
	}

	private void handleCollision(Ball b1, Ball b2) {
		//Translate velocity from rect to polar
		PolCord c1 = new PolCord(b1.vx, b1.vy);
		PolCord c2 = new PolCord(b2.vx, b2.vy);

		double I = (b1.m * c1.length + b2.m * c2.length);
		double R = -(c2.length-c1.length);

		c1.length = ((-I+b2.m*R)/(b1.m+b2.m));
		c2.length = ( ( I/(b1.m+b2.m) ) + R-( (b1.m*R)/(b1.m+b2.m) ) );

		//c1.angle += Math.PI;
		//c2.angle += Math.PI;

		//Translate velocity back from polar to rect
		b1.vx = c1.getX();
		b1.vy = c1.getY();
		b2.vx = c2.getX();
		b2.vy = c2.getY();


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
			/*	if (distance <= (this.r + other.r) && !this.collidingWith.contains(other)){
				this.collidingWith.add(other);
				return true;
			}
			if (distance > (this.r + other.r) && this.collidingWith.contains(other))
				this.collidingWith.remove(other);
			return false;
			 */
		}
	}
	private class PolCord{
		private double length, angle;
		private PolCord(double x, double y){
			this.length = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
			this.angle = Math.atan(y/x);
		}
		//		private PolCord(){
		//			this.length = this.angle = 0;
		//		}
		private double getX(){
			return (Math.cos(angle))*length;
		}
		private double getY(){
			return (Math.sin(angle))*length;
		}
	}
}
