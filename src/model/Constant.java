package model;

import java.awt.*;

public class Constant {
    public static final Color BALL_COLOR[] = {Color.WHITE, Color.YELLOW, Color.RED, Color.PINK, Color.GREEN, Color.BLACK};
    public static final double HOLE_SIZE = 8f;

    /*
        흰 공을 쳤을 때 가해지는 힘 배수. 공이 너무 빠르면 충돌이 제대로 되지 않을 수 있음
     */
    public static final double POWER_UNIT = 0.1f;

    // 당구대의 반발 계수입니다. 0 초과 1 이하의 값
    public static final double TABLE_COR = 0.8;

    // 바닥의 마찰력입니다. 0 이상 1 미만의 값
    public static final double FRICTION = 0.01;

    //최대 파울 개수
    public static final int MAX_FOUL = 3;

    public static final int FPS = 60;

    /**
     * Don't Touch Below!!!
     */
    static final int SIZE_UNIT = 5;
    static final int TABLE_WIDTH = 254;
    static final int TABLE_HEIGHT = 127;
    static final double VELOC_BOUND = 0.003f;
    static final int SKIP_TICKS = 1000/FPS;
}
