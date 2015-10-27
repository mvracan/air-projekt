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

import hr.foi.teamup.model.Credentials;
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

        startAnimation();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * checks if username and passwords are valid
     * @param username text from username edittext
     * @param password text from password edittext
     * @return true if valid, false otherwise
     */
    private boolean checkInputs(String username, String password) {
        // TODO: login value checks with logs
        Log.i("hr.foi.teamup.debug", "LoginActivity -- initiating username and password input check");
        return false;
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
            // TODO: checkInputs(usernameValue, passwordValue);
            // ako usernameValue ili passwordValue ne valja treba staviti
            // username.setError("TEXT DA NE VALJA USERNAME");
            Credentials credentials = new Credentials(usernameValue, passwordValue);
            Log.i("hr.foi.teamup.debug", "LoginActivity -- sending credentials to service");
            ServiceParams params = new ServiceParams("", "POST", credentials, loginHandler);
            new ServiceAsyncTask().execute(params);
        }
    };

    // handler that is called when user login is finished
    ServiceResponseHandler loginHandler = new ServiceResponseHandler() {
        @Override
        public boolean handleResponse(ServiceResponse response) {
            // TODO: add response checks with logs
            //if(response.getHttpCode() == 200) {
            Intent intent = new Intent(getApplicationContext(), GroupListActivity.class);
            startActivity(intent);
            return true;
            //} else {
            //    return false;
            //}
        }
    };
}
