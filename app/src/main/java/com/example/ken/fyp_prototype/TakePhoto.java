package com.example.ken.fyp_prototype;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.media.ThumbnailUtils;


import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by Jason on 25/1/2016.
 */
public class TakePhoto extends Activity{
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Button btnSelect;
    ImageView ivImage;
    Bitmap bm, goingtoupload,little,bitmap;
    String imageName;
    JSONObject jsonobject;
    JSONArray jsonarray;
    public class WorldPopulation {

        private String rank ="";

        private String country="";

        private String population="";

        private String flag="";

        public WorldPopulation (){

        }

        public void setRank(String _rank){
            this.rank=_rank;
        }

        public void setCountry(String _country){
            this.country=_country;
        }

        public void setPopulation(String _population) {
            this.population = _population;
        }

        public void setFlag(String _flag){
            this.flag=_flag;
        }

        public String getRank(){
            return this.rank;
        }

        public String getCountry()
        {

            return this.country;

        }

        public String getPopulation()

        {

            return this.population;

        }

        public String getFlag()

        {

            return this.flag;

        }

    }
    ProgressDialog mProgressDialog;
    ArrayList<String> items;
    ArrayList<WorldPopulation> world;


    private  static final String SEVER_ADDRESS = "http://chiwai.netai.net/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo);
        btnSelect = (Button) findViewById(R.id.btnSelectPhoto);
        btnSelect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        ivImage = (ImageView) findViewById(R.id.ivImage);
        new DownloadJSON().execute();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(TakePhoto.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    /*private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        bitmap = thumbnail;
        //-----------------------------------------------------
        //File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //String pictureName = getPictureName();
        //File imageFile = new File(pictureDirectory,pictureName);
        Uri selectedImageUri = data.getData();

        String[] projection = { MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();
        String selectedImagePath = cursor.getString(column_index);
        Log.i(selectedImagePath,"jjj");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);

        final int REQUIRED_SIZE = 1000;
        //final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 1.1 >= REQUIRED_SIZE
                && options.outHeight / scale / 1.1 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);
        bitmap = BitmapFactory.decodeFile(selectedImagePath,options);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        //-----------------------------------------------------
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //little = ThumbnailUtils.extractThumbnail(bitmap, 50, 50);
        ivImage.setImageBitmap(bm);
    }*/
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File imgFile = destination;
        if(imgFile.exists()){
            bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            little = ThumbnailUtils.extractThumbnail(bm,100,100);
        }

        ivImage.setImageBitmap(little);
    }
    /*private void onCaptureImageResult(Intent data) {
        Log.d("onCaptureImageResult", "onCaptureImageResult");
        Bitmap bm = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        little = ThumbnailUtils.extractThumbnail(bm, 100, 100);
        ivImage.setImageBitmap(little);
    }*/

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);
        Log.i(selectedImagePath,"jjj");
//fit to image view
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 1000;
        //final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 1.1 >= REQUIRED_SIZE
                && options.outHeight / scale / 1.1 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);
        bitmap = BitmapFactory.decodeFile(selectedImagePath,options);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        little = ThumbnailUtils.extractThumbnail(bm, 200, 200);
        Log.i("T_Height: " + String.valueOf(little.getHeight()) + "T_Width: " + String.valueOf(little.getWidth()), " uploaded");
        ivImage.setImageBitmap(little);
    }

    public void up(View v){

        Bitmap image = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();

            Log.i("Upload", "OK");
            String timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(new Date());
            imageName = timeStamp;
            //new UploadImage(waterMark(bm, timeStamp), imageName).execute();
            new UploadImage(bm,imageName).execute();
    }

    public void finish(View j){
        Intent go2Last = new Intent("com.example.ken.fyp_prototype.lastStep");
        startActivity(go2Last);
    }


    private String getPictureName()
    {
        SimpleDateFormat adf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = adf.format(new Date());
        return "TownGas" + timeStamp + ".jpg";
    }

    /*public static Bitmap waterMark(Bitmap src, String watermark) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        paint.setUnderlineText(true);
        canvas.drawText(watermark, 5, 45, paint);

        return result;
    }*/

    private class UploadImage extends AsyncTask<Void, Void,Void> {
        Bitmap image;
        String name;
        public UploadImage(Bitmap image, String name) {
            this.image = image;
            this.name = name;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_LONG).show();
            //Log.i("Height: " + String.valueOf(image.getHeight()) + "Width: " + String.valueOf(image.getWidth()), " uploaded");
            ivImage.setImageResource(R.drawable.ic_launcher); //reset the image after upload
        }



        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("image", encodedImage));
            dataToSend.add(new BasicNameValuePair("name", name));
            HttpParams httpRequestParams = getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SEVER_ADDRESS + "SavePicture.php");
            Log.i("Height: " + String.valueOf(image.getHeight()) + "Width: "+String.valueOf(image.getWidth())," uploading");
            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;

        }
    }
    private HttpParams getHttpRequestParams(){
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
        HttpConnectionParams.setSoTimeout(httpRequestParams,1000 * 30);
        return httpRequestParams;
    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            world = new ArrayList<WorldPopulation>();
            //create an array to dropdown menu
            items = new ArrayList<String>();
            jsonobject = JSONfunction.getJSONfromURL("http://175.159.209.21/towngas.txt");
            try {
                jsonarray = jsonobject.getJSONArray("worldpopulation");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    WorldPopulation worldpop = new WorldPopulation();

                    worldpop.setRank(jsonobject.optString("rank"));
                    worldpop.setCountry(jsonobject.optString("country"));
                    worldpop.setPopulation(jsonobject.optString("population"));
                    worldpop.setFlag(jsonobject.optString("flag"));
                    world.add(worldpop);

                    // Populate spinner with country names
                    items.add(jsonobject.optString("country"));
                }


            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml
            Spinner mySpinner = (Spinner) findViewById(R.id.testing);

            // Spinner adapter
            mySpinner.setAdapter(new ArrayAdapter<String>(TakePhoto.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    items));
        }
    }
}
