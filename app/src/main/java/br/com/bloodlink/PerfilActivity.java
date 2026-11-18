package br.com.bloodlink;

import android.content.res.ColorStateList;
import android.view.View;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/** Tela 6: perfil do doador, próxima doação, badges e histórico. */
public class PerfilActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        configurarHeader("Meu perfil", true);

        findViewById(R.id.btnSimularDoacao).setOnClickListener(v -> {
            Sessao.registrarDoacaoHoje("Hemocentro Central (demo)");
            Toast.makeText(this, "Doação registrada! O prazo para a próxima já está correndo.",
                    Toast.LENGTH_LONG).show();
            renderizar();
        });
        renderizar();
    }

    private void renderizar() {
        ((TextView) findViewById(R.id.tvTipoPerfil)).setText(Sessao.tipo);
        ((TextView) findViewById(R.id.tvNomeDoador)).setText("Doador " + Sessao.tipo);
        ((TextView) findViewById(R.id.tvIntervalo)).setText(
                "Intervalo entre doações: " + Sessao.intervaloDias() + " dias");

        TextView status = findViewById(R.id.tvStatusDoacao);
        TextView proxima = findViewById(R.id.tvProximaData);
        if (Sessao.podeDoar()) {
            status.setText("Você já pode doar!");
            proxima.setText("Última doação em " + Sessao.formatar(Sessao.ultimaDoacao)
                    + " (há " + Sessao.diasDesdeUltima() + " dias)");
        } else {
            status.setText("Próxima doação em " + Sessao.diasRestantes() + " dias");
            proxima.setText("Liberada em " + Sessao.formatar(Sessao.proximaDoacao())
                    + " · última doação em " + Sessao.formatar(Sessao.ultimaDoacao));
        }

        TextView agendamento = findViewById(R.id.tvAgendamento);
        if (Sessao.agendamento != null) {
            agendamento.setText("Agendado: " + Sessao.agendamento);
            agendamento.setVisibility(View.VISIBLE);
        }

        renderizarBadges();
        renderizarHistorico();
    }

    private void renderizarBadges() {
        LinearLayout ll = findViewById(R.id.llBadges);
        ll.removeAllViews();
        String proximaMeta = null;
        for (int i = 0; i < Conquistas.METAS.length; i++) {
            boolean ganhou = Sessao.totalDoacoes >= Conquistas.METAS[i];
            View item = getLayoutInflater().inflate(R.layout.item_badge, ll, false);
            TextView num = item.findViewById(R.id.tvBadgeNum);
            num.setText(String.valueOf(Conquistas.METAS[i]));
            num.setBackgroundTintList(ColorStateList.valueOf(ganhou ? Conquistas.CORES[i] : 0xFFBDBDBD));
            ((TextView) item.findViewById(R.id.tvBadgeNome)).setText(Conquistas.NOMES[i]);
            item.setAlpha(ganhou ? 1f : 0.55f);
            ll.addView(item);
            if (!ganhou && proximaMeta == null) {
                int faltam = Conquistas.METAS[i] - Sessao.totalDoacoes;
                proximaMeta = "Faltam " + faltam + (faltam == 1 ? " doação" : " doações")
                        + " para a conquista \"" + Conquistas.NOMES[i] + "\".";
            }
        }
        ((TextView) findViewById(R.id.tvProgresso)).setText(
                proximaMeta != null ? proximaMeta : "Você desbloqueou todas as conquistas!");
    }

    private void renderizarHistorico() {
        ((TextView) findViewById(R.id.tvTotalDoacoes)).setText(
                "Histórico de doações (" + Sessao.totalDoacoes + ")");
        LinearLayout ll = findViewById(R.id.llHistorico);
        ll.removeAllViews();
        for (String[] item : Sessao.historico) {
            TextView tv = new TextView(this);
            tv.setText(item[0] + "  ·  " + item[1]);
            tv.setTextSize(15);
            tv.setTextColor(0xFF212121);
            tv.setPadding(0, dp(6), 0, dp(6));
            ll.addView(tv);
        }
    }
}
