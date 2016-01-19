package hr.foi.teamup.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import hr.foi.air.teamup.Logger;
import hr.foi.teamup.R;
import hr.foi.teamup.adapters.PersonAdapter;
import hr.foi.teamup.model.Person;

/**
 * fragment containing current team members list
 * Created by Tomislav Turek on 05.12.15..
 */
public class TeamFragment extends Fragment {

    ListView users;
    PersonAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        View v = inflater.inflate(R.layout.fragment_team_current, container, false);

        users = (ListView)v.findViewById(R.id.current_team_list);
        return v;
    }

    public void updateList(ArrayList<Person> list){
        if(list != null && isVisible()) {
            Logger.log("PROSO", Log.ERROR);
            adapter = new PersonAdapter(getActivity().getApplicationContext(), R.layout.fragment_team_current, list);
            users.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        users.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter = (PersonAdapter)users.getAdapter();
    }
}
