package com.bqteam.appforlearn.function.image01;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bqteam.appforlearn.R;
import com.bqteam.appforlearn.util.FileUtil;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
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

    private boolean draw = false;

    private final int IMAGE_REQUEST_CODE = 0;
    private final int CAMERA_REQUEST_CODE = 1;
    private final int RESULT_REQUEST_CODE = 2;
    private final String items[] = {"拍照", "从相册里选择"};

//    private File imageCrop;
//    private Uri imageUri;
//    private Uri imageUriCrop;

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

    @OnClick(R.id.addImage)
    public void onViewClicked() {
        alterImage();
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
//        image.setVisibility(View.INVISIBLE);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
//            grantResults) {
//        if (requestCode == CAMERA_REQUEST_CODE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                takePhoto();
//            } else {
//                Toast.makeText(Image01Activity.this, "您拒绝了拍照请求", Toast.LENGTH_SHORT).show();
//            }
//            return;
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    private void alterImage() {
//        imageCrop = new File(FileUtil.getImageDirectory(), "imageCrop.jpeg");
//        imageUri = Uri.fromFile(new File(FileUtil.getImageDirectory(), "image.jpeg"));
//        imageUriCrop = Uri.fromFile(imageCrop);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("修改头像");
        dialog.setItems(items, (dialog1, which) -> {
//            switch (which) {
//                case 0:
//                    if (ContextCompat.checkSelfPermission(Image01Activity.this, Manifest.permission.CAMERA)
//                            == PackageManager.PERMISSION_GRANTED) {
//                        takePhoto();
//                    } else {
//                        ActivityCompat.requestPermissions(Image01Activity.this,
//                                new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
//                    }
//                    break;
//                case 1:
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, IMAGE_REQUEST_CODE);
//                    break;
//                default:
//                    break;
//            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create();
        dialog.show();
    }

//    private void takePhoto() {
//        image01.setVisibility(View.INVISIBLE);
//
//        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intentFromCapture.putExtra("return-data", false);
//        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        intentFromCapture.putExtra("noFaceDetection", true);
//        startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case IMAGE_REQUEST_CODE:
//                cropPhoto(data.getData());
//                Bitmap bitmap = data.getParcelableExtra("data");
//                image.setImageBitmap(bitmap);
                Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor =getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);  //获取照片路径
                cursor.close();
                Bitmap bitmap= BitmapFactory.decodeFile(picturePath);
                image.setImageBitmap(bitmap);
                break;
//            case CAMERA_REQUEST_CODE:
//                cropPhoto(imageUri);
//                break;
            case RESULT_REQUEST_CODE:
//                showNewImage();
//                    Bitmap bitmap = data.getParcelableExtra("data");
//                    image.setImageBitmap(bitmap);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        //裁剪之后，保存在裁剪文件中，关键
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriCrop);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    private void showNewImage() {
        image01.setVisibility(View.VISIBLE);
        Picasso.with(Image01Activity.this).load(new File(FileUtil.getImageDirectory(), "image.jpeg"))
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(image);

    }
}
