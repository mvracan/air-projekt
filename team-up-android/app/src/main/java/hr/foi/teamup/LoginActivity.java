package hr.foi.teamup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import hr.foi.air.teamup.Input;
import hr.foi.air.teamup.Logger;
import hr.foi.air.teamup.SessionManager;
import hr.foi.air.teamup.prompts.LoadingPrompt;
import hr.foi.teamup.handlers.LoginHandler;
import hr.foi.teamup.model.Credentials;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.webservice.ServiceAsyncTask;
import hr.foi.teamup.webservice.ServiceCaller;
import hr.foi.teamup.webservice.ServiceParams;

public class LoginActivity extends Activity {

    ImageView logo;
    EditText username;
    EditText password;
    TextInputLayout usernameLayout;
    TextInputLayout passwordLayout;
    Button signIn;
    TextView register;
    List<Input> inputs;
    LoadingPrompt loadingPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // binding
        logo=(ImageView)findViewById(R.id.imgvLogo);
        username=(EditText)findViewById(R.id.txtUsername);
        password=(EditText)findViewById(R.id.txtPassword);
        signIn=(Button)findViewById(R.id.btnSignIn);
        register=(TextView)findViewById(R.id.txtRegister);
        usernameLayout=(TextInputLayout)findViewById(R.id.txtUsernameLayout);
        passwordLayout=(TextInputLayout)findViewById(R.id.txtPasswordLayout);
        signIn.setOnClickListener(onSignIn);
        register.setOnClickListener(onRegister);
        loadingPrompt = new LoadingPrompt(this);

        // for validation
        inputs = Arrays.asList(
                new Input(username, Input.TEXT_MAIN_PATTERN, getString(R.string.username_error)),
                new Input(password, Input.PASSWORD_PATTERN, getString(R.string.password_error))
        );

        // if user is logged in, start main activity
        if(SessionManager.getInstance(this)
                .retrieveSession(SessionManager.PERSON_INFO_KEY, Person.class) != null) {
            startActivity(new Intent(this, TeamActivity.class));
        }

        // animation only on first run
        if(savedInstanceState == null) {
            startAnimation();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * starts login animations
     */
    private void startAnimation() {
        Logger.log("Login animation started", getClass().getName());
        Animation moveAnimation= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_logo);
        Animation fadeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_login_form);

        logo.startAnimation(moveAnimation);
        username.startAnimation(fadeAnimation);
        password.startAnimation(fadeAnimation);
        usernameLayout.startAnimation(fadeAnimation);
        passwordLayout.startAnimation(fadeAnimation);
        signIn.startAnimation(fadeAnimation);
        register.startAnimation(fadeAnimation);
        Logger.log("Login animation ended", getClass().getName());
    }

    /**
     * listener that is called when sign in button is clicked
     */
    View.OnClickListener onSignIn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Logger.log("Initiated login", getClass().getName());

            String usernameValue = username.getText().toString();
            String passwordValue = password.getText().toString();

            if(Input.validate(inputs)) {

                Credentials credentials = new Credentials(usernameValue, passwordValue);
                Logger.log("Sending credentials to service", getClass().getName());


                LoginHandler loginHandler = new LoginHandler(LoginActivity.this);
                ServiceParams params = new ServiceParams(
                        getString(hr.foi.teamup.webservice.R.string.person_login_path),
                        ServiceCaller.HTTP_POST, credentials);
                new ServiceAsyncTask(loginHandler).execute(params);
            }
        }
    };

    /**
     * called when register is clicked
     */
    View.OnClickListener onRegister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
        }
    };
}
