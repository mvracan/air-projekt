package hr.foi.air.teamup.nfcaccess;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import hr.foi.air.teamup.TeamJoinerCallback;

/**
 * abstract activity to extend when nfc needs to be used
 * Created by Tomislav Turek on 07.12.15..
 */
public abstract class NfcBeamActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {

    private NfcAdapter adapter;
    private String message;
    private static final String NFC_MIME_TYPE = "text/plain";
    private TeamJoinerCallback callback;

    /**
     * gets called after intent is set to handle it
     */
    @Override
    protected void onResume() {

        super.onResume();
        Intent intent = getIntent();
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] raw = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage ndefMessage = (NdefMessage) raw[0];
            callback.onMessageReceived(new String(ndefMessage.getRecords()[0].getPayload()));

        }
    }

    /**
     * creates the ndef message to beam from passed message
     * @param event devices recognized
     * @return message in ndef format
     */
    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        return new NdefMessage(NdefRecord.createMime(NFC_MIME_TYPE, message.getBytes()));
    }

    /**
     * sets the intent when beam is recognized
     * @param intent passed beam intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    /**
     * starts the nfc adapter, should be called in onCreate
     * @throws NfcNotAvailableException thrown if the phone does not support nfc
     */
    protected void startNfcAdapter() throws NfcNotAvailableException, NfcNotEnabledException {
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC)) {
            throw new NfcNotAvailableException("Nfc adapter is not available on this phone");
        }
        if((adapter = NfcAdapter.getDefaultAdapter(this))==null){
            throw new NfcNotEnabledException("Nfc adapter is not enabled on this phone");
        }
    }

    /**
     * starts the beaming process, by default in reader mode,
     * waits for the devices to get paired
     * @param callback callback object that handles beam response
     * @throws NfcNotAvailableException
     */
    protected void startNfcBeam(TeamJoinerCallback callback) throws NfcNotAvailableException {
        startNfcBeam(null, callback);
    }

    /**
     * starts the beaming process, waits for the devices to get paired
     * @param message message to beam to the phone, null if in reader mode
     * @throws NfcNotAvailableException thrown if adapter was not initialized
     */
    protected void startNfcBeam(String message, TeamJoinerCallback callback) throws NfcNotAvailableException {

        if(adapter == null) {
            throw new NfcNotAvailableException("Nfc adapter is not available or isn't working," +
                    " use startNfcAdapter before beaming");
        } else {
            this.message = message;
            this.callback = callback;
            adapter.setNdefPushMessageCallback(this, this);
        }
    }
}
