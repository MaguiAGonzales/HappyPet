package adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.happypet.movil.happypet.R;

import java.util.List;

import clases.Evento;
import clases.Mascota;

/**
 * Created by Farly on 18/06/2017.
 */

public class adaptadorEvento extends ArrayAdapter<Evento> {

    public adaptadorEvento(Context context, List<Evento> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View v = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo
            v = inflater.inflate(R.layout.activity_evento_item,parent,false);
        }

        //Obteniendo instancias de los elementos
        TextView titulo = (TextView)v.findViewById(R.id.tvNombreEventoItem);
        TextView descripcion = (TextView)v.findViewById(R.id.tvDescripcion);
        TextView fecha = (TextView)v.findViewById(R.id.tvFecha);
        TextView hora = (TextView)v.findViewById(R.id.tvHora);
        TextView lugar = (TextView)v.findViewById(R.id.tvLugar);
        TextView referencia = (TextView)v.findViewById(R.id.tvReferencia);

        //Obteniendo instancia de la Tarea en la posición actual
        final Evento item = getItem(position);

//        System.out.println("----------- NOMBRE ---------->     " + item.getNombre());
        titulo.setText(item.getTitulo());
        descripcion.setText(item.getDescripcion());
        fecha.setText((CharSequence) item.getFecha());
        hora.setText((CharSequence) item.getHora());
        lugar.setText(item.getLugar());
        referencia.setText(item.getReferencia());


        return v;

    }


}
