import java.awt.geom.Ellipse2D;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class Model3 implements IBouncingBallsModel {

	private final double areaWidth;
	private final double areaHeight;
	private ArrayList<Ball> myBalls;


	public Model3(double width, double height) {
		this.areaWidth = width;
		this.areaHeight = height;
		myBalls = new ArrayList<Ball>();

		myBalls.add(new Ball(1,4,2.3,1,1));
		myBalls.add(new Ball(3,3,2.3,1,1));
	}

	@Override
	public void tick(double deltaT) { //y' = vy; vy' = -g GRAVITATION (tillståndet i y-led, (y, vy))
		for (int i=0; i<this.myBalls.size(); i++) {
			Ball b = this.myBalls.get(i);
			if (b.x < b.r || b.x > areaWidth - b.r)
				b.vx *= -1;
			if (b.y < b.r || b.y > areaHeight - b.r)
				b.vy *= -1;

			b.x += b.vx * deltaT;
			b.y += b.vy * deltaT;
			b.vy += -9.82*deltaT;
			
			for(int j=i; j<this.myBalls.size(); j++){ //Only checks with subsequent balls (eliminates redundant checks)
				Ball b2 = this.myBalls.get(j);
				if(b.collidesWith(b2)){
					b.vx *= -1;
					b2.vx *= -1;
				}
			}
		}
	}

	@Override
	public List<Ellipse2D> getBalls() {
		List<Ellipse2D> ballShapes = new ArrayList<Ellipse2D>();
		for (Ball b : this.myBalls)
			ballShapes.add(b.getShape());
		return ballShapes;
	}

	private class Ball{
		private double x, y, vx, vy, r;
		private Ball(double x, double y, double vx, double vy, double r){
			this.x = x;
			this.y = y;
			this.vx = vx;
			this.vy = vy;
			this.r = r;
		}
		private Ellipse2D getShape(){
			return new Ellipse2D.Double(this.x - this.r, this.y - this.r, 2 * this.r, 2 * this.r);
		}
		private boolean collidesWith(Ball other){
			double distance = Math.sqrt((Math.pow(this.x-other.x, 2) + Math.pow(this.y-other.y, 2)));
			return distance < (this.r + other.r);
		}
	}
}
