package hr.foi.teamup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import hr.foi.teamup.model.Credentials;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.webservice.ServiceAsyncTask;
import hr.foi.teamup.webservice.ServiceParams;
import hr.foi.teamup.webservice.ServiceResponse;
import hr.foi.teamup.webservice.ServiceResponseHandler;

public class RegistrationActivity extends AppCompatActivity {

    Button submit;
    EditText firstName;
    EditText lastName;
    EditText username;
    EditText password;
    EditText confirmPassword;

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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * Checks registration form input values
     * @param firstNameValue name value
     * @param lastNameValue surname value
     * @param usernameValue username value
     * @param passwordValue password value
     * @param confirmPasswordValue confirmed password value
     * @return false if there is an error else return true
     */
    private boolean checkValues(String firstNameValue, String lastNameValue, String usernameValue,
                                String passwordValue, String confirmPasswordValue){
        if(firstNameValue.length() < 3 || firstNameValue.length() > 45){
            Log.w("hr.foi.teamup.debug","RegistrationActivity -- first name too short or too long");
            firstName.setError("First name has to be 3-45 characters long");
            return false;
        }
        else if (lastNameValue.length() < 3 || lastNameValue.length() > 45){
            Log.w("hr.foi.teamup.debug","RegistrationActivity -- last name too short or too long");
            lastName.setError("Last name has to be 3-45 characters long");
            return false;
        }
        else if(usernameValue.length() < 5 || usernameValue.length() > 45){
            Log.w("hr.foi.teamup.debug","RegistrationActivity -- username too short or too long");
            username.setError("Username has to be 5-45 characters long");
            return false;
        }
        else if(passwordValue.length() < 5){
            Log.w("hr.foi.teamup.debug","RegistrationActivity -- password too short");
            password.setError("Password too short (min 5)");
            return false;
        }
        else if(!confirmPasswordValue.equals(passwordValue)){
            Log.w("hr.foi.teamup.debug","RegistrationActivity -- passwords do not match");
            confirmPassword.setError("Passwords do not match");
            return false;
        }
        Log.i("hr.foi.teamup.debug","RegistrationActivity -- all fields are valid");
        return true;
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

            if(checkValues(firstNameValue,lastNameValue,usernameValue,passwordValue,confirmPasswordValue)){
                Log.i("hr.foi.teamup.debug", "RegistrationActivity -- creating new user and sending info to service");
                Credentials credentials = new Credentials(usernameValue,passwordValue);
                Person person = new Person(0,firstNameValue,lastNameValue,credentials);
                // TODO: change url
                new ServiceAsyncTask().execute(new ServiceParams("url", "POST", person, registrationHandler));
            }
        }
    };

    // handler that is called when user registration is finished
    ServiceResponseHandler registrationHandler = new ServiceResponseHandler() {
        @Override
        public boolean handleResponse(ServiceResponse response) {
            // TODO: if everything went fine, initiate GroupListActivity through Intent (checks with logs)
            Intent intent = new Intent(getApplicationContext(),GroupListActivity.class);
            startActivity(intent);
            return false;
        }
    };
}
