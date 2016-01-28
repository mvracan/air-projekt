package hr.foi.air.teamup.prompts;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import hr.foi.air.teamup.ModularityCallback;

/**
 * dialog that requires user input
 * Created by Tomislav Turek on 09.11.15..
 */
public class InputPrompt extends AlertPrompt {

    EditText editText;

    /**
     * default constructor
     * @param context current application context
     */
    public InputPrompt(Context context) {
        super(context);
    }

    @Override
    public Prompt prepare(int title, DialogInterface.OnClickListener positive, int positiveMessage,
                        DialogInterface.OnClickListener negative, int negativeMessage) {
        super.prepare(title, positive, positiveMessage, negative, negativeMessage);

        editText = new EditText(getContext());
        this.getBuilder().setView(editText);
        return this;

    }

    /**
     * modular prompt approach to preparing
     * @param title dialog title
     * @param callback stomp subscription callback
     * @param positiveMessage positive dialog button message
     * @param negativeMessage negative dialog button message
     * @return this prompt
     */
    public Prompt prepare(int title, final ModularityCallback callback, int positiveMessage, int negativeMessage) {
        this.prepare(title, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onAction(getInput());
            }
        }, positiveMessage, null, negativeMessage);
        return this;
    }

    public String getInput() {
        return this.editText.getText().toString();
    }

}
