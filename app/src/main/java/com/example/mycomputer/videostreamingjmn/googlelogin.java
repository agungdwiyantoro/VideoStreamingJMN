package com.example.mycomputer.videostreamingjmn; /**
 * Created by My Computer on 7/19/2016.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import org.json.JSONException;
import org.json.JSONObject;


//----ACTIVITY UNTUK LOGIN
//----LOGIN MENGGUNAKAN GOOGLE SIGN-IN

//---! GOOGLE SIGN-IN HANYA BISA DILAKUKAN jika google-services.json telah ter-generate pada project


public class googlelogin extends Activity implements OnClickListener,
        ConnectionCallbacks, OnConnectionFailedListener {

    private static String[] iklanImageURL;
    private static String[] iklanURL;

    Intent mainactivity;
    private  String email;

    private LinearLayout llProfileLayout;
    private static int countRowValue, countIklanValue, countCategorieValue;

    private AsyncTask countRows, countIklan, countCategories, getChannels, getCategories, getIklan;

    //---- "KEY" UNTUK SHAREDPREFERENCE
    public static String channelKey = "channel";
    public static String channelNameKey = "Namechannel";
    public static String channelURLIconKey = "URLICONchannel";
    public static String channelCategoriesKey = "Categorieschannel";
    public static String statusVIP = "statusVIP";

    public static String IklanIcon = "Iklan_Icon";
    public static String IklanLink = "Iklan_Link";

    // Logcat tag
    private static final String TAG = "MainActivity";

    private static final int RC_SIGN_IN = 0;
    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

    // Google client to interact with Google API
    public static GoogleApiClient mGoogleApiClient;

    private ProgressDialog signInGoogle;
    private AlertDialog.Builder pesanKoneksi;

    private boolean mIntentInProgress;

    private boolean mSignInClicked;

    private ConnectionResult mConnectionResult;

    private SignInButton btnSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.googlelogin);

        mainactivity = new Intent(googlelogin.this, MainActivity.class);
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);

        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);

        btnSignIn.setOnClickListener(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();


        pesanKoneksi = new AlertDialog.Builder(googlelogin.this);
        pesanKoneksi.setMessage("KONEKSI TIDAK ADA");
        pesanKoneksi.setPositiveButton("RELOAD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                recreate();
            }
        });

        if(isNetworkAvailable()) {
            countRows = new countRowsx(getApplicationContext()).execute();
            countIklan = new countRowsIklanx(getApplicationContext()).execute();
        }
        else{
            pesanKoneksi.show();
        }

        signInGoogle = new ProgressDialog(this);
        signInGoogle.setMessage("Gathering user's information");

    }



    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Method to resolve any signin errors
     */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);

            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            mConnectionResult = result;

            if (mSignInClicked) {
                resolveSignInError();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;

        // Get user's information
        getProfileInformation();

        // Update the UI after signin

        updateUI(true);


    }

    /**
     * Updating the UI, showing/hiding buttons and profile layout
     */
    public void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            startActivity(mainactivity);
            finish();
        }
    }

    /**
     * Fetching user's information name, email, profile pic
     */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);

                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;


                mainactivity.putExtra("nama", personName);
                mainactivity.putExtra("email", email);
                mainactivity.putExtra("pp", personPhotoUrl);

            } else {
                Toast.makeText(this,
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        updateUI(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * Button on click listener
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                try {
                    if (countIklan.get() != null && countRows.get() != null) {

                        getChannels = new doInBackgroundx(this).execute(countRowValue);
                        getIklan = new doInBackgroundIklanxx(this).execute(countIklanValue);

                    }
                } catch (InterruptedException e) {
                    System.out.println("GOOGLE LOGIN INTERUPTED!");
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    System.out.println("GOOGLE LOGIN EXECUTION FAILED!");
                    e.printStackTrace();
                }

                // Signin button clicked
                signInWithGplus();

                break;
            case R.id.btn_sign_out:
                // Signout button clicked
                signOutFromGplus();
                break;
            case R.id.btn_revoke_access:
                // Revoke access button clicked
                revokeGplusAccess();
                break;
        }
    }

    /**
     * Sign-in into google
     */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    /**
     * Sign-out from google
     */
    public void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            updateUI(false);
        }
    }

    /**
     * Revoking access from google
     */
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                                  updateUI(false);
                        }

                    });
        }
    }


    //----CEK KONEKSI INTERNET
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


    /*---DATA - DATA YANG DI DAPAT MENGGUNAKAN ASYNCTASK DISIMPAN KE SAVEPREFERENCE----*/

    //----INNER CLASS UNTUK AMBIL JUMLAH DATA CHANNEL DARI SERVER
    static class countRowsx extends AsyncTask<Void, Void, String> {

        private Context context;

        countRowsx(Context contextx) {
            context = contextx;
        }


        @Override
        protected String doInBackground(Void... params) {
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://megacleon.com/JMN_Streaming_TV/php/countRows.php");

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);

                InputStream is = httpURLConnection.getInputStream();

                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;

                }
                System.out.println("data count rows" + data);
                is.close();
                httpURLConnection.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }


            return data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            String err = null;
            s = s.trim();

            System.out.println("count rows is " + s);
            countRowValue = Integer.parseInt(s);
            LoginClasses.SaveSharedPreference.setCountRows(context, countRowValue);

        }
    }


    //----INNNER CLASS UNTUK AMBIL JUMLAH DATA IKLAN DARI SERVER
    static class countRowsIklanx extends AsyncTask<Void, Void, String> {

        private Context context;

        public countRowsIklanx(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://megacleon.com/JMN_Streaming_TV/php/countRowsIklan.php");


                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);


                InputStream is = httpURLConnection.getInputStream();

                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;

                }


                System.out.println("data count rows" + data);

                is.close();
                httpURLConnection.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }


            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            String err = null;
            s = s.trim();
            countIklanValue = Integer.parseInt(s);
            LoginClasses.SaveSharedPreference.setCountRowsIklan(context, countIklanValue);
            System.out.println("count rows Iklan is " + s);


        }

    }


    //----INNER CLASS UNTUK AMBIL SEMUA DATA CHANNEL DARI SERVER
    public class doInBackgroundx extends AsyncTask<Integer, Void, String> {

        private int count;
        private Context context;


        private String[] names;
        private String[] channels;
        private String[] icon_url;
        private String[] categoryx;
        private String[] status;


        public doInBackgroundx(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Integer... params) {

            String data = "";
            int x = 0;
            int tmp;
            count = params[0];


            names = new String[count];
            channels = new String[count];
            icon_url = new String[count];
            categoryx = new String[count];
            status = new String[count];

            while (x < count) {
                try {
                    URL url = new URL("http://megacleon.com/JMN_Streaming_TV/php/ambil_link_channel.php");
                    String urlParams = "id_channel=" + (x + 1);

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    OutputStream os = httpURLConnection.getOutputStream();
                    os.write(urlParams.getBytes());
                    os.flush();
                    os.close();


                    InputStream is = httpURLConnection.getInputStream();

                    while ((tmp = is.read()) != -1) {
                        data += (char) tmp;

                    }


                    data = data + "pisah";
                    is.close();
                    httpURLConnection.disconnect();

                    x++;


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return "Exception: " + e.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Exception: " + e.getMessage();
                }

            }
            return data;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            String err = null;
            JSONObject jsonRootObject;
            JSONObject jsonArray;


            System.out.println("S is " + s);
            String[] parts = s.split("pisah");

            try {

                for (int j = 0; j < count; j++) {
                    jsonRootObject = new JSONObject(parts[j]);
                    jsonArray = jsonRootObject.getJSONObject("user_data");
                    channels[j] = jsonArray.getString("link");
                    names[j] = jsonArray.getString("Name");
                    icon_url[j] = jsonArray.getString("icon_url");
                    categoryx[j] = jsonArray.getString("Categories");
                    status[j] = jsonArray.getString("Status");



                    LoginClasses.SaveSharedPreference.setChannel(context, channels[j], channelKey + j);

                    LoginClasses.SaveSharedPreference.setChannelName(context, names[j], channelNameKey + j);

                    LoginClasses.SaveSharedPreference.setIconURL(context, icon_url[j], channelURLIconKey + j);

                    LoginClasses.SaveSharedPreference.setCategories(context, categoryx[j], channelCategoriesKey + j);

                    LoginClasses.SaveSharedPreference.setStatusVIP(context, status[j], statusVIP + j);
                }


            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception " + e.getMessage();
            }


        }
    }


    //----INNER CLASS UNTUK AMBIL DATA IKLAN DARI CHANNEL
    public class doInBackgroundIklanxx extends AsyncTask<Integer, Void, String> {


        private Context context;

        private int count;


        public doInBackgroundIklanxx(Context context) {
            this.context = context;
        }


        @Override
        protected String doInBackground(Integer... params) {
            String data = "";
            int x = 0;
            int tmp;
            count = params[0];

            iklanImageURL = new String[count];
            iklanURL = new String[count];

            while (x < count) {
                try {
                    URL url = new URL("http://megacleon.com/JMN_Streaming_TV/php/ambil_link_iklan.php");
                    String urlParams = "id_channel=" + (x + 1);

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    OutputStream os = httpURLConnection.getOutputStream();
                    os.write(urlParams.getBytes());
                    os.flush();
                    os.close();


                    InputStream is = httpURLConnection.getInputStream();

                    while ((tmp = is.read()) != -1) {
                        data += (char) tmp;

                    }


                    data = data + "pisah";
                    is.close();
                    httpURLConnection.disconnect();

                    x++;


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return "Exception: " + e.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Exception: " + e.getMessage();
                }

            }
            return data;
        }


        @Override
        protected void onPostExecute(String s) {
            String err = null;
            JSONObject jsonRootObject;
            JSONObject jsonArray;


            System.out.println("S is " + s);
            String[] parts = s.split("pisah");

            try {

                for (int j = 0; j < count; j++) {
                    jsonRootObject = new JSONObject(parts[j]);
                    jsonArray = jsonRootObject.getJSONObject("user_data");

                    iklanImageURL[j] = jsonArray.getString("Image_url");
                    iklanURL[j] = jsonArray.getString("link_url");

                    LoginClasses.SaveSharedPreference.setIklanIconURL(context, iklanImageURL[j], IklanIcon + j);
                    LoginClasses.SaveSharedPreference.setIklanLinkURL(context, iklanURL[j], IklanLink + j);
                }


            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception " + e.getMessage();
            }

        }

    }
}
