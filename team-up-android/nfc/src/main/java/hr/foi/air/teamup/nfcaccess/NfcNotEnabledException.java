package hr.foi.air.teamup.nfcaccess;

/**
 * thrown when nfc is not turned on
 * Created by paz on 08.12.15..
 */
public class NfcNotEnabledException extends Exception {

    public NfcNotEnabledException() {
        super();
    }

    public NfcNotEnabledException(String detailMessage) {
        super(detailMessage);
    }

    public NfcNotEnabledException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NfcNotEnabledException(Throwable throwable) {
        super(throwable);
    }

}
