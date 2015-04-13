package bamboo_software.journey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


/**
 *
 */
public class AdaptadorPaquetes {

    public static final String KEY_NOMBRE = "nombre";
    public static final String KEY_DESTINO = "destino";
    public static final String KEY_PRECIO = "precio";
    public static final String KEY_DURACION = "duracion";
    public static final String KEY_CALIFICACION = "calificacion";
    public static final String KEY_DESCRIPCION = "descripcion";
    public static final String KEY_IMAGEN = "imagen";
    public static final String KEY_ROWID = "_id";


    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;




    private static final String DATABASE_TABLE = "paquete";


    private final Context mCtx;


    /**
     * Constructor
     *
     * @param ctx el contexto con el que trabajar
     */
    public AdaptadorPaquetes(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Abre la base de datos. Si no la puede abrir, intenta crear una nueva instancia de la base de datos.
     * Si no la puede crear lanza una excepcion de fallo.
     *
     * @return this auto referencia
     * @throws SQLException si la base de datos no puede ser ni creada ni abierta
     */
    public AdaptadorPaquetes open() throws SQLException {
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
    public long crearPaquete(String nombre, String destino, int precio, int duracion,
                             int calificacion, String descripcion, String imagen) {
        try {
            if (nombre.equals("")) {
                return -1;
            }

            ContentValues valoresIniciales = new ContentValues();
            valoresIniciales.put(KEY_NOMBRE, nombre);
            valoresIniciales.put(KEY_DESTINO, destino);
            valoresIniciales.put(KEY_PRECIO, precio);
            valoresIniciales.put(KEY_DURACION, duracion);
            valoresIniciales.put(KEY_CALIFICACION, calificacion);
            valoresIniciales.put(KEY_DESCRIPCION, descripcion);
            valoresIniciales.put(KEY_IMAGEN, imagen);

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
     * Devuelve un Cursor sobre la lista de todos los paquetes de la base de datos
     *
     * @return Cursor sobre todos los paquetes
     */
    public Cursor listarPaquetes() {

        return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NOMBRE, KEY_DESTINO,
                        KEY_PRECIO, KEY_DURACION, KEY_CALIFICACION, KEY_DESCRIPCION, KEY_IMAGEN},
                null, null, null, null, KEY_NOMBRE);
    }

    /**
     * Devuelve un Cursor colocado en el paquete que coincide con la rowId dada
     *
     * @param rowId id del paquete a recuperar
     * @return Cursor colocado en el paquete deseado si esta
     * @throws SQLException si no se puede encontrar
     */
    public Cursor listarPaquete(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NOMBRE, KEY_NOMBRE,
                                KEY_PRECIO, KEY_DURACION, KEY_CALIFICACION,
                                KEY_DESCRIPCION, KEY_IMAGEN},
                        KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Actualiza el paquete con los detalles proporcionados.
     * El paquete a actualizar se especifica mediante la rowId
     *
     * @param nombre       el nombre del paquete
     * @param precio       el precio del paquete
     * @param duracion     la duracion del paquete
     * @param calificacion la calificacion del paquete
     * @param descripcion  la descripcion del paquete
     * @return rowId o -1 si falla
     */
    public boolean actualizarPaquete(long rowId, String nombre, String destino, int precio, int duracion, int calificacion,
                                     String descripcion, String imagen) {
        try {
            if (nombre.equals("")) {
                return false;
            }

            ContentValues args = new ContentValues();
            args.put(KEY_NOMBRE, nombre);
            args.put(KEY_DESTINO, destino);
            args.put(KEY_PRECIO, precio);
            args.put(KEY_DURACION, duracion);
            args.put(KEY_CALIFICACION, calificacion);
            args.put(KEY_DESCRIPCION, descripcion);
            args.put(KEY_IMAGEN, imagen);

            return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
        } catch (Throwable e) {
            return false;
        }
    }


    /*
     * Devuelve un ArrayList de Paquete por cada elemento que hay
     * en el cursor dado.
     *
     * @param cur  Cursor de una consulta
     * @return ArrayList
     */
    public ArrayList<Paquete> cursorAArray(Cursor cur) {
        ArrayList<Paquete> mArrayList = new ArrayList<>();
        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
            // The Cursor is now set to the right position
            mArrayList.add(new Paquete(
                            cur.getLong(cur.getColumnIndex(KEY_ROWID)),
                            cur.getString(cur.getColumnIndex(KEY_NOMBRE)),
                            cur.getString(cur.getColumnIndex(KEY_DESTINO)),
                            cur.getInt(cur.getColumnIndex(KEY_PRECIO)),
                            cur.getInt(cur.getColumnIndex(KEY_DURACION)),
                            cur.getInt(cur.getColumnIndex(KEY_CALIFICACION)),
                            cur.getString(cur.getColumnIndex(KEY_DESCRIPCION)),
                            cur.getString(cur.getColumnIndex(KEY_IMAGEN))
                    )
            );
        }
        return mArrayList;
    }
}
