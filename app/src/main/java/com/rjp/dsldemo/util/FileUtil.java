package com.rjp.dsldemo.util;

import android.content.Context;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * author: jinpeng.ren create at 2020/4/28 11:02
 */
public class FileUtil {

    /**
     * 获取存储的cache目录 非重要数据 重要数据存储在getExternalFileDir
     */
    public static File getStorageCacheDirectory(Context context) {
        if (isMounted()) {
            return context.getExternalCacheDir();
        } else {
            return context.getCacheDir();
        }
    }

    public static boolean isMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 图片拍照的保存路径
     * @param context
     * @return
     */
    public static String getAppImagesPath(Context context) {
        File file = createNewDir(new File(getStorageCacheDirectory(context), "Images"));
        return file == null ? "" : file.getAbsolutePath();
    }

    public static String getAppVideoPath(Context context) {
        File file = createNewDir(new File(getStorageCacheDirectory(context), "Videos"));
        return file == null ? "" : file.getAbsolutePath();
    }

    public static File createNewDir(File dir) {
        if (dir == null) {
            return null;
        }
        try {
            if (dir.exists()) {
                return dir;
            } else {
                dir.mkdirs();
            }
        } catch (Exception e) {
            return null;
        }
        return dir;
    }

    public static String readAssets2String(Context context, String assetsFilePath) {
        InputStream is;
        try {
            is = context.getAssets().open(assetsFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        byte[] bytes = is2Bytes(is);
        if (bytes == null) return null;
        if (isSpace("utf-8")) {
            return new String(bytes);
        } else {
            try {
                return new String(bytes, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static final int BUFFER_SIZE = 8192;

    private static byte[] is2Bytes(final InputStream is) {
        if (is == null) return null;
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            byte[] b = new byte[BUFFER_SIZE];
            int len;
            while ((len = is.read(b, 0, BUFFER_SIZE)) != -1) {
                os.write(b, 0, len);
            }
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
