package hr.foi.teamup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import hr.foi.air.teamup.SessionManager;
import hr.foi.air.teamup.nfcaccess.NfcBeamActivity;
import hr.foi.air.teamup.nfcaccess.NfcNotAvailableException;
import hr.foi.air.teamup.nfcaccess.NfcNotEnabledException;
import hr.foi.air.teamup.prompts.AlertPrompt;
import hr.foi.teamup.model.Team;

public class BeamActivity extends NfcBeamActivity {

    ImageView beamDevice;
    ImageView fdDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beam);

        beamDevice = (ImageView)findViewById(R.id.beamDevice);
        fdDevice = (ImageView)findViewById(R.id.fdDevice);

        Team t = SessionManager.getInstance(this).retrieveSession(SessionManager.TEAM_INFO_KEY, Team.class);

        // handle nfc
        try {
            startNfcAdapter();

            if(t!=null) {
                startNfcBeam(Long.toString(t.getIdTeam()), callback);
                startAnimation();
            }

        } catch (NfcNotAvailableException e) {
            Toast.makeText(this, "This phone does not support NFC", Toast.LENGTH_LONG).show();
            finish();
        } catch (NfcNotEnabledException e) {
            // ask and open settings
            AlertPrompt prompt = new AlertPrompt(this);
            prompt.prepare(R.string.nfc_error, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                }
            }, R.string.open_settings, null, R.string.cancel);
            prompt.showPrompt();
            finish();
        }

    }

    /**
     * animates devices
     */
    private void startAnimation(){
        Animation left= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_left_nfc);
        Animation right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_right_nfc);
        beamDevice.startAnimation(left);
        fdDevice.startAnimation(right);
    }

}
