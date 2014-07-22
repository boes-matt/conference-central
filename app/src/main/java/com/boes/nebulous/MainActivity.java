package com.boes.nebulous;

import android.accounts.Account;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.boes.nebulous.backend.conference.Conference;
import com.boes.nebulous.backend.conference.model.MyBean;
import com.boes.nebulous.backend.conference.model.Profile;
import com.boes.nebulous.backend.conference.model.ProfileForm;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;

import java.io.IOException;

public class MainActivity extends Activity {

    private static final String ANDROID_AUDIENCE = "server:client_id:" + PrivateConstants.WEB_CLIENT_ID;

    private static final int PLAY_REQUEST = 101;

    private TextView mAccountNameView;
    private TextView mEmailView;
    private TextView mDisplayNameView;
    private TextView mTeeShirtSizeView;

    private GoogleAccountCredential mCredential;
    private Conference mConferenceApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        mCredential = GoogleAccountCredential.usingAudience(this, ANDROID_AUDIENCE);
        mConferenceApi = buildApi(mCredential);
        initAccount();

        sayHi("Matt");
        saveProfile();
    }

    private void initViews() {
        mAccountNameView = (TextView) findViewById(R.id.account_name);
        mEmailView = (TextView) findViewById(R.id.email);
        mDisplayNameView = (TextView) findViewById(R.id.display_name);
        mTeeShirtSizeView = (TextView) findViewById(R.id.tee_shirt_size);
    }

    private Conference buildApi(GoogleAccountCredential credential) {
        Conference.Builder builder =
                new Conference.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), credential);
        builder.setApplicationName("com.boes.nebulous");
        return builder.build();
    }

    private void initAccount() {
        Account[] accounts = mCredential.getAllAccounts();
        mCredential.setSelectedAccountName(accounts[0].name);
        mAccountNameView.setText(accounts[0].name);
    }

    private void sayHi(String name) {
        SayHiTask sayHiTask = new SayHiTask();
        sayHiTask.execute(name);
    }

    private void saveProfile() {
        SaveProfileTask saveProfileTask = new SaveProfileTask();
        saveProfileTask.execute();
    }

    private void setViews(Profile profile) {
        mEmailView.setText(profile.getEmail());
        mDisplayNameView.setText(profile.getDisplayName());
        mTeeShirtSizeView.setText(profile.getTeeShirtSize());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLAY_REQUEST && resultCode == RESULT_OK) {
            sayHi("Matt");
            saveProfile();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class SayHiTask extends AsyncTask<String, Void, MyBean> {

        @Override
        protected MyBean doInBackground(String... params) {
            String name = params[0];
            MyBean bean = null;

            try {
                bean = mConferenceApi.sayHi(name).execute();
            } catch (GooglePlayServicesAvailabilityIOException playEx) {
                resolveGooglePlayServices(playEx.getCause());
            } catch (IOException ioEx) {
                handleIOException("SayHiTask", ioEx);
            }

            return bean;
        }

        @Override
        protected void onPostExecute(MyBean bean) {
            if (bean != null) toastOnUIThread(bean.getData());
        }

    }

    private class SaveProfileTask extends AsyncTask<Void, Void, Profile> {

        @Override
        protected Profile doInBackground(Void... params) {
            Profile profile = null;

            ProfileForm form = new ProfileForm();
            form.setDisplayName("Matthew");
            form.setTeeShirtSize("M");

            try {
                profile = mConferenceApi.saveProfile(form).execute();
            } catch (GooglePlayServicesAvailabilityIOException playEx) {
                resolveGooglePlayServices(playEx.getCause());
            } catch (IOException ioEx) {
                handleIOException("SaveProfileTask", ioEx);
            }

            return profile;
        }

        @Override
        protected void onPostExecute(Profile profile) {
            if (profile != null) setViews(profile);
        }

    }

    private void resolveGooglePlayServices(final GooglePlayServicesAvailabilityException playEx) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(playEx.getConnectionStatusCode(), MainActivity.this, PLAY_REQUEST);
                dialog.show();
            }
        });
    }

    private void handleIOException(String task, IOException ioEx) {
        Log.e(task, ioEx.toString(), ioEx);
        toastOnUIThread("Network request failed");
    }

    private void toastOnUIThread(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
