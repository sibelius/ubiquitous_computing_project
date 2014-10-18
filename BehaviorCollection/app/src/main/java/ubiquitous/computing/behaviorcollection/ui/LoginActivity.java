package ubiquitous.computing.behaviorcollection.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import ubiquitous.computing.behaviorcollection.R;
import ubiquitous.computing.behaviorcollection.Util;


public class LoginActivity extends Activity {

    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPrefs = getSharedPreferences(Util.BEHAVIOR_COLLECTION_PREFS, 0);

        String username = mPrefs.getString(Util.TAG_USERNAME, "");
        if (!username.isEmpty())
            startBehaviorCollectionActivity();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clickLogin(View v) {
        //TODO save login information
        EditText txtUsername = (EditText) findViewById(R.id.username_edit_text);

        if (!txtUsername.getText().toString().isEmpty()) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString(Util.TAG_USERNAME, txtUsername.getText().toString());
            editor.apply();

            startBehaviorCollectionActivity();
        }
    }

    private void startBehaviorCollectionActivity() {
        Intent intent = new Intent(this, BehaviorCollectionActivity.class);
        startActivity(intent);
        finish();
    }
}
