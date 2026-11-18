package br.com.bloodlink;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

public class GaugesAdapter extends RecyclerView.Adapter<GaugesAdapter.VH> {

    private final BancoSangue banco;

    public GaugesAdapter(BancoSangue banco) {
        this.banco = banco;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gauge, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        String tipo = Dados.TIPOS[position];
        int nivel = banco.nivelPorIndice(position);
        boolean meuTipo = tipo.equals(Sessao.tipo);

        h.gauge.setValor(nivel);
        h.tipo.setText(meuTipo ? tipo + " (você)" : tipo);
        h.nivel.setText(Niveis.rotulo(nivel));
        h.nivel.setTextColor(Niveis.cor(nivel));

        float density = h.itemView.getResources().getDisplayMetrics().density;
        h.card.setStrokeColor(0xFFB71C1C);
        h.card.setStrokeWidth(meuTipo ? Math.round(2 * density) : 0);
    }

    @Override
    public int getItemCount() {
        return Dados.TIPOS.length;
    }

    static class VH extends RecyclerView.ViewHolder {
        final MaterialCardView card;
        final GaugeView gauge;
        final TextView tipo, nivel;

        VH(@NonNull View itemView) {
            super(itemView);
            card = (MaterialCardView) itemView;
            gauge = itemView.findViewById(R.id.gauge);
            tipo = itemView.findViewById(R.id.tvTipoGauge);
            nivel = itemView.findViewById(R.id.tvNivelGauge);
        }
    }
}
