package bamboo_software.journey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Jorge on 07/04/2015.
 */
public class AdaptadorCompras {

    public static final String KEY_ID = "id";
    public static final String KEY_NOMBRE = "nombre";
    public static final String KEY_CORREO = "correo";
    public static final String KEY_FECHA = "fecha";
    public static final String KEY_PERSONAS = "personas";


    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;


    private static final String DATABASE_TABLE = "comprar";

    private final Context mCtx;


    /**
     * Constructor
     *
     * @param ctx el contexto con el que trabajar
     */
    public AdaptadorCompras(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Abre la base de datos. Si no la puede abrir, intenta crear una nueva instancia de la base de datos.
     * Si no la puede crear lanza una excepcion de fallo.
     *
     * @return this auto referencia
     * @throws android.database.SQLException si la base de datos no puede ser ni creada ni abierta
     */
    public AdaptadorCompras open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Crea una nueva compra usando el id, correo y fecha
     * proporcionados. Si la compra se crea con exito devuelve la rowId de esa compra,
     * de otra manera se devuelve -1 para indicar un fallo.
     *
     * @param correo el correo del usuario
     * @param id     el id del paquete
     * @param fecha  la fecha de compra
     * @return rowId o -1 si falla
     */
    public long crearCompra(long id, String nombre, String correo, String fecha, int personas) {
        try {
            if (correo.equals("")) {
                return -1;
            }

            ContentValues valoresIniciales = new ContentValues();
            valoresIniciales.put(KEY_ID, id);
            valoresIniciales.put(KEY_NOMBRE, nombre);
            valoresIniciales.put(KEY_CORREO, correo);
            valoresIniciales.put(KEY_FECHA, fecha);
            valoresIniciales.put(KEY_PERSONAS, personas);

            return mDb.insert(DATABASE_TABLE, null, valoresIniciales);
        } catch (Throwable e) {
            return -1;
        }
    }

    /**
     * Borrar la compra de la id y correo dados
     *
     * @param id id del usuario
     * @return true si se ha borrado, false de otra manera
     */
    public boolean borrarCompra(long id, String correo) {
        try {
            return mDb.delete(DATABASE_TABLE, KEY_ID + "=" + id + "AND" +
                    KEY_CORREO + "=" + correo, null) > 0;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * Devuelve un Cursor sobre la lista de todas las compras de la base de datos
     *
     * @return Cursor sobre todas las compras
     */
    public Cursor listarCompras() {

        return mDb.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_NOMBRE, KEY_CORREO,
                KEY_FECHA, KEY_PERSONAS}, null, null, null, null, KEY_ID);
    }

    /**
     * Devuelve un Cursor colocado en la compra que coincide con la id y correo dados
     *
     * @param correo correo del usuario de la compra a recuperar
     * @return Cursor colocado en la compra deseada si esta
     * @throws SQLException si no se puede encontrar
     */
    public Cursor listarCompra(String correo) throws SQLException {

        Cursor mCursor =

                mDb.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_NOMBRE, KEY_CORREO,
                                KEY_FECHA, KEY_PERSONAS}, KEY_CORREO + "=" + correo,
                        null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Actualiza la compra con los detalles proporcionados.
     * La compra a actualizar se especifica mediante la id y el correo
     *
     * @param correo el correo del usuario
     * @param id     el id del paquete
     * @param fecha  la fecha de compra
     * @return rowId o -1 si falla
     */
    public boolean actualizarUsuario(long id, String nombre, String correo, String fecha, int personas) {
        try {
            if (correo.equals("")) {
                return false;
            }

            ContentValues args = new ContentValues();
            args.put(KEY_ID, id);
            args.put(KEY_NOMBRE, nombre);
            args.put(KEY_CORREO, correo);
            args.put(KEY_FECHA, fecha);
            args.put(KEY_PERSONAS, personas);

            return mDb.update(DATABASE_TABLE, args,
                    KEY_ID + "=" + id + "AND" + KEY_CORREO + "=" + correo, null) > 0;
        } catch (Throwable e) {
            return false;
        }
    }
}
