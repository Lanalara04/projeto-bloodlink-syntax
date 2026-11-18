package br.com.bloodlink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Dados fictícios para a demonstração.
 * Na versão real, esta classe seria substituída pelo Firebase (Firestore) em tempo real.
 */
public final class Dados {
    private Dados() {}

    public static final String[] TIPOS = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    // Ordenados por distância. Estoque na ordem: A+, A-, B+, B-, AB+, AB-, O+, O-
    public static final List<BancoSangue> BANCOS = new ArrayList<>(Arrays.asList(
            new BancoSangue("Hemocentro Central", "Rua das Flores, 120 - Centro",
                    "Seg a Sáb, 7h às 17h", 1.2, 200,
                    new String[]{"08:00", "09:00", "10:30", "13:00", "14:30", "16:00"},
                    new int[]{68, 42, 55, 30, 78, 45, 60, 12}),
            new BancoSangue("Banco de Sangue Vila Nova", "Av. Brasil, 850 - Vila Nova",
                    "Seg a Sex, 8h às 16h", 2.8, 320,
                    new String[]{"08:30", "09:30", "11:00", "13:30", "15:00"},
                    new int[]{80, 30, 70, 48, 85, 66, 72, 55}),
            new BancoSangue("Hospital Santa Clara", "Rua da Saúde, 45 - Jardim América",
                    "Todos os dias, 7h às 19h", 4.1, 60,
                    new String[]{"07:30", "09:00", "10:00", "14:00", "17:30"},
                    new int[]{45, 60, 22, 35, 50, 40, 65, 58}),
            new BancoSangue("Hemonúcleo Zona Leste", "Av. Leste, 2300 - Vila Esperança",
                    "Seg a Sex, 7h às 15h", 6.5, 130,
                    new String[]{"07:00", "08:00", "09:30", "11:30", "13:00"},
                    new int[]{90, 80, 85, 70, 92, 75, 88, 72})
    ));

    public static List<BancoSangue> bancosNoRaio(int raioKm) {
        List<BancoSangue> lista = new ArrayList<>();
        for (BancoSangue b : BANCOS) {
            if (b.distanciaKm <= raioKm) lista.add(b);
        }
        return lista;
    }

    public static int indice(BancoSangue banco) {
        return Math.max(0, BANCOS.indexOf(banco));
    }

    /** Banco usado na simulação de alerta: o mais próximo dentro do raio escolhido. */
    public static BancoSangue bancoParaAlerta(int raioKm) {
        List<BancoSangue> noRaio = bancosNoRaio(raioKm);
        return noRaio.isEmpty() ? BANCOS.get(0) : noRaio.get(0);
    }
}
