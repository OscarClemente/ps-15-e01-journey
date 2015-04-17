package bamboo_software.journey;

/**
 * Created by Jorge on 08/04/2015.
 */
public class Paquete {

    private long id;
    private String nombre;
    private String destino;
    private int precio;
    private int duracion;
    private int calificacion;
    private String descripcion;
    private int imagen;


    public Paquete(long id, String nombre, String destino, int precio, int duracion,
                   int calificacion, String descripcion, int imagen) {
        this.id = id;
        this.nombre = nombre;
        this.destino = destino;
        this.precio = precio;
        this.duracion = duracion;
        this.calificacion = calificacion;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public long getId() {
        return id;
    }

    public String getDestino() { return destino; }

    public void setDestino(String destino) { this.destino = destino; }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
