package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.BitSet;

public class CroppingPage extends AppCompatActivity {

    private int current_box_num = 0;
    private ImageView imgPicture;
    private Bitmap bit_image;
    private Uri saved_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropping_page);

        Button pick = findViewById(R.id.next_crop);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCrop();
            }
        });

        //Text
        current_box_num = ((MyApplication) this.getApplication()).getCurrent_textbox_num();
        ((TextView)findViewById(R.id.textbox_num)).setText("Text Box " + current_box_num);
        ((TextView)findViewById(R.id.instructions)).setText("Instructions: Crop image to include the text needed for Text Box " + current_box_num);

        // gets a reference to the image view that has the image that the user will see
        imgPicture = findViewById(R.id.imgPicture);

        // show the image to the user
        ImageView imgPicture = findViewById(R.id.imgPicture);
        bit_image = ((MyApplication) this.getApplication()).getOriginal_image();
        imgPicture.setImageBitmap(bit_image);

        saved_img = ((MyApplication) this.getApplication()).getImage_file_location();
    }


    private void startCrop(){
        CropImage.activity(saved_img)
                .start(this);
    }

    public void TextDetection(Bitmap bitmap){
        //TODO 1. define TextRecognizer
        TextRecognizer recognizer = new TextRecognizer.Builder(CroppingPage.this).build();

        //TODO 2. Get bitmap from imageview

        //TODO 3. get frame from bitmap
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

        //TODO 4. get data from frame
        SparseArray<TextBlock> sparseArray = recognizer.detect(frame);

        //TODO 5. set data on textview
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i < sparseArray.size(); i++){
            TextBlock tx = sparseArray.get(i);
            String str = tx.getValue();
            stringBuilder.append (str);
        }

        TextView d = findViewById(R.id.textView);
        d.setText(stringBuilder);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bitmap_img = null;
                try {
                    bitmap_img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Save cropped image
                ((MyApplication) this.getApplication()).setAll_cropped(current_box_num - 1, resultUri);

                //Start Text Detection
                TextDetection(bitmap_img);

                //Start Translate


                //Save Translation to array


                //Move to next page
                ((MyApplication) this.getApplication()).setCurrent_textbox_num(current_box_num + 1);
                if((current_box_num + 1) > ((MyApplication) this.getApplication()).getNum_box()) {
                    Intent i = new Intent(this, FinalEditImage.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(this, CroppingPage.class);
                    startActivity(i);
                }

//                ImageView v = findViewById(R.id.test_image);
//                v.setImageURI(resultUri);
//                v.setImageURI(((MyApplication) this.getApplication()).getAll_cropped(current_box_num - 1));
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }









//        Activity act = this;
//
//        CropImage.activity(hold)
//                .start(act);
//
//        Button next = findViewById(R.id.next_crop);
//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                CroppingPageClick();
//            }
//        });




//    private void CroppingPageClick() {
//
//        //Save cropped image
//
//
//        //Move to next page
//        ((MyApplication) this.getApplication()).setCurrent_textbox_num(current_box_num + 1);
//        if((current_box_num + 1) > ((MyApplication) this.getApplication()).getNum_box()) {
//            Intent i = new Intent(this, FinalEditImage.class);
//            startActivity(i);
//        } else {
//            Intent i = new Intent(this, CroppingPage.class);
//            startActivity(i);
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                //This is the returned cropped result
//                Uri resultUri = result.getUri();
//
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
//        }
//    }
}