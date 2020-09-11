package com.java.chenxin.universal;

public class DoubleClickCheck {
    private static long lastClickTime = 0;
    private final static int SPACE_TIME = 300;

    public synchronized static boolean isDoubleClick() {
        long currentTime = System.currentTimeMillis();
        boolean isClickDouble = false;
        if (currentTime - lastClickTime < SPACE_TIME) {
            isClickDouble = true;
        }
        lastClickTime = currentTime;
        return isClickDouble;
    }
}
