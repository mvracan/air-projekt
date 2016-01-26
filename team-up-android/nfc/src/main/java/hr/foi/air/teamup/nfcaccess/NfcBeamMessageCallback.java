package hr.foi.air.teamup.nfcaccess;

/**
 * nfc callback interface
 * Created by Tomislav Turek on 07.12.15..
 */
public interface NfcBeamMessageCallback {

    void onMessageReceived(String message);

}
