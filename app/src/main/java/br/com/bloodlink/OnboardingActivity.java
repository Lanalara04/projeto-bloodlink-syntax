package br.com.bloodlink;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

/** Tela 1: onboarding + cadastro do tipo sanguíneo. */
public class OnboardingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        configurarHeader("BloodLink", false);

        ChipGroup cgTipos = findViewById(R.id.cgTipos);
        for (String tipo : Dados.TIPOS) {
            Chip chip = novoChip(cgTipos, tipo);
            if (tipo.equals("O+")) cgTipos.check(chip.getId());
        }

        RadioGroup rgSexo = findViewById(R.id.rgSexo);
        MaterialButton btnComprovante = findViewById(R.id.btnComprovante);
        btnComprovante.setOnClickListener(v -> {
            btnComprovante.setText("✔ Carteirinha anexada (simulação)");
            Toast.makeText(this, "Na versão final, aqui abre a câmera/galeria.", Toast.LENGTH_SHORT).show();
        });

        MaterialButton btnContinuar = findViewById(R.id.btnContinuar);
        btnContinuar.setOnClickListener(v -> {
            if (rgSexo.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Selecione o sexo para calcular o intervalo entre doações.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            Chip escolhido = findViewById(cgTipos.getCheckedChipId());
            Sessao.tipo = escolhido.getText().toString();
            Sessao.sexo = rgSexo.getCheckedRadioButtonId() == R.id.rbMasc ? "M" : "F";
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
    }
}
