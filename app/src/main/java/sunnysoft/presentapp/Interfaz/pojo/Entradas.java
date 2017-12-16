package sunnysoft.presentapp.Interfaz.pojo;

import java.util.List;

/**
 * Created by Chris on 12/3/17.
 */

public class Entradas {

    private String nombre;
    private String fecha;
    private List<String> tags;

    public Entradas(String nombre, String fecha, List<String> tags) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.tags = tags;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFecha() {
        return fecha;
    }



    //public String[] getTags() {
      //  return tags;
    //}

}
