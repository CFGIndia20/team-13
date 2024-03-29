package com.techninjas.umeedforwomen.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.techninjas.umeedforwomen.DB.ProgressDBUtil;
import com.techninjas.umeedforwomen.Models.Progress;
import com.techninjas.umeedforwomen.R;
import com.techninjas.umeedforwomen.Utils.Constants;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.techninjas.umeedforwomen.Utils.ImageEncodeUtil.encodeFile;

public class ImageActivity extends AppCompatActivity {

    private static final String TAG = "ImageActivity";

    // Elements
    private ImageButton ibBack;
    private ImageView ivImage;
    private Button btnSave;
    //private TextView status;
    private ProgressBar progressBar;

    private String tempFilePath, compressedFilePath;
    private Uri tempPhotoUri, compressedPhotoUri;
    private boolean imageSaved = false;
    private static final int CAMERA_REQUEST = 120;
    private static final int PERMISSION_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        // Data Binding
        init();

        // Clicks
        ibBack.setOnClickListener(view -> this.onBackPressed());

        btnSave.setOnClickListener(view -> {
            //Log.d("APP_LOGS", "Here");
            if (imageSaved && compressedPhotoUri != null) {
                //Log.d("APP_LOGS", "Here");
                ProgressDBUtil progressDBUtil = new ProgressDBUtil(this);
                String taskId = getIntent().getStringExtra("id");
                String qty = getIntent().getStringExtra("qty");
                String timestamp = (new Date()).toString();
                Progress progress = new Progress(taskId, Integer.parseInt(qty), timestamp, compressedFilePath);
                //Progress progress = new Progress("100", 10, timestamp, com)
                progressDBUtil.insertData(progress);
                this.finish();
            } else Toast.makeText(this, "Please upload an image first", Toast.LENGTH_SHORT).show();
        });
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbarImage);
        setSupportActionBar(toolbar);
        ibBack = findViewById(R.id.ibImageBack);
        ivImage = findViewById(R.id.ivImage);
        btnSave = findViewById(R.id.btnImageSave);
        //status = findViewById(R.id.status);
        progressBar = findViewById(R.id.progressBarImage);

        checkPermissions();
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean cameraPermission = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
            boolean storagePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            boolean coarseLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            boolean fineLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

            List<String> permissionsList = new ArrayList<>();
            if (!cameraPermission) permissionsList.add(Manifest.permission.CAMERA);
            if (!storagePermission)
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (!coarseLocationPermission)
                permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            if (!fineLocationPermission)
                permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            //Log.d("APP_LOGS", permissionsList.toString());
            if (permissionsList.size() == 0) {
                dispatchCameraIntent();
                return;
            }

            String[] permissions = new String[permissionsList.size()];
            for (int i = 0; i < permissionsList.size(); i++)
                permissions[i] = permissionsList.get(i);

            requestPermissions(permissions, PERMISSION_CODE);
        } else dispatchCameraIntent();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Please grant all the permissions", Toast.LENGTH_SHORT).show();
                        this.finish();
                    }
                }
                dispatchCameraIntent();
            } else Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void dispatchCameraIntent() {
        // Create camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            Log.d(TAG, "dispatchCameraIntent: Creating a temporary file...");
            // Create a temporary file
            File tempFile = createFile();

            if (tempFile != null) {
                // Temporary file is created successfully
                Log.d(TAG, "dispatchCameraIntent: Temporary File created successfully");

                // Get path of temporary file
                tempFilePath = tempFile.getAbsolutePath();
                tempPhotoUri = FileProvider.getUriForFile(ImageActivity.this, Constants.AUTHORITY, tempFile);

                // Give the temporary file uri to the camera
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempPhotoUri);

                // Start the camera
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Log.d(TAG, "dispatchCameraIntent: Temporary file could not be created");
            }
        } else Toast.makeText(this, "No Package Manager", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: Temporary File Uri: " + tempPhotoUri);
                Log.d(TAG, "onActivityResult: Temporary File Path:" + tempFilePath);
                new ImageCompressionAsyncTask().execute(tempFilePath);
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File createFile() {
        @SuppressLint("SimpleDateFormat")
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDirectory = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "Team Tech Ninjas");

        boolean directoryExists = true;

        Log.d(TAG, "createFile: Storage directory: " + storageDirectory.getPath());
        if (!storageDirectory.exists())
            directoryExists = storageDirectory.mkdirs();

        if (directoryExists) {
            File image = null;
            try {
                image = File.createTempFile(name, ".jpg", storageDirectory);
            } catch (IOException e) {
                Log.d(TAG, "createFile: Exception: " + e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return image;
        } else {
            Toast.makeText(this, "Folder could not be created", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Start the progress bar
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            return compressImage(strings[0]);
        }

        public String compressImage(String imageUri) {
            String filePath = getRealPathFromURI(imageUri);
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;
            float maxHeight = 816.0f;
            float maxWidth = 612.0f;
            float imgRatio = (float) actualWidth / (float) actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inTempStorage = new byte[16 * 1024];

            try {
                bmp = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            assert scaledBitmap != null;
            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - (float) bmp.getWidth() / 2, middleY - (float) bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);

                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileOutputStream out;
            String filename = getFilename();
            try {
                out = new FileOutputStream(filename);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return filename;

        }

        public String getFilename() {
            File file = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "Team Tech Ninjas");
            if (!file.exists()) {
                if (file.mkdirs())
                    return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
                else return null;
            }
            return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        }

        public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                final int heightRatio = Math.round((float) height / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = Math.min(heightRatio, widthRatio);

            }
            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
            return inSampleSize;
        }

        @Override
        protected void onPostExecute(String compressedFileName) {
            super.onPostExecute(compressedFileName);

            // Stop the progress bar
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            Log.d(TAG, "onPostExecute: Compressed File Name: " + compressedFileName);
            compressedFilePath = getRealPathFromURI(compressedFileName);

            // Get the temporary file from file path and delete it
            File tempFile = new File(tempFilePath);
            if (tempFile.delete())
                Log.d(TAG, "onPostExecute: File deleted...");
            else Log.d(TAG, "onPostExecute: File not deleted...");
            Log.d("APP_LOGS", compressedFilePath);
            compressedPhotoUri = Uri.parse(compressedFilePath);
            ivImage.setImageURI(compressedPhotoUri);
            imageSaved = true;

            //successfulEnding();
            //status.setText("Image Saved Locally!");
            //Log.d("APP_LOGS", encodeFile(ImageActivity.this, compressedPhotoUri));
            //encodeFile(ImageActivity.this, compressedFilePath);
            /*Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    successfulEnding();
                }
            }, 3000);*/
        }
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String realPath = cursor.getString(idx);
            cursor.close();
            return realPath;
        }
    }

    @Override
    public void onBackPressed() {
        if (imageSaved) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure you want to go back?")
                    .setMessage("All unsaved changes will be lost")
                    .setPositiveButton("Yes", (dialogInterface, i) -> this.finish())
                    .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
        } else this.finish();
    }
}