package hr.foi.teamup.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import hr.foi.air.teamup.Logger;
import hr.foi.teamup.R;
import hr.foi.teamup.model.Person;

/**
 * used to list persons in teams
 * Created by Tomislav Turek on 06.12.15..
 */
public class PersonAdapter extends BaseAdapter<Person> {

    public PersonAdapter(Context context, int resource, ArrayList<Person> items) {
        super(context, resource, items);
    }

    public static class ViewHolder {
        public ImageView status;
        public TextView personName;
        public TextView personSurname;
        public TextView personUsername;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = getInflater().inflate(R.layout.list_item_members, null);
                holder = new ViewHolder();
                holder.status = (ImageView) vi.findViewById(R.id.status);
                holder.personName = (TextView) vi.findViewById(R.id.itemName);
                holder.personSurname = (TextView) vi.findViewById(R.id.itemSurname);
                holder.personUsername = (TextView) vi.findViewById(R.id.itemUsername);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            Person current = getItems().get(position);
            holder.personName.setText(current.getName());
            holder.personUsername.setText(current.getCredentials().getUsername());
            holder.personSurname.setText(current.getSurname());

            setStatusIcon(holder,current.getPanic());


        } catch (Exception e) {
            Logger.log("Failed to fill view with team members", getClass().getName(), Log.ERROR);
        }
        return vi;
    }

    /**
     * Sets icon based on user status
     * @param holder
     * @param panic
     */
    public void setStatusIcon(ViewHolder holder,int panic){

        if(panic != 1)
            holder.status.setImageDrawable(getContext().getResources().getDrawable(android.R.drawable.presence_online));
        else
            holder.status.setImageDrawable(getContext().getResources().getDrawable(android.R.drawable.presence_busy));

    }

}
