package hr.foi.teamup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.HashMap;
import java.util.Map;

import hr.foi.air.teamup.SessionManager;
import hr.foi.teamup.handlers.CookieHandler;
import hr.foi.teamup.handlers.StompMessageHandler;
import hr.foi.teamup.model.ChatMessage;
import hr.foi.teamup.model.Credentials;
import hr.foi.teamup.model.Location;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.stomp.ListenerSubscription;
import hr.foi.teamup.stomp.ListenerWSNetwork;
import hr.foi.teamup.stomp.Stomp;
import hr.foi.teamup.stomp.Subscription;
import hr.foi.teamup.stomp.TeamConnection;
import hr.foi.teamup.webservice.ServiceAsyncTask;
import hr.foi.teamup.webservice.ServiceCaller;
import hr.foi.teamup.webservice.ServiceParams;


public class StompActivity extends AppCompatActivity {

    String websocketConnection="ws://46.101.115.78:8080";
    Stomp client;
    EditText firstName;
    EditText lastName;
    TextView viewText;
    Button submit;
    Button ping;
    Button send;
    String cookie;
    TeamConnection socket;
    SessionManager manager;
    HashMap <String, ListenerSubscription> subscriptionsChannels = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stomp_proba);

        // binding
        firstName = (EditText) findViewById(R.id.firstNameInput);
        lastName = (EditText) findViewById(R.id.lastNameInput);

        submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(onSubmit);

        ping= (Button) findViewById(R.id.pingButton);
        ping.setOnClickListener(onPing);

        send= (Button) findViewById(R.id.sendButton);
        send.setOnClickListener(onSend);

        viewText = (TextView)findViewById(R.id.tv);



        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        }


    private void joinGroup(){

        subscriptionsChannels.put("/user/queue/messages",new StompMessageHandler());
        subscriptionsChannels.put("/topic/group/1",new StompMessageHandler());

        socket= new TeamConnection(
        subscriptionsChannels, cookie);

        socket.start();


    }
    View.OnClickListener onSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Credentials credentials = new Credentials( "lpalaic" , "palaic" );


            CookieHandler cookieHandler = new CookieHandler(StompActivity.this, credentials);

            new ServiceAsyncTask(cookieHandler).execute(new ServiceParams(
                    "/login",
                    ServiceCaller.HTTP_POST, "application/x-www-form-urlencoded" ,null,"username=lpalaic&password=palaic"));



        }

        };

    View.OnClickListener onPing = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("here","here");
            manager = SessionManager.getInstance(getApplicationContext());
            cookie= manager.retrieveSession(SessionManager.COOKIE, String.class);

            if(cookie!=null) {
                joinGroup();
            }else {
                Log.i("no cookie", "nocokkie");
            }


        }

    };
    View.OnClickListener onSend = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Log.i("sending my information", " lalal");

            //client.send("/app/activeUsers", null, null);
            
            ChatMessage message = new ChatMessage();

            Credentials cred=new Credentials("a","a");
            Location loc=new Location(1,1);

            Person test= new Person(1,"a","a",cred,loc);
            message.setMessage(test);


            socket.send("/app/group/1",message);

        }

    };



}


