package hr.foi.teamup.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hr.foi.teamup.R;

/**
 * delivers message that no team is present
 * Created by Tomislav Turek on 26.01.16..
 */
public class EmptyDataFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.layout_empty_data, container, false);

        TextView t = (TextView) v.findViewById(R.id.empty_message);
        t.setText(R.string.empty_team);

        return v;
    }
}
