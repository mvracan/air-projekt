package hr.foi.air.teamup.nfcaccess;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;

/**
 * base nfc activity
 * Created by Tomislav Turek on 19.01.16..
 */
abstract class NfcActivity extends AppCompatActivity {

    private NfcAdapter adapter;

    /**
     * starts the nfc adapter, should be called in onCreate
     * @throws NfcNotAvailableException thrown if the phone does not support nfc
     */
    protected void startNfcAdapter() throws NfcNotAvailableException, NfcNotEnabledException {
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC)) {
            throw new NfcNotAvailableException(getString(R.string.nfc_not_available));
        }
        if((adapter = NfcAdapter.getDefaultAdapter(this))==null){
            throw new NfcNotEnabledException(getString(R.string.nfc_not_enabled));
        }
    }

    /**
     * sets the intent when beam is recognized
     * @param intent passed beam intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    protected NfcAdapter getAdapter() {
        return adapter;
    }

}
