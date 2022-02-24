package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.TextViewCompat;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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

    private int fix_img_width = 0;
    private int fix_img_height = 0;
    private int fix_img_pos_x = 0;
    private int fix_img_pos_y = 0;
    private TextView[] all_text_box = new TextView[10];
    private Bitmap final_image_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_edit_image);



        ActivityCompat.requestPermissions(FinalEditImage.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(FinalEditImage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        layout = findViewById(R.id.linear);
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
            // Helps center the image
            int centerX = (int)(distance_width_true / 2);
            int centerY = (int)(device_height / 2) - 150;   //fix 150

            fix_img_width = (int) distance_width_true;
            fix_img_height = (int) Math.floor(scale_factor * new_height);
            fix_img_pos_x = (int) (centerX - (distance_width_true / 2));
            fix_img_pos_y = (int)(centerY - (int)(Math.floor(scale_factor * new_height)) / 2);

        } else {//More vertical
            float scale_factor = distance_height_true / new_height;
            // Helps center image
            int centerX = (int)(distance_width_true / 2);
            int centerY = (int)(device_height / 2) - fix_device_height;

            fix_img_width = (int) Math.floor(scale_factor * new_width);
            fix_img_height = (int) distance_height_true;
            fix_img_pos_x = (int) (centerX - (int)(Math.floor(scale_factor * new_width)) / 2);
            fix_img_pos_y = (int) (centerY - (distance_height_true / 2));
        }
        // Setting layout params to our RelativeLayout
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(fix_img_width, fix_img_height);

        // Setting position of our ImageView
        layoutParams.leftMargin = fix_img_pos_x;
        layoutParams.topMargin = fix_img_pos_y;

        //Set drawing on
        layout.setDrawingCacheEnabled(true);

        // Finally adding the imageView to RelativeLayout and its position
        layout.addView(imgPicture, layoutParams);

        // show the image to the user
        imgPicture.setImageBitmap(bit_image);

        GenerateTextBoxes();
    }



    private void GenerateTextBoxes(){
        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius(12);
        shape.setColor(ColorParser(((MyApplication) this.getApplication()).getBackground_color()));

        for(int i = 0; i < 10; i++){
            if(((MyApplication) this.getApplication()).getAll_translated(i) != null){//not null
                TextView tv = new TextView(this);

                tv.setBackground(shape);
                tv.setTextColor(ColorParser(((MyApplication) this.getApplication()).getFont_color()));
                tv.setTypeface(Typeface.create(((MyApplication) this.getApplication()).getFont_family(), Typeface.NORMAL));

                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(0,0);
                layoutParams.width = ((MyApplication) this.getApplication()).getAll_cropped_size(i).x;
                layoutParams.height = ((MyApplication) this.getApplication()).getAll_cropped_size(i).y;
                tv.setLayoutParams(layoutParams);
                TextViewCompat.setAutoSizeTextTypeWithDefaults(tv,TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);

                tv.setText(((MyApplication) this.getApplication()).getAll_translated(i));
                tv.setX(fix_img_pos_x + ((MyApplication) this.getApplication()).getAll_points(i).x);
                tv.setY(fix_img_pos_y + ((MyApplication) this.getApplication()).getAll_points(i).y);

                tv.setOnTouchListener(mOnTouchListenerTv2);
                layout.addView(tv, i+1);
                all_text_box[i] = tv;
            }
        }
    }




    public int ColorParser(String color){
        switch(color){
            case "Red":
                return Color.RED;
            case "None":
                return Color.TRANSPARENT;
            case "Orange":
                return Color.parseColor("#ffa500");
            case "Yellow":
                return Color.YELLOW;
            case "Green":
                return Color.GREEN;
            case "Blue":
                return Color.BLUE;
            case "Magenta":
                return Color.MAGENTA;
            case "Pink":
                return Color.parseColor("#ffc0cb");
            case "Black":
                return Color.BLACK;
            case "White":
                return Color.WHITE;
            case "Grey":
                return Color.GRAY;
        }
        return Color.TRANSPARENT;
    }






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
        //Save Image to device
        if(SaveImage(CreateSaveableImage())) {
            Toast.makeText (getApplicationContext (), "Image Successfully Saved",
                    Toast.LENGTH_SHORT).show ();
            //Reset all variables
            ((MyApplication) this.getApplication()).resetEverything();
            //Move back to start
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else {
            Toast.makeText (getApplicationContext (), "Image Failed To Save: Please try again",
                    Toast.LENGTH_SHORT).show ();
        }
    }



    public boolean SaveImage(Bitmap bitmap){
        //Bitmap bitmap = ((BitmapDrawable) imgPicture.getDrawable()).getBitmap();  //Imageview to bitmap code

        File file = new File("/storage/emulated/0/Pictures", System.currentTimeMillis() + ".jpg");
        if (!file.exists()) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                return true;
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }



    public Bitmap CreateSaveableImage() {
        Bitmap bit = Bitmap.createBitmap(layout.getDrawingCache());
        layout.setDrawingCacheEnabled(false);
        return Bitmap.createBitmap(bit, fix_img_pos_x, fix_img_pos_y, imgPicture.getWidth(), imgPicture.getHeight());
    }

}