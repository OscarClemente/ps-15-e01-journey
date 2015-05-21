package bamboo_software.journey;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by javier on 12/05/15.
 */
public class Mail {

    private Context context;
    private static final String FROM = "bamboosoftwaresoporte@gmail.com";
    private static final String NOMBRE = "Bamboo Software";
    private static final String PASSWORD = "bamboo-software";
    private static final String ASUNTO = "Acabas de comprar un viaje con Journey";
    private String mensaje;

    public Mail(Context ctx, String titulo, int personas, String fecha) {
        context = ctx;
        String inicio = "Estos son los datos de tu compra:\n";
        String _titulo = "\nTitulo: ";
        String _personas = "\nNº de personas: ";
        String _fecha = "\nFecha de compra: ";
        mensaje = inicio + _titulo + titulo + _personas + personas + _fecha + fecha + "\n";
    }

    public void enviar(String destinatario) {
        Session session = createSessionObject();

        try {
            Message message = createMessage(destinatario, ASUNTO, mensaje, session);
            new SendMailTask(context).execute(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Message createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM, NOMBRE));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
        message.setText(messageBody);
        return message;
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PASSWORD);
            }
        });
    }

    public class SendMailTask extends AsyncTask<Message, Void, Void> {
        private ProgressDialog progressDialog;
        private Context context;

        public SendMailTask(Context ctx) {
            context = ctx;
        }

        /*
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(context, "Por favor, espera", "Enviando mail",
                            true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        */
        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
