package hr.foi.teamup.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hr.foi.air.teamup.Logger;
import hr.foi.teamup.R;
import hr.foi.teamup.model.Team;

/**
 *
 * Created by maja on 27.11.15..
 */
public class TeamAdapter extends ArrayAdapter<Team> {

    LayoutInflater inflater;
    ArrayList<Team> teams;

    public TeamAdapter(Context context, int resource, ArrayList<Team> teams) {
        super(context, resource);
        this.teams = teams;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return teams.size();
    }

    @Override
    public Team getItem(int position) {
        return teams.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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

            holder.teamName.setText(teams.get(position).getName());
            holder.teamCode.setText(teams.get(position).getNfcCode());

        } catch (Exception e) {
            Logger.log("Failed to fill view with team names", getClass().getName(), Log.ERROR);
        }
        return vi;
    }
}
