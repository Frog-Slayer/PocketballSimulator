import java.util.Arrays;

public class Player {
	int order = 0;
	double balls[][];

	double power = 100f;
	double angle = 0f;

	public Player(int order, double balls[][]){
		this.order = order;
		this.balls = balls;
	}

	public double getAngle() {
		for (int i = 1; i < balls.length - 1; i++) if (isObjectBall(i)) angle = getAngle(0, i);
		return angle;
	}
	
	public double getPower() {
		return power;
	}

	private double getDist(int i, int j) {
		double xDiff = balls[i][0] - balls[j][0];
		double yDiff = balls[i][1] - balls[j][1];

		return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
	}

	private double getAngle(int i, int j){
		double xDiff = balls[j][0] - balls[i][0];
		double yDiff = balls[j][1] - balls[i][1];

		return Math.toDegrees(Math.atan2(yDiff, xDiff));
	}

	private boolean isObjectBall(int n){

		return true;
	}


}
