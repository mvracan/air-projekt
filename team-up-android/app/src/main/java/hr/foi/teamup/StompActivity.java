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
import hr.foi.teamup.model.ChatMessage;
import hr.foi.teamup.model.Credentials;
import hr.foi.teamup.model.Location;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.stomp.ListenerSubscription;
import hr.foi.teamup.stomp.ListenerWSNetwork;
import hr.foi.teamup.stomp.Stomp;
import hr.foi.teamup.stomp.Subscription;
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
    Map<String,String> headersSetup = new HashMap<String,String>();
    SessionManager manager;


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
        Thread thread= new Thread(new Runnable(){
            @Override
            public void run(){


                headersSetup.put("Cookie", cookie);
                Log.i("connect", "going to connect to stomp with cookie" + cookie);
                Log.i("connect", "going to connect to stomp");

                client = new Stomp(websocketConnection + "/chat", headersSetup, new ListenerWSNetwork() {
                    @Override
                    public void onState(int state) {
                        Log.i("State :", new Integer(state).toString());
                    }
                });

                client.connect();


               // client.send("app/activeUsers", null, null);

                Log.i("connect - headers ", client.getHeaders().get("Cookie"));

                /*
                client.subscribe(new Subscription("/topic/active", new ListenerSubscription() {
                    @Override
                    public void onMessage(Map<String, String> headers, final String message) {

                        Log.i("la", "la");

                        Log.i("poeuka", message);

                    }

                    @Override
                    public void onPreSend() {

                    }

                    @Override
                    public void onPostSend() {

                    }
                }));
                */


                client.subscribe(new Subscription("/user/queue/messages", new ListenerSubscription() {
                    @Override
                    public void onMessage(Map<String, String> headers, final String message) {

                        Log.i("la", "la");

                        Log.i("la", message);




                    }

                    @Override
                    public void onPreSend() {

                    }

                    @Override
                    public void onPostSend() {

                    }
                }));

                client.subscribe(new Subscription("/topic/group/1", new ListenerSubscription() {
                    @Override
                    public void onMessage(Map<String, String> headers, final String message) {

                        Log.i("la", "la");

                        Log.i("la", message);




                    }

                    @Override
                    public void onPreSend() {

                    }

                    @Override
                    public void onPostSend() {

                    }
                }));


            //client.send(websocketConnection+"/app/activeUsers", null ,null);

            }
        });



    thread.start();

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
            SessionManager manager = SessionManager.getInstance(getApplicationContext());
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


            client.send("/app/group/1",null, new Gson().toJson(message));

        }

    };



}


