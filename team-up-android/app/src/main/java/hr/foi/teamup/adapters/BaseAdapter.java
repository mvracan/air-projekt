package hr.foi.teamup.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * Created by Tomislav Turek on 06.12.15..
 */
public abstract class BaseAdapter extends ArrayAdapter<Serializable> {

    LayoutInflater inflater;
    ArrayList<Serializable> items;

    public BaseAdapter(Context context, int resource, ArrayList<Serializable> items) {
        super(context, resource);
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Serializable getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getPosition(Serializable item) {
        for(int i = 0; i < items.size(); i++) {
            if(items.get(i) == item) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public ArrayList<Serializable> getItems() {
        return items;
    }
}
