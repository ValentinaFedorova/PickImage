package com.example.pickpicture;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    final int GALLERY_REQUEST_CODE = 123;

    ImageView imV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imV = findViewById(R.id.photo_image_view);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == GALLERY_REQUEST_CODE){
            Uri uri = data.getData();
            Log.d("mytag","request: "+uri);

            // запрашиваем данные контакта по его Uri
            final String[] columns = {
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.WIDTH,
                    MediaStore.Images.Media.HEIGHT
            };
            Cursor cursor = getContentResolver()
                    .query(uri, columns, null, null, null);



            Log.d("mytag","cursor size: "+cursor.getCount());
            cursor.moveToFirst();
            int width = cursor.getInt(1); // поле - ширина
            int height = cursor.getInt(2); // поле - ширина

            Log.d("mytag","image w: "+width + " image h: " +height);

            Bitmap bitmap = null;
            ImageView imageView = (ImageView) findViewById(R.id.photo_image_view);

            switch(requestCode) {
                case GALLERY_REQUEST_CODE:
                    if(resultCode == RESULT_OK){
                        Uri selectedImage = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        imageView.setImageBitmap(bitmap);
                    }
            }

        }
    }

    public Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public void onPickImage(View view){
        Intent intent=new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");

        startActivityForResult(intent,GALLERY_REQUEST_CODE);



    }
}


