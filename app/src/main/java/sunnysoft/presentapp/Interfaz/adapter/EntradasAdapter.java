package sunnysoft.presentapp.Interfaz.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import sunnysoft.presentapp.Interfaz.pojo.Entradas;
import sunnysoft.presentapp.Interfaz.pojo.Murales;
import sunnysoft.presentapp.R;

/**
 * Created by dchizavo on 12/12/17.
 */

public class EntradasAdapter extends RecyclerView.Adapter<EntradasAdapter.ViewHolder> {

    Context context;
    List<Entradas>entradasList;

    public EntradasAdapter(Context context, List<Entradas> entradasList) {
        this.context = context;
        this.entradasList = entradasList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entradas, parent, false);
        final ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.txvNombre.setText(muralesList.get(position).getNombre());
        //holder.txvFecha.setText(muralesList.get(position).getFecha());
        //holder.web.getSettings().setJavaScriptEnabled(true);
        //holder.web.loadDataWithBaseURL(null,muralesList.get(position).getContenido(),"text/html","utf-8",null);
        //Log.i("Adapter",muralesList.get(position).getImagen_persona());

        /*Picasso.with(context)
                .load(muralesList.get(position).getImagen_persona())
                .error(R.drawable.logo)
                .into(holder.imgPersona);*/

        /*if (muralesList.get(position).isAdjuntos()){
            holder.contAdjuntos.setVisibility(View.VISIBLE);
        }else{
            holder.contAdjuntos.setVisibility(View.GONE);
        }*/

        /*if (muralesList.get(position).isAdjutos_imagen()){
            holder.contImgAdjuntos.setVisibility(View.VISIBLE);
        }else{
            holder.contImgAdjuntos.setVisibility(View.GONE);
        }*/

    }

    @Override
    public int getItemCount() {
        return entradasList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout contImgAdjuntos;
        private ImageView adjImg3;
        private ImageView adjImg2;
        private ImageView adjImg1;
        private ConstraintLayout contAdjuntos;
        private Button adjunto5;
        private Button adjunto6;
        private Button adjunto4;
        private Button adjunto3;
        private Button adjunto2;
        private Button adjunto1;
        private WebView web;
        private TextView txvFecha;
        private TextView txvNombre;
        private ImageView imgPersona;

        public ViewHolder(View itemView) {
            super(itemView);

            web = (WebView)itemView.findViewById(R.id.web_contenido);
            txvFecha = (TextView)itemView.findViewById( R.id.txv_fecha );
            txvNombre = (TextView)itemView.findViewById( R.id.txv_nombre );
            contAdjuntos = (ConstraintLayout)itemView.findViewById( R.id.cont_adjuntos );
            contImgAdjuntos = (ConstraintLayout)itemView.findViewById( R.id.cont_img_adjuntos );
            imgPersona = (ImageView)itemView.findViewById(R.id.img_persona);
        }

    }
}
