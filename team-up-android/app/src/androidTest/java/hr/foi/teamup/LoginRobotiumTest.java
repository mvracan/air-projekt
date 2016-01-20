package hr.foi.teamup;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by maja on 20.01.16..
 */
public class LoginRobotiumTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;

    public LoginRobotiumTest() {
        super(LoginActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testLogin(){
        solo.waitForActivity(LoginActivity.class,1000);
        solo.clickOnText("Register");
        solo.goBack();
        solo.clickOnButton("Sign in");
        solo.waitForActivity(TeamActivity.class, 1000);
        solo.clickOnImageButton(0);
        solo.clickOnButton("Log out");
        solo.waitForDialogToOpen();
        solo.clickOnButton("Log out");
        solo.waitForActivity(LoginActivity.class,1000);
    }
}
