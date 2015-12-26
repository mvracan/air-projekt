package hr.foi.teamup;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import hr.foi.air.teamup.Logger;
import hr.foi.air.teamup.SessionManager;
import hr.foi.air.teamup.nfcaccess.TeamJoinerCallback;
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
    String GROUP_PATH = "/topic/team/";
    String IMG_PATH="@drawable/";
    String TEAM_ID;
    ImageView image;
    SessionManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_beam);

        image = (ImageView)findViewById(R.id.imageView);

        if(image==null)
            Logger.log("Null je");

        if(SessionManager.getInstance(this).retrieveSession(SessionManager.TEAM_INFO_KEY, Team.class) != null)
            image = setImage(image, true);
        else
            image = setImage(image, false);




    }

    public ImageView setImage(ImageView img,boolean isIn){
        int imageResource;
        if(isIn){
            imageResource = getResources().getIdentifier(IMG_PATH+"send", null, getPackageName());
            Logger.log("in : "+Integer.toString(imageResource));
        }
        else{
            imageResource = getResources().getIdentifier(IMG_PATH+"receive", null, getPackageName());
            Logger.log("out : "+ Integer.toString(imageResource));
        }

        img.setImageResource(imageResource);

        return img;
    }

    public void onBeam(View view) {


            Logger.log("Here");



        subscriptionChannels = new HashMap<>();
        Team t = SessionManager.getInstance(this).retrieveSession(SessionManager.TEAM_INFO_KEY, Team.class);

        if(t!=null) {
            try {
                Logger.log("Sending team");
                startNfcAdapter();
                startNfcBeam(Long.toString(t.getIdTeam()), callback);
            } catch (NfcNotAvailableException e) {
                e.printStackTrace();
            } catch (NfcNotEnabledException e) {
                e.printStackTrace();
            }
        }else{

            this.onResume();

        }

    }

    private void joinGroup(){

        manager = SessionManager.getInstance(getApplicationContext());
        cookie= manager.retrieveSession(SessionManager.COOKIE_KEY, String.class);

        if(cookie != null) {
            subscriptionChannels.put(USER_CHANNEL_PATH, subscription);
            subscriptionChannels.put(GROUP_PATH, subscription);

            socket = new TeamConnection(subscriptionChannels, cookie);
            socket.start();
        }

    }



    ListenerSubscription subscription=new ListenerSubscription() {
        @Override
        public void onMessage(Map<String, String> headers, String body) {
            Logger.log(body);
        }


    };

    TeamJoinerCallback callback = new TeamJoinerCallback() {
        @Override
        public void onMessageReceived(String message) {
            Logger.log(message);

            TEAM_ID=message;

            Person client= manager.retrieveSession(SessionManager.PERSON_INFO_KEY, Person.class);

            new ServiceAsyncTask(handler).execute(new ServiceParams(
                    "/login",
                    ServiceCaller.HTTP_POST, "application/x-www-form-urlencoded", null, "username=" + client.getCredentials().getUsername() +
                    "&password=" + client.getCredentials().getPassword()));


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

                if(cookie!=null) {

                    joinGroup();
                    Ping();

                }else {
                    Log.i("no cookie", "nocokkie");
                }

                return true;

            } else {
                // http code different from 200 OK
                Logger.log("LoginHandler -- invalid credentials sent", Log.WARN);
                Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_LONG).show();
                return false;
            }
        }
    };

    public void Ping(){

        TeamMessage message = new TeamMessage();

        Person test= manager.retrieveSession(SessionManager.PERSON_INFO_KEY, Person.class);
        message.setMessage(test);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        socket.send("/app/team/"+TEAM_ID,message);

    }

}
