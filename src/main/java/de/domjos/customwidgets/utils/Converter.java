/*
 * MamaPlanner
 * Copyright (C) 2019 Domjos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.domjos.customwidgets.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

import de.domjos.customwidgets.R;

public class Converter {

    public static byte[] downloadFile(URL url) throws Exception  {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("User-Agent", "Firefox");

        try (InputStream inputStream = conn.getInputStream()) {
            int n;
            byte[] buffer = new byte[1024];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }
        byte[] img = output.toByteArray();
        ByteBuffer imageBytes = ByteBuffer.wrap(img);
        return imageBytes.array();
    }

    public static String convertStreamToString(InputStream stream) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        return sb.toString();
    }

    public static Date convertStringToDate(String dt, String format) throws Exception {
        if(dt!=null) {
            if(!dt.isEmpty()) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Global.getLocale());
                return simpleDateFormat.parse(dt);
            }
        }
        return null;
    }

    public static Date convertStringToDate(String dt, Context context) throws Exception {
        return Converter.convertStringToDate(dt, context.getString(R.string.date_format));
    }

    public static String convertDateToString(Date date, String format) {
        if(date!=null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Global.getLocale());
            return simpleDateFormat.format(date);
        }
        return null;
    }

    public static String convertDateToString(Date date, Context context) {
        return Converter.convertDateToString(date, context.getString(R.string.date_format));
    }

    public static Calendar convertStringToCalendar(String dt, Context context) throws Exception {
        Date date = Converter.convertStringToDate(dt, context);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Time convertStringToTime(Context context, String time, int icon) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return new Time(Objects.requireNonNull(formatter.parse(time)).getTime());
        } catch (Exception ex) {
            MessageHelper.printException(ex, icon, context);
        }
        return null;
    }

    public static Date convertStringTimeToDate(Context context, String time, int icon) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(new Date());
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);
            return formatter.parse(String.format("%s.%s.%s %s", day, month, year, time));
        } catch (Exception ex) {
            MessageHelper.printException(ex, icon, context);
        }
        return null;
    }

    public static Drawable convertStringToImage(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] convertStringToByteArray(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Converter.convertStreamToByteArray(is);
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] convertDrawableToByteArray(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static byte[] convertDrawableToByteArray(Context context, int id) {
        Drawable d;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            d = context.getDrawable(id);
        } else {
            d = context.getResources().getDrawable(id);
        }
        BitmapDrawable bitmapDrawable = ((BitmapDrawable)d);
        if(bitmapDrawable!=null) {
            Bitmap bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            return stream.toByteArray();
        }
        return null;
    }

    public static Bitmap convertByteArrayToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static byte[] convertBitmapToByteArray(Bitmap bitmap) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        stream.close();
        return byteArray;
    }

    private static byte[] convertStreamToByteArray(InputStream stream) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = stream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        stream.close();
        return buffer.toByteArray();
    }

    public static void convertByteArrayToFile(byte[] content, File file) throws Exception {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(content);
        fos.close();
    }

    @SuppressWarnings("deprecation")
    public static Drawable convertResourcesToDrawable(Context context, int resource_id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(resource_id);
        } else {
            return context.getResources().getDrawable(resource_id);
        }
    }

    public static String convertURIToStringPath(Context context, Uri contentUri, int icon) {
        Cursor cursor = null;
        try {
            String[] projection = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  projection, null, null, null);
            int column_index = 0;
            if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            }
            if (cursor != null) {
                cursor.moveToFirst();
            }
            if (cursor != null) {
                return cursor.getString(column_index);
            }
        } catch (Exception ex) {
            MessageHelper.printException(ex, icon, context);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return "";
    }

    public static Bitmap convertUriToBitmap(Context context, Uri uri) throws Exception {
        return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
    }
}
