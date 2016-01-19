package hr.foi.teamup;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import hr.foi.air.teamup.Logger;
import hr.foi.air.teamup.SessionManager;
import hr.foi.air.teamup.nfcaccess.NfcBeamMessageCallback;
import hr.foi.air.teamup.nfcaccess.NfcBeamActivity;
import hr.foi.air.teamup.nfcaccess.NfcNotAvailableException;
import hr.foi.air.teamup.nfcaccess.NfcNotEnabledException;
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

    String IMG_PATH="@drawable/";
    ImageView beamDevice;
    ImageView fdDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_beam);

        beamDevice = (ImageView)findViewById(R.id.beamDevice);
        fdDevice = (ImageView)findViewById(R.id.fdDevice);


        Team t = SessionManager.getInstance(this).retrieveSession(SessionManager.TEAM_INFO_KEY, Team.class);


            try {
                Logger.log("Sending team");
                startNfcAdapter();

                if(t!=null) {
                    startNfcBeam(Long.toString(t.getIdTeam()), callback);
                    startAnimation();
                }

            } catch (NfcNotAvailableException e) {
                e.printStackTrace();
            } catch (NfcNotEnabledException e) {
                e.printStackTrace();
            }

    }

    private void startAnimation(){
        Animation left= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_left_nfc);
        Animation right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_right_nfc);

        beamDevice.startAnimation(left);
        fdDevice.startAnimation(right);

    }

}
