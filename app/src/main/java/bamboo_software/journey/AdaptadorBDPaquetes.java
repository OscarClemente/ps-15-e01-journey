package bamboo_software.journey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *
 */
public class AdaptadorBDPaquetes {

    public static final String KEY_NOMBRE = "nombre";
    public static final String KEY_PRECIO = "precio";
    public static final String KEY_DURACION = "duracion";
    public static final String KEY_CALIFICACION = "calificacion";
    public static final String KEY_DESCRIPCION = "descripcion";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "AdaptadorBDPaquetes";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table paquete (_id integer primary key autoincrement, "
                    + "nombre text not null, precio integer not null,"
                    + "duracion integer not null, calificacion integer"
                    + "descripcion text not null);";

    private static final String DATABASE_CREATE2 =
            "create table comprar (_id integer not null, "
                    + "correo text not null, fecha text not null,"
                    + "FOREIGN KEY(_id) REFERENCES paquete(_id),"
                    + "FOREIGN KEY(trackartist) REFERENCES usuario(correo),"
                    + "PRIMARY KEY(_id, correo));";

    private static final String DATABASE_CREATE3 =
            "create table usuario (correo text primary key not null, "
                    + "nick text not null, pass text not null, telefono integer); ";


    private static final String DATABASE_NAME = "journey";
    private static final String DATABASE_TABLE = "paquete";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    public static class DatabaseHelper extends SQLiteOpenHelper {

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
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            db.execSQL("DROP TABLE IF EXISTS categories");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public AdaptadorBDPaquetes(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     * initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public AdaptadorBDPaquetes open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Crea un nuevo paquete usando el nombre, precio, duración, calificación, descripción
     * proporcionados. Si el paquete se crea con exito devuelve la rowId de ese paquete,
     * de otra manera se devuelve -1 para indicar un fallo.
     *
     * @param nombre       el nombre del paquete
     * @param precio       el precio del paquete
     * @param duracion     la duracion del paquete
     * @param calificacion la calificacion del paquete
     * @param descripcion  la descripcion del paquete
     * @return rowId o -1 si falla
     */
    public long crearPaquete(String nombre, int precio, int duracion, int calificacion,
                             String descripcion) {
        try {
            if (nombre == "") {
                return -1;
            }

            ContentValues valoresIniciales = new ContentValues();
            valoresIniciales.put(KEY_NOMBRE, nombre);
            valoresIniciales.put(KEY_PRECIO, precio);
            valoresIniciales.put(KEY_DURACION, duracion);
            valoresIniciales.put(KEY_CALIFICACION, calificacion);
            valoresIniciales.put(KEY_DESCRIPCION, descripcion);

            return mDb.insert(DATABASE_TABLE, null, valoresIniciales);
        } catch (Throwable e) {
            return -1;
        }
    }

    /**
     * Borrar el paquete de la id dada
     *
     * @param rowId id del paquete
     * @return true si se ha borrado, false de otra manera
     */
    public boolean borrarPaquete(long rowId) {
        try {
            return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * Return a Cursor over the list of all notes in the database
     *
     * @return Cursor over all notes
     */
    public Cursor listarPaquetes() {

        return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NOMBRE,
                        KEY_PRECIO, KEY_DURACION, KEY_CALIFICACION, KEY_DESCRIPCION},
                null, null, null, null, KEY_NOMBRE);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     *
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor listarPaquete(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NOMBRE,
                                KEY_PRECIO, KEY_DURACION, KEY_CALIFICACION, KEY_DESCRIPCION},
                        KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     *
     * @param nombre       el nombre del paquete
     * @param precio       el precio del paquete
     * @param duracion     la duracion del paquete
     * @param calificacion la calificacion del paquete
     * @param descripcion  la descripcion del paquete
     * @return rowId o -1 si falla
     */
    public boolean actualizarPaquete(long rowId, String nombre, int precio, int duracion, int calificacion,
                                     String descripcion) {
        try {
            if (nombre == "") {
                return false;
            }

            ContentValues args = new ContentValues();
            args.put(KEY_NOMBRE, nombre);
            args.put(KEY_PRECIO, precio);
            args.put(KEY_DURACION, duracion);
            args.put(KEY_CALIFICACION, calificacion);
            args.put(KEY_DESCRIPCION, descripcion);

            return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
        } catch (Throwable e) {
            return false;
        }
    }
}
