package at.wada811.storagereadwritechecker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends ActionBarActivity {

    private ArrayList<Storage> storages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            storages = new ArrayList<Storage>();
            initList(storages);
            for (Storage storage : storages) {
                storage.read();
                if(storage.isReadable()) {
                    storage.write();
                }
                storage.read();
            }
            StorageAdapter storageAdapter = new StorageAdapter(this, storages);
            StorageListFragment storageListFragment = StorageListFragment.newInstance(getString(R.string.dir_name), storageAdapter);
            getSupportFragmentManager().beginTransaction().replace(R.id.list, storageListFragment).commit();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initList(ArrayList<Storage> storages){
        storages.add(new Storage(getCacheDir(), "getCacheDir"));
        storages.add(new Storage(getDir(getString(R.string.dir_name), Context.MODE_PRIVATE), "getDir"));
        storages.add(new Storage(getDatabasePath(getString(R.string.dir_name)), "getDatabasePath"));
        storages.add(new Storage(getExternalCacheDir(), "getExternalCacheDir"));
        ArrayList<String> types = new ArrayList<String>(Arrays.asList(
                Environment.DIRECTORY_ALARMS,
                Environment.DIRECTORY_DCIM,
                Environment.DIRECTORY_DOWNLOADS,
                Environment.DIRECTORY_MOVIES,
                Environment.DIRECTORY_MUSIC,
                Environment.DIRECTORY_NOTIFICATIONS,
                Environment.DIRECTORY_PICTURES,
                Environment.DIRECTORY_PODCASTS,
                Environment.DIRECTORY_RINGTONES
        ));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            types.add(Environment.DIRECTORY_DOCUMENTS);
        }
        for (String type : types) {
            storages.add(new Storage(getExternalFilesDir(type), "getExternalFilesDir(" + type + ")"));
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            for (File file : getExternalCacheDirs()) {
                storages.add(new Storage(file, "getExternalCacheDirs(" + file.getName() + ")"));
            }
            for (String type : types) {
                for (File file : getExternalFilesDirs(type)) {
                    storages.add(new Storage(file, "getExternalFilesDirs(" + file.getName() + ")"));
                }
            }
        }
        storages.add(new Storage(getFileStreamPath(getString(R.string.dir_name)), "getFileStreamPath"));
        storages.add(new Storage(getFilesDir(), "getFilesDir"));
        storages.add(new Storage(getObbDir(), "getObbDir"));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            for (File file : getObbDirs()) {
                storages.add(new Storage(file, "getObbDirs(" + file.getName() + ")"));
            }
        }
        storages.add(new Storage(new File(getPackageCodePath()), "getPackageCodePath"));
        storages.add(new Storage(new File(getPackageResourcePath()), "getPackageResourcePath"));
        storages.add(new Storage(Environment.getDataDirectory(), "Environment.getDataDirectory"));
        storages.add(new Storage(Environment.getDownloadCacheDirectory(), "Environment.getDownloadCacheDirectory"));
        storages.add(new Storage(Environment.getExternalStorageDirectory(), "Environment.getExternalStorageDirectory"));
        for (String type : types) {
            storages.add(new Storage(Environment.getExternalStoragePublicDirectory(type), "Environment.getExternalStorageDirectory(" + type + ")"));
        }
        storages.add(new Storage(Environment.getRootDirectory(), "Environment.getRootDirectory"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_export) {
            JSONArray jsonArray = new JSONArray();
            for (Storage storage : storages) {
                try {
                    jsonArray.put(new JSONObject(storage.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType(HTTP.PLAIN_TEXT_TYPE);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{});
//            intent.putExtra(Intent.EXTRA_CC, cc);
//            intent.putExtra(Intent.EXTRA_BCC, bcc);
            intent.putExtra(Intent.EXTRA_SUBJECT, BuildConfig.FLAVOR);
            intent.putExtra(Intent.EXTRA_TEXT, jsonArray.toString());
            intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
            startActivity(intent);
            return true;
        }else if(id == R.id.action_delete){
            int count = 0;
            int size = 0;
            Toast toast = Toast.makeText(this, "deleting ", Toast.LENGTH_SHORT);
            for (Storage storage : storages) {
                if(storage.isWritable()){
                    size++;
                }
                boolean success = storage.delete();
                if(success){
                    toast.setText("deleting " + ++count + "/" + size);
                    toast.show();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
