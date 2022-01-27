package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LanguageAndTextBoxInfo extends AppCompatActivity {

    String lang_from_before_spin = "";
    String lang_from_after_spin = "";
    String font_family_result = "";
    String font_color_result = "";
    String background_color_result = "";
    int num_box_result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_and_text_box_info);


        // show the image to the user
        ImageView imgPicture = findViewById(R.id.imageView3);
        Bitmap bit_image = ((MyApplication) this.getApplication()).getOriginal_image();
        imgPicture.setImageBitmap(bit_image);

        //BEFORE LANGUAGE SPINNER
        Spinner before_spinner = (Spinner) findViewById(R.id.after_la);

        ArrayAdapter<String> before_adapt = new ArrayAdapter<String>(LanguageAndTextBoxInfo.this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.languages));
        before_adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        before_spinner.setAdapter(before_adapt);

        before_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                lang_from_before_spin = parent.getSelectedItem().toString();
                ((TextView) parent.getChildAt(0)).setTextSize(22);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });



        // AFTER LANGUAGE SPINNER
        Spinner after_spinner = (Spinner) findViewById(R.id.after_la);

        ArrayAdapter<String> after_adapt = new ArrayAdapter<String>(LanguageAndTextBoxInfo.this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.languages));
        after_adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        after_spinner.setAdapter(after_adapt);

        after_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                lang_from_after_spin = parent.getSelectedItem().toString();
                ((TextView) parent.getChildAt(0)).setTextSize(22);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });



        // FONT FAMILY SPINNER
        Spinner font_fam_spin = (Spinner) findViewById(R.id.font_family_spin);

        ArrayAdapter<String> font_fam_adapt = new ArrayAdapter<String>(LanguageAndTextBoxInfo.this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.font_family));
        font_fam_adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        font_fam_spin.setAdapter(font_fam_adapt);

        font_fam_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                font_family_result = parent.getSelectedItem().toString();
                ((TextView) parent.getChildAt(0)).setTextSize(22);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });



        // FONT COLOR SPINNER
        Spinner font_color_spin = (Spinner) findViewById(R.id.font_color_spin);

        ArrayAdapter<String> font_color_adapt = new ArrayAdapter<String>(LanguageAndTextBoxInfo.this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.color));
        font_color_adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        font_color_spin.setAdapter(font_color_adapt);

        font_color_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                font_color_result = parent.getSelectedItem().toString();
                ((TextView) parent.getChildAt(0)).setTextSize(22);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });



        // BACKGROUND COLOR SPINNER
        Spinner background_color_spinner = (Spinner) findViewById(R.id.background_color_spin);

        ArrayAdapter<String> background_color_adapt = new ArrayAdapter<String>(LanguageAndTextBoxInfo.this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.color_back));
        background_color_adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        background_color_spinner.setAdapter(background_color_adapt);

        background_color_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                background_color_result = parent.getSelectedItem().toString();
                ((TextView) parent.getChildAt(0)).setTextSize(22);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });



        // NUMBER OF TEXT BOXES SPINNER
        Spinner num_box_spinner = (Spinner) findViewById(R.id.number_boxes);

        ArrayAdapter<String> num_box_adapt = new ArrayAdapter<String>(LanguageAndTextBoxInfo.this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.number));
        num_box_adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        num_box_spinner.setAdapter(num_box_adapt);

        num_box_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                num_box_result = Integer.parseInt(parent.getSelectedItem().toString());
                ((TextView) parent.getChildAt(0)).setTextSize(22);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void CroppingPageClick(View view) {
        Intent i = new Intent(this, CroppingPage.class);

        ((MyApplication) this.getApplication()).setLang_from_before(lang_from_before_spin);
        ((MyApplication) this.getApplication()).setLang_from_after(lang_from_after_spin);
        ((MyApplication) this.getApplication()).setFont_family(font_family_result);
        ((MyApplication) this.getApplication()).setFont_color(font_color_result);
        ((MyApplication) this.getApplication()).setBackground_color(background_color_result);
        ((MyApplication) this.getApplication()).setNum_box(num_box_result);

        startActivity(i);
    }
}