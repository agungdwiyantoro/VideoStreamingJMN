package com.example.mycomputer.videostreamingjmn;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mycomputer.videostreamingjmn.SlidingImage_Adapter.SlidingImage_Adapter;
import com.example.mycomputer.videostreamingjmn.fragments.PanelPesanJMN;
import com.example.mycomputer.videostreamingjmn.fragments.daftarvip;
import com.example.mycomputer.videostreamingjmn.fragments.tentangJMN;
import com.example.mycomputer.videostreamingjmn.fragments.tentangVIP;
import com.example.mycomputer.videostreamingjmn.model.item_sliding_menu;
import com.example.mycomputer.videostreamingjmn.sliding_menu_adapter.sliding_menu_adapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import LoginClasses.SaveSharedPreference;


//----ACTIVITY MENU UTAMA----//

public class MainActivity extends ActionBarActivity  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<People.LoadPeopleResult>{


    AlertDialog.Builder exit;

    Fragment fragment;
    FragmentManager fragmentManager;
    android.support.v4.app.FragmentTransaction transaction;

    private List<item_sliding_menu> listSliding;
    private sliding_menu_adapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Context mA = this;
    private RelativeLayout screen;


    private ImageView photoProfil;
    private ImageView vipAbu;
    private TextView UserName;
    private TextView gmail;

    Intent emails;

    private GoogleApiClient mGoogleApiClient;

    private ImageView[]Iklan;

    int i = 0;


    private LinearLayout.LayoutParams layoutParams;

    private ImageButton logout;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Bitmap[] IMAGES = null;

    private Bitmap[]bitmaps;
    private ArrayList<Bitmap> ImagesArray = new ArrayList<Bitmap>();

    public static String checkArrayValue = "checkArrayValue";

    private LinearLayout[]rows;
    private LinearLayout[] channels_wrap;
    private ImageView[]channels;

    private ImageView opendrawer;

    private TextView[]NamaChannel;



    private LinearLayout.LayoutParams paramsButton, paramsRow,paramsWrap;

    int row = 0;

    int x;
    int indexBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drawer_layout);

        //--- VARIABLE - VARIABLE UNTUK MENYIMPAN DATA AKUN GOOGLE-SIGN IN
        String userName = getIntent().getStringExtra("nama");
        String email = getIntent().getStringExtra("email");
        String image = getIntent().getStringExtra("pp");

        photoProfil = (ImageView) findViewById(R.id.photo_profil);
        opendrawer = (ImageView) findViewById(R.id.open_drawer);

        opendrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(screen);
            }
        });

        vipAbu = (ImageView) findViewById(R.id.vipabu);

        UserName = (TextView) findViewById(R.id.tv_name);
        gmail = (TextView) findViewById(R.id.tv_email);

        //----ASYNCTASK UNTUK MENG-APPLY GAMBAR GOOGLE-SIGN KE IMAGEVIEW
        new LoadProfileImage(photoProfil).execute(image);

        UserName.setText(userName);
        gmail.setText(email);

        //----ASYNCTASK UNTUK MENGECEK STATUS USER (VIP ATAU FREE)
        new vipStatus(this).execute(email);

        listViewSliding = (ListView) findViewById(R.id.listViewItem);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listSliding = new ArrayList<>();
        screen = (RelativeLayout) findViewById(R.id.drawer_pane);




        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        //---- VARIABLE ARRAY IKLAN DIBERI 'BOUND' / BATAS
        Iklan = new ImageView[LoginClasses.SaveSharedPreference.getCountRowsIklan(getApplicationContext())];

        //---- SLIDEIMAGE UNTUK IKLAN DI APPLY DI MAINTHREAD
        for(int h =0; h < LoginClasses.SaveSharedPreference.getCountRowsIklan(this); h++) {

                new LoadProfileImage2(this,h).execute(LoginClasses.SaveSharedPreference.getIklanLinkURL(this, googlelogin.IklanIcon+h));
        }

        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

        //----Kode untuk menambah list menu pada Navigation Drawer------//
        listSliding.add(new item_sliding_menu("Tentang VIP"));
        listSliding.add(new item_sliding_menu("Pesan untuk Developer"));
        listSliding.add(new item_sliding_menu("Tentang PapmoStreamingTV"));


        //----
        adapter = new sliding_menu_adapter(this, listSliding);
        listViewSliding.setAdapter(adapter);

        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listViewSliding.setItemChecked(position, true);
                replaceFragment(position);
                drawerLayout.closeDrawer(screen);
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        //------ Kode untuk sign-out akun Google ----//
        logout = (ImageButton) findViewById(R.id.button_keluar);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                    Intent googleLogin = new Intent(MainActivity.this, googlelogin.class);
                    startActivity(googleLogin);
                    finish();
                }


            }
        });

        vipAbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(0);
                drawerLayout.closeDrawer(screen);
            }
        });

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);


        //-------kode untuk mengatur letak / layout channel-------------//
        indexBound =  LoginClasses.SaveSharedPreference.getCountRows(this);

        rows = new LinearLayout[indexBound];
        channels_wrap = new LinearLayout[indexBound];
        channels = new ImageView[indexBound];
        NamaChannel = new TextView[indexBound];

        System.out.println("FUFIFIFK W" + (int) getResources().getDimension(R.dimen.button_channel_width)/getResources().getDisplayMetrics().density);
        System.out.println("FUFIFIFK H" + (int) getResources().getDimension(R.dimen.button_channel_height)/getResources().getDisplayMetrics().density);

        System.out.println("Display metrics " + getResources().getDisplayMetrics().density);

        paramsButton = new LinearLayout.LayoutParams((int) (getResources().getDimension(R.dimen.button_channel_width)/getResources().getDisplayMetrics().density),(int) (getResources().getDimension(R.dimen.button_channel_height)/getResources().getDisplayMetrics().density));
        paramsWrap = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsRow = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
       // paramsX = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT,1f);

        paramsButton.setMargins((int) (getResources().getDimension(R.dimen.button_channel_margin)), 0, (int) (getResources().getDimension(R.dimen.button_channel_margin)), 0);

        paramsRow.setMargins(0, (int) (getResources().getDimension(R.dimen.button_channel_margin)), 0, (int) (getResources().getDimension(R.dimen.button_channel_margin)));
        paramsRow.gravity = Gravity.CENTER_HORIZONTAL;

        paramsWrap.gravity = Gravity.CENTER_HORIZONTAL;

        for (x = 0; x < indexBound; x++) {

            rows[x] = new LinearLayout(this);

            channels_wrap[x] = new LinearLayout(this);
            channels_wrap[x].setOrientation(LinearLayout.VERTICAL);

            channels[x] = new ImageView(this);
          //  NamaChannel[x] = new TextView(this);
          //  NamaChannel[x].setBackgroundColor(getResources().getColor(R.color.DarkSalmon));
          //  NamaChannel[x].setMaxWidth(8);
          ///  NamaChannel[x].setLayoutParams(paramsWrap);
          //  NamaChannel[x].setText(SaveSharedPreference.getChannelName(this,googlelogin.channelNameKey+x));


            Glide.with(this).load(SaveSharedPreference.getIconURL(this, googlelogin.channelURLIconKey+x)).into(channels[x]);

            channels[x].setId(x);
            channels[x].setScaleType(ImageView.ScaleType.FIT_XY);
            channels[x].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        if(LoginClasses.SaveSharedPreference.getStatus(MainActivity.this)) {
                            toVideo(LoginClasses.SaveSharedPreference.getChannel(MainActivity.this, googlelogin.channelKey + v.getId()));
                        }
                        else{
                            if((LoginClasses.SaveSharedPreference.getStatusVIP(MainActivity.this, googlelogin.statusVIP + v.getId())).equals("FREE")) {
                                toVideo(LoginClasses.SaveSharedPreference.getChannel(MainActivity.this, googlelogin.channelKey + v.getId()));
                            }
                        else {
                                replaceFragment(3);
                            }
                        }
                }


            });



            channels[x].setLayoutParams(paramsButton);
//            NamaChannel[x].setLayoutParams(paramsWrap);

            rows[x].setOrientation(LinearLayout.HORIZONTAL);
            rows[x].setLayoutParams(paramsRow);


            channels_wrap[x].setLayoutParams(paramsWrap);

            channels_wrap[x].addView(channels[x]);
          //  channels_wrap[x].addView(NamaChannel[x]);
        }



        int left = x % 3;
        int plus = 0;
        int min = 0;
        if (left == 0) {
            plus = 0;
            min = 3;
        }
        else if (left == 1) {
            plus = 1;
            min = 2;

        }
        else if (left == 2) {
            plus = 2;
            min = 1;

        }

        else if (left == 3){
            plus = 3;
            min = 0;
        }

        System.out.println("Sisa = " + left);
        int plusUse = 4;
        int minUse = 3;
        for (int k = 0; k < x; k++) {

            System.out.println("x berapa " + x);
            if (k == (4 * row)) {
                int l;
                if (k == (x - left)) {
                    plusUse = plus;
                    minUse = min;
                }
                for (l = k; l < k + plusUse; l++) {
                    rows[row].addView(channels_wrap[l]);

                }
                row++;
                k = l - minUse;
            }
        }

        for (int m = 0; m < row; m++) {
            linearLayout.addView(rows[m]);
        }

    }

    //---modul untuk menuju actiivty video--//
    public void toVideo(String link){
        Intent x = new Intent(this,Video.class);
        x.putExtra("link",link);
        startActivity(x);
    }
        protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }


    //--Modul untuk memanggil / menampilkan menu - menu pada navigation drawer--//
    private void replaceFragment(int pos) {
        fragment = null;
        switch (pos) {
            case 0:

                fragment = new tentangVIP();

                break;

            case 1:

                fragment = new PanelPesanJMN();

                break;

            case 2:

                fragment = new tentangJMN();

                break;

            case 3:

                fragment = new daftarvip();

                break;



            default:

                fragment = new tentangVIP();

                break;
        }


        if(null!=fragment) {

            fragmentManager = getSupportFragmentManager();

            transaction = fragmentManager.beginTransaction();

            transaction.setCustomAnimations(R.anim.in_from_left,R.anim.out_to_right,R.anim.right_in,R.anim.left_out);

            if(pos==3){
                transaction.setCustomAnimations(R.anim.fadein,R.anim.fadeout,R.anim.fadein,R.anim.fadeout);
            }
            else{
                transaction.setCustomAnimations(R.anim.in_from_left,R.anim.out_to_right,R.anim.right_in,R.anim.left_out);
            }
            transaction.replace(R.id.justContent, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {

       if(fragment!=null) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
           fragment=null;
       }
       else{
           exitMessage("KELUAR APLIKASI?",0);

       }

    }

    //-------------Modul pesan pop-up jika user ingin keluar aplikasi
    public void exitMessage(String message,final int id){
        exit = new AlertDialog.Builder(this);
        exit.setMessage(message);
        exit.setCancelable(false);
        exit.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                    MainActivity.super.onBackPressed();
                    finish();

            }
        });

        exit.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        exit.create().show();
    }





    //----Modul untuk memuat gambar dari akun Google
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        public LoadProfileImage(ImageView bmImage) {
            photoProfil= bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            photoProfil.setImageBitmap(result);


        }
    }

    //----Modul untuk memuat gambar iklan dan menampilkannya pada image slider
    private class LoadProfileImage2 extends AsyncTask<String, Void, Bitmap> {
        int index = 0;
        Context context;

        public LoadProfileImage2(Context context,int index) {
            bitmaps = new Bitmap[LoginClasses.SaveSharedPreference.getCountRowsIklan(context)];
            this.index = index;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            init(result, index);
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub
    //    mSignInClicked = false;

        // updateUI(true);
        Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(
                this);
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub
        mGoogleApiClient.connect();
        // updateUI(false);
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

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

    @Override
    public void onResult(People.LoadPeopleResult arg0) {
        // TODO Auto-generated method stub

    }



    //--- modul pembuatan ImageSlider dengan indicator
    private void init(Bitmap result, int index) {

        bitmaps[index] = result;
        ImagesArray.add(bitmaps[index]);

        mPager = (ViewPager) findViewById(R.id.pager);


        mPager.setAdapter(new SlidingImage_Adapter(MainActivity.this,ImagesArray));


        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius((float) (3.5 * density));

        NUM_PAGES =bitmaps.length;

        if(NUM_PAGES==LoginClasses.SaveSharedPreference.getCountRowsIklan(MainActivity.this)){
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 9000, 9000);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    //----------Modul untuk men-check apakah user merupakan VIP atau tidak, sekaligus menampilkan status user---//
    public class vipStatus extends AsyncTask<String, Void, String> {


        private Context context;

        private String user;

        public vipStatus(Context context) {
            this.context = context;
        }



        @Override
        protected String doInBackground(String... params) {
            String data = "";

            int tmp;
            user = params[0];


            try {
                URL url = new URL("http://megacleon.com/JMN_Streaming_TV/php/VIP.php");
                String urlParams = "id_channel=" +  user;

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


            int ahoy = Integer.parseInt(s);

            if(ahoy==0){
                LoginClasses.SaveSharedPreference.setStatus(context,false);
                vipAbu.setImageResource(R.drawable.vipabu);
                System.out.println("ini free");
            }
            else{
                LoginClasses.SaveSharedPreference.setStatus(context,true);
                vipAbu.setImageResource(R.drawable.vipemas);
                System.out.println("ini vip");
            }

        }

    }

}



