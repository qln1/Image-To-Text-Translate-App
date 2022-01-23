package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FinalEditImage extends AppCompatActivity {

    private ImageView imgPicture;
    private Bitmap bit_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_edit_image);

        // gets a reference to the image view that has the image that the user will see
        imgPicture = findViewById(R.id.imgPicture);

        // show the image to the user
        ImageView imgPicture = findViewById(R.id.imgPicture);
        bit_image = ((MyApplication) this.getApplication()).getOriginal_image();
        imgPicture.setImageBitmap(bit_image);


        imgPicture.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int height = imgPicture.getHeight();
                int width = imgPicture.getWidth();
                int x = imgPicture.getLeft();
                int y = imgPicture.getTop();
                TextView d = findViewById(R.id.text);
                d.setText(height + " " +  width);

                // Removes the listener to prevent being called again by future layout events:
                imgPicture.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });



    }


    public void SaveImageClick(View view) {
        //Save image


        //Display Toast saying image saved successful or not

        //if yes then move back to first screen
        //Reset all global variables
        ((MyApplication) this.getApplication()).setCurrent_textbox_num(1);
        ((MyApplication) this.getApplication()).setOriginal_image(null);

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        //if no then don't move
    }
}