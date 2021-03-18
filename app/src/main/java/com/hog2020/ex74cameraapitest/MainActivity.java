package com.hog2020.ex74cameraapitest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    CameraView cv;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cv=findViewById(R.id.cv);
        iv=findViewById(R.id.iv);

        //동적 퍼미션(버전 영향없이 호환성버전으로 퍼미션 체크하기)
        String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
        int checkResult=ActivityCompat.checkSelfPermission(this,permission[0]);
        if (checkResult== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,permission,0);
        }
    }

    public void clickcapture(View view) {
        cv.camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //캡쳐한 사진 데이터를 Byte[]로 전달해줌
                //이미지뷰에 보여주려면 Bitmap객체로 생성 해야만 함
                Bitmap bm= BitmapFactory.decodeByteArray(data,0,data.length);
                iv.setImageBitmap(bm);

                //휴대폰에 저장되게 하려면 외부저장소에 직접 byte[] data를 .jpg로 지정[파일입출력]

                File file= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
                String fileName= sdf.format(new Date())+".JPG";
                File file1= new File(file,fileName);
                
                //만들어진 파일경로에 바이트 배열 데이터 출력하기 =파일저장하기
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(data);
                    fos.flush();
                    fos.close();

                    Toast.makeText(MainActivity.this, "저장완료", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //캡쳐하면 미리보기가 멈추므로 다시 실행되도록
                cv.camera.startPreview();

            }
        });
    }
}