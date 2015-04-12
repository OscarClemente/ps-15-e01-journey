package bamboo_software.journey;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by Jorge on 07/04/2015.
 */
public class Prueba {

    public void main(Context c) {
        AdaptadorPaquetes db = new AdaptadorPaquetes(c);
        db.open();
        long id4 = db.crearPaquete("Malvinas de ensueño", "Malvinas", 1000, 10, 3, "Las islas Malvinas5 (en inglés: Falkland Islands, AFI: [ˈfɔːlklənd ˈaɪləndz]; del francés îles Malouines, nombre dado por el explorador Bougainville) es un archipiélago situado en la plataforma continental de América del Sur, dentro del sector de mar epicontinental del océano Atlántico Sur que Argentina denomina mar Argentino. La menor distancia de las islas Malvinas al continente americano se encuentra entre la roca Mintay al sudoeste de la isla Beauchene y el cabo San Juan en la isla de los Estados, a 356,4 km. La principal localidad de las islas Malvinas es Puerto Argentino (Stanley en inglés), ésta está ubicada en la costa oriental de la isla Soledad.");
        long id2 = db.crearPaquete("Teide Enigmático", "Tenerife", 300, 10, 3, "El Teide es un volcán situado en la isla de Tenerife (Islas Canarias, España). Con una altitud de 3718 metros sobre el nivel del mar y 7500 metros sobre el lecho oceánico, es el pico más alto de España, el de cualquier tierra emergida del océano Atlántico y el tercer mayor volcán de la Tierra desde su base en el lecho oceánico, después del Mauna Kea y el Mauna Loa, ambos en la isla de Hawái.1 La altitud del Teide convierte además a la isla de Tenerife en la décima isla más alta de todo el mundo.");
        long id3 = db.crearPaquete("Piramides Faraónicas", "Egipto", 9900, 10, 3, "Las pirámides de Egipto son, de todos los vestigios legados por egipcios de la Antigüedad, los más portentosos y emblemáticos monumentos de esta civilización, y en particular, las tres grandes pirámides de Guiza, las tumbas o cenotafios de los faraones Keops, Kefrén y Micerino, cuya construcción se remonta, para la gran mayoría de estudiosos, al periodo denominado Imperio Antiguo de Egipto. La Gran Pirámide de Guiza, construida por Keops (Jufu), es una de las Siete Maravillas del Mundo Antiguo, además de ser la única que aún perdura.");
        System.out.printf("holaaaaa");
        Cursor cur = db.listarPaquetes();

        ArrayList<Paquete> mArrayList = new ArrayList<>();
        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
            // The Cursor is now set to the right position
            mArrayList.add(new Paquete(
                            cur.getLong(cur.getColumnIndex(db.KEY_ROWID)),
                            cur.getString(cur.getColumnIndex(db.KEY_NOMBRE)),
                            cur.getString(cur.getColumnIndex(db.KEY_DESTINO)),
                            cur.getInt(cur.getColumnIndex(db.KEY_PRECIO)),
                            cur.getInt(cur.getColumnIndex(db.KEY_DURACION)),
                            cur.getInt(cur.getColumnIndex(db.KEY_CALIFICACION)),
                            cur.getString(cur.getColumnIndex(db.KEY_DESCRIPCION))
                    )
            );
        }
        System.out.printf("holaaaaa");
    }

}
