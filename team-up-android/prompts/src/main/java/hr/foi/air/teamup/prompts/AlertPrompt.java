package hr.foi.air.teamup.prompts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 *
 * Created by Tomislav Turek on 09.11.15..
 */
public class AlertPrompt implements Prompt {

    Context context;
    AlertDialog.Builder builder;

    /**
     * default constructor
     * @param context current application context
     */
    public AlertPrompt(Context context) {
        this.context = context;
        this.builder = new AlertDialog.Builder(context);
    }

    /**
     * prepares the dialog
     * @param title dialog title
     * @param positive positive button action
     * @param positiveMessage positive button text id
     * @param negative negative button action
     * @param negativeMessage negative button text id
     */
    public void prepare(int title, DialogInterface.OnClickListener positive, int positiveMessage,
                        DialogInterface.OnClickListener negative, int negativeMessage) {
        this.builder.setMessage(title)
                .setPositiveButton(positiveMessage, positive)
                .setNegativeButton(negativeMessage, negative);
    }

    @Override
    public void showPrompt() {
        this.builder.create().show();
    }
}
