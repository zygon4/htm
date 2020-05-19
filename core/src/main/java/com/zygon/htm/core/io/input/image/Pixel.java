
package com.zygon.htm.core.io.input.image;

/**
 * TBD: this could/should be integrated into the Pixel Input
 *
 * @author zygon
 */
public class Pixel {

    public static final int R_IDX = 0;
    public static final int G_IDX = 1;
    public static final int B_IDX = 2;
    
    private final int x;
    private final int y;
    private final int[] value;
    
    private static String genKey (int x, int y) {
        return x+"_"+y;
    }
    
    public Pixel (int i, int j, int[] value) {
        this.x = i;
        this.y = j;
        this.value = value;
    }
    
    public int[] getLocation() {
        return new int[] {this.x, this.y};
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int[] getValue() {
        return this.value;
    }
    
    public boolean isActive() {
        int[] rgb = this.getValue();
        return rgb[R_IDX] < 120 || rgb[G_IDX] < 120 || rgb[B_IDX] < 120;
    }
}
