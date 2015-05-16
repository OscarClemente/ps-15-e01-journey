package bamboo_software.journey;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Clase que se encarga de identificar si los datos indroducidos por el usuario se
 * corresponden con los almacenados en la BD. Si coinciden, te redirecciona a la clase
 * InicioPaquetes si el usuario es admin y a MainActivity en el resto de los casos.
 * Por otro lado, también te puede redireccionar a RegistroActivity si el usuario no
 * está registrado
 */
public class InicioActivity extends Activity {

    private static final int ACTIVITY_ADMIN = 0;
    private static final int ACTIVITY_CLIENTE = 1;
    private static final String USUARIO = "CorreoUsuario";

    private EditText mNickText;         // Nick del usuario
    private EditText mPassText;         // Contraseña del usuario

    private String mRowNick;
    private AdaptadorUsuarios dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inicio);
        //setTitle(R.string.inicio);

        TextView txt = (TextView) findViewById(R.id.Journey);
        Typeface font = Typeface.createFromAsset(getAssets(), "futura.ttf");
        txt.setTypeface(font);

        Button botonRegistrar = (Button) findViewById(R.id.registrar);

        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(InicioActivity.this, RegistroActivity.class);
                InicioActivity.this.startActivityForResult(i, ACTIVITY_CLIENTE);
            }

        });

        mNickText = (EditText) findViewById(R.id.nick);
        mPassText = (EditText) findViewById(R.id.pass);

        Button botonIdentificar = (Button) findViewById(R.id.identificar);

        botonIdentificar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                InicioActivity.this.comprobarUsuario();
            }

        });

        //CODIGO DE TEST
        //AdaptadorUsuarios adUsuarios = new AdaptadorUsuarios(this);
        //adUsuarios.open();
        //adUsuarios.crearUsuario("test", "test", "test", "test", "test", 1234);
    }

    /*
     * Comprueba que el nick del usuario introducido se encuentra en la base de datos y verifica que
     * el nick y la contraseña introducidos se corresponden con los que se están alojados en la BD
     */
    public void comprobarUsuario() {
        dbHelper = new AdaptadorUsuarios(this);
        dbHelper.open();

        String nickIntroducido = mNickText.getText().toString();
        String passIntroducido = mPassText.getText().toString();

        Cursor usuario = dbHelper.listarUsuarioNick(nickIntroducido);
        if (usuario != null && usuario.moveToFirst()) {
            String nick = usuario.getString(usuario.getColumnIndexOrThrow(AdaptadorUsuarios.KEY_NICK));
            String pass = usuario.getString(usuario.getColumnIndexOrThrow(AdaptadorUsuarios.KEY_PASS));
            String correo = usuario.getString(usuario.getColumnIndexOrThrow(AdaptadorUsuarios.KEY_CORREO));

            if (nickIntroducido.equals(nick) && passIntroducido.equals(pass)) {
                mRowNick = nick;
                if (nickIntroducido.equals("admin")) {
                    Intent i = new Intent(this, InicioPaquetes.class);
                    startActivityForResult(i, ACTIVITY_ADMIN);
                } else {
                    actualizarPrefsUsuario(correo);
                    Intent i = new Intent(this, MainActivity.class);
                    startActivityForResult(i, ACTIVITY_CLIENTE);
                }
            }
        }
        if (usuario != null && !usuario.isClosed()){
            usuario.close();
        }
        dbHelper.close();
    }

    private void actualizarPrefsUsuario(String correo) {
        SharedPreferences prefsCorreo = getSharedPreferences(USUARIO, 0);
        SharedPreferences.Editor editor = prefsCorreo.edit();
        editor.putString("correo", correo);
        editor.commit();
    }

}