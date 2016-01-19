package hr.foi.teamup.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hr.foi.air.teamup.Logger;
import hr.foi.teamup.R;
import hr.foi.teamup.model.Team;

/**
 * used to list teams in team history
 * Created by maja on 27.11.15..
 */
public class TeamAdapter extends BaseAdapter<Team> {

    public TeamAdapter(Context context, int resource, ArrayList<Team> items) {
        super(context, resource, items);
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
                vi = getInflater().inflate(R.layout.list_item_team_history, null);
                holder = new ViewHolder();
                holder.teamCode = (TextView) vi.findViewById(R.id.team_code);
                holder.teamName = (TextView) vi.findViewById(R.id.team_name);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            Team current = getItems().get(position);
            holder.teamName.setText(current.getName());
            holder.teamCode.setText(current.getNfcCode());

        } catch (Exception e) {
            Logger.log("Failed to fill view with team names", getClass().getName(), Log.ERROR);
        }
        return vi;
    }
}
