package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FinalEditImage extends AppCompatActivity {

    private ImageView imgPicture;
    private Bitmap bit_image;
    private float title_location = 0;
    private float button_location = 0;

    private int fix_device_height = 120;
    private float distance_height_true = 1500;
    private float distance_width_true = 0;
    private float device_height = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_edit_image);

        imgPicture = new ImageView(this);
        bit_image = ((MyApplication) this.getApplication()).getOriginal_image();

        //Gets the true space between the title and save button and the devices width. Fix later
        View title = findViewById(R.id.imageView);
        View button = findViewById(R.id.save_button);

        //Gets the width of entire device
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        distance_width_true = displayMetrics.widthPixels;
        device_height = displayMetrics.heightPixels;

        //Original Dimensions of Image
        int new_height = bit_image.getHeight();
        int new_width = bit_image.getWidth();


        // Create ImageView
        // Is user inputted picture more horizontal or vertical?
        if(new_width >= new_height) {
            float scale_factor = distance_width_true / new_width;

            imgPicture.setLayoutParams(new ViewGroup.LayoutParams(
                    (int) distance_width_true,
                    (int) Math.floor(scale_factor * new_height)));

            this.addContentView(imgPicture,new ViewGroup.LayoutParams(
                    (int) distance_width_true,
                    (int) Math.floor(scale_factor * new_height)));

            //Move to center of screen
            // get the center point of the screen
            int centerX = (int)(distance_width_true / 2);
            int centerY = (int)(device_height / 2) - 150;   //fix 150

            // set the imageview minus half the width and height so its centered
            imgPicture.setX(centerX - (distance_width_true / 2));
            imgPicture.setY(centerY - (int)(Math.floor(scale_factor * new_height)) / 2);
        } else {
            double scale_factor = distance_height_true / new_height;

            imgPicture.setLayoutParams(new ViewGroup.LayoutParams(
                    (int) Math.floor(scale_factor * new_width),
                    (int) distance_height_true));

            this.addContentView(imgPicture,new ViewGroup.LayoutParams(
                    (int) Math.floor(scale_factor * new_width),
                    (int) distance_height_true));

            //Move to center of screen
            // get the center point of the screen
            int centerX = (int)(distance_width_true / 2);
            int centerY = (int)(device_height / 2) - fix_device_height;

            // set the imageview minus half the width and height so its centered
            imgPicture.setX(centerX - (int)(Math.floor(scale_factor * new_width)) / 2);
            imgPicture.setY(centerY - (distance_height_true / 2));
        }

        // show the image to the user
        imgPicture.setImageBitmap(bit_image);
//        RectF s = calculateRectOnScreen(imgPicture);
        TextView ss = new TextView(this);
        ss.setTextSize(30);
        ss.setText("ASDFGH");
        ss.setY(600);
        ss.setX(700);
        this.addContentView(ss, new ViewGroup.LayoutParams(
                (int) 500,
                (int) 500));
        //ss.setText(imgPicture.getHeight() +  "    " + imgPicture.getWidth()  + "                 " + imgPicture.getTop() + "     " + imgPicture.getLeft());
    }


    public static RectF calculateRectOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return new RectF(location[0], location[1], location[0] + view.getMeasuredWidth(), location[1] + view.getMeasuredHeight());
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