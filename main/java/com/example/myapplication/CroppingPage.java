package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.theartofdev.edmodo.cropper.CropImage;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import cz.msebera.android.httpclient.Header;


interface OnTranslationCallback {
    void onTranslationResponse(boolean success, String response);
}



public class CroppingPage extends AppCompatActivity {

    private int current_box_num = 0;
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

        // show the image to the user
        ImageView imgPicture = findViewById(R.id.imgPicture);
        bit_image = ((MyApplication) this.getApplication()).getOriginal_image();
        imgPicture.setImageBitmap(bit_image);

        saved_img = ((MyApplication) this.getApplication()).getImage_file_location();
    }


    private void startCrop(){
        CropImage.activity(saved_img).start(this);
    }

    public String TextDetection(Bitmap bitmap){
        //1. define TextRecognizer
        TextRecognizer recognizer = new TextRecognizer.Builder(CroppingPage.this).build();

        //2. get frame from bitmap
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

        //3. get data from frame
        SparseArray<TextBlock> sparseArray = recognizer.detect(frame);

        //4. set data on textview
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
//                        getTranslation(new OnTranslationCallback() {
//                            @Override
//                            public void onTranslationResponse(boolean success, String response) {
//                                if(success){
//                                    text_from_img = response;
//                                }
//                            }
//                        });

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
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }




    //New translation
    public void getTranslation(OnTranslationCallback callback) {
        String lang_bef = LanguageName(((MyApplication) this.getApplication()).getLang_from_before());
        String lang_aft = LanguageName(((MyApplication) this.getApplication()).getLang_from_after());
        String url = Http.getUrl(text_from_img, lang_bef, lang_aft);

        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String str = new String(responseBody);
                    String translated = "";
                    for(int i = 71; i < str.length(); i++) {
                        if(str.charAt(i) == '\"')  break;
                        else  translated += str.charAt(i);
                    }
                    Log.d("miner", "yes");
                    callback.onTranslationResponse(true, translated);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("miner", "no");
                callback.onTranslationResponse(false, "");
            }
        });
        callback.onTranslationResponse(false, "");
    }



        // Real Translation app (old)
//    public void StartTranslation(){
//        String lang_bef = LanguageName(((MyApplication) this.getApplication()).getLang_from_before());
//        String lang_aft = LanguageName(((MyApplication) this.getApplication()).getLang_from_after());
//        String url = Http.getUrl(text_from_img, lang_bef, lang_aft);
//        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess (int statusCode, cz.msebera.android.httpclient.Header [] headers, byte [] responseBody) {
//                            String str = new String(responseBody);
//                            String translated = "";
//                            for(int i = 71; i < str.length(); i++) {
//                                if(str.charAt(i) == '\"')  break;
//                                else  translated += str.charAt(i);
//                            }
//                            text_from_img = translated;
//            }
//            @Override
//            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
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



    public String LanguageName(String lang){
        switch(lang){
            case "Spanish":
                return "es";
            case "French":
                return "fr";
            case "English":
                return "en";
        }
        return "en";
    }



    // Precondition: search and temp exists in the code directory and is a valid image.
    // Postcondition: Returns a Point of the row and col for the search image
    //                that contains the pixels that matches the template
    static Point SearchForTemplate(Bitmap search, Bitmap temp) {
        Point point = new Point();
        boolean skip = false;

        for (int row = 0; row < (search.getHeight() - temp.getHeight() + 1); row++) {
            for (int col = 0; col < (search.getWidth() - temp.getWidth() + 1); col++) {
                for (int t_row = 0; t_row < temp.getHeight(); t_row+=25) {
                    for (int t_col = 0; t_col < temp.getWidth(); t_col+=35) {

                        int pixel_src = search.getPixel(col + t_col, row + t_row);
                        int pixel_temp = temp.getPixel(t_col, t_row);

                        int s_red = Color.red(pixel_src);
                        int t_red = Color.red(pixel_temp);

                        int s_green = Color.green(pixel_src);
                        int t_green = Color.green(pixel_temp);

                        int s_blue = Color.blue(pixel_src);
                        int t_blue = Color.blue(pixel_temp);

                        if (!(((s_red <= t_red + 20) && (s_red >= t_red - 20)) ||
                            ((s_green <= t_green + 20) && (s_green >= t_green - 20)) ||
                            ((s_blue <= t_blue + 20) && (s_blue >= t_blue - 20)))) {
                            skip = true;
                            break;
                        }
                    }
                    if (skip) break;
                }
                if (!skip) {
                    point.x = col;
                    point.y = row;
                    return point;
                } else skip = false;
            }
        }
        point.x = -1;
        point.y = -1;
        return point;
    }

}