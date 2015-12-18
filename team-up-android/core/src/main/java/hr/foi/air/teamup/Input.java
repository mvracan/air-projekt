package hr.foi.air.teamup;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import java.util.List;
import java.util.regex.Pattern;

/**
 * main use in validation
 * Created by Tomislav Turek on 09.11.15..
 */
public class Input {

    EditText editText;
    Pattern pattern;
    String errorMessage;

    public static final String PASSWORD_PATTERN = "[a-zA-Z\\d!@#$%&*]{5,45}";
    public static final String TEXT_MAIN_PATTERN = "[A-Za-z]{3,45}";
    public static final String RADIUS_PATTERN="^\\d+(\\.\\d{1,2})?$";

    /**
     * default constructor
     * @param editText layout edit text
     * @param pattern pattern for the edit text
     * @param errorMessage error message to show if error occurs on validation
     */
    public Input(EditText editText, String pattern, String errorMessage) {
        this.editText = editText;
        this.pattern = Pattern.compile(pattern);
        this.errorMessage = errorMessage;
    }

    /**
     * checks if edit text input matches the pattern
     * @return true if match, false otherwise
     */
    public boolean isValid() {
        return this.pattern.matcher(editText.getText().toString()).matches();
    }

    /**
     * sets error to edit text with error message
     */
    public void setError() {
        editText.setError(this.errorMessage);
    }

    public EditText getEditText() {
        return this.editText;
    }

    /**
     * checks if input text matches the input text passed as parameter
     * @param input input to check matching
     * @return true if input texts match, false otherwise
     */
    public boolean equals(Input input) {
        boolean equal = true;
        if(!TextUtils.equals(this.editText.getText().toString(),
                input.getEditText().getText().toString())) {
            input.setError();
            equal = false;
        }
        return equal;
    }

    /**
     * static method that validates all inputs defined in list
     * @param inputs list of inputs to validate
     * @return true if all inputs match their pattern, false otherwise
     */
    public static boolean validate(List<Input> inputs) {
        boolean valid = true;
        for (Input i: inputs) {
            if(!i.isValid()) {
                Logger.log("Input -- there are invalid inputs", Log.WARN);
                valid = false;
                i.setError();
            }
        }
        Logger.log("Input -- all fields are valid");
        return valid;
    }

}
