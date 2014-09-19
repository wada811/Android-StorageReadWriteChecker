package at.wada811.storagereadwritechecker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wada on 2014/07/10.
 */
public class StorageAdapter extends BindableAdapter<Storage> {

    public StorageAdapter(Context context, List<Storage> list) {
        super(context, list);
    }

    class ViewHolder {

        TextView method;
        TextView dir;
        TextView read;
        TextView write;
        TextView remain;

        public ViewHolder(View view) {
            method = (TextView)view.findViewById(R.id.method);
            dir = (TextView)view.findViewById(R.id.dir);
            read = (TextView)view.findViewById(R.id.read);
            write = (TextView)view.findViewById(R.id.write);
            remain = (TextView)view.findViewById(R.id.remain);
        }
    }

    @Override
    public View newView(LayoutInflater inflater, int position, ViewGroup container) {
        View view = inflater.inflate(R.layout.list_item_main, container, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(Storage item, int position, View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        // bind
        holder.method.setText(item.getMethod());
        holder.dir.setText(item.getFile() == null ? "ー" : item.getFile().getAbsolutePath());
        holder.read.setText(item.isReadable() ? "読" : "ー");
        holder.write.setText(item.isWritable() ? "書" : "ー");
        holder.remain.setText(item.isRemain() ? "残" : "ー");
    }
}
