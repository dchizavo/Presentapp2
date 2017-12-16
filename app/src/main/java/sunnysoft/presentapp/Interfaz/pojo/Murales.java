package sunnysoft.presentapp.Interfaz.pojo;

import java.security.PublicKey;
import java.util.List;

/**
 * Created by gustavo on 12/11/17.
 */

public class Murales {

    private String nombre;
    private String fecha;
    private String contenido;
    private String imagen_persona;
    private  String readmore;
    private Boolean adjuntos;
    private Boolean adjutos_imagen;





    public Murales(String nombre, String fecha, String contenido, String readmore, String imagen_persona, Boolean adjuntos, Boolean adjutos_imagen) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.contenido = contenido;
        this.imagen_persona = imagen_persona;
        this.adjuntos = adjuntos;
        this.adjutos_imagen = adjutos_imagen;
        this.readmore = readmore;

    }
    public String getImagen_persona() {
        return imagen_persona;
    }

    public void setImagen_persona(String imagen_persona) {
        this.imagen_persona = imagen_persona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getreadmore() {
        return readmore;
    }

    public void setreadmore(String readmore) {
        this.readmore = readmore;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Boolean isAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(Boolean adjuntos) {
        this.adjuntos = adjuntos;
    }

    public Boolean isAdjutos_imagen() {
        return adjutos_imagen;
    }

    public void setAdjutos_imagen(Boolean adjutos_imagen) {
        this.adjutos_imagen = adjutos_imagen;
    }







}
