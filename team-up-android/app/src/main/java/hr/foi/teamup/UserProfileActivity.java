package hr.foi.teamup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class UserProfileActivity extends AppCompatActivity {

    Button change;
    EditText firstName;
    EditText lastName;
    EditText username;
    EditText password;
    EditText confirmPassword;
    Person user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        user= SessionManager.getInstance(getApplicationContext()).retrieveSession("person", Person.class);

        firstName = (EditText) findViewById(R.id.firstNameInput);
        username=(EditText) findViewById(R.id.usernameInput);
        username.setClickable(false);
        lastName = (EditText) findViewById(R.id.lastNameInput);

        password = (EditText) findViewById(R.id.passwordInput);
        confirmPassword = (EditText) findViewById(R.id.confirmPasswordInput);
        change = (Button) findViewById(R.id.submitButton);

        firstName.setText(user.getName());
        lastName.setText(user.getSurname());
        username.setText(user.getCredentials().getUsername());
        password.setText(user.getCredentials().getPassword());

        change.setOnClickListener(onChange);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    View.OnClickListener onChange = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("hr.foi.teamup.debug", "UserProfileActivity -- initiated user update");

            String firstNameValue = firstName.getText().toString();
            String lastNameValue = lastName.getText().toString();
            String passwordValue = password.getText().toString();
            String confirmPasswordValue = confirmPassword.getText().toString();

            if (checkValues(firstNameValue, lastNameValue, passwordValue, confirmPasswordValue)) {

                Log.i("hr.foi.teamup.debug", "UserProfileActivity -- fetching user from session");

                Log.i("hr.foi.teamup.debug", "UserProfileActivity --  user fetched from session " + user.toString());
                user.setName(firstNameValue);
                user.setSurname(lastNameValue);
                Credentials changedPassword = new Credentials(user.getCredentials().getUsername(), passwordValue);
                user.setCredentials(changedPassword);

                Log.i("hr.foi.teamup.debug", "UserProfileActivity --  calling web service ");

                new ServiceAsyncTask().execute(new ServiceParams("/person/" + user.getidPerson(),
                        "PUT", user, updateHandler));
            }
        }


    };

    private boolean checkValues(String firstNameValue, String lastNameValue,
                                String passwordValue, String confirmPasswordValue){
        if(firstNameValue.length() < 3 || firstNameValue.length() > 45){
            Log.w("hr.foi.teamup.debug","UserProfileActivity -- first name too short or too long");
            firstName.setError("First name has to be 3-45 characters long");
            return false;
        }
        else if (lastNameValue.length() < 3 || lastNameValue.length() > 45){
            Log.w("hr.foi.teamup.debug","UserProfileActivity -- last name too short or too long");
            lastName.setError("Last name has to be 3-45 characters long");
            return false;
        }

        else if(passwordValue.length() < 5){
            Log.w("hr.foi.teamup.debug","UserProfileActivity-- password too short");
            password.setError("Password too short (min 5)");
            return false;
        }
        else if(!confirmPasswordValue.equals(passwordValue)){
            Log.w("hr.foi.teamup.debug","UserProfileActivity -- passwords do not match");
            confirmPassword.setError("Passwords do not match");
            return false;
        }
        Log.i("hr.foi.teamup.debug", "UserProfileActivity -- all fields are valid");
        return true;
    }

    ServiceResponseHandler updateHandler = new ServiceResponseHandler() {
        @Override
        public boolean handleResponse(ServiceResponse response) {
            if(response.getHttpCode() == 200) {
                Log.i("hr.foi.teamup.debug", "UserProfileActivity -- successfully updated user");
                SessionManager manager= SessionManager.getInstance(getApplicationContext());
                manager.destroySession("person");
                manager.createSession(user, "person");
                Toast.makeText(getApplicationContext(),
                        "Update successful", Toast.LENGTH_LONG).show();
                finish();
                return true;
            } else {
                Log.w("hr.foi.teamup.debug",
                        "UserProfileActivity -- update failed, server returned code " + response.getHttpCode());
                // show fail
                Toast.makeText(getApplicationContext(),
                        "Update failed, please try again ("+response.getHttpCode()+")",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }
    };

}
