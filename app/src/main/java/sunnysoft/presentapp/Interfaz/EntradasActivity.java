package sunnysoft.presentapp.Interfaz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import sunnysoft.presentapp.Interfaz.adapter.EntradasAdapter;

import sunnysoft.presentapp.Interfaz.pojo.Entradas;

import sunnysoft.presentapp.R;

public class EntradasActivity extends AppCompatActivity {

   // private static final int NUM_TABS = 4;

    private TabLayout tabLayout;
    private EntradasActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;


    String subdomain;
    String email;
    String url;
    String token;

    String image_user;
    String entradas_url;
    int Notification_count;

    int notification_count;



    // declaracion de BD
    private DatabaseHelper midb;






    @Override
    public void onBackPressed() {
        Toast.makeText(EntradasActivity.this, "El botón retroceder se ha deshabilitado", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entradas);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbar_title = (TextView)toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Entradas");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        midb = new DatabaseHelper(this);

        Cursor Resultados = midb.Session();

        subdomain =Resultados.getString(Resultados.getColumnIndex("subdomain"));
        token =Resultados.getString(Resultados.getColumnIndex("token"));
        email =Resultados.getString(Resultados.getColumnIndex("user"));


        // construir url de servicio de tabs

        url = "http://"+subdomain;
        url += ".present.com.co/api/entrada/users";
        url += "?token="+token;
        url += "&email="+ email;

        // funcion que crea los tabs
        //
        seteartabs(url);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new EntradasActivity.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewpagerent);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabsent);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(EntradasActivity.this, MenuActivity.class);

                startActivity(i);
            }
        });

    }


    public void seteartabs(String url){


        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;

        // llamado del servicio
        RequestHandle post  = client.get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        // Declaracion de variables
                        String responseStr = null;

                        //respuesta del servicio
                        try {
                            responseStr = new String(responseBody, "UTF-8");
                            // manejo del primer nivel de objetos

                            JSONArray items = new JSONArray(responseStr);

                            for(int i=0; i < items.length(); i++) {

                                String item = items.getString(i);

                                JSONObject valores = new JSONObject(item);

                                image_user = valores.getString("image");
                                entradas_url = valores.getString("entradas_url");
                                notification_count = valores.getInt("notification_count");


                                /////////////////////creacion de tabs dinamicamente//////////////////////
                                tabLayout.addTab(tabLayout.newTab().setText(image_user));


                            }

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
                                    Toast.makeText(EntradasActivity.this, mensaje, Toast.LENGTH_LONG).show();
                                }

                                midb.logouth();
                                midb.oncreateusers();
                                Intent i = new Intent(EntradasActivity.this, InicioActivity.class);
                                startActivity(i);

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {

                            Toast.makeText(EntradasActivity.this, "Erros Statuscode = 500", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Log.i("On Failure", "NN");
                            Toast.makeText(EntradasActivity.this, "On Failure ", Toast.LENGTH_LONG).show();

                            //Institución no valida.
                        }

                    }

                });



    }


/**
 * A placeholder fragment containing a simple view.
 */
public static class PlaceholderFragment extends Fragment {

    View rootView;

    private static final String ARG_SECTION_NUMBER = "section_number";
    RecyclerView recyclerEntradas;
    EntradasAdapter adapter;

    List<Entradas> EntradasList;

    public PlaceholderFragment() {

    }
    public static EntradasActivity.PlaceholderFragment newInstance(int sectionNumber) {
        EntradasActivity.PlaceholderFragment fragment = new EntradasActivity.PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_entradas, container, false);
        recyclerEntradas = (RecyclerView) rootView.findViewById(R.id.recycler_entradas);
        EntradasList = new ArrayList<>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerEntradas.setLayoutManager(linearLayoutManager);

        Contenido();

        return rootView;
    }

    public void Contenido(){

        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;
        String url = "http://serverprueba.present.com.co/api/entradas/2?email=daniela@dc.co&token=$2y$10$xeziClDwuVYHjpTcQB6ziufL.6/RYpP2pBjJpyBhQO1Qdqn1GKL4.";


        // llamado del servicio
        RequestHandle post  = client.get(url, new AsyncHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        // Declaracion de variables
                        String responseStr = null;
                        List<String> nomtags = new ArrayList<>();

                        //respuesta del servicio
                        try {
                            responseStr = new String(responseBody, "UTF-8");

                            JSONObject comunicados = new JSONObject(responseStr);
                            // Se obtiene valores del objeto

                             String valordata = comunicados.getString("data");

                            JSONArray items = new JSONArray(valordata);

                            for (int i = 0; i < items.length(); i++) {
                                String item = items.getString(i);

                                JSONObject valores = new JSONObject(item);

                                String proceso_name= valores.getString("proceso_name");
                                String created_at = valores.getString("created_at");
                                String tags = valores.getString("tags");

                                JSONArray itags = new JSONArray(tags);

                                for (int a = 0; a < itags.length(); a++) {

                                    String tag = itags.getString(a);

                                    JSONObject valors = new JSONObject(item);

                                    String tagitm = valors.getString("name");

                                    nomtags.add(tagitm);

                                }

                                EntradasList.add(new Entradas(proceso_name,created_at,nomtags));

                            }

                            adapter = new EntradasAdapter(getContext(), EntradasList);
                            recyclerEntradas.setAdapter(adapter);
                            adapter = new EntradasAdapter(getContext(),EntradasList);
                            recyclerEntradas.setAdapter(adapter);


                            } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });






    }


}

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return EntradasActivity.PlaceholderFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.

        return 3;
    }
}
}
