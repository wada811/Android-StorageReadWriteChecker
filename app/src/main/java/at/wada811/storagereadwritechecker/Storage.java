package at.wada811.storagereadwritechecker;

import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by wada on 2014/07/10.
 */
public class Storage {

    private String method;
    private File file;
    private boolean isReadable;
    private boolean isWritable;
    private boolean isRemain;

    public Storage(File file, String method) {
        this.file = file;
        this.method = method;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void read(){
        if(file == null){
            isReadable = false;
        }else {
            isReadable = file.canRead();
        }
    }

    public boolean isReadable() {
        return isReadable;
    }

    public void setReadable(boolean isReadable) {
        this.isReadable = isReadable;
    }

    public void write(){
        if(file == null){
            isWritable = false;
        }else {
            file.mkdirs();
            try {
                File tempFile = new File(file, BuildConfig.FLAVOR);
                isRemain = !tempFile.createNewFile();
                isWritable = true;
            } catch (IOException e) {
                e.printStackTrace();
                isWritable = false;
            }
//            isWritable = file.canWrite();
        }
    }

    public boolean isWritable() {
        return isWritable;
    }

    public void setWritable(boolean isWritable) {
        this.isWritable = isWritable;
    }

    public boolean isRemain() {
        return isRemain;
    }

    public void setRemain(boolean isRemain) {
        this.isRemain = isRemain;
    }

    public boolean delete(){
        File tempFile = new File(file, BuildConfig.FLAVOR);
        return tempFile.delete();
    }


    @Override
    public String toString(){
        JSONObject json = new JSONObject();
        try {
            json.put("method", method);
            json.put("path", file == null ? "" : file.getAbsolutePath());
            json.put("isReadable", isReadable);
            json.put("isWritable", isWritable);
            json.put("isRemain", isRemain);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}
