package com.techninjas.umeedforwomen.Utils;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.techninjas.umeedforwomen.Utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageEncodeUtil {

    public ImageEncodeUtil() {}
    // Encode a file
    public static String encodeFile(Context context, String path){//Uri imageUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(Uri.fromFile(new File(path)));
            if (inputStream != null) {
                byte[] bytes;
                byte[] buffer = new byte[8192];
                int bytesRead;
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                bytes = output.toByteArray();
                String encodedString = Base64.encodeToString(bytes, Base64.NO_WRAP);

                Log.d(Constants.LOG, "encodeFile: Encoded String: " + encodedString);
                return encodedString;
            } else return null;
        } catch (IOException e) {
            Log.d(Constants.LOG, "encodeFile: " + e.getMessage());
            return null;
        }
    }

}
