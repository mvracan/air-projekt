package hr.foi.teamup;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.EditText;

import hr.foi.air.teamup.Input;

/**
 *
 * Created by Tomislav Turek on 20.01.16..
 */
public class InputTest extends ActivityInstrumentationTestCase2<TeamActivity> {

    TeamActivity activity;

    public InputTest() {
        super(TeamActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }

    @Override
    protected void tearDown() throws Exception {
        activity.finish();
        super.tearDown();
    }

    @SmallTest
    public void testValid() {
        EditText editText = new EditText(activity);
        editText.setText(R.string.test_asdf123);
        Input i = new Input(editText, Input.PASSWORD_PATTERN, null);

        assertEquals(true, i.isValid());
    }

    @LargeTest
    public void testAll() {
        EditText[] e = new EditText[] {
                new EditText(activity),
                new EditText(activity),
                new EditText(activity)
        };

        e[0].setText(R.string.test_as);
        e[1].setText(R.string.test_asdf);
        e[2].setText(R.string.test_lalalalalala);

        Input[] inputs = new Input[] {
                new Input(e[0], Input.TEXT_MAIN_PATTERN, null),
                new Input(e[1], Input.TEXT_MAIN_PATTERN, null),
                new Input(e[2], Input.TEXT_MAIN_PATTERN, null)
        };

        assertEquals(inputs[0].isValid(), false);
        assertEquals(inputs[1].isValid(), true);
        assertEquals(inputs[2].isValid(), true);
    }

}
