package lit.lads.cataloguev3;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class color {
    private String TAG = color.class.getSimpleName();
    public int red = 0;
    public int blue = 0;
    public int yellow = 0;
    public int magenta = 0;
    public int green = 0;
    public int cyan = 0;
    public int white = 0;
    public int black = 0;
    private Bitmap image;

    enum Field {
        red,
        blue,
        cyan,
        magenta,
        green,
        yellow,
        white,
        black
    }
    public color(Bitmap bitmap) {
        this.image = bitmap;
    }

    public String getBiggest() {
        this.classify();
        List<Integer> list = new ArrayList<Integer>();
        list.add(red);
        list.add(blue);
        list.add(cyan);
        list.add(magenta);
        list.add(green);
        list.add(yellow);
        list.add(white);
        list.add(black);

        Collections.sort(list);
        int max = list.get(list.size() - 1);
        Log.i(TAG, Integer.toString(max));
        if (max == white) return "WHITE";
        else if (max == blue) return "BLUE";
        else if (max == cyan) return "CYAN";
        else if (max == magenta) return "MAGENTA";
        else if (max == green) return "GREEN";
        else if (max == yellow) return "YELLOW";
        else if (max == red) return "RED";
        else return "BLACK";
    }

    public void classify() {
        int w = image.getWidth() - 1;
        int h = image.getHeight() - 1;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int rgb = image.getPixel(w, h);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb) & 0xFF;

                float[] hsb = new float[3];
                Color.RGBToHSV(r, g, b, hsb);
                if (hsb[1] < 0.1 && hsb[2] > 0.9) white++;
                else if (hsb[2] < 0.1) black++;
                else {
                    float deg = hsb[0] * 360;
                    if (deg >= 0 && deg < 30) red++;
                    else if (deg >= 30 && deg < 90) yellow++;
                    else if (deg >= 90 && deg < 150) green++;
                    else if (deg >= 150 && deg < 210) cyan++;
                    else if (deg >= 210 && deg < 270) blue++;
                    else if (deg >= 270 && deg < 330) magenta++;
                    else red++;
                }

            }

        }
        Log.i(TAG, "Test");

        Field largestField = Field.white;
        int largestValue = white;
        if(blue > largestValue) {
            largestField = Field.blue;
            largestValue = blue;
        }
        if(cyan > largestValue) {
            largestField = Field.cyan;
            largestValue = cyan;
        }
        if(magenta > largestValue) {
            largestField = Field.magenta;
            largestValue = magenta;
        }
        if(green > largestValue) {
            largestField = Field.green;
            largestValue = green;
        }
        if(yellow > largestValue) {
            largestField = Field.yellow;
            largestValue = yellow;
        }
        if(red > largestValue) {
            largestField = Field.red;
            largestValue = red;
        }
        if(black > largestValue) {
            largestField = Field.black;
            largestValue = black;
        }
        switch(largestField) {
            case red:
                break;
            case blue:
                break;
            case cyan:
                break;
            case magenta:
                break;
            case green:
                break;
            case yellow:
                break;
            case white:
                break;
            case black:
                break;
        }
    }

}
