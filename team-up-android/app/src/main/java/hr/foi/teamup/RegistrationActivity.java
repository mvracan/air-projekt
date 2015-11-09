package hr.foi.teamup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import hr.foi.air.teamup.Input;
import hr.foi.teamup.handlers.RegistrationHandler;
import hr.foi.teamup.model.Credentials;
import hr.foi.teamup.model.Location;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.webservice.ServiceAsyncTask;
import hr.foi.teamup.webservice.ServiceParams;

public class RegistrationActivity extends AppCompatActivity implements Serializable {

    Button submit;
    EditText firstName;
    EditText lastName;
    EditText username;
    EditText password;
    EditText confirmPassword;
    Credentials credentials;
    List<Input> inputs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firstName = (EditText) findViewById(R.id.firstNameInput);
        lastName = (EditText) findViewById(R.id.lastNameInput);
        username = (EditText) findViewById(R.id.usernameInput);
        password = (EditText) findViewById(R.id.passwordInput);
        confirmPassword = (EditText) findViewById(R.id.confirmPasswordInput);
        submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(onSubmit);
        inputs = Arrays.asList(
                new Input(firstName, "[A-Za-z]{3,45}", "First name can only contain letters (min 3, max 45)"),
                new Input(lastName, "[A-Za-z]{3,45}", "Last name can only contain letters (min 3, max 45)"),
                new Input(username, "[a-zA-Z]{5,45}", "Username can only contain letters (min 5, max 45"),
                new Input(password, "[a-zA-Z]{5,45}", "Password too long or too short (min 5, max 45)")
        );

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * Checks registration form input values
     * @param passwordValue password value
     * @param confirmPasswordValue confirmed password value
     * @return false if there is an error else return true
     */
    private boolean checkValues(String passwordValue, String confirmPasswordValue){
        if(!confirmPasswordValue.equals(passwordValue)){
            Log.w("hr.foi.teamup.debug","RegistrationActivity -- passwords do not match");
            confirmPassword.setError("Passwords do not match");
            return false;
        }
        Log.i("hr.foi.teamup.debug","RegistrationActivity -- all fields are valid");
        return Input.validate(inputs);
    }

    // listener that triggers when submit is clicked
    View.OnClickListener onSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("hr.foi.teamup.debug", "RegistrationActivity -- initiated user registration");

            String firstNameValue = firstName.getText().toString();
            String lastNameValue = lastName.getText().toString();
            String usernameValue = username.getText().toString();
            String passwordValue = password.getText().toString();
            String confirmPasswordValue = confirmPassword.getText().toString();

            if(checkValues(passwordValue,confirmPasswordValue)){
                Log.i("hr.foi.teamup.debug", "RegistrationActivity -- creating new user and sending info to service");
                credentials = new Credentials(usernameValue,passwordValue);
                Person person = new Person(0,firstNameValue,lastNameValue,credentials, new Location(0, 0));
                RegistrationHandler registrationHandler = new RegistrationHandler(getApplicationContext(), credentials);
                new ServiceAsyncTask().execute(new ServiceParams("/person/signup", "POST", person, registrationHandler));
            }
        }
    };
}
