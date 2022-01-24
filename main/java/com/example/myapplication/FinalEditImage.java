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

        // gets a reference to the image view that has the image that the user will see
        imgPicture = new ImageView(this);
        bit_image = ((MyApplication) this.getApplication()).getOriginal_image();
        //ImageView new_img = new ImageView(this);


        //imgPicture.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //public void onGlobalLayout() {
                //Gets the true space between the title and save button and the devices width
                View title = findViewById(R.id.imageView);
                View button = findViewById(R.id.save_button);


                //Gets the width of entire device
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                distance_width_true = displayMetrics.widthPixels;////
                device_height = displayMetrics.heightPixels;////


                int new_height = bit_image.getHeight();
                int new_width = bit_image.getWidth();


                // Is user inputted picture more horizontal or vertical?
                if(new_width >= new_height) {
                    float scale_factor = distance_width_true / new_width;

                    //Set imageview width to device's width
                    //imgPicture.getLayoutParams().width = (int)distance_width_true;

                    //Set imageview height to computed height
                    //imgPicture.getLayoutParams().height = (int) Math.floor(scale_factor * new_height);


                    imgPicture.setLayoutParams(new ViewGroup.LayoutParams(
                            (int) distance_width_true,
                            (int) Math.floor(scale_factor * new_height)));

//                    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams((int)distance_width_true,
//                            (int) Math.floor(scale_factor * new_height));
//                    layoutParams.gravity= Gravity.CENTER;
//                    imgPicture.setLayoutParams(layoutParams);

                    this.addContentView(imgPicture,new ViewGroup.LayoutParams(
                            (int) distance_width_true,
                            (int) Math.floor(scale_factor * new_height)));


                    //Move to center of screen

                    // get the center point of the screen
                    int centerX = (int)(distance_width_true / 2);
                    int centerY = (int)(device_height / 2) - 150;   //fix 150

                    // set the imageview minus half the width and height so its centered
                    imgPicture.setTranslationX(centerX - (distance_width_true / 2));
                    imgPicture.setTranslationY(centerY - (int)(Math.floor(scale_factor * new_height)) / 2);
                    //imgPicture.setY(100);

                } else {
                    double scale_factor = distance_height_true / new_height;

                    //Set imageview height to max distance between title and button
                    //imgPicture.getLayoutParams().height = (int)distance_height_true;

                    //Set imageview width to computed width
                   // imgPicture.getLayoutParams().width = (int) Math.floor(scale_factor * new_width);

                    imgPicture.setLayoutParams(new ViewGroup.LayoutParams(
                            (int) Math.floor(scale_factor * new_width),
                            (int) distance_height_true));

//                    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams((int)distance_width_true,
//                            (int) Math.floor(scale_factor * new_height));
//                    layoutParams.gravity= Gravity.CENTER;
//                    imgPicture.setLayoutParams(layoutParams);

                    this.addContentView(imgPicture,new ViewGroup.LayoutParams(
                            (int) Math.floor(scale_factor * new_width),
                            (int) distance_height_true));


                    //Move to center of screen

                    // get the center point of the screen
                    int centerX = (int)(distance_width_true / 2);
                    int centerY = (int)(device_height / 2) - fix_device_height;

                    // set the imageview minus half the width and height so its centered
                    imgPicture.setTranslationX(centerX - (int)(Math.floor(scale_factor * new_width)) / 2);
                    imgPicture.setTranslationY(centerY - (distance_height_true / 2));
                }


//                TextView d = findViewById(R.id.text);
//                d.setText(imgPicture.getLayoutParams().height + "      " +  imgPicture.getLayoutParams().width);


                // show the image to the user

                imgPicture.setImageBitmap(bit_image);
                RectF s = calculateRectOnScreen(imgPicture);
                TextView ss = findViewById(R.id.textView2);
                ss.setText(s.top +  "    " + s.left  + "                 " + s.right + "     " + s.bottom);


                // Removes the listener to prevent being called again by future layout events:
                //imgPicture.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            //}
        //});







        //int x = imgPicture.getTop() + imgPicture.getDrawable().getBounds().top;
        //int y = imgPicture.getLeft() + imgPicture.getDrawable().getBounds().left;

        //RectF oneRect3 = calculateRectOnScreen(imgPicture);



        //Rect s = getBitmapPositionInsideImageView(imgPicture);


        //ss.setText(s.top +  "    " + s.left  + "                 " + s.right + "     " + s.bottom);

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