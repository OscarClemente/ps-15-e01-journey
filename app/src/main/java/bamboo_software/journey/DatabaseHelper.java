package bamboo_software.journey;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {


    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table paquete (_id integer primary key autoincrement, "
                    + "nombre text not null, destino text not null,"
                    + "precio integer not null,"
                    + "duracion integer not null, calificacion integer not null,"
                    + "descripcion text not null, imagen text not null);";

    private static final String DATABASE_CREATE2 =
            "create table comprar (id integer not null, "
                    + "correo text not null, fecha text not null,"
                    + "FOREIGN KEY(id) REFERENCES paquete(_id),"
                    + "FOREIGN KEY(correo) REFERENCES usuario(correo),"
                    + "PRIMARY KEY(id, correo)) WITHOUT ROWID;";

    private static final String DATABASE_CREATE3 =
            "create table usuario (correo text primary key not null, "
                    + "nick text not null, nombre text not null, direccion text not null,"
                    + "pass text not null, telefono integer) WITHOUT ROWID; ";

    private static final String TAG = "AdaptadorBD";
    private static final String DATABASE_NAME = "journey";
    private static final int DATABASE_VERSION = 1;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE3);
        db.execSQL(DATABASE_CREATE2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Actualizar la base de datos desde la version " + oldVersion + " a la version "
                + newVersion + "destruira todos los datos");
        db.execSQL("DROP TABLE IF EXISTS compras");
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL("DROP TABLE IF EXISTS paquete");
        onCreate(db);
    }
}