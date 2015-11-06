package hr.foi.teamup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hr.foi.air.teamup.SessionManager;
import hr.foi.teamup.model.Credentials;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.webservice.ServiceAsyncTask;
import hr.foi.teamup.webservice.ServiceParams;
import hr.foi.teamup.webservice.ServiceResponse;
import hr.foi.teamup.webservice.ServiceResponseHandler;

/**
 * Created by paz on 06.11.15..
 */
public class UserProfileActivity extends AppCompatActivity {

    Button change;
    EditText firstName;
    EditText lastName;
    EditText password;
    EditText confirmPassword;
    ServiceParams updateParams;
    Person user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firstName = (EditText) findViewById(R.id.firstNameInput);
        lastName = (EditText) findViewById(R.id.lastNameInput);

        password = (EditText) findViewById(R.id.passwordInput);
        confirmPassword = (EditText) findViewById(R.id.confirmPasswordInput);
        change = (Button) findViewById(R.id.submitButton);
        change.setOnClickListener(onChange);



        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    View.OnClickListener onChange = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("hr.foi.teamup.debug", "RegistrationActivity -- initiated user registration");

            String firstNameValue = firstName.getText().toString();
            String lastNameValue = lastName.getText().toString();
            String passwordValue = password.getText().toString();
            String confirmPasswordValue = confirmPassword.getText().toString();

            if(checkValues(firstNameValue,lastNameValue,passwordValue,confirmPasswordValue)){
                Log.i("hr.foi.teamup.debug", "RegistrationActivity -- creating new user and sending info to service");
                user= SessionManager.getInstance(getApplicationContext()).retrieveSession("person");
                user.setName(firstNameValue);
                user.setSurname(lastNameValue);
                Credentials changedPassword=new Credentials(user.getCredentials().getUsername(),passwordValue);
                user.setCredentials(changedPassword);
                updateParams.setObject(user); // credentials to send later
                // TODO: change url
                new ServiceAsyncTask().execute(new ServiceParams(""+user.getidPerson(),
                        "PUT", user, registrationHandler));
            }
        }
    };

    private boolean checkValues(String firstNameValue, String lastNameValue,
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

    ServiceResponseHandler registrationHandler = new ServiceResponseHandler() {
        @Override
        public boolean handleResponse(ServiceResponse response) {
            if(response.getHttpCode() == 200) {
                Log.i("hr.foi.teamup.debug", "RegistrationActivity -- successfully registered user, logging in now...");
                // login

                return true;
            } else {
                Log.w("hr.foi.teamup.debug",
                        "RegistrationActivity -- registration failed, server returned code " + response.getHttpCode());
                // show fail
                Toast.makeText(getApplicationContext(),
                        "Registration failed, please try again (" + response.getHttpCode() + ")",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }
    };





}
