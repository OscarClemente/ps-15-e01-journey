package bamboo_software.journey;

/**
 * Created by Oscar on 5/04/15.
 *
 * --FICHERO TEMPORAL A SER ELIMINADO--
 *
 * Crea una estructura para los cardViews con la que poder testear la interfaz
 */
public class CardData {

    String name;
    String info;
    String image;
    long id_;

    public CardData(String name, String info, String image,  long id_) {
        this.name = name;
        this.info = info;
        this.image = image;
        this.id_ = id_;
    }


    public String getName() {
        return name;
    }


    public String getInfo() {
        return info;
    }


    public String getImage() {
        return image;
    }

    public long getId() {
        return id_;
    }
}
