package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
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
    private RelativeLayout layout;

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
        if(new_width >= new_height) {//More horizontal
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
        } else {//More vertical
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

//        TextView ss = new TextView(this);
//        ss.setTextSize(30);
//        ss.setText("ASDFGH");
//
//        ss.setY(imgPicture.getY());/////////////////////////
//        ss.setX(imgPicture.getX());
//
//        this.addContentView(ss, new ViewGroup.LayoutParams(
//                (int) 100,
//                (int) 100));

        //layout = (RelativeLayout) findViewById(R.id.linear);
        //GenerateTextBoxes();


        //ss.setText(imgPicture.getHeight() +  "    " + imgPicture.getWidth()  + "                 " + imgPicture.getTop() + "     " + imgPicture.getLeft());
    }



    private void GenerateTextBoxes(){
        String font_fam = ((MyApplication) this.getApplication()).getFont_family();
        String font_col = ((MyApplication) this.getApplication()).getFont_color();
        String back_col = ((MyApplication) this.getApplication()).getBackground_color();

        Point p = ((MyApplication) this.getApplication()).getAll_points(0);
        Point size = ((MyApplication) this.getApplication()).getAll_cropped_size(0);
        String f = ((MyApplication) this.getApplication()).getAll_translated(0);

        for(int i = 0; i < 10; i++){
            int d = 0;
            if(((MyApplication) this.getApplication()).getAll_translated(i) != null){//not null
                TextView tv = new TextView(this);

                //tv.setTextColor(ColorParser(((MyApplication) this.getApplication()).getFont_color()));
                tv.setTextColor(Color.BLACK);
                //tv.setBackgroundColor(ColorParser(((MyApplication) this.getApplication()).getBackground_color()));
                tv.setFontFeatureSettings(((MyApplication) this.getApplication()).getFont_family());

                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(0,0);
                layoutParams.width = ((MyApplication) this.getApplication()).getAll_cropped_size(i).x;
                layoutParams.height = ((MyApplication) this.getApplication()).getAll_cropped_size(i).y;
                tv.setLayoutParams(layoutParams);
                TextViewCompat.setAutoSizeTextTypeWithDefaults(tv,TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);

                tv.setText(((MyApplication) this.getApplication()).getAll_translated(i));
                tv.setX(imgPicture.getX() + ((MyApplication) this.getApplication()).getAll_points(i).x);
                tv.setY(imgPicture.getY() + ((MyApplication) this.getApplication()).getAll_points(i).y);

                tv.setOnTouchListener(mOnTouchListenerTv2);
                layout.addView(tv);
            }
        }
    }




//    public int ColorParser(String color){
//        if() {
//            return Color.RED;
//        } else if() {
//            return Color.
//        }
//    }






    public final View.OnTouchListener mOnTouchListenerTv2 = new View.OnTouchListener() {
        int prevX, prevY;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final RelativeLayout.LayoutParams par = (RelativeLayout.LayoutParams) v.getLayoutParams();
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE: {
                    par.topMargin += (int) event.getRawY() - prevY;
                    prevY = (int) event.getRawY();
                    par.leftMargin += (int) event.getRawX() - prevX;
                    prevX = (int) event.getRawX();
                    v.setLayoutParams(par);
                    return true;
                }
                case MotionEvent.ACTION_UP: {
                    par.topMargin += (int) event.getRawY() - prevY;
                    par.leftMargin += (int) event.getRawX() - prevX;
                    v.setLayoutParams(par);
                    return true;
                }
                case MotionEvent.ACTION_DOWN: {
                    prevX = (int) event.getRawX();
                    prevY = (int) event.getRawY();
                    par.bottomMargin = -2 * v.getHeight();
                    par.rightMargin = -2 * v.getWidth();
                    v.setLayoutParams(par);
                    return true;
                }
            }
            return false;
        }
    };







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