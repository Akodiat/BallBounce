import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;

public class Model2 implements IBouncingBallsModel {

	private final double areaWidth;
	private final double areaHeight;
	private List<Ball> myBalls;


	public Model2(double width, double height) {
		this.areaWidth = width;
		this.areaHeight = height;
		myBalls = new LinkedList<Ball>();

		myBalls.add(new Ball(1,4,2.3,1,1));
		myBalls.add(new Ball(3,3,2.3,1,1));
	}

	@Override
	public void tick(double deltaT) { //y' = vy; vy' = -g GRAVITATION (tillståndet i y-led, (y, vy))
		for (Ball b : this.myBalls) {
			if (b.x < b.r || b.x > areaWidth - b.r)
				b.vx *= -1;
			if (b.y < b.r || b.y > areaHeight - b.r)
				b.vy *= -1;

			b.x += b.vx * deltaT;
			b.y += b.vy * deltaT;
			b.vy += -9.82*deltaT;
		}
	}

	@Override
	public List<Ellipse2D> getBalls() {
		List<Ellipse2D> ballShapes = new LinkedList<Ellipse2D>();
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
	}
}
