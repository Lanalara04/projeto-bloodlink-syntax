package br.com.bloodlink;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BancosAdapter extends RecyclerView.Adapter<BancosAdapter.VH> {

    public interface OnBancoClick {
        void aoClicar(BancoSangue banco);
    }

    private List<BancoSangue> lista = new ArrayList<>();
    private final OnBancoClick callback;

    public BancosAdapter(OnBancoClick callback) {
        this.callback = callback;
    }

    public void setLista(List<BancoSangue> nova) {
        this.lista = nova;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banco, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        BancoSangue b = lista.get(position);
        h.nome.setText(b.nome);
        h.endereco.setText(b.endereco);
        h.dist.setText(String.format(Locale.forLanguageTag("pt-BR"), "%.1f km de você", b.distanciaKm));

        String pior = b.tipoMaisCritico();
        int nivel = b.nivelDoTipo(pior);
        h.status.setText(nivel >= 75 ? "Estoque estável" : pior + " · " + Niveis.rotulo(nivel));
        h.status.setBackgroundTintList(ColorStateList.valueOf(Niveis.cor(nivel)));
        h.itemView.setOnClickListener(v -> callback.aoClicar(b));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView nome, endereco, dist, status;

        VH(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.tvNomeBanco);
            endereco = itemView.findViewById(R.id.tvEnderecoBanco);
            dist = itemView.findViewById(R.id.tvDistBanco);
            status = itemView.findViewById(R.id.tvStatusBanco);
        }
    }
}
