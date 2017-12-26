package com.example.lelik.rp5;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import java.util.HashMap;

/**
 * Created by lelik on 04.04.2017.
 */

enum ImageType {
    Clouds,
    Rain
}

class ResourceHelper {
    private static final HashMap<String, Point> cssClouds = new HashMap<>();
    private static final HashMap<String, Point> cssRainsDyn = new HashMap<>();
    private static final HashMap<String, Point> cssRainsConv = new HashMap<>();
    private static final HashMap<String, Point> cssRainsConvStrong = new HashMap<>();

    private Bitmap spriteClouds;
    private Bitmap spriteRainsDyn;
    private Bitmap spriteRainsConv;
    private Bitmap spriteRainsConvStrong;

    private HashMap<String, Bitmap> bitmaps = new HashMap<>();
    private HashMap<WindDirection, Bitmap> windImages = new HashMap<>();

    ResourceHelper(Context context) {
        cssClouds.put("cd0", new Point(215, 102));
        cssClouds.put("cd1", new Point(245, 102));
        cssClouds.put("cd2", new Point(185, 102));
        cssClouds.put("cd3", new Point(125, 102));
        cssClouds.put("cd4", new Point(155, 102));
        cssClouds.put("cd5", new Point(275, 102));
        cssClouds.put("cd6", new Point(362, 102));
        cssClouds.put("cd7", new Point(116, 136));
        cssClouds.put("cn0", new Point(206, 136));
        cssClouds.put("cn1", new Point(176, 136));
        cssClouds.put("cn2", new Point(236, 136));
        cssClouds.put("cn3", new Point(305, 102));
        cssClouds.put("cn4", new Point(296, 136));
        cssClouds.put("cn5", new Point(266, 136));
        cssClouds.put("cn6", new Point(146, 136));
        cssClouds.put("cn7", new Point(69, 136));

        cssRainsConv.put("wp_0", new Point(162, 108));
        cssRainsConv.put("wp_0_left", new Point(36, 81));
        cssRainsConv.put("wp_o1d1", new Point(18, 81));
        cssRainsConv.put("wp_o1d1_left", new Point(108, 81));
        cssRainsConv.put("wp_o1d2", new Point(90, 81));
        cssRainsConv.put("wp_o1d2_left", new Point(0, 81));
        cssRainsConv.put("wp_o1d3", new Point(216, 54));
        cssRainsConv.put("wp_o1d3_left", new Point(126, 54));
        cssRainsConv.put("wp_o1d4", new Point(108, 54));
        cssRainsConv.put("wp_o1d4_left", new Point(144, 54));
        cssRainsConv.put("wp_o1d5", new Point(162, 54));
        cssRainsConv.put("wp_o1d5_left", new Point(198, 54));
        cssRainsConv.put("wp_o1d6", new Point(180, 54));
        cssRainsConv.put("wp_o1d6_left", new Point(126, 81));
        cssRainsConv.put("wp_o1d7", new Point(144, 81));
        cssRainsConv.put("wp_o1d7_left", new Point(90, 108));
        cssRainsConv.put("wp_o1d8", new Point(72, 108));
        cssRainsConv.put("wp_o1d8_left", new Point(108, 108));
        cssRainsConv.put("wp_o1d9", new Point(126, 108));
        cssRainsConv.put("wp_o1d9_left", new Point(144, 108));
        cssRainsConv.put("wp_o1d10_left", new Point(72, 81));
        cssRainsConv.put("wp_o2d1", new Point(54, 108));
        cssRainsConv.put("wp_o2d1_left", new Point(162, 81));
        cssRainsConv.put("wp_o2d2", new Point(198, 81));
        cssRainsConv.put("wp_o2d2_left", new Point(216, 81));
        cssRainsConv.put("wp_o2d3", new Point(18, 108));
        cssRainsConv.put("wp_o2d3_left", new Point(0, 108));
        cssRainsConv.put("wp_o2d4", new Point(90, 54));
        cssRainsConv.put("wp_o2d4_left", new Point(72, 54));
        cssRainsConv.put("wp_o2d5", new Point(162, 0));
        cssRainsConv.put("wp_o2d5_left", new Point(144, 0));
        cssRainsConv.put("wp_o2d5", new Point(162, 0));
        cssRainsConv.put("wp_o2d6", new Point(180, 0));
        cssRainsConv.put("wp_o2d6_left", new Point(198, 0));
        cssRainsConv.put("wp_o2d7", new Point(0, 27));
        cssRainsConv.put("wp_o2d7_left", new Point(216, 0));
        cssRainsConv.put("wp_o2d8_left", new Point(108, 0));
        cssRainsConv.put("wp_o3d1", new Point(36, 0));
        cssRainsConv.put("wp_o3d1_left", new Point(72, 0));
        cssRainsConv.put("wp_o3d2", new Point(18, 27));
        cssRainsConv.put("wp_o3d2_left", new Point(36, 27));

        cssRainsConvStrong.put("wp_o1d10_strong", new Point(54, 0));

        cssRainsDyn.put("wp_o1d1d", new Point(54, 81));
        cssRainsDyn.put("wp_o1d1d_left", new Point(72, 81));
        cssRainsDyn.put("wp_o1d2d", new Point(18, 81));
        cssRainsDyn.put("wp_o1d2d_left", new Point(0, 81));
        cssRainsDyn.put("wp_o1d3d", new Point(90, 54));
        cssRainsDyn.put("wp_o1d3d_left", new Point(108, 54));
        cssRainsDyn.put("wp_o1d4d", new Point(126, 54));
        cssRainsDyn.put("wp_o1d4d_left", new Point(90, 81));
        cssRainsDyn.put("wp_o1d5d", new Point(108, 81));
        cssRainsDyn.put("wp_o1d5d_left", new Point(72, 108));
        cssRainsDyn.put("wp_o1d6d", new Point(90, 108));
        cssRainsDyn.put("wp_o1d6d_left", new Point(108, 108));
        cssRainsDyn.put("wp_o1d7d", new Point(54, 108));
        cssRainsDyn.put("wp_o1d7d_left", new Point(36, 108));
        cssRainsDyn.put("wp_o1d8d", new Point(126, 81));
        cssRainsDyn.put("wp_o1d8d_left", new Point(0, 108));
        cssRainsDyn.put("wp_o1d9d", new Point(18, 108));
        cssRainsDyn.put("wp_o1d9d_left", new Point(72, 54));
        cssRainsDyn.put("wp_o3d1d", new Point(126, 0));
        cssRainsDyn.put("wp_o3d1d_left", new Point(0, 27));
        cssRainsDyn.put("wp_o3d2d", new Point(90, 0));
        cssRainsDyn.put("wp_o3d2d_left", new Point(72, 0));
        cssRainsDyn.put("wp_o3d3d", new Point(0, 0));
        cssRainsDyn.put("wp_o3d3d_left", new Point(18, 0));
        cssRainsDyn.put("wp_o3d4d", new Point(36, 0));
        cssRainsDyn.put("wp_o3d4d_left", new Point(54, 0));
        cssRainsDyn.put("wp_o3d5d", new Point(18, 27));

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        spriteClouds = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_w, options);
        spriteRainsConv = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_wp_pr_conv, options);
        spriteRainsConvStrong = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_wp_pr_conv_strong, options);
        spriteRainsDyn = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_wp_pr_dyn, options);

        windImages.put(null, BitmapFactory.decodeResource(context.getResources(), R.drawable.empty));
        windImages.put(WindDirection.N, BitmapFactory.decodeResource(context.getResources(), R.drawable.n));
        windImages.put(WindDirection.NW, BitmapFactory.decodeResource(context.getResources(), R.drawable.nw));
        windImages.put(WindDirection.NE, BitmapFactory.decodeResource(context.getResources(), R.drawable.ne));
        windImages.put(WindDirection.S, BitmapFactory.decodeResource(context.getResources(), R.drawable.s));
        windImages.put(WindDirection.SW, BitmapFactory.decodeResource(context.getResources(), R.drawable.sw));
        windImages.put(WindDirection.SE, BitmapFactory.decodeResource(context.getResources(), R.drawable.se));
        windImages.put(WindDirection.E, BitmapFactory.decodeResource(context.getResources(), R.drawable.e));
        windImages.put(WindDirection.W, BitmapFactory.decodeResource(context.getResources(), R.drawable.w));
    }

    Bitmap GetWindBitmap(WindDirection direction) {
        return windImages.get(direction);
    }

    Bitmap GetBitmap(String css, ImageType type) {
        if (bitmaps.containsKey(css)) {
            return bitmaps.get(css);
        }

        Bitmap piece;
        switch (type) {
            case Clouds: {
                if (!cssClouds.containsKey(css)) {
                    return null;
                }
                Point pnt = cssClouds.get(css);
                piece = Bitmap.createBitmap(spriteClouds, pnt.x, pnt.y, 30, 25);
                break;
            }
            case Rain: {
                Point pnt = cssRainsConv.get(css);
                if (pnt != null) {
                    piece = Bitmap.createBitmap(spriteRainsConv, pnt.x, pnt.y, 17, 27);
                    break;
                }

                pnt = cssRainsConvStrong.get(css);
                if (pnt != null) {
                    piece = Bitmap.createBitmap(spriteRainsConvStrong, pnt.x, pnt.y, 17, 27);
                    break;
                }

                pnt = cssRainsDyn.get(css);
                if (pnt != null) {
                    piece = Bitmap.createBitmap(spriteRainsDyn, pnt.x, pnt.y, 17, 27);
                    break;
                }
                return null;
            }
            default:
                return null;
        }

        bitmaps.put(css, piece);
        return piece;
    }
}
