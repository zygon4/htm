/*
 * 
 */
package com.zygon.htm.motor.sensor.util;

/**
 *
 * @author zygon
 */
public class ImageUtils {

    public static final int RGB_RED_IDX = 0;
    public static final int RGB_GREEN_IDX = 1;
    public static final int RGB_BLUE_IDX = 2;

    private ImageUtils() {
    }

    public static int getValue(int[] argb) {

        int red = argb[0] << 16;
        int green = argb[1] << 8;
        int blue = argb[2];

        return (red | green | blue) & 0xFFFFFF;
    }

    public static int[] getRGB(int argb) {
        return new int[]{
            (argb >> 16) & 0xff, //red
            (argb >> 8) & 0xff, //green
            (argb) & 0xff //blue
        };
    }

    public static byte[] getRGBAsByteArray(int argb) {
        return new byte[]{
            (byte) ((argb >> 16) & 0xff), //red
            (byte) ((argb >> 8) & 0xff), //green
            (byte) ((argb) & 0xff) //blue
        };
    }
}
