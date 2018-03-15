package com.bqteam.appforlearn.function.image01;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bqteam.appforlearn.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author charles
 */
public class Image01Activity extends AppCompatActivity {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.image01)
    Image01View image01;
    @BindView(R.id.addImage)
    Button addImage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image01);
        ButterKnife.bind(this);


        image01.setOnTouchListener((v1, event) -> {
            int width = v1.getWidth();
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (event.getX() > width / 2) {
                    image01.increaseWatershed();
                } else {
                    image01.decreaseWatershed();
                }
            }
            return false;
        });
    }

    @OnClick({R.id.addImage, R.id.image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addImage:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);

                image01.setVisibility(View.INVISIBLE);
                break;
            case R.id.image:
                draw01();
                break;
            default:
                break;
        }
    }

    private void draw01() {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();

        List<ColorItem> colorList = new ArrayList<>();
        double scale = (double) (bitmap.getWidth()) / image.getMeasuredWidth();
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
        image01.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        //获取系统返回的照片的Uri
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        //从系统表中查询指定Uri对应的照片
        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        //获取照片路径
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
        image.setImageBitmap(bitmap);

        super.onActivityResult(requestCode, resultCode, data);
    }
}
