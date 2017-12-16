package sunnysoft.presentapp.Interfaz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.domain.Event;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import cz.msebera.android.httpclient.entity.StringEntity;
import me.gujun.android.taggroup.TagGroup;
import sunnysoft.presentapp.R;

public class VereventoActivity extends AppCompatActivity {

    String url;
    TextView nombreevento;
    TextView cursogrupo;
    TextView txv_nombre;
    ImageView img_persona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verevento);
        Bundle datos = getIntent().getExtras();
        url = datos.getString("DetailUrl");
        url += "?token=$2y$10$uAYJ/m8k3P73nca0sQ87Ze69s6Sf/ZQSITdL/pprzCCz.OmBBywHq&email=jefferson.ceballos@dc.co";
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbar_title = (TextView)toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        toolbar_title.setText(getResources().getText(R.string.txt_menu_calendario));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final TagGroup mTagGroup = (TagGroup) findViewById(R.id.tag_group);

        //traer datos de ws
        final AsyncHttpClient client = new AsyncHttpClient();
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;

        nombreevento = (TextView) findViewById(R.id.nombreevento);
        cursogrupo = (TextView) findViewById(R.id.cursogrupo);
        txv_nombre = (TextView) findViewById(R.id.txv_nombre);

        img_persona = (ImageView) findViewById(R.id.img_persona);

        // Toast.makeText(VereventoActivity.this, "Fallo por "+url, Toast.LENGTH_LONG).show();

        // Invoke RESTful Web Service with Http parameters
        RequestHandle post = client.get(VereventoActivity.this, url, entity, "application/json", new AsyncHttpResponseHandler() {

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
                String proceso_name = null;
                String curso_grupo = null;
                String user_name = null;
                String user_image = null;
                String name = null;
                String detail_url = null;

                try {

                    responseStr = new String(responseBody, "UTF-8");
                    JSONObject jsonobject = new JSONObject(responseStr);
                    Iterator x = jsonobject.keys();
                   // id = jsonobject.getString("id");
                    proceso_name = jsonobject.getString("proceso_name");
                    curso_grupo = jsonobject.getString("curso_grupo");
                    user_name = jsonobject.getString("user_name");
                    user_image = jsonobject.getString("user_image");
                    user_image = jsonobject.getString("user_image");
                    JSONArray jsonarray = new JSONArray(jsonobject.getString("tags"));
                    nombreevento.setText(proceso_name);
                    cursogrupo.setText(curso_grupo);
                    txv_nombre.setText(user_name);
                    nombreevento.setTextColor(Color.parseColor("#DC9233"));
                    //descarga de imagen logo del colegio
                    new DownloadImage().execute(user_image);

                    for(int i=0; i < jsonarray.length(); i++) {

                        JSONObject jsonobject_tags = jsonarray.getJSONObject(i);
                        name       = jsonobject_tags.getString("name");

                        mTagGroup.setTags(new String[]{"Tag1", "Tdfgdfgdfgdgdgdfag2", "Tafghfhfg3"});
                        Toast.makeText(VereventoActivity.this, "Fallo por "+name, Toast.LENGTH_LONG).show();
                    }

                    } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                    Toast.makeText(VereventoActivity.this, "Fallo por a", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(VereventoActivity.this, "Fallo por "+e, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

                if (statusCode == 404) {
                    Log.i("On Failure", "404");
                    Toast.makeText(VereventoActivity.this, "Fallo por 404 ", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Log.i("On Failure", "500");
                    Toast.makeText(VereventoActivity.this, "Fallo por 500 ", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Log.i("On Failure", "NN");
                    Toast.makeText(VereventoActivity.this, "Fallo por NN ", Toast.LENGTH_LONG).show();
                }

            }

        });


    }

    // funciones de descarga de imagenes
    private void setImage(Drawable drawable) {

        setBackgroundDrawable(drawable);
    }

    @Deprecated
    public void setBackgroundDrawable(Drawable drawable) {

        img_persona.setBackgroundDrawable(drawable);
    }

    public class DownloadImage extends AsyncTask<String, Integer, Drawable> {
        @Override
        protected Drawable doInBackground(String... arg0) {
            // This is done in a background thread
            return downloadImage(arg0[0]);
        }

        protected void onPostExecute(Drawable image)
        {
            setImage(image);
        }

        private Drawable downloadImage(String _url)
        {
            //Prepare to download image
            URL url;
            BufferedOutputStream out;
            InputStream in;
            BufferedInputStream buf;

            //BufferedInputStream buf;
            try {
                url = new URL(_url);
                in = url.openStream();

                // Read the inputstream
                buf = new BufferedInputStream(in);

                // Convert the BufferedInputStream to a Bitmap
                Bitmap bMap = BitmapFactory.decodeStream(buf);
                if (in != null) {
                    in.close();
                }
                if (buf != null) {
                    buf.close();
                }

                return new BitmapDrawable(bMap);

            } catch (Exception e) {
                Log.e("Error reading file", e.toString());
            }

            return null;
        }
    }

}
