package sunnysoft.presentapp.Interfaz.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sunnysoft.presentapp.Interfaz.DetalleMuralesActivity;
import sunnysoft.presentapp.Interfaz.DetallemailActivity;
import sunnysoft.presentapp.Interfaz.pojo.Correos;
import sunnysoft.presentapp.Interfaz.pojo.Murales;
import sunnysoft.presentapp.R;

/**
 * Created by dchizavo on 8/01/18.
 */

public class CorreosAdapter  extends RecyclerView.Adapter<sunnysoft.presentapp.Interfaz.adapter.CorreosAdapter.ViewHolder> {


        Context context;
        List<Correos> correosList;


        public CorreosAdapter(Context context, List<Correos> correosList) {
            this.context = context;
            this.correosList = correosList;
        }

        @Override
        public sunnysoft.presentapp.Interfaz.adapter.CorreosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_correos, parent, false);
            final sunnysoft.presentapp.Interfaz.adapter.CorreosAdapter.ViewHolder viewHolder = new sunnysoft.presentapp.Interfaz.adapter.CorreosAdapter.ViewHolder(itemView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(sunnysoft.presentapp.Interfaz.adapter.CorreosAdapter.ViewHolder holder, int position) {
            holder.txvNombre.setText(correosList.get(position).getNombre());
            holder.txvFecha.setText(correosList.get(position).getFecha());
            holder.txvasunto.setText(correosList.get(position).getAsunto());
            holder.txvhora.setText(correosList.get(position).getHora());


            Picasso.with(context)
                    .load(correosList.get(position).getImagen_persona())
                    .error(R.drawable.logo)
                    .into(holder.imgPersona);

            holder.cardCorreo.setOnClickListener(new CorreosAdapter.IntemClickListener(correosList.get(position).getUrl_detalle()));

        }





        @Override
        public int getItemCount() {
            return correosList.size();
        }

        class IntemClickListener implements View.OnClickListener{
            String url;

            public IntemClickListener(String url) {
                this.url = url;
            }

            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetallemailActivity.class);
                i.putExtra("servicio",url);
                context.startActivity(i);
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

             private TextView txvFecha;
            private TextView txvNombre;
            private TextView txvasunto;
            private TextView txvhora;
            private ImageView imgPersona;
            private CardView cardCorreo;

            public ViewHolder(View itemView) {
                super(itemView);

                txvFecha = (TextView)itemView.findViewById( R.id.txv_fecha );
                txvNombre = (TextView)itemView.findViewById( R.id.txv_nombre );
                txvasunto = (TextView)itemView.findViewById( R.id.txv_asunto );
                txvhora = (TextView)itemView.findViewById( R.id.txv_hora );
                imgPersona = (ImageView)itemView.findViewById(R.id.img_persona);
                cardCorreo = (CardView) itemView.findViewById(R.id.card_correos);

            }

        }
    }


