package br.com.bloodlink;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public abstract class BaseActivity extends AppCompatActivity {

    /** Configura o cabeçalho vermelho (layout header.xml) presente em todas as telas. */
    protected void configurarHeader(String titulo, boolean comVoltar) {
        TextView tvTitulo = findViewById(R.id.tvTitulo);
        tvTitulo.setText(titulo);
        ImageButton btnVoltar = findViewById(R.id.btnVoltar);
        if (comVoltar) {
            btnVoltar.setOnClickListener(v -> finish());
        } else {
            btnVoltar.setVisibility(View.GONE);
        }
    }

    /** Cria um chip de escolha única dentro do grupo. */
    protected Chip novoChip(ChipGroup grupo, String texto) {
        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_escolha, grupo, false);
        chip.setId(View.generateViewId());
        chip.setText(texto);
        grupo.addView(chip);
        return chip;
    }

    protected int dp(int valor) {
        return Math.round(valor * getResources().getDisplayMetrics().density);
    }
}
