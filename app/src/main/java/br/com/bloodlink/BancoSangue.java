package br.com.bloodlink;

public class BancoSangue {
    public final String nome;
    public final String endereco;
    public final String funcionamento;
    public final double distanciaKm;
    /** Direção do banco em relação ao usuário no mapa ilustrativo (graus). */
    public final double anguloGraus;
    public final String[] horarios;
    /** Estoque por tipo (0 a 100), na mesma ordem de Dados.TIPOS. */
    private final int[] niveis;

    public BancoSangue(String nome, String endereco, String funcionamento, double distanciaKm,
                       double anguloGraus, String[] horarios, int[] niveis) {
        this.nome = nome;
        this.endereco = endereco;
        this.funcionamento = funcionamento;
        this.distanciaKm = distanciaKm;
        this.anguloGraus = anguloGraus;
        this.horarios = horarios;
        this.niveis = niveis;
    }

    public static int indice(String tipo) {
        for (int i = 0; i < Dados.TIPOS.length; i++) {
            if (Dados.TIPOS[i].equals(tipo)) return i;
        }
        return 0;
    }

    public int nivelPorIndice(int i) {
        return niveis[i];
    }

    public int nivelDoTipo(String tipo) {
        return niveis[indice(tipo)];
    }

    public void setNivel(String tipo, int valor) {
        niveis[indice(tipo)] = valor;
    }

    public int menorNivel() {
        int min = 100;
        for (int n : niveis) min = Math.min(min, n);
        return min;
    }

    public String tipoMaisCritico() {
        int pos = 0;
        for (int i = 1; i < niveis.length; i++) {
            if (niveis[i] < niveis[pos]) pos = i;
        }
        return Dados.TIPOS[pos];
    }
}
