package bamboo_software.journey;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by Jorge on 07/04/2015.
 */
public class Prueba {

    public void main(Context c) {
        AdaptadorPaquetes a = new AdaptadorPaquetes(c);
        a.open();
        long id = a.crearPaquete("islas malvinas", 1000, 10, 5, "mu bonitooooo");
        long id2 = a.crearPaquete("islas CANARIAS", 1000, 10, 5, "muCHISMO bonitooooo");
        long id3 = a.crearPaquete("egipto enigmatico", 1000, 10, 5, "mu bonitooooo");
        System.out.printf("holaaaaa");
        Cursor cur = a.listarPaquetes();

        ArrayList<Paquete> mArrayList = new ArrayList<>();
        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
            // The Cursor is now set to the right position
            mArrayList.add(new Paquete(
                            cur.getLong(cur.getColumnIndex(a.KEY_ROWID)),
                            cur.getString(cur.getColumnIndex(a.KEY_NOMBRE)),
                            cur.getInt(cur.getColumnIndex(a.KEY_PRECIO)),
                            cur.getInt(cur.getColumnIndex(a.KEY_DURACION)),
                            cur.getInt(cur.getColumnIndex(a.KEY_CALIFICACION)),
                            cur.getString(cur.getColumnIndex(a.KEY_DESCRIPCION))
                    )
            );
        }
        System.out.printf("holaaaaa");
    }

}
