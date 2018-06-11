package br.com.sinapse.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import br.com.sinapse.R;

import static android.content.ContentValues.TAG;

public class CDCMessasingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
                RemoteMessage.Notification notification = remoteMessage.getNotification();
                mostrarNotificacao(notification);
            }
        }
    }

    public void mostrarNotificacao(RemoteMessage.Notification notification) {
        String titulo = notification.getTitle();
        String mensagem = notification.getBody();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext());
        Notification notificacao = builder.setContentTitle(titulo)
                .setContentText(mensagem).setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificacao);
    }
}
