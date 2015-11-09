package hr.foi.teamup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;

import hr.foi.air.teamup.Input;
import hr.foi.air.teamup.SessionManager;
import hr.foi.teamup.handlers.UpdateHandler;
import hr.foi.teamup.model.Credentials;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.webservice.ServiceAsyncTask;
import hr.foi.teamup.webservice.ServiceParams;

public class UserProfileActivity extends AppCompatActivity {

    Button change;
    EditText firstName;
    EditText lastName;
    EditText username;
    EditText password;
    EditText confirmPassword;
    Person user;
    List<Input> inputs;

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
        inputs = Arrays.asList(
                new Input(firstName, "[A-Za-z]{3,45}", "First name can only contain letters (min 3, max 45)"),
                new Input(lastName, "[A-Za-z]{3,45}", "Last name can only contain letters (min 3, max 45)"),
                new Input(password, "[a-zA-Z]{5,45}", "Password too long or too short (min 5, max 45)")
        );

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    // invoked when submit button is clicked
    View.OnClickListener onChange = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("hr.foi.teamup.debug", "UserProfileActivity -- initiated user update");

            String firstNameValue = firstName.getText().toString();
            String lastNameValue = lastName.getText().toString();
            String passwordValue = password.getText().toString();
            String confirmPasswordValue = confirmPassword.getText().toString();

            if (checkValues(passwordValue, confirmPasswordValue)) {

                Log.i("hr.foi.teamup.debug", "UserProfileActivity -- fetching user from session");

                Log.i("hr.foi.teamup.debug", "UserProfileActivity --  user fetched from session " + user.toString());
                user.setName(firstNameValue);
                user.setSurname(lastNameValue);
                Credentials changedPassword = new Credentials(user.getCredentials().getUsername(), passwordValue);
                user.setCredentials(changedPassword);

                Log.i("hr.foi.teamup.debug", "UserProfileActivity --  calling web service ");

                UpdateHandler updateHandler = new UpdateHandler(getApplicationContext(), user);
                new ServiceAsyncTask().execute(new ServiceParams("/person/" + user.getidPerson(),
                        "PUT", user, updateHandler));
            }
        }


    };

    /**
     * used to check values of user input
     * @param passwordValue user password
     * @param confirmPasswordValue user confirm password
     * @return true if input valid, false otherwise
     */
    private boolean checkValues(String passwordValue, String confirmPasswordValue){
        if(!confirmPasswordValue.equals(passwordValue)){
            Log.w("hr.foi.teamup.debug","UserProfileActivity -- passwords do not match");
            confirmPassword.setError("Passwords do not match");
            return false;
        }
        return Input.validate(inputs);
    }

}
