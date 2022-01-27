package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
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
    private String text_from_img;

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

        TextView d = findViewById(R.id.textView);///////////////////////////////////////////////////////////////
        d.setText(stringBuilder);

        text_from_img = stringBuilder.toString();
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


                //Get Points of cropped image
                Point point = SearchForTemplate(bit_image , bitmap_img);

                //Save Points of cropped image



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




    // Precondition: search and temp exists in the code directory and is a valid image.
    // Postcondition: Returns a Point of the row and col for the search image
    //                that contains the pixels that matches the template
    static Point SearchForTemplate(Bitmap search, Bitmap temp) {
        Point point = new Point();
        Boolean skip = false;
        int s = search.getHeight();
        int gg = search.getWidth();
        int d = temp.getHeight();
        int p = temp.getWidth();
        //int pixel_search1 = search.getRGB(2, 2);
        //int pixel_temp1 = temp.getRGB(1, 1);
//        int das = Color.red(pixel_src1);
//        int sdf = Color.red(pixel_temp1);
//        int sdfa = Color.green(pixel_src1);
//        int dfsdfs = Color.green(pixel_temp1);
//        int sdfsdf = Color.blue(pixel_src1);
//        int fdsdf = Color.blue(pixel_temp1);



        // int red =   (clr & 0x00ff0000) >> 16;
        // int green = (clr & 0x0000ff00) >> 8;
        // int blue =   clr & 0x000000ff;



        for (int row = 0; row < (search.getHeight() - temp.getHeight() + 1); row++) {
            for (int col = 0; col < (search.getWidth() - temp.getWidth() + 1); col++) {


                for (int t_row = 0; t_row < temp.getHeight(); t_row++) {
                    for (int t_col = 0; t_col < temp.getWidth(); t_col++) {

                        int pixel_src = search.getPixel(col + t_col, row + t_row);
                        int pixel_temp = temp.getPixel(t_col, t_row);

                        int s_red =   (pixel_src & 0x00ff0000) >> 16;
                        int s_green = (pixel_src & 0x0000ff00) >> 8;
                        int s_blue =   pixel_src & 0x000000ff;

                        int t_red =   (pixel_temp & 0x00ff0000) >> 16;
                        int t_green = (pixel_temp & 0x0000ff00) >> 8;
                        int t_blue =   pixel_temp & 0x000000ff;


                         int das = Color.red(pixel_src);
                         int sdf = Color.red(pixel_temp);
                         int sdfa = Color.green(pixel_src);
                         int dfsdfs = Color.green(pixel_temp);
                         int sdfsdf = Color.blue(pixel_src);
                         int fdsdf = Color.blue(pixel_temp);


                        if (!(((s_red <= t_red + 25) && (s_red >= t_red - 25)) ||
                                ((s_green <= t_green + 25) && (s_green >= t_green - 25)) ||
                                ((s_blue <= t_blue + 25) && (s_blue >= t_blue - 25)))) {
                            skip = true;
                            break;
                        }

//                                if(t_col == 38) {
//                                    int p56 = Color.green(pixel_temp);
//                                    int ko0 = Color.blue(pixel_src);
//                                    int ko00 = Color.blue(pixel_temp);
//                                }


                    }
                    if (skip) {
                        break;
                    }
                }

                if (!skip) {
                    point.x = col;
                    point.y = row;
                    return point;
                } else {
                    skip = false;
                }

            }
        }
        point.x = -1;
        point.y = -1;
        return point;
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