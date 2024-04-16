package model;

import view.Display;


import java.awt.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;


public class Game {
	public static Ball[] Balls;
	public static double[][] balls; //0번째는 큐 볼. balls[][0]은 x좌표, [1]은 y좌표, 마지막 번호의 공이 검은 공

	public Toolkit toolkit = Toolkit.getDefaultToolkit();
	public static final int[][] HOLES = { { 0, 0 }, { 127, 0 }, { 254, 0 }, { 0, 127 }, { 127, 127 }, { 254, 127 } };

	private boolean isMoving = false;
	private final int playerCount;

	private int order = 0;
	private int turnCount = 0;
	private boolean isPlaying;
	private boolean pocketNotObject = false;

	private int[] fouls;
	private int[] playerBallCount;

	private LocalTime time;

	private Player[] players;
	private Display display;

	public Game(int playerCount, int ballCountForEachPlayer){
		this.playerCount = playerCount;

		//공 생성
		generateBalls(ballCountForEachPlayer);

		//새로운 플레이어 생성
		generatePlayers(playerCount);

		//디스플레이 생성
		this.display = new Display("Pocket model.Ball", Constant.TABLE_WIDTH, Constant.TABLE_HEIGHT, Constant.SIZE_UNIT);
		this.display.setBalls(Balls);

		//게임의 상태
		order = 0;
		isPlaying = true;
		turnCount = 2;
		playerBallCount = new int[playerCount];
		for (int i = 0; i < playerCount; i++) playerBallCount[i] = ballCountForEachPlayer + 1;


		System.out.println("----------------  게  임  시  작  -----------------");
		for (int i = 0; i < Balls.length; i++){
			if (i == Balls.length - 1) {
				System.out.println("    " + i + "번 공의 색은 검정");
				continue;
			}
			switch (i){
				case 0: System.out.print("    " + i + "번 공의 색은 흰색"); break;
				case 1: System.out.print("    " + i + "번 공의 색은 노랑"); break;
				case 2: System.out.print("    " + i + "번 공의 색은 빨강"); break;
				case 3: System.out.print("    " + i + "번 공의 색은 분홍"); break;
				case 4: System.out.print("    " + i + "번 공의 색은 초록"); break;
				case 5: System.out.print("    " + i + "번 공의 색은 검정"); break;
			}
			if (i % 2 == 1) System.out.println();
		}
		System.out.println("-------------------------------------------------");
		play();
	}

	/**
	 * 임의의 위치에 공을 생성
	 */
	private void generateBalls(int ballCountForEachPlayer) {
		balls = new double[ballCountForEachPlayer * playerCount + 2][2];
		time = LocalTime.now();

		//흰 공의 위치 설정
		balls[0][0] = 254f/4;
		balls[0][1] = 127f/2;
		//검은 공의 위치 설정
		balls[balls.length-1][0] = 254f/4*3;
		balls[balls.length-1][1] = 127f/2;
		
		for (int i = 1; i < balls.length - 1; i++) {
			double rand = Math.random();
			balls[i][0] = rand * (Constant.TABLE_WIDTH - 15) + 6;
			rand = Math.random();
			balls[i][1] = rand * (Constant.TABLE_HEIGHT - 15) + 6;
			if (prevBallCollision(i) || tableCollision(i)) i--; 
		}

		Balls = new Ball[balls.length];
		for (int i = 0; i < balls.length; i++) {
			Balls[i] = new Ball(i, balls[i][0], balls[i][1]);
		}
	}

	/**
	 * 플레이어 생성
	 * @param playerCount 만들 플레이어의 수
	 */
	private void generatePlayers(int playerCount){
		players = new Player[playerCount];
		fouls = new int[playerCount];
		for (int i = 0; i < playerCount; i++) players[i] = new Player(i, balls);
	}

	private boolean prevBallCollision(int cnt) {
		for (int i = 0; i < cnt; i++) {
			if (getDist(balls[i], balls[cnt]) < Ball.DIAMETER) return true;
		}
		if (getDist(balls[cnt], balls[balls.length-1]) < Ball.DIAMETER) return true;
		return false;
	}
	
	private boolean tableCollision(int i) {
		if (balls[i][0] > Constant.TABLE_WIDTH - Ball.DIAMETER/2) return true;
		if (balls[i][0] < Ball.DIAMETER/2) return true;
		if (balls[i][1] < Ball.DIAMETER/2) return true;
		return balls[i][1] > Constant.TABLE_HEIGHT - Ball.DIAMETER/2;
	}
	
	private double getDist(double []a, double[]b) {
		return (a[0] - b[0])*(a[0] - b[0]) + (a[1] - b[1])*(a[1] - b[1]);
	}

	private void play() {
		while (isPlaying) {
			System.out.println("[" + (order + 1) +"번 플레이어의 " + (turnCount/2) +"번 째 차례]");
			//플레이어로부터 힘과 각도를 받아서
			double angle = players[order].getAngle();
			double power = players[order].getPower();
			if (power > 100f) power = 100f;
			if (power < 0f) power = 0f;

			power *= Constant.POWER_UNIT;
			//수구에 힘과 각도를 넣기
			Balls[0].addPower(power, angle);

			time = LocalTime.now();
			isMoving = true;

			int whiteFirstHit = 0;
			int pocket = 0;
			boolean continueOrder = false;
			pocketNotObject = false;

			while (isMoving) {
				isMoving = false;
				if (time.until(LocalTime.now(), ChronoUnit.MILLIS) < Constant.SKIP_TICKS) {
					isMoving = true;
					continue;
				}
				for (int i = 0; i < balls.length; i++) {
					if (Math.abs(Balls[i].xVeloc) > Constant.VELOC_BOUND || Math.abs(Balls[i].yVeloc) > Constant.VELOC_BOUND) {
						isMoving = true;
					}
				}
				int hit = tryMove();
				if (whiteFirstHit == 0) whiteFirstHit = hit;

				int u = update();
				if (pocket == 0) pocket = u;
				else if (u < 0 && pocket >= 0) pocket = u;

				display.draw();
				time = LocalTime.now();
				toolkit.sync();
			}

			if (!Balls[0].isValid) {
				System.out.println("흰 공을 넣었으므로 흰 공의 위치를 재설정하고 게임을 재진행합니다.");
				Balls[0].isValid = true;
				Balls[0].x = Constant.TABLE_WIDTH / 2;
				Balls[0].y = Constant.TABLE_HEIGHT / 2;
				balls[0] = new double[]{Balls[0].x, Balls[0].y};
				try {
					Thread.sleep(1000);
				}
				catch (Exception e) {

				}
				continueOrder = false;
			}
			if (isPlaying) {
				if (whiteFirstHit == 0) {
					fouls[order]++;
					System.out.println("아무 공도 맞히지 못했습니다. 파울(" + fouls[order] + ")");
					continueOrder = false;
				} else if (!isObjectBall(whiteFirstHit)) {
					fouls[order]++;
					System.out.println("목적구가 아닌 공(" + whiteFirstHit + "번)을 먼저 맞혔습니다. 파울(" + fouls[order] + ")");
					continueOrder = false;
				} else if (pocketNotObject) {
					fouls[order]++;
					System.out.println("목적구가 아닌 궁을 포켓했습니다. 파울(" + fouls[order] + ")");
					continueOrder = false;
				}

				if (fouls[order] == 3) {
					System.out.println((order + 1) + "번 플레이어가 3번의 파울로 패배했습니다.");
					isPlaying = false;
				}

				if (pocket > 0) continueOrder = true;

				for (int i = 0; i < playerCount; i++){
					if (playerBallCount[i] == 0){
						System.out.println((i + 1) + "번 플레이어의 승리.");
						isPlaying = false;
					}
				}

				if (!continueOrder) setNextOrder();
			}

			System.out.println("-------------------------------------------------");
		}
		System.out.println("게임 종료.");
	}

	private void setNextOrder(){
		order = (order + 1) % playerCount;
		turnCount += 1;
	}
	
	private int tryMove() {
		int firstHit = 0;
		for (int i = 0; i < balls.length; i++) 	if (Balls[i].isValid) Balls[i].calcNext();

		for (int i = 0; i < balls.length; i++) {
			if (!Balls[i].isValid) continue;
			for (int j = 0; j < balls.length; j++) {
				if (i == j) continue;
				if (!Balls[j].isValid) continue;
				if (Balls[i].collides(Balls[j])) {
					if ((i == 0 || j == 0) && firstHit == 0) {
						firstHit = i + j;
					}
				}
				Balls[i].collidesTable();
			}
		}
		return firstHit;
	}

	private int update() {
		int pocket = 0;
		for (int i = 0; i < Balls.length; i++) {
			Balls[i].x = Balls[i].nx;
			Balls[i].y = Balls[i].ny;
			balls[i][0] = Balls[i].x;
			balls[i][1] = Balls[i].y;
			int h = checkHoles(i);
			if (pocket == 0) pocket = h;
			else if (h < 0 && pocket >= 0) pocket = -1;
		}
		return pocket;
	}

	private boolean isObjectBall(int ballNum){
		if (ballNum == 0) return false;
		if (playerCount == 2) {
			if (ballNum % 2 != order && ballNum != Balls.length - 1) return true;
			if (playerBallCount[order] == 1 && ballNum == Balls.length - 1) return true;
		}
		if (ballNum != Balls.length - 1) return true;
		return playerBallCount[order] == 1 && ballNum == Balls.length-1;
	}

	private int checkHoles(int idx){
		if (!Balls[idx].isValid) return 0;
		double x = Balls[idx].x;
		double y = Balls[idx].y;

        for (int[] hole : HOLES) {
            if (getDist(new double[]{x, y}, new double[]{(double) hole[0], (double) hole[1]}) < Constant.HOLE_SIZE * Constant.HOLE_SIZE) {
                if (Balls[idx].isValid) System.out.printf("%d번 공 포켓!!\n", idx);
                Balls[idx].isValid = false;
                Balls[idx].yVeloc = Balls[idx].xVeloc = 0;
                if (!isObjectBall(idx)) {
                    if (idx == Balls.length - 1 && playerBallCount[order] != 1) {
                        System.out.printf("공이 남아있는데 마지막 공을 넣었으므로 %d번 플레이어의 패배입니다.\n", order);
                        isPlaying = false;
                    } else if (idx != 0) {
                        playerBallCount[playerCount - order - 1]--;
                    }
                    return -1;
                }
                System.out.println("목적구를 넣는 데 성공했습니다.");
                playerBallCount[order]--;
                return 1;
            }
        }
		return 0;
	}
}
