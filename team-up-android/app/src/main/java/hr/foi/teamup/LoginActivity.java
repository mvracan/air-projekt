package hr.foi.teamup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import hr.foi.air.teamup.SessionManager;
import hr.foi.teamup.model.Credentials;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.webservice.ServiceAsyncTask;
import hr.foi.teamup.webservice.ServiceParams;
import hr.foi.teamup.webservice.ServiceResponse;
import hr.foi.teamup.webservice.ServiceResponseHandler;

public class LoginActivity extends Activity {

    ImageView logo;
    EditText username;
    EditText password;
    TextInputLayout usernameLayout;
    TextInputLayout passwordLayout;
    Button signIn;
    TextView register;
    Toast errorWrongCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logo=(ImageView)findViewById(R.id.imgvLogo);
        username=(EditText)findViewById(R.id.txtUsername);
        password=(EditText)findViewById(R.id.txtPassword);
        signIn=(Button)findViewById(R.id.btnSignIn);
        register=(TextView)findViewById(R.id.txtRegister);
        usernameLayout=(TextInputLayout)findViewById(R.id.txtUsernameLayout);
        passwordLayout=(TextInputLayout)findViewById(R.id.txtPasswordLayout);
        signIn.setOnClickListener(onSignIn);
        register.setOnClickListener(onRegister);

        if(savedInstanceState == null) {
            startAnimation();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * checks if username and passwords are valid
     * @param usernameText text from username edittext
     * @param passwordText text from password edittext
     * @return true if valid, false otherwise
     */
    private boolean checkInputs(String usernameText, String passwordText) {
        Log.i("hr.foi.teamup.debug", "LoginActivity -- initiating username and password input check");
        if(usernameText.length() <5 ){
            Log.w("hr.foi.teamup.debug","LoginActivity -- username too short");
            username.setError("Username too short (min 5 characters)");
            return false;
        }
        else if(passwordText.length() < 5){
            Log.w("hr.foi.teamup.debug","LoginActivity -- password too short");
            password.setError("Password too short (min 5 characters)");
            return false;
        }
        Log.i("hr.foi.teamup.debug","LoginActivity -- username and password are valid");
        return true;
    }

    /**
     * starts login animations
     */
    private void startAnimation() {
        Log.i("hr.foi.teamup.debug", "LoginActivity -- login animation started");
        Animation moveAnimation= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_logo);
        Animation fadeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_login_form);

        logo.startAnimation(moveAnimation);
        username.startAnimation(fadeAnimation);
        password.startAnimation(fadeAnimation);
        usernameLayout.startAnimation(fadeAnimation);
        passwordLayout.startAnimation(fadeAnimation);
        signIn.startAnimation(fadeAnimation);
        register.startAnimation(fadeAnimation);
        Log.i("hr.foi.teamup.debug", "LoginActivity -- login animation ended");
    }

    // listener that is called when sign in button is clicked
    View.OnClickListener onSignIn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("hr.foi.teamup.debug", "LoginActivity -- initiated login");
            String usernameValue = username.getText().toString();
            String passwordValue = password.getText().toString();
            if(checkInputs(usernameValue,passwordValue)) {
                Credentials credentials = new Credentials(usernameValue, passwordValue);
                Log.i("hr.foi.teamup.debug", "LoginActivity -- sending credentials to service");
                ServiceParams params = new ServiceParams("", "POST", credentials, loginHandler);
                new ServiceAsyncTask().execute(params);
            }
        }
    };

    // handler that is called when user login is finished
    ServiceResponseHandler loginHandler = new ServiceResponseHandler() {
        @Override
        public boolean handleResponse(ServiceResponse response) {

            if(response.getHttpCode() == 200) {

                Person person = new Gson().fromJson(response.getJsonResponse(), Person.class);
                SessionManager manager = SessionManager.getInstance(getApplicationContext());
                if(manager.createSession(person, "person")) {

                    // TODO: test if session conversion works
                    Person sessionPerson = (Person)manager.retrieveSession("person");
                    Log.i("hr.foi.teamup.debug",
                            "LoginActivity -- valid user, created session: " + sessionPerson.toString()
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
                errorWrongCredentials = Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_LONG);
                errorWrongCredentials.show();
                return false;

            }
        }
    };

    // called when register is clicked
    View.OnClickListener onRegister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // save service parameters for login to call later from registration activity
            ServiceParams params = new ServiceParams("", "POST", null, loginHandler);
            Bundle bundle = new Bundle();
            bundle.putSerializable("params", params);
            Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
            intent.putExtras(bundle);
            // start activity
            startActivity(intent);
        }
    };
}
