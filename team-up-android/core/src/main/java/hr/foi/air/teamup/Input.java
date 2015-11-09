package hr.foi.air.teamup;

import android.util.Log;
import android.widget.EditText;

import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * Created by Tomislav Turek on 09.11.15..
 */
public class Input {

    EditText editText;
    Pattern pattern;
    String errorMessage;

    public Input(EditText editText, String pattern, String errorMessage) {
        this.editText = editText;
        this.pattern = Pattern.compile(pattern);
        this.errorMessage = errorMessage;
    }

    public boolean isValid() {
        return this.pattern.matcher(editText.getText().toString()).matches();
    }

    public void setError() {
        editText.setError(this.errorMessage);
    }

    public static boolean validate(List<Input> inputs) {
        boolean valid = true;
        for (Input i: inputs) {
            if(!i.isValid()) {
                Log.w("hr.foi.teamup.debug", "Input -- there are invalid inputs");
                valid = false;
                i.setError();
            }
        }
        Log.i("hr.foi.teamup.debug", "Input -- all fields are valid");
        return valid;
    }

}
