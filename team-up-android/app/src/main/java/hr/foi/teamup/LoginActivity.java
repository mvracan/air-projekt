package hr.foi.teamup;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends Activity {

    ImageView logo;
    TextInputLayout username;
    TextInputLayout password;
    Button signIn;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logo=(ImageView)findViewById(R.id.imgvLogo);
        username=(TextInputLayout)findViewById(R.id.txtUsernameLayout);
        password=(TextInputLayout)findViewById(R.id.txtPasswordLayout);
        signIn=(Button)findViewById(R.id.btnSignIn);
        register=(TextView)findViewById(R.id.txtRegister);
        startAnimation();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private  void startAnimation() {
        Log.i("hr.foi.teamup.debug", "Animation started");
        Animation moveAnimation= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_logo);
        Animation fadeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_login_form);
        logo.startAnimation(moveAnimation);
        username.startAnimation(fadeAnimation);
        password.startAnimation(fadeAnimation);
        signIn.startAnimation(fadeAnimation);
        register.startAnimation(fadeAnimation);
    }
}
