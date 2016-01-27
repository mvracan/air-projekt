package hr.foi.air.teamup.prompts;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

/**
 *
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

    public String getInput() {
        return this.editText.getText().toString();
    }

}
