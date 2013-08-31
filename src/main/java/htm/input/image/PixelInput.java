
package htm.input.image;

import htm.input.Input;

/**
 *
 * @author david.charubini
 */
public class PixelInput extends Input<int[]> {

    private static int R_IDX = 0;
    private static int G_IDX = 1;
    private static int B_IDX = 2;
    
    private final int x;
    private final int y;
    
    private static String genKey (int x, int y) {
        return x+"_"+y;
    }
    
    PixelInput(int i, int j) {
        super(genKey(i, j));
        this.x = i;
        this.y = j;
    }
    
    @Override
    public String getDisplayString() {
        return this.isActive() ? "+" : "_";
    }

    public int[] getLocation() {
        return new int[] {this.x, this.y};
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    @Override
    public boolean isActive() {
        int[] rgb = this.getValue();
        return rgb[R_IDX] < 120 || rgb[G_IDX] < 120 || rgb[B_IDX] < 120;
    }
}
