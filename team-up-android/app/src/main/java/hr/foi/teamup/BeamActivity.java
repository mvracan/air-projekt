package hr.foi.teamup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.net.CookieHandler;
import java.util.HashMap;
import java.util.Map;

import hr.foi.air.teamup.Logger;
import hr.foi.air.teamup.SessionManager;
import hr.foi.air.teamup.TeamJoinerCallback;
import hr.foi.air.teamup.nfcaccess.NfcBeamActivity;
import hr.foi.air.teamup.nfcaccess.NfcNotAvailableException;
import hr.foi.air.teamup.nfcaccess.NfcNotEnabledException;
import hr.foi.teamup.model.Credentials;
import hr.foi.teamup.model.Location;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.model.Team;
import hr.foi.teamup.model.TeamMessage;
import hr.foi.teamup.stomp.ListenerSubscription;
import hr.foi.teamup.stomp.TeamConnection;
import hr.foi.teamup.webservice.ServiceAsyncTask;
import hr.foi.teamup.webservice.ServiceCaller;
import hr.foi.teamup.webservice.ServiceParams;
import hr.foi.teamup.webservice.ServiceResponse;
import hr.foi.teamup.webservice.SimpleResponseHandler;

public class BeamActivity extends NfcBeamActivity {

    HashMap<String,ListenerSubscription> subscriptionChannels;
    TeamConnection socket;
    String cookie;
    String USER_CHANNEL_PATH = "/user/queue/messages";
    String GROUP_PATH = "/topic/team/1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beam);

        subscriptionChannels = new HashMap<>();
        Team t = SessionManager.getInstance(this).retrieveSession(SessionManager.TEAM_INFO_KEY, Team.class);

        try {
            startNfcAdapter();
            startNfcBeam("asdf", callback);
        } catch (NfcNotAvailableException e) {
            e.printStackTrace();
        } catch (NfcNotEnabledException e) {
            e.printStackTrace();
        }
        onSubmit.onClick(null);

    }

    private void joinGroup(){
        subscriptionChannels.put(USER_CHANNEL_PATH,subscription);
        subscriptionChannels.put(GROUP_PATH,subscription);

        socket = new TeamConnection(subscriptionChannels,cookie);
        socket.start();
    }

    ListenerSubscription subscription=new ListenerSubscription() {
        @Override
        public void onMessage(Map<String, String> headers, String body) {
            Logger.log(body);
        }

        @Override
        public void onPreSend() {

        }

        @Override
        public void onPostSend() {

        }
    };

    TeamJoinerCallback callback = new TeamJoinerCallback() {
        @Override
        public void onMessageReceived(String message) {
            Logger.log(message);
        }
    };

    View.OnClickListener onSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Credentials credentials = new Credentials( "lpalaic" , "palaic" );


            new ServiceAsyncTask(handler).execute(new ServiceParams(
                    "/login",
                    ServiceCaller.HTTP_POST, "application/x-www-form-urlencoded" ,null,"username=lpalaic&password=palaic"));



        }

    };

    SimpleResponseHandler handler = new SimpleResponseHandler() {
        @Override
        public boolean handleResponse(ServiceResponse response) {
            Logger.log("Got response: " + response.toString(), getClass().getName(), Log.DEBUG);

            if (response.getHttpCode() == 202) {

                Log.i("COOKIE ", response.getCookie());

                SessionManager manager = SessionManager.getInstance(getApplicationContext());
                manager.createSession(response.getCookie(), SessionManager.COOKIE_KEY);

                //Intent intent = new Intent(getApplicationContext(), BeamActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //getApplicationContext().startActivity(intent);

                onPing.onClick(null);


                return true;

            } else {
                // http code different from 200 OK
                Logger.log("LoginHandler -- invalid credentials sent", Log.WARN);
                Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_LONG).show();
                return false;
            }
        }
    };

    View.OnClickListener onPing = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("here","here");
            SessionManager manager = SessionManager.getInstance(getApplicationContext());
            cookie= manager.retrieveSession(SessionManager.COOKIE_KEY, String.class);

            if(cookie!=null) {
                joinGroup();
            }else {
                Log.i("no cookie", "nocokkie");
            }

            onSend.onClick(null);
        }

    };
    View.OnClickListener onSend = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Log.i("sending my information", " lalal");

            //client.send("/app/activeUsers", null, null);

            TeamMessage message = new TeamMessage();

            Credentials cred=new Credentials("a","a");
            Location loc=new Location(1,1);

            Person test= new Person(1,"a","a",cred,loc);
            message.setMessage(test);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            socket.send("/app/team/1",message);

        }

    };


}
