package bamboo_software.journey;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InicioActivity extends ActionBarActivity {

    private static final int ACTIVITY_ADMIN = 0;
    private static final int ACTIVITY_CLIENTE = 1;

    private EditText mNickText;
    private EditText mPassText;

    private String mRowNick;
    private AdaptadorUsuarios dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inicio);
        setTitle(R.string.inicio);

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
                InicioActivity.this.guardarEstado();
            }

        });
    }

    public void guardarEstado() {
        dbHelper = new AdaptadorUsuarios(this);
        dbHelper.open();

        String nickIntroducido = mNickText.getText().toString();
        String passIntroducido = mPassText.getText().toString();

        Cursor usuario = dbHelper.listarUsuarioNick(nickIntroducido);
        if (usuario != null && usuario.moveToFirst()) {
            String nick = usuario.getString(usuario.getColumnIndexOrThrow(AdaptadorUsuarios.KEY_NICK));
            String pass = usuario.getString(usuario.getColumnIndexOrThrow(AdaptadorUsuarios.KEY_PASS));

            if (nickIntroducido.equals(nick) && passIntroducido.equals(pass)) {
                mRowNick = nick;
                if (nickIntroducido.equals("admin")) {
                    Intent i = new Intent(this, InicioPaquetes.class);
                    startActivityForResult(i, ACTIVITY_ADMIN);
                } else {
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

}
