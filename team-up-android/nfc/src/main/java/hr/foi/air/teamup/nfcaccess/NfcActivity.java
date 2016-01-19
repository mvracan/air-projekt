package hr.foi.air.teamup.nfcaccess;

import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;

/**
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
            throw new NfcNotAvailableException("Nfc adapter is not available on this phone");
        }
        if((adapter = NfcAdapter.getDefaultAdapter(this))==null){
            throw new NfcNotEnabledException("Nfc adapter is not enabled on this phone");
        }
    }

    public NfcAdapter getAdapter() {
        return adapter;
    }
}
