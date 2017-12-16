package sunnysoft.presentapp.Datos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;

import cz.msebera.android.httpclient.client.methods.HttpPost;
import sunnysoft.presentapp.Interfaz.CreateentradaActivity;

/**
 * Created by dchizavo on 9/12/17.
 */

public class MyAsyncTask extends AsyncTask<String,Void,String> {

    private ProgressDialog progressDialog;
    private Context context;
    HttpPost httppost;
    String proceso;
    String urlv;


    /**Constructor de clase */
    public MyAsyncTask(Context context, HttpPost httppost, String proceso, String urlv ) {
        this.context = context;
        this.httppost = httppost;
        this.proceso = proceso;
        this.urlv= urlv;

    }


    @Override
    protected String doInBackground(String... params) {

        datosentradas myRestFulGP = new datosentradas();
        String resultado=null;

        try {

            switch (proceso){
                case "Crear Entrada":

                    resultado = myRestFulGP.enviardatos(httppost);
                break;
                default:
                     resultado = null;
                    break;
            }
        }catch (Exception e){
            return null;
        }
        return resultado;

    }

    /**
     * Antes de comenzar la tarea muestra el progressDialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(
                context, "Por favor espere", "Procesando...");
    }

    /**
     * Cuando se termina de ejecutar, cierra el progressDialog y retorna el resultado a la interfaz
     * **/
    @Override
    protected void onPostExecute(String resul) {
        progressDialog.dismiss();
        Intent i = new Intent(context, CreateentradaActivity.class);
        i.putExtra("url", urlv);
        context.startActivity(i);

    }



}
