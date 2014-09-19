package at.wada811.storagereadwritechecker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ListAdapter;


public class StorageListFragment extends ListFragment {

    public static final String TAG = StorageListFragment.class.getSimpleName();
    private static final String KEY_EMPTY_TEXT = "KEY_EMPTY_TEXT";

    public static StorageListFragment newInstance(String emptyText, ListAdapter adapter){
        StorageListFragment storageListFragment = new StorageListFragment();
        storageListFragment.setListAdapter(adapter);
        Bundle args = new Bundle();
        args.putString(KEY_EMPTY_TEXT, emptyText);
        storageListFragment.setArguments(args);
        return storageListFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(getArguments().getString(KEY_EMPTY_TEXT));
    }

}
