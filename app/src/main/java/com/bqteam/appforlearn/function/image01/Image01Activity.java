package com.bqteam.appforlearn.function.image01;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bqteam.appforlearn.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author charles
 */
public class Image01Activity extends AppCompatActivity {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.image01)
    Image01View image01;

    private boolean draw = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image01);
        ButterKnife.bind(this);


        image01.setOnTouchListener((v1, event) -> {
            if (draw) {
                int width = v1.getWidth();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getX() > width / 2) {
                        image01.increaseWatershed();
                    } else {
                        image01.decreaseWatershed();
                    }
                    Log.d("x", event.getX() + "");
                }
            } else {
                draw01();
                draw = true;
            }
            return false;
        });
    }

    private void draw01() {

        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();

        List<ColorItem> colorList = new ArrayList<>();
        double scale = (double)(bitmap.getWidth()) / image.getMeasuredWidth();
        int offset = 10;
        int h = image.getMeasuredWidth() / offset;
        int v = image.getMeasuredHeight() / offset;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < v; j++) {
                if ((int) (i * offset * scale) < bitmap.getWidth() && (int) (j * offset * scale) < bitmap.getHeight()) {
                    int color = bitmap.getPixel((int) (i * offset * scale), (int) (j * offset * scale));
                    int r = (color & 0xff0000) >> 16;
                    int g = (color & 0xff00) >> 8;
                    int b = (color & 0xff);
                    int gray = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                    colorList.add(new ColorItem(i * offset, j * offset, gray));
                }
            }
        }

        image01.setColorList(colorList);
        image.setVisibility(View.INVISIBLE);
    }
}
