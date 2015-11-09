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

import java.util.Arrays;
import java.util.List;

import hr.foi.air.teamup.Input;
import hr.foi.teamup.handlers.LoginHandler;
import hr.foi.teamup.model.Credentials;
import hr.foi.teamup.webservice.ServiceAsyncTask;
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
        // for validation
        inputs = Arrays.asList(
                new Input(username, "[a-zA-Z]{5,45}", "Username can only contain letters (min 5, max 45)"),
                new Input(password, "[a-zA-Z]{5,45}", "Password too short or too long (min 5, max 45)")
        );

        if(savedInstanceState == null) {
            startAnimation();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
            if(Input.validate(inputs)) {
                Credentials credentials = new Credentials(usernameValue, passwordValue);
                Log.i("hr.foi.teamup.debug", "LoginActivity -- sending credentials to service");
                LoginHandler loginHandler = new LoginHandler(getApplicationContext());
                ServiceParams params = new ServiceParams("/person/login", "POST", credentials, loginHandler);
                new ServiceAsyncTask().execute(params);
            }
        }
    };

    // called when register is clicked
    View.OnClickListener onRegister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivity(intent);
        }
    };
}
