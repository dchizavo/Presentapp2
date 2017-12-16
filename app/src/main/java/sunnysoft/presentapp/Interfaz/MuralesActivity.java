package sunnysoft.presentapp.Interfaz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import sunnysoft.presentapp.Datos.DatabaseHelper;
import sunnysoft.presentapp.Interfaz.adapter.MuralesAdapter;
import sunnysoft.presentapp.Interfaz.pojo.Murales;
import sunnysoft.presentapp.Interfaz.pojo.Photos;
import sunnysoft.presentapp.Interfaz.pojo.files;
import sunnysoft.presentapp.R;

public class MuralesActivity extends AppCompatActivity {

    // Definir variables


    public String token;

    String subdomain;
    public String email;
    String url;
    String nombremural;
    int notification_count;
    String mural_url;


    public static List<String> urls;
    List<String> nomes;



    // declaracion de BD
    private DatabaseHelper midb;

    // tabs layouts

    TabLayout tabLayout;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    Context context;

    final ProgressDialog[] progressDialog = new ProgressDialog[1];

    @Override
    public void onBackPressed() {
        Toast.makeText(MuralesActivity.this, "El botón retroceder se ha deshabilitado", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_murales);

        // Recibir parametros

        midb = new DatabaseHelper(this);
        context = this;

        Cursor Resultados = midb.Session();

        subdomain =Resultados.getString(Resultados.getColumnIndex("subdomain"));
        token =Resultados.getString(Resultados.getColumnIndex("token"));
        email =Resultados.getString(Resultados.getColumnIndex("user"));

        // construir url de servicio de tabs

        url = "http://"+subdomain;
        url += ".present.com.co//api/murales/names";
        url += "?token="+token;
        url += "&email="+ email;

        urls = new ArrayList<>();
        nomes = new ArrayList<>();


        // funcion que crea los tabs
        //
        seteartabs(url);

        //Tooblar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbar_title = (TextView)toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        toolbar_title.setText(getResources().getText(R.string.txt_menu_murales));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

/*
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);



        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MuralesActivity.this, MenuActivity.class);
               startActivity(i);
            }
        });

    }

    public void seteartabs(final String url){

        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        // llamado del servicio
        RequestHandle post  = client.get(url, new AsyncHttpResponseHandler() {



            @Override
            public void onStart(){

                super.onStart();
                progressDialog[0] = ProgressDialog.show(
                        context, "Por favor espere", "Procesando...");

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                // Declaracion de variables
                String responseStr = null;




                try {

                    //respuesta del servicio
                    responseStr = new String(responseBody, "UTF-8");
                    // manejo del primer nivel de objetos
                    JSONObject user = new JSONObject(responseStr);
                    // Se obtiene valores del objeto
                    String valorLlave = user.getString("murales");

                    JSONArray items = new JSONArray(valorLlave);

                    for(int i=0; i < items.length(); i++) {
                        String item = items.getString(i);

                        JSONObject valores = new JSONObject(item);

                        nombremural = valores.getString("name");
                        notification_count = valores.getInt("notificaciones_count");
                        mural_url = valores.getString("mural_url");

                        urls.add(mural_url);
                        nomes.add(nombremural);

                    }

                    for (int a = 0; a< nomes.size(); a++){

                        /////////////////////creacion de tabs dinamicamente//////////////////////
                        tabLayout.addTab(tabLayout.newTab().setText(nomes.get(a)));
                    }

                    progressDialog[0].dismiss();


                    // Create the adapter that will return a fragment for each of the three
                    // primary sections of the activity.
                    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                    // Set up the ViewPager with the sections adapter.
                    mViewPager = (ViewPager) findViewById(R.id.container);
                    mViewPager.setAdapter(mSectionsPagerAdapter);


                    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));



                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                if (statusCode == 421) {
                    //declaracion de variables
                    String responseStr = null;

                    try {
                        // respuesta del servicio
                        responseStr = new String(responseBody, "UTF-8");
                        // manejo de primer nivel de objetos del json
                        JSONObject errorx = new JSONObject(responseStr);
                        // se obtiene los valores del json
                        String valorLlave = errorx.getString("errors");
                        // manejo del segundo nivel de valores de json
                        JSONObject errorxa = new JSONObject(valorLlave);
                        // se obtiene los valores que contiene el objeto
                        String msgerror = errorxa.getString("login");
                        // se maneja el array json
                        JSONArray jsonarray = new JSONArray(msgerror);

                        //se obtiene cada uno de los mensajes que se encuentran dentro del json
                        for(int i=0; i < jsonarray.length(); i++) {
                            String mensaje = jsonarray.getString(i);
                            Toast.makeText(MuralesActivity.this, mensaje, Toast.LENGTH_LONG).show();
                        }

                        midb.logouth();
                        midb.oncreateusers();
                        Intent i = new Intent(MuralesActivity.this, InicioActivity.class);
                        startActivity(i);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // When Http response code is '500'
                else if (statusCode == 500) {

                    Toast.makeText(MuralesActivity.this, "Erros Statuscode = 500", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Log.i("On Failure", "NN");
                    Toast.makeText(MuralesActivity.this, "On Failure ", Toast.LENGTH_LONG).show();

                    //Institución no valida.
                }


            }

        });


    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        View rootView;

        private static final String ARG_SECTION_NUMBER = "section_number";

        List<Murales> muralesList;
        MuralesAdapter adapter;
        RecyclerView recyclerMurales;
        public int contador;


        public PlaceholderFragment() {


        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, List<String> urls, String email, String token) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt("position",sectionNumber);
            args.putStringArrayList("ARG_urls", (ArrayList<String>) urls);
            args.putString("email", email);
            args.putString("token", token);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_murales, container, false);
            recyclerMurales = (RecyclerView) rootView.findViewById(R.id.recycler_murales);
            muralesList = new ArrayList<>();
            List<String> urls = new ArrayList<>();

            Bundle extras = getArguments();
            int contador = extras.getInt("position");
            urls = extras.getStringArrayList("ARG_urls");
            String email= extras.getString("email");
            String token= extras.getString("token");




            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerMurales.setLayoutManager(linearLayoutManager);

            Contenido(contador,urls, email, token);

            return rootView;
        }

    public void Contenido(int contador, List<String> urls, String email, String token){



            AsyncHttpClient client = new AsyncHttpClient();

        final List<String> archivos2;
        final List<String> photos2;

        final Boolean[] archivo = new Boolean[1];
        final Boolean[] photos = new Boolean[1];


        //String url = "http://serverprueba.present.com.co/api/mural/institucional/posts?email=daniela@dc.co&token=$2y$10$xeziClDwuVYHjpTcQB6ziufL.6/RYpP2pBjJpyBhQO1Qdqn1GKL4.";
        String url = urls.get(contador-1);
        url += "?token="+token;
        url += "&email="+ email;

        archivos2 = new ArrayList<>();

        photos2 = new ArrayList<>();

            // llamado del servicio


        RequestHandle post  = client.get(url, new AsyncHttpResponseHandler() {

                final ProgressDialog[] progressDialog = new ProgressDialog[1];


                @Override
                public void onStart(){

                    super.onStart();


                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    // Declaracion de variables
                    String responseStr = null;


                    try {

                        //respuesta del servicio
                        responseStr = new String(responseBody, "UTF-8");
                        // manejo del primer nivel de objetos
                        JSONObject comunicados = new JSONObject(responseStr);
                        // Se obtiene valores del objeto
                        String valorLlave = comunicados.getString("comunicados");

                        JSONObject segundoobj = new JSONObject(valorLlave);

                        String valordata = segundoobj.getString("data");

                        JSONArray items = new JSONArray(valordata);

                        for (int i = 0; i < items.length(); i++) {

                            String item = items.getString(i);

                            JSONObject valores = new JSONObject(item);

                            String user_namedata = valores.getString("user_name");
                            String created_atdata = valores.getString("created_at");
                            String contentdata = valores.getString("content");
                            String user_photo = valores.getString("user_photo");
                            String read_more = valores.getString("read_more");

                            String file = valores.getString("files");
                            String phoos = valores.getString("photos");
                            //String read_more = "false";

                            JSONArray fle = new JSONArray(file);



                            for (int a = 0; a < fle.length(); a++) {
                                String fles = fle.getString(a);

                                JSONObject its = new JSONObject(fles);

                                //archivos.add(new files(its.getString("original_name"), its.getString("url")));
                                archivos2.add(its.getString("original_name"));

                            }

                            JSONArray fle2 = new JSONArray(phoos);

                            for (int b = 0; b < fle2.length(); b++) {
                                String fles2 = fle2.getString(b);

                                JSONObject its2 = new JSONObject(fles2);

                                photos2.add(its2.getString("original_name"));

                            }

                            if(archivos2.size()>0){
                                archivo[0] = true;

                            }  else {
                                archivo[0] = false;
                                archivos2.clear();
                            }

                            if(photos2.size()>0){
                                photos[0] = true;

                            }  else {
                                photos[0] = false;

                            }



                            muralesList.add(new Murales(user_namedata, created_atdata, contentdata, read_more,user_photo, archivo[0], photos[0]));

                            adapter = new MuralesAdapter(getContext(), muralesList);
                            recyclerMurales.setAdapter(adapter);

                        }
                    }  catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

            adapter = new MuralesAdapter(getContext(),muralesList);
            recyclerMurales.setAdapter(adapter);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/
            //return rootView;
            //return rootView;
        }
        //public void vistamural(){
        public void vistamural(){


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    if (statusCode == 421) {
                        //declaracion de variables
                        String responseStr = null;

                        try {
                            // respuesta del servicio
                            responseStr = new String(responseBody, "UTF-8");
                            // manejo de primer nivel de objetos del json
                            JSONObject errorx = new JSONObject(responseStr);
                            // se obtiene los valores del json
                            String valorLlave = errorx.getString("errors");
                            // manejo del segundo nivel de valores de json
                            JSONObject errorxa = new JSONObject(valorLlave);
                            // se obtiene los valores que contiene el objeto
                            String msgerror = errorxa.getString("login");
                            // se maneja el array json
                            JSONArray jsonarray = new JSONArray(msgerror);

                            //se obtiene cada uno de los mensajes que se encuentran dentro del json
                            for(int i=0; i < jsonarray.length(); i++) {
                                String mensaje = jsonarray.getString(i);
                                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
                            }

                            //midb.logouth();
                            //midb.oncreateusers();
                            //Intent i = new Intent(MuralesActivity.this, InicioActivity.class);
                            //startActivity(i);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // When Http response code is '500'
                    else if (statusCode == 500) {

                         Toast.makeText(getContext(), "Erros Statuscode = 500", Toast.LENGTH_LONG).show();
                    }
                    // When Http response code other than 404, 500
                    else {
                        Log.i("On Failure", "NN");
                          Toast.makeText(getContext(), "On Failure ", Toast.LENGTH_LONG).show();

                        //Institución no valida.
                    }


                }

            });


        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
           return PlaceholderFragment.newInstance(position + 1, urls , email, token);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return urls.size();
        }
    }
}
