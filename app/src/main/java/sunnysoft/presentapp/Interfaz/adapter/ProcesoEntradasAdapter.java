package sunnysoft.presentapp.Interfaz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import me.gujun.android.taggroup.TagGroup;
import sunnysoft.presentapp.Interfaz.pojo.Entradas;
import sunnysoft.presentapp.R;

/**
 * Created by esantopc on 19/12/17.
 */

public class ProcesoEntradasAdapter extends ArrayAdapter<Entradas> {

    Context context;

    public ProcesoEntradasAdapter(Context context, List<Entradas> objects) {

        super(context, 0, objects);
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;

        // ¿Ya se infló este view?
        if (null == convertView) {
            //Si no existe, entonces inflarlo con image_list_view.xml
            convertView = inflater.inflate(
                    R.layout.list_items_verentradas,
                    parent,
                    false);

            holder = new ViewHolder();
            holder.titulo = (TextView) convertView.findViewById(R.id.tituloverentradas);
            holder.detalle = (TextView) convertView.findViewById(R.id.detalleverentradas);
            holder.imgPersona = (ImageView)convertView.findViewById(R.id.img_persona);
            holder.mTagGroup = (TagGroup)convertView.findViewById(R.id.tag_group_entradas);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }

        // Actual.
        Entradas entrada = getItem(position);
        // Setup.

        holder.titulo.setText(entrada.getNombre());
        holder.detalle.setText(entrada.getDetalle());

        Picasso.with(context)
                .load(entrada.getImage_persona())
                .error(R.drawable.logo)
                .into(holder.imgPersona);

        holder.mTagGroup.setTags(entrada.getNomtags());

        return convertView;
    }

    static class ViewHolder {

        TagGroup mTagGroup;
        TextView titulo;
        TextView detalle;
        ImageView imgPersona;
    }
}
