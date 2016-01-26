package hr.foi.air.teamup.nfcaccess;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;

import hr.foi.air.teamup.Logger;

/**
 * abstract activity to extend when nfc needs to be used
 * Created by Tomislav Turek on 07.12.15..
 */
public abstract class NfcBeamActivity extends NfcActivity implements NfcAdapter.CreateNdefMessageCallback {

    protected String message;

    /**
     * creates the ndef message to beam from passed message
     * @param event devices recognized
     * @return message in ndef format
     */
    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        return new NdefMessage(NdefRecord.createMime(getString(R.string.nfc_mime_type), message.getBytes()));
    }

    /**
     * starts the beaming process, waits for the devices to get paired
     * @param message message to beam to the phone, null if in reader mode
     * @throws NfcNotAvailableException thrown if adapter was not initialized
     */
    protected void startNfcBeam(String message) throws NfcNotAvailableException {

        NfcAdapter adapter = getAdapter();
        if(adapter == null) {
            throw new NfcNotAvailableException(getString(R.string.unknown_error));
        } else {
            this.message = message;
            adapter.setNdefPushMessageCallback(this, this);
        }
    }
}
