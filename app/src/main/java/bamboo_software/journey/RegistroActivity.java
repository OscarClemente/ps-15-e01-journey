package bamboo_software.journey;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistroActivity extends ActionBarActivity {

    private static final int ACTIVITY_CLIENTE = 1;

    private EditText mNickText;
    private EditText mPassText;
    private EditText mCorreoText;
    private EditText mTelefonoText;
    private EditText mNombreText;
    private EditText mDireccionText;

    private String mRowCorreo;
    private String mRowNick;
    private AdaptadorUsuarios dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registro);
        setTitle(R.string.registro);

        mNickText = (EditText) findViewById(R.id.nick);
        mPassText = (EditText) findViewById(R.id.pass);
        mCorreoText = (EditText) findViewById(R.id.correo);
        mNombreText = (EditText) findViewById(R.id.nombre);
        mDireccionText = (EditText) findViewById(R.id.direccion);
        mTelefonoText = (EditText) findViewById(R.id.telefono);
        
        Button botonConfirmar = (Button) findViewById(R.id.confirmar);

        /* mRowNick = (savedInstanceState == null) ? "" :
                (String) savedInstanceState.getSerializable(AdaptadorUsuarios.KEY_NICK);
        if (mRowNick.equals("")) {
            Bundle extras = getIntent().getExtras();
                mRowNick = extras != null ? extras.getString(AdaptadorUsuarios.KEY_NICK)
                    : "";
        }

        poblarCampos(); */

        botonConfirmar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                boolean registrado = RegistroActivity.this.registrarUsuario();
                // Si el usuario se ha registrado correctamente, se redirije a MainActivity
                if (registrado) {
                    Intent i = new Intent(RegistroActivity.this, MainActivity.class);
                    RegistroActivity.this.startActivityForResult(i, ACTIVITY_CLIENTE);
                }
            }

        });
    }

    /*
     * En caso de que mRowNick tenga un valor, rellena los campos del usuario correspondiente
     * con los datos almacenados en la BD
     */
    private void poblarCampos() {
        if (!mRowNick.equals("")) {
            Cursor usuario = dbHelper.listarUsuarioNick(mNickText.getText().toString());
            //startManagingCursor(usuario);
            mNickText.setText(usuario.getString(
            		usuario.getColumnIndexOrThrow(AdaptadorUsuarios.KEY_NICK)));
            mPassText.setText(usuario.getString(
            		usuario.getColumnIndexOrThrow(AdaptadorUsuarios.KEY_PASS)));
            mCorreoText.setText(usuario.getString(
                    usuario.getColumnIndexOrThrow(AdaptadorUsuarios.KEY_CORREO)));
            mNombreText.setText(usuario.getString(
                    usuario.getColumnIndexOrThrow(AdaptadorUsuarios.KEY_NOMBRE)));
            mDireccionText.setText(usuario.getString(
                    usuario.getColumnIndexOrThrow(AdaptadorUsuarios.KEY_DIRECCION)));
            mTelefonoText.setText(usuario.getString(
            		usuario.getColumnIndexOrThrow(AdaptadorUsuarios.KEY_TELEFONO)));
            mRowCorreo = usuario.getString(
                    usuario.getColumnIndexOrThrow(AdaptadorUsuarios.KEY_CORREO));
        }
       
    }

    /*
     * Comprueba que los datos introducidos no son nulos o tienen un valor válido.
     * Posteriormente verifica si el correo y el nick introducidos no han sido utilizados
     * anteriormente en otra cuenta. En caso afirmativo, inserta los datos del nuevo usuario
     * en la base de datos
     */
    public boolean registrarUsuario() {
        dbHelper = new AdaptadorUsuarios(this);
        dbHelper.open();

        String nick = mNickText.getText().toString();
        String pass = mPassText.getText().toString();
        String correo = mCorreoText.getText().toString();
        String nombre = mNombreText.getText().toString();
        String direccion = mDireccionText.getText().toString();
        int telefono;
        try {
            telefono = Integer.parseInt(mTelefonoText.getText().toString());
        } catch (NumberFormatException e) {
            telefono = -1;
        }

        boolean registrado = false;

        Cursor usuarioCorreo = dbHelper.listarUsuario(correo);
        Cursor usuarioNick = dbHelper.listarUsuarioNick(nick);

        /* if (mRowNick.equals("")) { */
            if (!correo.equals("") && correo.contains("@") && !nick.equals("") && !nombre.equals("") &&
                    !direccion.equals("") && !pass.equals("") && telefono != -1) {
                long resultado = dbHelper.crearUsuario(correo, nick, nombre, direccion, pass, telefono);
                registrado = true;
                /*if (resultado > 0) {
                    mRowNick = nick;
                }*/
            }
        /* }
        else {
            if (!mRowCorreo.equals(correo) || !mRowNick.equals(nick)) {
                if (usuarioCorreo == null && usuarioNick == null) {
                    dbHelper.actualizarUsuario(correo, nick, nombre, direccion, pass, telefono);
                    mRowCorreo = correo;
                }
            }
            else {
                dbHelper.actualizarUsuario(correo, nick, nombre, direccion, pass, telefono);
            }
        } */
        dbHelper.close();
        return registrado;
    }

}
