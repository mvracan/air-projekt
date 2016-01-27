package hr.foi.air.teamup.prompts;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * simple implementation that shows the progress dialog
 * Created by Tomislav Turek on 09.11.15..
 */
public class LoadingPrompt extends AlertPrompt {

    ProgressDialog progressDialog;

    public LoadingPrompt(Context context) {
        super(context);
    }

    /**
     * shows the progress dialog
     */
    @Override
    public void showPrompt() {
        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = ProgressDialog.show(getContext(), null,
                    getContext().getString(R.string.please_wait), true, false);
        }
    }

    /**
     * hides the progress dialog
     */
    public void hidePrompt() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
