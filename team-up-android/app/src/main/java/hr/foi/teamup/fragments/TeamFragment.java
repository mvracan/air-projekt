package hr.foi.teamup.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import hr.foi.air.teamup.Logger;
import hr.foi.teamup.R;
import hr.foi.teamup.adapters.PersonAdapter;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.webservice.ServiceAsyncTask;
import hr.foi.teamup.webservice.ServiceCaller;
import hr.foi.teamup.webservice.ServiceParams;
import hr.foi.teamup.webservice.ServiceResponse;
import hr.foi.teamup.webservice.SimpleResponseHandler;

/**
 * fragment containing current team members list
 * Created by Tomislav Turek on 05.12.15..
 */
public class TeamFragment extends Fragment {

    ListView users;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_team_current, container, false);

        /*ServiceParams params = new ServiceParams(getString(hr.foi.teamup.webservice.R.string.team_path),
                ServiceCaller.HTTP_GET, null);
        new ServiceAsyncTask(currentTeamHandler).execute(params);*/

        users = (ListView)v.findViewById(R.id.current_team_list);
        return v;
    }

    SimpleResponseHandler currentTeamHandler = new SimpleResponseHandler() {
        @Override
        public boolean handleResponse(ServiceResponse response) {
            if(response.getHttpCode() == 200) {
                Type listType = new TypeToken<ArrayList<Person>>() {
                }.getType();
                ArrayList<Person> p = new Gson().fromJson(response.getJsonResponse(), listType);

                Logger.log("Setting team history data...", getClass().getName());
                users.setAdapter(new PersonAdapter(getActivity().getApplicationContext(),
                        R.layout.fragment_team_current, p));

                return true;
            } else {
                return false;
            }
        }
    };
}
