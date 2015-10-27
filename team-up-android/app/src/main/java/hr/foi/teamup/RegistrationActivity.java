package hr.foi.teamup;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import hr.foi.teamup.model.Credentials;
import hr.foi.teamup.model.User;
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
    }

    // listener that triggers when submit is clicked
    View.OnClickListener onSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("hr.foi.teamup.debug", "RegistrationActivity -- initiated user registration");
            // TODO: check if password and confirmPassword match and if user input is valid with logs
            // (username/password length, etc)
            // if everything is ok, load credentials into user and call service (fill url)

            Log.i("hr.foi.teamup.debug", "RegistrationActivity -- creating new user and sending info to service");
            User user = new User(0, firstName.getText().toString(), lastName.getText().toString(),
                    new Credentials(username.getText().toString(), password.getText().toString()));
            // TODO: change url
            new ServiceAsyncTask().execute(new ServiceParams("url", "POST", user, registrationHandler));
        }
    };

    // handler that is called when user registration is finished
    ServiceResponseHandler registrationHandler = new ServiceResponseHandler() {
        @Override
        public boolean handleResponse(ServiceResponse response) {
            // TODO: if everything went fine, initiate GroupListActivity through Intent (checks with logs)
            return false;
        }
    };
}
