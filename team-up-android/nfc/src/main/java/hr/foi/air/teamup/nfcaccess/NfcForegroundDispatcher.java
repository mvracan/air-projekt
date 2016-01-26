package hr.foi.air.teamup.nfcaccess;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Parcelable;

/**
 *
 * Created by Tomislav Turek on 19.01.16..
 */
public abstract class NfcForegroundDispatcher extends NfcActivity {

    protected PendingIntent mPendingIntent;
    protected IntentFilter[] mFilters;
    protected String[][] mTechLists;
    protected NfcBeamMessageCallback nfcDispatchCallback;

    public void setNfcDispatchCallback(NfcBeamMessageCallback callback) {
        this.nfcDispatchCallback = callback;
    }

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
            throw new RuntimeException(getString(R.string.unknown_mime_type), e);
        }
        mFilters = new IntentFilter[] {
                ndef,
        };

        // Setup a tech list for all NfcF tags
        mTechLists = new String[][] { new String[] { NfcF.class.getName() } };
        getAdapter().enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);

        Intent intent = getIntent();

        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] raw = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage ndefMessage = (NdefMessage) raw[0];
            nfcDispatchCallback.onMessageReceived(new String(ndefMessage.getRecords()[0].getPayload()));
        }
    }
}
