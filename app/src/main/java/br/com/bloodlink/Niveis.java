package br.com.bloodlink;

/** Regras de nível de estoque: cor e rótulo a partir da porcentagem (0 a 100). */
public final class Niveis {
    private Niveis() {}

    public static final int CRITICO = 0xFFC62828;
    public static final int BAIXO = 0xFFF9A825;
    public static final int MEDIO = 0xFF7CB342;
    public static final int ESTAVEL = 0xFF2E7D32;

    public static int cor(int pct) {
        if (pct < 25) return CRITICO;
        if (pct < 50) return BAIXO;
        if (pct < 75) return MEDIO;
        return ESTAVEL;
    }

    public static String rotulo(int pct) {
        if (pct < 25) return "Crítico";
        if (pct < 50) return "Baixo";
        if (pct < 75) return "Médio";
        return "Estável";
    }
}
