package hr.foi.air.teamup.nfcaccess;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.tech.NfcF;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import hr.foi.air.teamup.Logger;

/**
 * abstract activity to extend when nfc needs to be used
 * Created by Tomislav Turek on 07.12.15..
 */
public abstract class NfcBeamActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {

    private NfcAdapter adapter;
    protected String message;
    private static final String NFC_MIME_TYPE = "text/plain";
    protected PendingIntent mPendingIntent;
    protected NfcBeamMessageCallback callback;
    protected IntentFilter[] mFilters;
    protected String[][] mTechLists;

    public void setCallback(NfcBeamMessageCallback callback) {
        this.callback = callback;
    }

    /**
     * gets called after intent is set to handle it
     */
    @Override
    protected void onResume() {

        super.onResume();
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Setup an intent filter for all MIME based dispatches
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[] {
                ndef,
        };

        // Setup a tech list for all NfcF tags
        mTechLists = new String[][] { new String[] { NfcF.class.getName() } };
        adapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);

        Intent intent = getIntent();

        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Logger.log("SReceiving team");
            Parcelable[] raw = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage ndefMessage = (NdefMessage) raw[0];
            Logger.log("Receiving team with id : " + new String(ndefMessage.getRecords()[0].getPayload()));

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
    protected void startNfcBeam(NfcBeamMessageCallback callback) throws NfcNotAvailableException {
        startNfcBeam(null, callback);
    }

    /**
     * starts the beaming process, waits for the devices to get paired
     * @param message message to beam to the phone, null if in reader mode
     * @throws NfcNotAvailableException thrown if adapter was not initialized
     */
    protected void startNfcBeam(String message, NfcBeamMessageCallback callback) throws NfcNotAvailableException {

        if(adapter == null) {
            throw new NfcNotAvailableException("Nfc adapter is not available or isn't working," +
                    " use startNfcAdapter before beaming");
        } else {
            Logger.log("Sending message" + message);
            this.message = message;
            this.callback = callback;
            adapter.setNdefPushMessageCallback(this, this);
        }
    }
}
