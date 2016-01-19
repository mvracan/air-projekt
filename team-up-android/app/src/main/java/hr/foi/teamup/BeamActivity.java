package hr.foi.teamup;

import android.os.Bundle;
import android.util.Log;
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
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_beam);

        image = (ImageView)findViewById(R.id.imageView);

        if(SessionManager.getInstance(this).retrieveSession(SessionManager.TEAM_INFO_KEY, Team.class) != null)
            image = setImage(image, true);
        else
            image = setImage(image, false);

        Team t = SessionManager.getInstance(this).retrieveSession(SessionManager.TEAM_INFO_KEY, Team.class);


            try {
                Logger.log("Sending team");
                startNfcAdapter();

                if(t!=null)
                    startNfcBeam(Long.toString(t.getIdTeam()), callback);

            } catch (NfcNotAvailableException e) {
                e.printStackTrace();
            } catch (NfcNotEnabledException e) {
                e.printStackTrace();
            }



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

}
