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

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        startAnimation();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * login user
     */
    // TODO: add response checks, login value checks and logs
    private void login() {
        Log.i("hr.foi.teamup.debug", "Initiated login");
        String usernameValue = username.getText().toString();
        String passwordValue = password.getText().toString();
        // ako usernameValue ili passwordValue ne valja treba staviti
        // username.setError("TEXT DA NE VALJA USERNAME");
        Credentials credentials = new Credentials(usernameValue, passwordValue);
        ServiceResponseHandler handler = new ServiceResponseHandler() {
            @Override
            public boolean handleResponse(ServiceResponse response) {
                //if(response.getHttpCode() == 200) {
                    Intent intent = new Intent(getApplicationContext(), GroupListActivity.class);
                    startActivity(intent);
                    return true;
                //} else {
                //    return false;
                //}
            }
        };
        ServiceParams params = new ServiceParams("", "POST", credentials, handler);
        new ServiceAsyncTask().execute(params);
    }

    /**
     * starts login animations
     */
    private  void startAnimation() {
        Log.i("hr.foi.teamup.debug", "Animation started");
        Animation moveAnimation= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_logo);
        Animation fadeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_login_form);

        logo.startAnimation(moveAnimation);
        username.startAnimation(fadeAnimation);
        password.startAnimation(fadeAnimation);
        usernameLayout.startAnimation(fadeAnimation);
        passwordLayout.startAnimation(fadeAnimation);
        signIn.startAnimation(fadeAnimation);
        register.startAnimation(fadeAnimation);
    }
}
