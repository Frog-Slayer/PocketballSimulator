package player;

public class Player {
	//order와 balls는 건드리면 됩니다.
	private int order = 0;
	private double[][] balls;

	private double power = 100f;
	private double angle = 0f;

	public Player(int order, double[][] balls){
		this.order = order;
		this.balls = balls;
	}

	/**
	 * 적절히 공을 넣을 수 있도록 getAngle()과 getPower() 메서드를 변경해야 합니다.
	 * 스스로 로직을 짜봅시다.
	*/
	public double getAngle() {
		//TODO
		for (int i = 1; i < balls.length; i++) {
			if (isObjectBall(i)) {
				return angle = getAngle(0, i);
			}
		}

		return angle = 0f;
	}
	
	public double getPower() {
		//TODO
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
		if (balls[n][0] == 0) return false;

		int ballCount = 0;
		for (int i = 1; i < balls.length - 1; i++){
			if (balls[i][0] == 0) continue;
			if (order == 0 && i % 2 == 1) ballCount++;
			else if (order == 1 && i % 2 == 0) ballCount++;
		}

		if (ballCount == 0) return n == balls.length - 1;

		if (n == balls.length - 1) return false;

		if (order == 0) return n % 2 == 1;
		return n % 2 == 0;
	}
}
