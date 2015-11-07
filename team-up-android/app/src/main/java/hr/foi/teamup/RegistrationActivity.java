package hr.foi.teamup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;

import hr.foi.air.teamup.SessionManager;
import hr.foi.teamup.model.Credentials;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.webservice.ServiceAsyncTask;
import hr.foi.teamup.webservice.ServiceParams;
import hr.foi.teamup.webservice.ServiceResponse;
import hr.foi.teamup.webservice.ServiceResponseHandler;

public class RegistrationActivity extends AppCompatActivity implements Serializable {

    Button submit;
    EditText firstName;
    EditText lastName;
    EditText username;
    EditText password;
    EditText confirmPassword;
    Credentials credentials;

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
                credentials = new Credentials(usernameValue,passwordValue);
                Person person = new Person(0,firstNameValue,lastNameValue,credentials);
                new ServiceAsyncTask().execute(new ServiceParams("/person/signup", "POST", person, registrationHandler));
            }
        }
    };

    // handler that is called when user registration is finished
    ServiceResponseHandler registrationHandler = new ServiceResponseHandler() {
        @Override
        public boolean handleResponse(ServiceResponse response) {
            if(response.getHttpCode() == 200) {
                Log.i("hr.foi.teamup.debug", "RegistrationActivity -- successfully registered user, logging in now...");
                // login
                ServiceParams params = new ServiceParams("/person/login","POST",credentials,loginHandler);
                new ServiceAsyncTask().execute(params);
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

    // handler that is called when user login is finished
    ServiceResponseHandler loginHandler = new ServiceResponseHandler() {
        @Override
        public boolean handleResponse(ServiceResponse response) {
            Log.i("hr.foi.teamup.debug", "Got response: " + response.toString());
            if(response.getHttpCode() == 200) {

                Person person = new Gson().fromJson(response.getJsonResponse(), Person.class);
                SessionManager manager = SessionManager.getInstance(getApplicationContext());
                if(manager.createSession(person, "person")) {

                    Person sessionPerson = manager.retrieveSession("person", Person.class);
                    Log.i("hr.foi.teamup.debug",
                            "RegistrationActivity -- valid user, created session: " + sessionPerson.toString()
                                    + ", proceeding to group activity");
                    Intent intent = new Intent(getApplicationContext(), TeamActivity.class);
                    startActivity(intent);
                    return true;

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Internal application error, please try again", Toast.LENGTH_LONG).show();
                    return false;
                }

            } else  {

                Log.i("hr.foi.teamup.debug", "LoginActivity -- invalid credentials sent");
                Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_LONG).show();
                return false;

            }
        }
    };
}
