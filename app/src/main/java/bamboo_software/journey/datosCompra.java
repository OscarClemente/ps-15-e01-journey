package bamboo_software.journey;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Daniel on 05/05/2015.
 */
public class DatosCompra extends Activity {

    private EditText mTarjeta;
    private EditText mPersonas;
    private EditText mPagador;

    private static final String USUARIO = "CorreoUsuario";
    private AdaptadorUsuarios usuarioDbHelper;
    private AdaptadorCompras compraDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_compra);

        compraDbHelper = new AdaptadorCompras(this);
        compraDbHelper.open();
        usuarioDbHelper = new AdaptadorUsuarios(this);
        usuarioDbHelper.open();

        setTitle("Finalizar Compra");

        mTarjeta = (EditText) findViewById(R.id.tarjeta);
        mPersonas = (EditText) findViewById(R.id.personas);
        mPagador = (EditText) findViewById(R.id.pagador);
        //String personas = (String) mPersonas.getText();

        Button comprar = (Button) findViewById(R.id.comprar);
        comprar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //compraDbHelper.crearCompra(i,getUsuario(),getdate(),);
                String[] to = { getUsuario() };
                enviar(to, "Listado de Compra", "Este es el listado de tu compra"+compraDbHelper.listarCompras());
                setResult(RESULT_OK);
                //finish();
            }
        });
    }

    private void enviar(String[] to, String asunto, String mensaje) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        //emailIntent.putExtra(Intent.EXTRA_CC, cc);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, asunto);
        emailIntent.putExtra(Intent.EXTRA_TEXT, mensaje);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Enviar Email"));
        /*JavaMail m = new JavaMail(from, to, user, password
                ,"smtp.gmail.com", "465", "465"
                ,asunto, mensaje);
        try {

            if(m.send()) {
                //A toast is a view containing a quick little message for the user
                Toast.makeText(this, "Mensaje enviado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Mensaje no enviado", Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e) {
            Toast.makeText(this, "Se produjo una excepción al enviar mensaje", Toast.LENGTH_SHORT).show();
            android.util.Log.d("EnviarCorreo", "Se produjo una excepción: " + e.getMessage());
        }*/
    }

    private String getUsuario() {
        SharedPreferences settings = getSharedPreferences(USUARIO, 0);
        String correo = settings.getString("correo", null);
        return correo;
    }
}
