package br.com.bloodlink;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

/** Tela 3: detalhe do banco, com um medidor (gauge) por tipo sanguíneo. */
public class DetalheBancoActivity extends BaseActivity {

    public static final String EXTRA_BANCO = "banco";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_banco);
        configurarHeader("Detalhe do banco", true);

        int indice = getIntent().getIntExtra(EXTRA_BANCO, 0);
        BancoSangue banco = Dados.BANCOS.get(indice);

        ((TextView) findViewById(R.id.tvNomeDetalhe)).setText(banco.nome);
        ((TextView) findViewById(R.id.tvEnderecoDetalhe)).setText(banco.endereco);
        ((TextView) findViewById(R.id.tvInfoDetalhe)).setText(String.format(
                Locale.forLanguageTag("pt-BR"), "%.1f km de você · %s",
                banco.distanciaKm, banco.funcionamento));

        int nivelMeuTipo = banco.nivelDoTipo(Sessao.tipo);
        if (nivelMeuTipo < 50) {
            TextView destaque = findViewById(R.id.tvDestaque);
            destaque.setText("Seu tipo (" + Sessao.tipo + ") está com estoque "
                    + Niveis.rotulo(nivelMeuTipo).toLowerCase(Locale.forLanguageTag("pt-BR"))
                    + " aqui. Sua doação faz diferença!");
            destaque.setBackgroundTintList(ColorStateList.valueOf(Niveis.cor(nivelMeuTipo)));
            destaque.setVisibility(android.view.View.VISIBLE);
        }

        RecyclerView rv = findViewById(R.id.rvGauges);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.setAdapter(new GaugesAdapter(banco));

        findViewById(R.id.btnAgendar).setOnClickListener(v -> {
            Intent i = new Intent(this, AgendamentoActivity.class);
            i.putExtra(EXTRA_BANCO, indice);
            startActivity(i);
        });
    }
}
