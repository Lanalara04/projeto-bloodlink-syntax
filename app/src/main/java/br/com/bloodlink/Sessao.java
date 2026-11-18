package br.com.bloodlink;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Estado do doador durante a demonstração (em memória).
 * Na versão real, o perfil e o histórico ficariam no Room (SQLite) e no Firebase.
 */
public final class Sessao {
    private Sessao() {}

    public static String tipo = "O+";
    public static String sexo = "F"; // "M" = 60 dias, "F" = 90 dias
    public static int raioKm = 5;
    public static int totalDoacoes = 3;
    public static String agendamento = null;
    public static BancoSangue bancoAlerta = null;
    public static final List<String[]> historico = new ArrayList<>();
    public static Calendar ultimaDoacao;

    static {
        ultimaDoacao = diasAtras(120);
        historico.add(new String[]{formatar(diasAtras(120)), "Hemocentro Central"});
        historico.add(new String[]{formatar(diasAtras(260)), "Banco de Sangue Vila Nova"});
        historico.add(new String[]{formatar(diasAtras(410)), "Hemocentro Central"});
    }

    public static Calendar diasAtras(int dias) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -dias);
        return c;
    }

    public static String formatar(Calendar c) {
        return new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR")).format(c.getTime());
    }

    public static int intervaloDias() {
        return "M".equals(sexo) ? 60 : 90;
    }

    public static int diasDesdeUltima() {
        long ms = System.currentTimeMillis() - ultimaDoacao.getTimeInMillis();
        return (int) TimeUnit.MILLISECONDS.toDays(ms);
    }

    public static int diasRestantes() {
        return Math.max(0, intervaloDias() - diasDesdeUltima());
    }

    public static boolean podeDoar() {
        return diasRestantes() == 0;
    }

    public static Calendar proximaDoacao() {
        Calendar c = (Calendar) ultimaDoacao.clone();
        c.add(Calendar.DAY_OF_YEAR, intervaloDias());
        return c;
    }

    public static void registrarDoacaoHoje(String local) {
        ultimaDoacao = Calendar.getInstance();
        totalDoacoes++;
        historico.add(0, new String[]{formatar(ultimaDoacao), local});
    }
}
