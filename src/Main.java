import model.Game;
import java.util.Scanner;

public class Main {
	private static final int MAX_PLAYER = 2;
	private static final int MAX_BALL = 2;
	
	public static void main(String[] args) throws Exception{
		int playerCnt, ballCnt;
		Scanner sc = new Scanner(System.in);
		System.out.println("플레이어 수(최대 2), 플레이어 별 공 수(최대 2)를 빈 칸 하나를 두고 입력해주세요.");
		playerCnt = sc.nextInt();
		ballCnt = sc.nextInt();

		if (playerCnt == 0 || ballCnt == 0 || playerCnt > MAX_PLAYER || ballCnt > MAX_BALL) {
			System.out.println("재입력");
			return;
		}

		Game game = new Game(playerCnt, ballCnt);
	}
}
