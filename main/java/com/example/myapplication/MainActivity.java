package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // This method invoked when user clicks button and will allow
    // user to select picture from gallery
    public void ImageGalleyClick(View view) {
        // invoke the image gallery using an implicit intent.
        Intent photo_picker = new Intent (Intent.ACTION_PICK);

        // where do we want to find the data?
        File picture_directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String picture_directory_path = picture_directory.getPath();

        // get a URI representation
        Uri data = Uri.parse(picture_directory_path);

        // set the data and type. Get all image types.
        photo_picker.setDataAndType (data, "image/*");

        // we will invoke this activity and get image back from it
        startActivityForResult (photo_picker, 20);  //this request code can be anything and is just an id
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // if we are here, everything processed successfully.
            if (requestCode == 20) {
                // if we are here, we are hearing back from the image gallery.
                // the address of the image on the SD Card.
                Uri imageUri = data.getData();

                // declare a stream to read the image data from the SD Card.
                InputStream inputStream;
                // we are getting an input stream, based on the URI of the image.
                try {
                    inputStream = getContentResolver().openInputStream(imageUri);

                    // get a bitmap from the stream.
                    Bitmap image = BitmapFactory.decodeStream (inputStream);

                    // show the image to the user
                    // ImageView imgPicture = findViewById(R.id.imgPicture);/////
                    // imgPicture.setImageBitmap(image);//////

                    //Store the image globally
                    ((MyApplication) this.getApplication()).setOriginal_image(image);
                    ((MyApplication) this.getApplication()).setImage_file_location(imageUri);

                    Intent i = new Intent(this, LanguageAndTextBoxInfo.class);
                    startActivity(i);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // show a message to the user indicating that the image is unavailable.
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}