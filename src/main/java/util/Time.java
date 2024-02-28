package util;

public class Time {
    // 记录程序启动的时间
    private static float timeStarted = System.nanoTime();

    public static float getTime(){
        return (float) ((System.nanoTime() - timeStarted)*1E-9); // 转换为单位秒
    }
}
