package hr.foi.teamup.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import hr.foi.air.teamup.Logger;
import hr.foi.air.teamup.SessionManager;
import hr.foi.teamup.R;
import hr.foi.teamup.adapters.TeamAdapter;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.model.Team;
import hr.foi.teamup.webservice.ServiceAsyncTask;
import hr.foi.teamup.webservice.ServiceCaller;
import hr.foi.teamup.webservice.ServiceParams;
import hr.foi.teamup.webservice.ServiceResponse;
import hr.foi.teamup.webservice.SimpleResponseHandler;

/**
 * fragment with teams list that were joined earlier (history)
 * Created by maja on 27.11.15..
 */
public class TeamHistoryFragment extends Fragment {

    ListView teams;
    Person self;
    ViewGroup container;
    LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        View v = inflater.inflate(R.layout.fragment_team_history, container, false);

        teams = (ListView) v.findViewById(R.id.history_teams_list);
        self = SessionManager.getInstance(getActivity().getApplicationContext())
                .retrieveSession(SessionManager.PERSON_INFO_KEY, Person.class);

        Logger.log("Initiated history, calling service...", getClass().getName());

        ServiceParams params = new ServiceParams(
                getString(hr.foi.teamup.webservice.R.string.team_history_path) + self.getIdPerson(),
                ServiceCaller.HTTP_GET, null);
        new ServiceAsyncTask(historyHandler).execute(params);

        return v;
    }

    // fills list view with teams from history
    SimpleResponseHandler historyHandler = new SimpleResponseHandler() {
        @Override
        public boolean handleResponse(ServiceResponse response) {
            if(response.getHttpCode() == 200) {

                Type listType = new TypeToken<ArrayList<Team>>() {
                }.getType();
                ArrayList<Team> t = new Gson().fromJson(response.getJsonResponse(), listType);

                if(t == null || t.size() == 0) {
                    // TODO: check if this works
                    container.removeAllViews();
                    View v = inflater.inflate(R.layout.layout_empty_data, container);
                    ((TextView)v.findViewById(R.id.empty_message)).setText(R.string.empty_history);
                    // view returned?
                } else {
                    Logger.log("Setting team history data...", getClass().getName());
                    teams.setAdapter(new TeamAdapter(getActivity().getApplicationContext(),
                            R.layout.fragment_team_history, t));
                }

                return true;
            } else {
                Logger.log("Failed to fetch teams from history", getClass().getName(), Log.ERROR);
                Toast.makeText(getActivity().getApplicationContext(), "Failed to fetch history items", Toast.LENGTH_LONG).show();
                return false;
            }
        }
    };
}
