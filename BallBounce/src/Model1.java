import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;

public class Model1 implements IBouncingBallsModel {

	private final double areaWidth;
	private final double areaHeight;

	private double x, y, vx, vy, r;

	public Model1(double width, double height) {
		this.areaWidth = width;
		this.areaHeight = height;
		x = 1;
		y = 4;
		vx = 2.3;
		vy = 1;
		r = 1;
	}

	@Override
	public void tick(double deltaT) { //y' = vy; vy' = -g GRAVITATION (tillståndet i y-led, (y, vy))
		if (x < r || x > areaWidth - r) {
			vx *= -1;
		}
		if (y < r || y > areaHeight - r) {
			vy *= -1;
		}


		x += vx * deltaT;
		y += vy * deltaT;
		vy += -9.82*deltaT;
	}

	@Override
	public List<Ellipse2D> getBalls() {
		List<Ellipse2D> myBalls = new LinkedList<Ellipse2D>();
		myBalls.add(new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r));
		return myBalls;
	}
}
