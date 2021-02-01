package com.h.gifutils.gif;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class GifUtils {
    /**
     * 生成gif图片
     */
    public static void makeGif(List<String> paths, String path, String name) {
        AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
        File pathFile = new File(path);
        File gifFile = new File(pathFile, name);
        if (!pathFile.exists()) {
            boolean mkdirs = pathFile.mkdirs();
        }
        if (!gifFile.exists())
            try {
                boolean newFile = gifFile.createNewFile();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        OutputStream os;
        try {
            os = new FileOutputStream(gifFile);
            gifEncoder.start(os);  //注意顺序
            for (String s : paths) {
                gifEncoder.addFrame(BitmapFactory.decodeFile(s));
            }
            gifEncoder.setDelay(500);
            gifEncoder.setRepeat(0);
            gifEncoder.finish();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void getBitmapSFromGif(Context context, Uri uri) {
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            GifDecoder gifDecoder = new GifDecoder();
            int code = gifDecoder.read(is);
            if (code == GifDecoder.STATUS_OK) {//解码成功
                GifDecoder.GifFrame[] frameList = gifDecoder.getFrames();

            } else if (code == GifDecoder.STATUS_FORMAT_ERROR) {//图片格式不是GIF

            } else {//图片读取失败

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
