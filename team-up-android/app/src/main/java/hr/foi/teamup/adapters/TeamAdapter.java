package hr.foi.teamup.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import hr.foi.air.teamup.Logger;
import hr.foi.teamup.R;
import hr.foi.teamup.model.Team;

/**
 * used to list teams in team history
 * Created by maja on 27.11.15..
 */
public class TeamAdapter extends ArrayAdapter<Team> {

    ArrayList<Team> items;
    LayoutInflater inflater;

    public TeamAdapter(Context context, int resource, ArrayList<Team> items) {
        super(context, resource, items);
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder {
        public TextView teamName;
        public TextView teamCode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                // TODO: check if works (parent was null before)
                vi = inflater.inflate(R.layout.list_item_team_history, parent);
                holder = new ViewHolder();
                holder.teamCode = (TextView) vi.findViewById(R.id.team_code);
                holder.teamName = (TextView) vi.findViewById(R.id.team_name);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            Team current = items.get(position);
            holder.teamName.setText(current.getName());
            holder.teamCode.setText(current.getNfcCode());

        } catch (Exception e) {
            Logger.log("Failed to fill view with team names", getClass().getName(), Log.ERROR);
        }
        return vi;
    }
}
