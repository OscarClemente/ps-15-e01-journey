package bamboo_software.journey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 *
 */
public class AdaptadorUsuarios {

    public static final String KEY_CORREO = "correo";
    public static final String KEY_NICK = "nick";
    public static final String KEY_PASS = "pass";
    public static final String KEY_TELEFONO = "telefono";
    public static final String KEY_NOMBRE = "nombre";
    public static final String KEY_DIRECCION = "direccion";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;


    private static final String DATABASE_TABLE = "usuario";

    private final Context mCtx;


    /**
     * Constructor
     *
     * @param ctx el contexto con el que trabajar
     */
    public AdaptadorUsuarios(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Abre la base de datos. Si no la puede abrir, intenta crear una nueva instancia de la base de datos.
     * Si no la puede crear lanza una excepcion de fallo.
     *
     * @return this auto referencia
     * @throws SQLException si la base de datos no puede ser ni creada ni abierta
     */
    public AdaptadorUsuarios open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Crea un nuevo usuario usando el correo, nick, nombre, direccion, pass, y telefono
     * proporcionados. Si el usuario se crea con exito devuelve la rowId de ese usuario,
     * de otra manera se devuelve -1 para indicar un fallo.
     *
     * @param correo       el correo del usuario
     * @param nick         el nick del usuario
     * @param nombre       el nombre del usuario
     * @param direccion    la direccion del usuario
     * @param pass         la pass del usuario
     * @param telefono     el telefono del usuario
     * @return rowId o -1 si falla
     */
    public long crearUsuario(String correo, String nick, String nombre, String direccion,
                             String pass, int telefono) {
        try {
            if (correo.equals("")) {
                return -1;
            }
            ContentValues valoresIniciales = new ContentValues();
            valoresIniciales.put(KEY_CORREO, correo);
            valoresIniciales.put(KEY_NICK, nick);
            valoresIniciales.put(KEY_NOMBRE, nombre);
            valoresIniciales.put(KEY_DIRECCION, direccion);
            valoresIniciales.put(KEY_PASS, pass);
            valoresIniciales.put(KEY_TELEFONO, telefono);

            return mDb.insert(DATABASE_TABLE, null, valoresIniciales);
        } catch (Throwable e) {
            return -1;
        }
    }

    /**
     * Borrar el usuario del correo dado
     *
     * @param correo correo del usuario
     * @return true si se ha borrado, false de otra manera
     */
    public boolean borrarUsuario(String correo) {
        try {
            return mDb.delete(DATABASE_TABLE, KEY_CORREO + "=" + correo, null) > 0;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * Devuelve un Cursor sobre la lista de todos los usuario de la base de datos
     *
     * @return Cursor sobre todos los usuarios
     */
    public Cursor listarUsuarios() {

        return mDb.query(DATABASE_TABLE, new String[]{KEY_CORREO, KEY_NOMBRE, KEY_DIRECCION,
                        KEY_NICK, KEY_PASS, KEY_TELEFONO},
                        null, null, null, null, KEY_NICK);
    }

    /**
     * Devuelve un Cursor colocado en el usuario que coincide con la rowId dada
     *
     * @param correo correo del usuario a recuperar
     * @return Cursor colocado en el usuario deseado si esta
     * @throws SQLException si no se puede encontrar
     */
    public Cursor listarUsuario(String correo ) throws SQLException {

        Cursor mCursor =

                mDb.query(DATABASE_TABLE, new String[]{KEY_CORREO, KEY_NOMBRE,
                                KEY_DIRECCION, KEY_NICK, KEY_PASS, KEY_TELEFONO},
                        KEY_CORREO + "='" + correo + "'", null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Devuelve un Cursor colocado en el usuario que coincide con la rowId dada
     *
     * @return Cursor colocado en el usuario deseado si esta
     * @throws SQLException si no se puede encontrar
     */
    public Cursor listarUsuarioNick(String nick) throws SQLException {

        Cursor mCursor =

                mDb.query(DATABASE_TABLE, new String[]{KEY_CORREO, KEY_NOMBRE,
                                KEY_DIRECCION, KEY_NICK, KEY_PASS, KEY_TELEFONO},
                        KEY_NICK + "='" + nick + "'", null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Actualiza el usuario con los detalles proporcionados.
     * El usuario a actualizar se especifica mediante la rowId
     *
     * @param correo       el correo del usuario
     * @param nick       el nick del usuario
     * @param pass     la pass del usuario
     * @param telefono la telefono del usuario
     * @return rowId o -1 si falla
     */
    public boolean actualizarUsuario(String correo, String nick, String nombre, String direccion,
                                     String pass, int telefono) {
        try {
            if (correo.equals("")) {
                return false;
            }

            ContentValues args = new ContentValues();
            args.put(KEY_CORREO, correo);
            args.put(KEY_NICK, nick);
            args.put(KEY_NOMBRE, nombre);
            args.put(KEY_DIRECCION, direccion);
            args.put(KEY_PASS, pass);
            args.put(KEY_TELEFONO, telefono);

            return mDb.update(DATABASE_TABLE, args, KEY_CORREO + "=" + correo, null) > 0;
        } catch (Throwable e) {
            return false;
        }
    }
}
