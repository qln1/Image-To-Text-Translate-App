package com.example.myapplication;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;

public class MyApplication extends Application {
    Bitmap original_image = null;
    Uri image_file_location = null;

    String lang_from_before = "";
    String lang_from_after = "";
    int num_box = 0;
    int current_textbox_num = 1;

    String font_family = "";
    String font_color = "";
    String background_color = "";

    Point[] all_cropped_size = new Point[10];
    String[] all_translated = new String[10];
    Point[] all_points = new Point[10];
    //ArrayList<Uri> all_cropped = new ArrayList<Uri>(1);


    public void resetEverything() {
        for(int i = 0; i < all_points.length; i++){
            all_points[i] = null;
            all_translated[i] = null;
            all_cropped_size[i] = null;
        }
        font_color = "";
        font_family = "";
        background_color = "";

        original_image = null;
        image_file_location = null;

        lang_from_before = "";
        lang_from_after = "";
        num_box = 0;
        current_textbox_num = 1;
    }

    public Point getAll_points(int i) {
        return all_points[i];
    }

    public void setAll_points(int i, Point p) {
        this.all_points[i] = p;
    }

    public String getAll_translated(int i) {
        return all_translated[i];
    }

    public void setAll_translated(int i, String text) {
        this.all_translated[i] = text;
    }

    public Point getAll_cropped_size(int i) {
        //return all_cropped.get(i);
        return all_cropped_size[i];
    }

    public void setAll_cropped_size(int i, Point new_img_pt) {
        //this.all_cropped.set(i, new_img);
        all_cropped_size[i] = new_img_pt;
    }

    public Uri getImage_file_location() {
        return image_file_location;
    }

    public void setImage_file_location(Uri image_file_location) {
        this.image_file_location = image_file_location;
    }

    public int getCurrent_textbox_num() {
        return current_textbox_num;
    }

    public void setCurrent_textbox_num(int current_textbox_num) {
        this.current_textbox_num = current_textbox_num;
        //all_cropped = new Uri[current_textbox_num];
    }

    public Bitmap getOriginal_image() {
        return original_image;
    }

    public void setOriginal_image(Bitmap original_image) {
        this.original_image = original_image;
    }

    public String getLang_from_before() {
        return lang_from_before;
    }

    public void setLang_from_before(String lang_from_before) {
        this.lang_from_before = lang_from_before;
    }

    public String getLang_from_after() {
        return lang_from_after;
    }

    public void setLang_from_after(String lang_from_after) {
        this.lang_from_after = lang_from_after;
    }

    public String getFont_family() {
        return font_family;
    }

    public void setFont_family(String font_family) {
        this.font_family = font_family;
    }

    public String getFont_color() {
        return font_color;
    }

    public void setFont_color(String font_color) {
        this.font_color = font_color;
    }

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    public int getNum_box() {
        return num_box;
    }

    public void setNum_box(int num_box) {
        this.num_box = num_box;
    }



}
