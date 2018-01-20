package sunnysoft.presentapp.Interfaz;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.domain.Event;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.entity.StringEntity;
import sunnysoft.presentapp.Datos.DatabaseHelper;
import sunnysoft.presentapp.Interfaz.adapter.EventosAdapter;
import sunnysoft.presentapp.Interfaz.adapter.ProcesoEntradasAdapter;
import sunnysoft.presentapp.Interfaz.pojo.Entradas;
import sunnysoft.presentapp.Interfaz.pojo.Eventos;
import sunnysoft.presentapp.R;

public class VerEntradas extends AppCompatActivity {

    String user_name;
    String user_image;
    String token;
    String logo;
    String user_type;
    String subdomain;
    String email;
    String url;
    private DatabaseHelper midb;
    private Toolbar secundaria;


    private HashMap<String, Entradas> entradas = new HashMap<>();

    @Override
    public void onBackPressed() {
        Toast.makeText(VerEntradas.this, "El botón retroceder se ha deshabilitado", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_entradas);


        final ListView mProcesoEntradasList;
        String View_all_url = getIntent().getStringExtra("View_all_url");

        mProcesoEntradasList = (ListView) findViewById(R.id.entradas_list);

        midb = new DatabaseHelper(this);



        // llamado de datos de base de datos

        Cursor Resultados = midb.Session();

        subdomain =Resultados.getString(Resultados.getColumnIndex("subdomain"));
        token =Resultados.getString(Resultados.getColumnIndex("token"));
        email =Resultados.getString(Resultados.getColumnIndex("user"));

        url = View_all_url;
        url += "?token="+token;
        url += "&email="+ email;

        //Tooblar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbar_title = (TextView)toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        toolbar_title.setText(getResources().getText(R.string.txt_menu_entradas));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(VerEntradas.this, MenuActivity.class);
                startActivity(i);
            }
        });


        //traer datos de ws
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;


        // Invoke RESTful Web Service with Http parameters
        RequestHandle post = client.get(VerEntradas.this, url, entity, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                //mydb.borrar_Users();
                //mydb.oncreateusers();

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

                Event ev1 = null;
                // called when response HTTP status is "200 OK"
                String responseStr = null;
                String id = null;
                String user_name = null;
                String curso_grupo = null;
                String created_at = null;
                String detalles = null;
                String img_persona = null;
                String name = null;


                try {

                    responseStr = new String(responseBody, "UTF-8");
                    JSONObject jsonobject = new JSONObject(responseStr);

                    JSONObject entradas = new JSONObject(jsonobject.getString("entradas"));
                    JSONObject proceso = new JSONObject(jsonobject.getString("proceso"));

                    String procesoname = proceso.getString("name");

                    ///////////////////////segunda toolbar /////////////////////
                    secundaria = (Toolbar) findViewById(R.id.toolbar_secundaria);
                    secundaria.setNavigationIcon(R.drawable.arrow_back);
                    TextView titulo_secundaria = (TextView) secundaria.findViewById(R.id.toolbar_secundaria_title);
                    titulo_secundaria.setText(procesoname);
                    secundaria.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(VerEntradas.this, ModulosActivity.class);
                            startActivity(i);
                        }
                    });


                    JSONArray jsonarray = new JSONArray(entradas.getString("data"));
                    JSONArray jsonarray_tags;
                    Entradas entrada_1;

                    for(int i=0; i < jsonarray.length(); i++) {

                        JSONObject jsonobject_data = jsonarray.getJSONObject(i);

                        id   = jsonobject_data.getString("id");
                        user_name   = jsonobject_data.getString("user_name");
                        curso_grupo = jsonobject_data.getString("curso_grupo");
                        created_at  = jsonobject_data.getString("created_at");
                        img_persona  = jsonobject_data.getString("user_image");
                        detalles = curso_grupo + " " + created_at;
                        jsonarray_tags = new JSONArray(jsonobject_data.getString("tags"));





                        String [] tags = new String [jsonarray_tags.length()];
                        for(int j=0; j < jsonarray_tags.length(); j++) {

                            JSONObject jsonobject_tags = jsonarray_tags.getJSONObject(j);
                            name       = jsonobject_tags.getString("name");
                            //tags.add(name);
                            tags[j] = name;
                            //Toast.makeText(VerEntradas.this, "Bien por "+name, Toast.LENGTH_LONG).show();

                        }

                        try {

                            final ProcesoEntradasAdapter mProcesoEntradasAdapter;

                            //entrada_1 = new Entradas(user_name, detalles, i + 1);
                            saveEntrada(new Entradas(user_name, detalles, i + 1,img_persona,tags));

                            // Toast.makeText(VerEntradas.this, "Bien por "+entrada_1.getIndice(), Toast.LENGTH_LONG).show();

                            // Inicializar el adaptador con la fuente de datos.
                            mProcesoEntradasAdapter = new ProcesoEntradasAdapter(VerEntradas.this,getEntradas());

                            //Relacionando la lista con el adaptador
                            mProcesoEntradasList.setAdapter(mProcesoEntradasAdapter);

                        }catch (Exception e){
                            Toast.makeText(VerEntradas.this, "Fallo por "+e, Toast.LENGTH_LONG).show();
                            Log.i("WSUsuarios","Fallo por "+e);
                        }
                    }

                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                    Toast.makeText(VerEntradas.this, "Fallo por a", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(VerEntradas.this, "Fallo por "+e, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

                if (statusCode == 404) {
                    Log.i("On Failure", "404");
                    Toast.makeText(VerEntradas.this, "Fallo por 404 ", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Log.i("On Failure", "500");
                    Toast.makeText(VerEntradas.this, "Fallo por 500 ", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Log.i("On Failure", "NN");
                    Toast.makeText(VerEntradas.this, "Fallo por NN ", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void saveEntrada(Entradas entrada) {
        entradas.put(String.valueOf(entrada.getIndice()), entrada);
    }

    public List<Entradas> getEntradas() {
        return new ArrayList<>(entradas.values());
    }

}
