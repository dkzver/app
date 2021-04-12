package com.wearetogether.v2.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import androidx.fragment.app.FragmentActivity;
import com.squareup.picasso.Picasso;
import com.wearetogether.v2.R;
import com.wearetogether.v2.ui.CropSquareTransformation;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtils {

    public static void RemoveImages(Context context) {
        File storageDir =
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir != null) {
            File[] files = storageDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    boolean is_delete = file.delete();
                }
            }
        }
    }

    public static int GetSquareSize(FragmentActivity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    public static File CropSquare(Bitmap bitmap, Context context, int size) throws IOException {
        return Crop(bitmap, context, size);
    }

    public static File CropSquare(Bitmap bitmap, Context context) throws IOException {
        int size = PreferenceUtils.GetSquareSize(context);
        return Crop(bitmap, context, size);
    }

    public static File Crop(Bitmap bitmap, Context context, int size) throws IOException {
        if (bitmap.getWidth() < size) {
            size = bitmap.getWidth();
        }
        if (bitmap.getHeight() < size) {
            size = bitmap.getHeight();
        }
        int dstWidth = size;
        int dstHeight = size;
        int startX = bitmap.getWidth() / 2 - (dstWidth / 2);
        int startY = bitmap.getHeight() / 2 - (dstHeight / 2);
        Bitmap dst = Bitmap.createBitmap(bitmap, startX, startY, dstWidth, dstHeight);

        File file = CreateFile(context);
        FileOutputStream fout = new FileOutputStream(file);
        dst.compress(Bitmap.CompressFormat.JPEG, 90, fout);
        return file;
    }

    public static File SaveBitmap(Bitmap source, File file) throws IOException {
        FileOutputStream oStream = new FileOutputStream(file);
        source.compress(Bitmap.CompressFormat.JPEG, 90, oStream);
        oStream.flush();
        oStream.close();
        return file;
    }

    public static File SaveBitmap(Bitmap source, File file, int quality) throws IOException {
        FileOutputStream oStream = new FileOutputStream(file);
        source.compress(Bitmap.CompressFormat.JPEG, quality, oStream);
        oStream.flush();
        oStream.close();
        return file;
    }

    private static Bitmap GetBitmapFromStorage(String src) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(src, options);
    }

    private static Bitmap GetBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File CreateRecordFile(Context context) throws IOException {
        String imageFileName = null;
        String timeStamp =
                new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",
                        Locale.getDefault()).format(new Date());
        imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".3gpp",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    public static File CreateFile(Context context) throws IOException {
        String imageFileName = null;
        String timeStamp =
                new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",
                        Locale.getDefault()).format(new Date());
        imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    public static File CreateFile(Context context, String prefix) throws IOException {
        String imageFileName = null;
        String timeStamp =
                new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",
                        Locale.getDefault()).format(new Date());
        imageFileName = "IMG_" + prefix + "_" + timeStamp + "_";
        File storageDir =
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    public static String DownloadImage(Context context, String path) {
        try {
            File file = FileUtils.CreateFile(context);
            URL url = new URL(path);
            URLConnection connection = url.openConnection();
            connection.connect();
            int fileLength = connection.getContentLength();
            InputStream input = new BufferedInputStream(connection.getInputStream());
            FileOutputStream output = new FileOutputStream(file);
            byte data[] = new byte[1024];
            long total = 0;
            int count;
            int progress;
            while ((count = input.read(data)) != -1) {
                total += count;
                progress = (int) (total * 100 / fileLength);
                System.out.println(progress);
                output.write(data, 0, count);
            }

            // close streams
            output.flush();
            output.close();
            input.close();

            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void RemoveFile(String path) {
        File file = new File(path);
        if (file != null) {
            file.delete();
        }
    }

    public static File DownloadImageFromUrl(String path, Context context) throws Exception {
        try {
            File file = CreateFile(context);
            if (file.exists()) {
                URL url = new URL(path);
                URLConnection connection = url.openConnection();
                connection.connect();
                int fileLength = connection.getContentLength();
                InputStream input = new BufferedInputStream(connection.getInputStream());
                FileOutputStream output = new FileOutputStream(file);
                byte data[] = new byte[1024];
                long total = 0;
                int count;
                int progress = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    progress = (int) (total * 100 / fileLength);
//                System.out.println("progress " + progress);
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
                return file;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static Bitmap RotateBitmap(Bitmap original, float degrees) {
        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);
        Bitmap rotatedBitmap = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
        original.recycle();
        return rotatedBitmap;
    }

    public static String ResizeImageStorage(String path, Context context, int w, int h, float degrees) {
        try {
            Bitmap source = GetBitmapFromStorage(path);
            Bitmap resized = Bitmap.createScaledBitmap(source, w, h, true);
            if (degrees != 0) {
                resized = RotateBitmap(resized, degrees);
            }
            File file = null;
            try {
                file = SaveBitmap(resized, CreateFile(context));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            if (file.exists()) {
                return file.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ResizeImageStorage " + e.getMessage());
        }
        return null;
    }

    public static void ShowImage(String path, ImageView imageView) {
        File imgFile = new File(path);
        if (imgFile.exists() && imageView != null) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }
    }

    public static void ShowImage(String path, ImageView imageView, int w, int h) {
        if (path == null) return;
        if (path.equals("")) return;
        if (path.contains("http")) {
            if (w > 0 && h > 0) {
                Picasso.get()
                        .load(path)
                        .placeholder(R.drawable.ic_launcher_background)
                        .transform(new CropSquareTransformation())
                        .resize(w, h)
                        .centerCrop()
                        .into(imageView);
            } else {
                Picasso.get()
                        .load(path)
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(imageView);
            }
        } else {
            if (w > 0 && h > 0) {
                Picasso.get()
                        .load(new File(path))
                        .placeholder(R.drawable.ic_launcher_background)
                        .transform(new CropSquareTransformation())
                        .resize(w, h)
                        .centerCrop().
                        into(imageView);
            } else {
                Picasso.get()
                        .load(new File(path))
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(imageView);
            }
        }
    }

    public static void LoadBitmap(FragmentActivity activity, String icon1, String icon2, ImageView imageView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = GetBitmap(icon1, icon2);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                            imageView.setVisibility(View.VISIBLE);
                        } else {
                            imageView.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }).start();
    }

    public static void LoadBitmap(FragmentActivity activity, String icon1, ImageView imageView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = GetBitmap(icon1);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                            imageView.setVisibility(View.VISIBLE);
                        } else {
                            imageView.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }).start();
    }

    public static Bitmap GetBitmap(String icon1, String icon2) {
        Bitmap bitmap = null;
        String path = icon1;
        if (path.equals("")) {
            path = icon2;
        }
        if (path.contains("http")) {
            bitmap = FileUtils.GetBitmapFromURL(path);
        } else {
            bitmap = FileUtils.GetBitmapFromStorage(path);
        }
        return bitmap;
    }

    public static Bitmap GetBitmap(String icon1) {
        Bitmap bitmap = null;
        try {
            if(icon1 == null) {
                throw new Exception("Error path==null");
            }
            if(icon1.equals("")) {
                throw new Exception("Error path==''");
            }
            if (icon1.contains("http")) {
                bitmap = FileUtils.GetBitmapFromURL(icon1);
            } else {
                bitmap = FileUtils.GetBitmapFromStorage(icon1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static float GetOrientationValue(String path) {
        int rotate = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(new File(path).getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            if (orientation == 6) {
                rotate = 90;
            } else if (orientation == 3) {
                rotate = 180;
            } else if (orientation == 8) {
                rotate = 270;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }
}
