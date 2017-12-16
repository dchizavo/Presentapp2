package sunnysoft.presentapp.Interfaz;

        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.github.sundeepk.compactcalendarview.CompactCalendarView;
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
        import java.util.List;
        import java.util.Locale;
        import cz.msebera.android.httpclient.entity.StringEntity;
        import sunnysoft.presentapp.Interfaz.adapter.EventosAdapter;
        import sunnysoft.presentapp.Interfaz.pojo.Eventos;
        import sunnysoft.presentapp.R;

public class CalendarioActivity extends AppCompatActivity {

    // Definir variables

    String user_name;
    String user_image;
    String token;
    String logo;
    String user_type;
    String subdomain;
    String email;
    String url;
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "CalendarioFragment";
    //Cursor resultado;

    //private BDExample BDExample;
    CompactCalendarView compactCalendar;
    TextView CalendarMonth;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    String fechaImpuesto;
    private HashMap<String, Eventos> eventos = new HashMap<>();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        final ListView mEventoList;


        mEventoList = (ListView) findViewById(R.id.eventodia_list);
        // Recibir parametros

        //  Bundle datos = getIntent().getExtras();
        token = "?token=$2y$10$uAYJ/m8k3P73nca0sQ87Ze69s6Sf/ZQSITdL/pprzCCz.OmBBywHq";
        //token = datos.getString("token");
        user_type = ".present.com.co/api/calendar";
        subdomain = "http://serverprueba";
        email = "&email=jefferson.ceballos@dc.co";

        // Instancia de helper

        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        final DateFormat originalFormatref = new SimpleDateFormat("yyyy-mm-dd");
        final DateFormat targetFormatref = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //Tooblar
        Toolbar toolbarfecha = (Toolbar) findViewById(R.id.toolbarfecha);
        final TextView toolbar_titlefecha = (TextView)toolbarfecha.findViewById(R.id.toolbar_titlefecha);
        setSupportActionBar(toolbarfecha);
        toolbar_titlefecha.setText("December-2017");

        try{

            // Creamos un nuevo Bundle
            Bundle args = new Bundle();

            // Colocamos el String
            args.putString("subdomain", subdomain);
            args.putString("user_type", user_type);
            args.putString("token", token);
            args.putString("email", email);

            // Supongamos que tu Fragment se llama TestFragment. Colocamos este nuevo Bundle como argumento en el fragmento.

        }catch(Exception e){

            Toast.makeText(CalendarioActivity.this, "error"+e, Toast.LENGTH_SHORT).show();

        }

        //Calendario

        // CalendarMonth = (TextView)findViewById(R.id.CalendarMonth);
        Event ev1 = null;

        final ArrayList<String> EventosArrayList = new ArrayList<String>();
        // Añade el elemento al ArrayList

        //traer datos de ws
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;


        //url = "http://"+subdomain;
        url = subdomain;
        url += user_type;
        url += token;
        url += email;
        //url += "?token="+token;
        //url += "&email="+ email;

        // Invoke RESTful Web Service with Http parameters
        RequestHandle post = client.get(CalendarioActivity.this, url, entity, "application/json", new AsyncHttpResponseHandler() {

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
                String title = null;
                String start = null;
                String end = null;
                String ad = null;
                String tipo_evento = null;
                String detail_url = null;

                try {

                    responseStr = new String(responseBody, "UTF-8");
                    JSONArray jsonarray = new JSONArray(responseStr);
                    for(int i=0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        id       = jsonobject.getString("id");
                        title       = jsonobject.getString("title");
                        start       = jsonobject.getString("start");
                        end       = jsonobject.getString("end");
                        ad       = jsonobject.getString("ad");
                        tipo_evento       = jsonobject.getString("tipo_evento");

                        try {
                            detail_url = jsonobject.getString("detail_url");
                        }catch (Exception e){
                            detail_url = null;
                        }

                        try {

                            try {

                                try {
                                    fechaImpuesto = start;

                                    DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    DateFormat originalFormatHour = new SimpleDateFormat("HH:mm:ss");
                                    DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date date = originalFormat.parse(fechaImpuesto);
                                    String dateref = originalFormat.format(date);
                                    String hourref = originalFormatHour.format(date);
                                   // String dateref = new SimpleDateFormat("yyyy-MM-dd").format(fechaImpuesto);
                                    //test

                                    EventosArrayList.add(dateref);
                                    EventosArrayList.add(title);

                                    saveEvento(new Eventos(id, title, tipo_evento, dateref,  hourref, detail_url, i + 1));
                                    ev1 = new Event(Color.BLUE, date.getTime(),start);
                                    //Set an event for Teachers' Professional Day 2016 which is 21st of October
                                    compactCalendar.addEvent(ev1);
                                } catch(Exception e) {

                                    Toast.makeText(CalendarioActivity.this, "Fine"+e, Toast.LENGTH_SHORT).show();
                                    // dateFormat.parse() could throw a ParseException
                                }
                            } catch(Exception e) {

                                Toast.makeText(CalendarioActivity.this, "error"+e, Toast.LENGTH_LONG).show();


                            }
                        }catch (Exception e){
                            //Toast.makeText(Activity_main.this, "Fallo por "+e, Toast.LENGTH_LONG).show();
                            Log.i("WSUsuarios","Fallo por "+e);
                        }
                    }

                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                    Toast.makeText(CalendarioActivity.this, "Fallo por a", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CalendarioActivity.this, "Fallo por "+e, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

                if (statusCode == 404) {
                    Log.i("On Failure", "404");
                    Toast.makeText(CalendarioActivity.this, "Fallo por 404 ", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Log.i("On Failure", "500");
                    Toast.makeText(CalendarioActivity.this, "Fallo por 500 ", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Log.i("On Failure", "NN");
                    Toast.makeText(CalendarioActivity.this, "Fallo por NN ", Toast.LENGTH_LONG).show();
                }

            }

        });

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();

                try{

                   // DateFormat originalFormatclicked = new SimpleDateFormat("yyyy-MM-dd");
                    //String daterefclicked = originalFormatclicked.format(dateFormatForDisplaying.format(dateClicked));


                    //Log.d(TAG, "inside onclick " + dateFormatForDisplaying.format(dateClicked));

                   // Toast.makeText(CalendarioActivity.this, "error"+daterefclicked, Toast.LENGTH_SHORT).show();

                    //Toast.makeText(CalendarioActivity.this, "error"+compactCalendar.getEvents(dateClicked), Toast.LENGTH_SHORT).show();

                    /*for (Event dataEvento : compactCalendar.getEvents(dateClicked)) {

                        Toast.makeText(CalendarioActivity.this, (String) dataEvento.getData(), Toast.LENGTH_SHORT).show();

                    }*/

                    final EventosAdapter mEventoAdapter;
                    // Inicializar el adaptador con la fuente de datos.
                    mEventoAdapter = new EventosAdapter(CalendarioActivity.this,getEventos(),dateFormatForDisplaying.format(dateClicked));

                    //Relacionando la lista con el adaptador
                    mEventoList.setAdapter(mEventoAdapter);


                    // Eventos
                    mEventoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            Bundle datos = getIntent().getExtras();
                            Eventos currentEvento = mEventoAdapter.getItem(position);
                            String getDetail_url = currentEvento.getDetail_url();
                           /* Toast.makeText(CalendarioActivity.this,
                                   "Iniciar screen de detalle para: \n" + getDetail_url,
                                   Toast.LENGTH_SHORT).show();*/
                            Intent i = new Intent(CalendarioActivity.this, VereventoActivity.class);
                            i.putExtra("DetailUrl", getDetail_url);
                            startActivity(i);
                        }
                    });

                }catch(Exception e){

                    Toast.makeText(CalendarioActivity.this, "error"+e, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                try{

                    toolbar_titlefecha.setText(dateFormatMonth.format(firstDayOfNewMonth));
                    // CalendarMonth.setText(dateFormatMonth.format(firstDayOfNewMonth));
                }catch(Exception e){
                    Toast.makeText(CalendarioActivity.this, "Error" + e, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //fin calendario

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CalendarioActivity.this, MenuActivity.class);
                i.putExtra("user_name", user_name);
                i.putExtra("user_image",user_image);
                i.putExtra("user_type",user_type);
                i.putExtra("token",token);
                i.putExtra("logo", logo);
                i.putExtra("subdomain", subdomain);
                i.putExtra("email", email);
                startActivity(i);
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CalendarioActivity.this, CreareventoActivity.class);
                i.putExtra("user_name", user_name);
                i.putExtra("user_image",user_image);
                i.putExtra("user_type",user_type);
                i.putExtra("token",token);
                i.putExtra("logo", logo);
                i.putExtra("subdomain", subdomain);
                i.putExtra("email", email);
                startActivity(i);
            }
        });

        //Tooblar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbar_title = (TextView)toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        toolbar_title.setText("CALENDARIO");



    }

    private void saveEvento(Eventos evento) {
        eventos.put(String.valueOf(evento.getIndice()), evento);
    }

    public List<Eventos> getEventos() {
        return new ArrayList<>(eventos.values());
    }



}