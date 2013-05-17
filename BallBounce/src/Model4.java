import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;


public class Model4 implements IBouncingBallsModel {

	private final double areaWidth;
	private final double areaHeight;
	private Ball a, b;

	public Model4(double width, double height) {
		this.areaWidth = width;
		this.areaHeight = height;

		a = new Ball(7,3,2,1.1,1,1);
		b = new Ball(3,7,2,2,2,2);
	}

	@Override
	public void tick(double deltaT) {
		a.vy += -9.82*deltaT/2;
		b.vy += -9.82*deltaT/2;

		if(a.collidesWith(b)){
			handleCollision(a,b);

		}

		//Ball B

		if ((b.x < b.r || b.x > areaWidth - b.r) && !b.inWallX){
			b.inWallX = true;
			b.vx *= -1;
		}
		else
			b.inWallX = false;


		if ((b.y < b.r || b.y > areaHeight - b.r) && !b.inWallY){
			b.inWallY = true;
			b.vy *= -1;
		}
		else
			b.inWallY = false;


		//Ball A
		if ((a.x < a.r || a.x > areaWidth - a.r) && !a.inWallX){
			a.inWallX = true;
			a.vx *= -1;
		}
		else
			a.inWallX = false;


		if ((a.y < a.r || a.y > areaHeight - a.r) && !a.inWallY){
			a.inWallY = true;
			a.vy *= -1;
		}
		else
			a.inWallY = false;



	

		a.x += a.vx * deltaT;
		a.y += a.vy * deltaT;
		b.x += b.vx * deltaT;
		b.y += b.vy * deltaT;
		
		a.vy += -9.82*deltaT/2;
		b.vy += -9.82*deltaT/2;

	}

	private void handleCollision(Ball b1, Ball b2) {
		//Find angle between original and collision coordinate system
		//double distance = Math.sqrt((Math.pow(b1.x-b2.x, 2) + Math.pow(b1.y-b2.y, 2)));
		double angle = Math.atan((b2.y-b1.y)/(b2.x-b1.x));	//Math.asin((b1.y-b2.y)/distance); 

		//Translate velocity from rect to polar
		PolCord v1 = new PolCord(b1.vx, b1.vy);
		PolCord v2 = new PolCord(b2.vx, b2.vy);

		//The polar coordinate angle in a rotated coordinate system is the old angle minus the rotation angle
		v1.angle -= angle;
		v2.angle -= angle;

		//Calculate the collision (the x-component is what changes)
		double I = (b1.m * v1.getX() + b2.m * v2.getX());
		double R = -(v2.getX() - v1.getX());
		v1 = new PolCord(	((-I+b2.m*R)/(b1.m+b2.m))							, v1.getY());
		v2 = new PolCord(	( ( I/(b1.m+b2.m) ) + R-( (b2.m*R)/(b1.m+b2.m) ) )	, v2.getY());


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
		ballShapes.add(a.getShape());
		ballShapes.add(b.getShape());

		return ballShapes;
	}

	private class Ball{
		private double x, y, vx, vy, r, m;
		private boolean inWallX, inWallY;
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
			this.inWallX = false;
			this.inWallY = false;
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
			if(x<0)
				this.angle += Math.PI;
		}
		private double getX(){
			return (Math.cos(angle))*length;
		}
		private double getY(){
			return (Math.sin(angle))*length;
		}
	}
}
