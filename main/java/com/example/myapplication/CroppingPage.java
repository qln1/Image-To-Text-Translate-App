package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
//import com.google.mlkit.common.model.DownloadConditions;
//import com.google.mlkit.nl.translate.TranslateLanguage;
//import com.google.mlkit.nl.translate.Translation;
//import com.google.mlkit.nl.translate.Translator;
//import com.google.mlkit.nl.translate.TranslatorOptions;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.BitSet;

import cz.msebera.android.httpclient.entity.mime.Header;

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

    public String TextDetection(Bitmap bitmap){
        //1. define TextRecognizer
        TextRecognizer recognizer = new TextRecognizer.Builder(CroppingPage.this).build();

        //2. Get bitmap from imageview

        //3. get frame from bitmap
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

        //4. get data from frame
        SparseArray<TextBlock> sparseArray = recognizer.detect(frame);

        //5. set data on textview
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i < sparseArray.size(); i++){
            TextBlock tx = sparseArray.get(i);
            String str = tx.getValue();
            stringBuilder.append (str);
        }

        return stringBuilder.toString();
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

                if(bitmap_img != null) {
                    //Start Text Detection
                    text_from_img = TextDetection(bitmap_img);

                    if(text_from_img.length() != 0) {
                        //Save cropped image's size  X = Col Width   Y = Row Height
                        Point size = new Point();
                        size.x = bitmap_img.getWidth();
                        size.y = bitmap_img.getHeight();
                        ((MyApplication) this.getApplication()).setAll_cropped_size(current_box_num - 1, size);

                        //Start Translate
                        StartTranslation();

                        //Save Translation to array
                        ((MyApplication) this.getApplication()).setAll_translated(current_box_num - 1, text_from_img);

                        //Get Points of cropped image
                        Point point = SearchForTemplate(bit_image , bitmap_img);

                        //Save Points of cropped image
                        ((MyApplication) this.getApplication()).setAll_points(current_box_num - 1, point);
                    }
                }





                //Move to next page
                ((MyApplication) this.getApplication()).setCurrent_textbox_num(current_box_num + 1);
                if((current_box_num + 1) > ((MyApplication) this.getApplication()).getNum_box()) {
                    Intent i = new Intent(this, FinalEditImage.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(this, CroppingPage.class);
                    startActivity(i);
                }



                //TextView d = findViewById(R.id.textView);///////////////////////////////////////////////////////////////
                //d.setText(text_from_img);
//                ImageView v = findViewById(R.id.test_image);
//                v.setImageURI(resultUri);
//                v.setImageURI(((MyApplication) this.getApplication()).getAll_cropped(current_box_num - 1));
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }





//    public void StartTranslation(){
//        if(text_from_img.length() != 0){
//            Log.d("myTag", "This is my messageyes");
//
//            TranslatorOptions trans_config = new TranslatorOptions.Builder().setSourceLanguage(TranslateLanguage.ENGLISH)
//                    .setTargetLanguage(TranslateLanguage.FRENCH).build();
//            final Translator trans = Translation.getClient(trans_config);
//
//            //Download Model if needed
//            DownloadConditions conditions = new DownloadConditions.Builder().requireWifi().build();
//            trans.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void unused) {
//                    Log.d("myTag", "This is my messageddddddddddddddddddddd");
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.d("myTag", "This is my failllllllllllllllllllllllllll");
//                }
//            });
//
//            trans.translate(text_from_img).addOnSuccessListener(new OnSuccessListener<String>() {
//                @Override
//                public void onSuccess(String s) {
//                    text_from_img = s;
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.d("myTag", "Tffffffffffffffffffffffffffffllllllll");
//                }
//            });
//        }
//    }



        // Real Translation app
//    public void StartTranslation(){
//        String lang_bef = ((MyApplication) this.getApplication()).getLang_from_before();
//        String lang_aft = ((MyApplication) this.getApplication()).getLang_from_after();
//        Http.post(text_from_img, lang_bef, lang_aft, new JsonHttpResponseHandler(){
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                try {
//                    JSONObject serverResp = new JSONObject(response.toString());
//                    JSONObject jsonObject = serverResp.getJSONObject("data");
//                    JSONArray transObject = jsonObject.getJSONArray("translations");
//                    JSONObject transObject2 =  transObject.getJSONObject(0);
//                    text_from_img = transObject2.getString("translatedText");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                    e.printStackTrace();
//            }
//        });
//    }

    //Temp for testing
    public void StartTranslation(){
        if(text_from_img.charAt(text_from_img.length() - 1) != 'n'){
            text_from_img = "Hello, who are you?";
        } else {
            text_from_img = "Hello my name is Jon";
        }
    }





//    public void StartTranslation(){
//        translate_api translate=new translate_api();
//        translate.setOnTranslationCompleteListener(new translate_api.OnTranslationCompleteListener() {
//            @Override
//            public void onStartTranslation() {
//                // here you can perform initial work before translated the text like displaying progress bar
//            }
//
//            @Override
//            public void onCompleted(String text) {
//                // "text" variable will give you the translated text
//                text_from_img = text;
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });
//        translate.execute(text_from_img, "en", "es");
//    }







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