package br.com.bloodlink;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

/** Tela 2: home com mapa, filtro de raio e bancos próximos. */
public class HomeActivity extends BaseActivity {

    private static final String CANAL_ID = "alertas_sangue";
    private static final int RAIO_MINIMO = 2;

    private MapaView mapa;
    private BancosAdapter adapter;
    private TextView tvRaio;
    private TextView tvVazio;

    private final ActivityResultLauncher<String> pedirPermissao =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), concedida -> {
                if (concedida) {
                    enviarNotificacao();
                } else {
                    // Sem permissão: segue a demonstração abrindo a tela do alerta direto.
                    startActivity(new Intent(this, AlertaActivity.class));
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        configurarHeader("BloodLink", false);

        ImageButton btnPerfil = findViewById(R.id.btnPerfil);
        btnPerfil.setVisibility(View.VISIBLE);
        btnPerfil.setOnClickListener(v -> startActivity(new Intent(this, PerfilActivity.class)));

        mapa = findViewById(R.id.mapa);
        mapa.setListener(this::abrirDetalhe);

        tvRaio = findViewById(R.id.tvRaio);
        tvVazio = findViewById(R.id.tvVazio);

        adapter = new BancosAdapter(this::abrirDetalhe);
        RecyclerView rv = findViewById(R.id.rvBancos);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        SeekBar sbRaio = findViewById(R.id.sbRaio);
        sbRaio.setProgress(Sessao.raioKm - RAIO_MINIMO);
        sbRaio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Sessao.raioKm = progress + RAIO_MINIMO;
                atualizar();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        findViewById(R.id.btnSimular).setOnClickListener(v -> simularAlerta());
        criarCanalNotificacao();
        atualizar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizar();
    }

    private void atualizar() {
        List<BancoSangue> noRaio = Dados.bancosNoRaio(Sessao.raioKm);
        adapter.setLista(noRaio);
        mapa.setBancos(noRaio, Sessao.raioKm);
        tvRaio.setText(String.format(Locale.forLanguageTag("pt-BR"),
                "Raio dos alertas: %d km", Sessao.raioKm));
        tvVazio.setVisibility(noRaio.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void abrirDetalhe(BancoSangue banco) {
        Intent i = new Intent(this, DetalheBancoActivity.class);
        i.putExtra(DetalheBancoActivity.EXTRA_BANCO, Dados.indice(banco));
        startActivity(i);
    }

    /**
     * Simula o que o Firebase Cloud Messaging faria: o estoque do tipo do doador cai para
     * nível crítico no banco mais próximo e uma notificação chega só para quem tem esse tipo.
     */
    private void simularAlerta() {
        BancoSangue banco = Dados.bancoParaAlerta(Sessao.raioKm);
        banco.setNivel(Sessao.tipo, 10);
        Sessao.bancoAlerta = banco;
        atualizar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            pedirPermissao.launch(Manifest.permission.POST_NOTIFICATIONS);
        } else {
            enviarNotificacao();
        }
    }

    private void criarCanalNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    CANAL_ID, "Alertas de estoque", NotificationManager.IMPORTANCE_HIGH);
            canal.setDescription("Avisos quando o seu tipo sanguíneo está crítico perto de você");
            getSystemService(NotificationManager.class).createNotificationChannel(canal);
        }
    }

    @SuppressLint("MissingPermission")
    private void enviarNotificacao() {
        BancoSangue banco = Sessao.bancoAlerta;
        if (banco == null) return;

        Intent intent = new Intent(this, AlertaActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        String texto = String.format(Locale.forLanguageTag("pt-BR"),
                "%s precisa de %s (estoque crítico) e fica a %.1f km de você.",
                banco.nome, Sessao.tipo, banco.distanciaKm);

        NotificationCompat.Builder nb = new NotificationCompat.Builder(this, CANAL_ID)
                .setSmallIcon(R.drawable.ic_drop)
                .setContentTitle("Você pode ajudar agora!")
                .setContentText(texto)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(texto))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(0xFFB71C1C)
                .setContentIntent(pi)
                .setAutoCancel(true);

        NotificationManagerCompat.from(this).notify(1, nb.build());
    }
}
