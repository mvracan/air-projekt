package hr.foi.teamup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import hr.foi.air.teamup.Input;
import hr.foi.air.teamup.Logger;
import hr.foi.teamup.handlers.RegistrationHandler;
import hr.foi.teamup.model.Credentials;
import hr.foi.teamup.model.Location;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.webservice.ServiceAsyncTask;
import hr.foi.teamup.webservice.ServiceCaller;
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

        // binding
        firstName = (EditText) findViewById(R.id.firstNameInput);
        lastName = (EditText) findViewById(R.id.lastNameInput);
        username = (EditText) findViewById(R.id.usernameInput);
        password = (EditText) findViewById(R.id.passwordInput);
        confirmPassword = (EditText) findViewById(R.id.confirmPasswordInput);
        submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(onSubmit);

        // for validation
        inputs = Arrays.asList(
                new Input(firstName, Input.TEXT_MAIN_PATTERN, getString(R.string.first_name_error)),
                new Input(lastName, Input.TEXT_MAIN_PATTERN, getString(R.string.last_name_error)),
                new Input(username, Input.TEXT_MAIN_PATTERN, getString(R.string.username_error)),
                new Input(password, Input.PASSWORD_PATTERN, getString(R.string.password_error)),
                new Input(confirmPassword, Input.PASSWORD_PATTERN, getString(R.string.confirm_password_error))
        );

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * listener that triggers when submit is clicked
     */
    View.OnClickListener onSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Logger.log("RegistrationActivity -- initiated user registration");

            String firstNameValue = firstName.getText().toString();
            String lastNameValue = lastName.getText().toString();
            String usernameValue = username.getText().toString();
            String passwordValue = password.getText().toString();

            if(Input.validate(inputs)
                    && inputs.get(inputs.size() - 2).equals(inputs.get(inputs.size() - 1))){

                Logger.log("RegistrationActivity -- creating new user and sending info to service");

                credentials = new Credentials(usernameValue,passwordValue);
                Person person = new Person(0,firstNameValue,lastNameValue,credentials, new Location(0, 0));

                RegistrationHandler registrationHandler = new RegistrationHandler(RegistrationActivity.this, credentials);

                new ServiceAsyncTask(registrationHandler).execute(new ServiceParams(
                        getString(hr.foi.teamup.webservice.R.string.person_signup_path),
                        ServiceCaller.HTTP_POST, person));
            }
        }
    };
}
