package br.com.bloodlink;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/** Tela 5: agendamento de doação (dia e horário). */
public class AgendamentoActivity extends BaseActivity {

    private BancoSangue banco;
    private ChipGroup cgDias;
    private ChipGroup cgHoras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento);
        configurarHeader("Agendar doação", true);

        banco = Dados.BANCOS.get(getIntent().getIntExtra(DetalheBancoActivity.EXTRA_BANCO, 0));
        ((TextView) findViewById(R.id.tvBancoAg)).setText(banco.nome + "\n" + banco.endereco);

        cgDias = findViewById(R.id.cgDias);
        cgHoras = findViewById(R.id.cgHoras);

        SimpleDateFormat formato = new SimpleDateFormat("EEE dd/MM", new Locale("pt", "BR"));
        Calendar dia = Calendar.getInstance();
        for (int i = 0; i < 5; i++) {
            dia.add(Calendar.DAY_OF_YEAR, 1);
            Chip chip = novoChip(cgDias, formato.format(dia.getTime()));
            if (i == 0) cgDias.check(chip.getId());
        }
        cgDias.setOnCheckedStateChangeListener((grupo, ids) -> montarHorarios());
        montarHorarios();

        findViewById(R.id.btnConfirmar).setOnClickListener(v -> confirmar());
    }

    private void montarHorarios() {
        cgHoras.removeAllViews();
        View diaMarcado = cgDias.findViewById(cgDias.getCheckedChipId());
        int indiceDia = Math.max(0, cgDias.indexOfChild(diaMarcado));
        for (int i = 0; i < banco.horarios.length; i++) {
            Chip chip = novoChip(cgHoras, banco.horarios[i]);
            // Simula horários já ocupados para variar a disponibilidade a cada dia.
            if ((i * 3 + indiceDia) % 5 == 0) {
                chip.setText(banco.horarios[i] + " (lotado)");
                chip.setEnabled(false);
            }
        }
    }

    private void confirmar() {
        int idHora = cgHoras.getCheckedChipId();
        if (idHora == View.NO_ID) {
            Toast.makeText(this, "Escolha um horário disponível.", Toast.LENGTH_SHORT).show();
            return;
        }
        Chip hora = cgHoras.findViewById(idHora);
        Chip dia = cgDias.findViewById(cgDias.getCheckedChipId());
        Sessao.agendamento = banco.nome + " · " + dia.getText() + " às " + hora.getText();

        new MaterialAlertDialogBuilder(this)
                .setTitle("Doação agendada!")
                .setMessage(Sessao.agendamento
                        + "\n\nVocê receberá um lembrete automático antes da data.")
                .setCancelable(false)
                .setPositiveButton("Ver meu perfil", (d, w) -> {
                    startActivity(new Intent(this, PerfilActivity.class));
                    finish();
                })
                .setNegativeButton("Fechar", (d, w) -> finish())
                .show();
    }
}
