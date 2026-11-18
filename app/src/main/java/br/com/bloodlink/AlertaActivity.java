package br.com.bloodlink;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;

/** Tela 4: destino da notificação — "Você pode ajudar agora". */
public class AlertaActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerta);
        configurarHeader("Alerta de estoque", true);

        BancoSangue banco = Sessao.bancoAlerta != null
                ? Sessao.bancoAlerta : Dados.bancoParaAlerta(Sessao.raioKm);
        int indice = Dados.indice(banco);
        int nivel = banco.nivelDoTipo(Sessao.tipo);
        Locale br = Locale.forLanguageTag("pt-BR");

        ((TextView) findViewById(R.id.tvTipoGrande)).setText(Sessao.tipo);
        ((TextView) findViewById(R.id.tvMensagemAlerta)).setText(String.format(br,
                "O %s está com estoque %s de %s, a %.1f km de você.",
                banco.nome, Niveis.rotulo(nivel).toLowerCase(br), Sessao.tipo, banco.distanciaKm));

        GaugeView gauge = findViewById(R.id.gaugeAlerta);
        gauge.setValor(nivel);
        TextView tvNivel = findViewById(R.id.tvNivelAlerta);
        tvNivel.setText("Nível " + Niveis.rotulo(nivel).toLowerCase(br));
        tvNivel.setTextColor(Niveis.cor(nivel));

        MaterialButton btnAgendar = findViewById(R.id.btnAgendarAgora);
        TextView tvElegivel = findViewById(R.id.tvElegivel);
        if (Sessao.podeDoar()) {
            tvElegivel.setText("Você é doador " + Sessao.tipo + " e já está apto a doar hoje.");
        } else {
            tvElegivel.setText("Você poderá doar novamente em "
                    + Sessao.formatar(Sessao.proximaDoacao()) + ".");
            btnAgendar.setEnabled(false);
        }

        btnAgendar.setOnClickListener(v -> abrir(AgendamentoActivity.class, indice));
        findViewById(R.id.btnVerEstoque).setOnClickListener(v -> abrir(DetalheBancoActivity.class, indice));
        findViewById(R.id.btnAgoraNao).setOnClickListener(v -> finish());
    }

    private void abrir(Class<?> destino, int indice) {
        Intent i = new Intent(this, destino);
        i.putExtra(DetalheBancoActivity.EXTRA_BANCO, indice);
        startActivity(i);
    }
}
