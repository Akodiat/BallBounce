import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.List;

/**
 * Extends Animator with capability to draw a bouncing balls model.
 * 
 * This class can be left unmodified for the bouncing balls lab. :)
 * 
 * @author Oscar Soderlund
 * 
 */
@SuppressWarnings("serial")
public final class BouncingBalls extends Animator {

	private static final double PIXELS_PER_METER = 30;
	private static final Color COLOR = Color.cyan;

	private IBouncingBallsModel model;
	private double modelHeight;
	private double deltaT;

	@Override
	public void init() {
		super.init();
		double modelWidth = canvasWidth / PIXELS_PER_METER;
		modelHeight = canvasHeight / PIXELS_PER_METER;
		model = new Model4(modelWidth, modelHeight);
	}

	@Override
	protected void drawFrame(Graphics2D g) {
		// Clear the canvas
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, canvasWidth, canvasHeight);
		// Update the model
		model.tick(deltaT);
		List<Ellipse2D> balls = model.getBalls();
		// Transform balls to fit canvas
		g.setColor(COLOR);
		g.scale(PIXELS_PER_METER, -PIXELS_PER_METER);
		g.translate(0, -modelHeight);
		for (Ellipse2D b : balls) {
			//g.setColor(Color.getHSBColor((float)Math.random(), (float)Math.random(), (float)Math.random()));
			g.fill(b);
			//g.setColor(Color.DARK_GRAY);
			//g.draw(b);
			//g.setColor(COLOR);
		}
	}

	@Override
	protected void setFrameRate(double fps) {
		super.setFrameRate(fps);
		// Update deltaT according to new frame rate
		deltaT = 1 / fps;
	}
}
