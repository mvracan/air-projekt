package hr.foi.teamup.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import hr.foi.air.teamup.Logger;
import hr.foi.teamup.R;
import hr.foi.teamup.TeamActivity;
import hr.foi.teamup.adapters.PersonAdapter;
import hr.foi.teamup.model.Person;

/**
 * fragment containing current team members list
 * Created by Tomislav Turek on 05.12.15..
 */
public class TeamFragment extends Fragment {

    ListView users;
    PersonAdapter adapter;
    FloatingActionButton panicButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
      View v = inflater.inflate(R.layout.fragment_team_current, container, false);

        users = (ListView)v.findViewById(R.id.current_team_list);


        panicButton = (FloatingActionButton) v.findViewById(R.id.panic_button);
        panicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.log("Panic button activated");
                ((TeamActivity)getActivity()).panic();
            }
        });

        return v;
    }


    public void setViewLayout(int id){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mainView= inflater.inflate(id, null);
        ((TextView)mainView.findViewById(R.id.empty_message)).setText(R.string.empty_team);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(mainView);
    }

    public void updateList(ArrayList<Person> list){
        if(list != null && isVisible()) {
            Logger.log("PROSO", Log.ERROR);
            adapter = new PersonAdapter(getActivity().getApplicationContext(), R.layout.fragment_team_current, list);
            users.setAdapter(adapter);
        }

    }


}
