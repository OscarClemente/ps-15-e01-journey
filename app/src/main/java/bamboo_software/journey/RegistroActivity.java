package bamboo_software.journey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Clase que se encarga de administrar los datos indroducidos por el usuario,
 * almacenándolos en la BD. En caso de que el registro del usuario se halla
 * realizado correctamente, se redirecciona a la clase MainActivity
 */
public class RegistroActivity extends ActionBarActivity {

    private static final int ACTIVITY_CLIENTE = 1;
    private static final String USUARIO = "CorreoUsuario";

    private EditText mNickText;             // Nick del usuario
    private EditText mPassText;             // Contraseña del usuario
    private EditText mCorreoText;           // Correo del usuario
    private EditText mTelefonoText;         // Teléfono del usuario
    private EditText mNombreText;           // Nombre del usuario
    private EditText mDireccionText;        // Dirección del usuario

    private String correo;
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

        //mTelefonoText.setRawInputType(Configuration.KEYBOARD_QWERTY);
        
        Button botonConfirmar = (Button) findViewById(R.id.confirmar);

        botonConfirmar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                boolean registrado = RegistroActivity.this.registrarUsuario();
                // Si el usuario se ha registrado correctamente, se redirige a MainActivity
                if (registrado) {
                    actualizarPrefsUsuario();

                    Intent i = new Intent(RegistroActivity.this, MainActivity.class);
                    RegistroActivity.this.startActivityForResult(i, ACTIVITY_CLIENTE);
                }
            }

        });
    }

    private void actualizarPrefsUsuario() {
        SharedPreferences prefsCorreo = getSharedPreferences(USUARIO, 0);
        SharedPreferences.Editor editor = prefsCorreo.edit();
        editor.putString("correo", correo);
        editor.commit();
    }

    /*
     * En caso de que mRowNick tenga un valor, rellena los campos del usuario correspondiente
     * con los datos almacenados en la BD
     */
    private void poblarCampos() {
        if (!mRowNick.equals("")) {
            Cursor usuario = dbHelper.listarUsuarioNick(mNickText.getText().toString());
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
        correo = mCorreoText.getText().toString();
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

        if (usuarioCorreo != null && !usuarioCorreo.moveToFirst() && usuarioNick != null &&
                !usuarioNick.moveToFirst() && !correo.equals("") && correo.contains("@") && !nick.equals("") &&
                !nombre.equals("") && !direccion.equals("") && !pass.equals("") && telefono != -1) {
            long resultado = dbHelper.crearUsuario(correo, nick, nombre, direccion, pass, telefono);
            registrado = true;
        }
        else {
            TextView userCorreo = (TextView) RegistroActivity.this.findViewById(R.id.usuarioError);
            TextView userNick = (TextView) RegistroActivity.this.findViewById(R.id.nickError);
            TextView mail = (TextView) RegistroActivity.this.findViewById(R.id.correoError);
            TextView user = (TextView) RegistroActivity.this.findViewById(R.id.nickError);
            TextView name = (TextView) RegistroActivity.this.findViewById(R.id.nombreError);
            TextView addr = (TextView) RegistroActivity.this.findViewById(R.id.direccionError);
            TextView pwd = (TextView) RegistroActivity.this.findViewById(R.id.passError);
            TextView tlf = (TextView) RegistroActivity.this.findViewById(R.id.telefonoError);
            if (usuarioCorreo == null) {
                userCorreo.setVisibility(View.VISIBLE);
                userNick.setVisibility(View.INVISIBLE);
                mail.setVisibility(View.INVISIBLE);
                user.setVisibility(View.INVISIBLE);
                name.setVisibility(View.INVISIBLE);
                addr.setVisibility(View.INVISIBLE);
                pwd.setVisibility(View.INVISIBLE);
                tlf.setVisibility(View.INVISIBLE);
            }
            else if(usuarioNick == null) {
                userCorreo.setVisibility(View.VISIBLE);
                userNick.setVisibility(View.INVISIBLE);
                mail.setVisibility(View.INVISIBLE);
                user.setVisibility(View.INVISIBLE);
                name.setVisibility(View.INVISIBLE);
                addr.setVisibility(View.INVISIBLE);
                pwd.setVisibility(View.INVISIBLE);
                tlf.setVisibility(View.INVISIBLE);
            }
            else if(correo.equals("")) {
                userCorreo.setVisibility(View.INVISIBLE);
                userNick.setVisibility(View.INVISIBLE);
                mail.setVisibility(View.VISIBLE);
                user.setVisibility(View.INVISIBLE);
                name.setVisibility(View.INVISIBLE);
                addr.setVisibility(View.INVISIBLE);
                pwd.setVisibility(View.INVISIBLE);
                tlf.setVisibility(View.INVISIBLE);
            }
            else if(correo.contains("@")) {
                userCorreo.setVisibility(View.INVISIBLE);
                userNick.setVisibility(View.INVISIBLE);
                mail.setVisibility(View.VISIBLE);
                user.setVisibility(View.INVISIBLE);
                name.setVisibility(View.INVISIBLE);
                addr.setVisibility(View.INVISIBLE);
                pwd.setVisibility(View.INVISIBLE);
                tlf.setVisibility(View.INVISIBLE);
            }
            else if(nick.equals("")) {
                userCorreo.setVisibility(View.INVISIBLE);
                userNick.setVisibility(View.INVISIBLE);
                mail.setVisibility(View.INVISIBLE);
                user.setVisibility(View.VISIBLE);
                name.setVisibility(View.INVISIBLE);
                addr.setVisibility(View.INVISIBLE);
                pwd.setVisibility(View.INVISIBLE);
                tlf.setVisibility(View.INVISIBLE);
            }
            else if(nombre.equals("")) {
                userCorreo.setVisibility(View.INVISIBLE);
                userNick.setVisibility(View.INVISIBLE);
                mail.setVisibility(View.INVISIBLE);
                user.setVisibility(View.INVISIBLE);
                name.setVisibility(View.VISIBLE);
                addr.setVisibility(View.INVISIBLE);
                pwd.setVisibility(View.INVISIBLE);
                tlf.setVisibility(View.INVISIBLE);
            }
            else if(direccion.equals("")) {
                userCorreo.setVisibility(View.INVISIBLE);
                userNick.setVisibility(View.INVISIBLE);
                mail.setVisibility(View.INVISIBLE);
                user.setVisibility(View.INVISIBLE);
                name.setVisibility(View.INVISIBLE);
                addr.setVisibility(View.VISIBLE);
                pwd.setVisibility(View.INVISIBLE);
                tlf.setVisibility(View.INVISIBLE);
            }
            else if(pass.equals("")) {
                userCorreo.setVisibility(View.INVISIBLE);
                userNick.setVisibility(View.INVISIBLE);
                mail.setVisibility(View.INVISIBLE);
                user.setVisibility(View.INVISIBLE);
                name.setVisibility(View.INVISIBLE);
                addr.setVisibility(View.INVISIBLE);
                pwd.setVisibility(View.VISIBLE);
                tlf.setVisibility(View.INVISIBLE);
            }
            else if(telefono == -1) {
                userCorreo.setVisibility(View.INVISIBLE);
                userNick.setVisibility(View.INVISIBLE);
                mail.setVisibility(View.INVISIBLE);
                user.setVisibility(View.INVISIBLE);
                name.setVisibility(View.INVISIBLE);
                addr.setVisibility(View.INVISIBLE);
                pwd.setVisibility(View.INVISIBLE);
                tlf.setVisibility(View.VISIBLE);
            }
        }
        dbHelper.close();
        return registrado;
    }

}