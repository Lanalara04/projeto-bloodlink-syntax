# BloodLink — versão de demonstração (MVP da ideia)

App Android (Java + layouts XML) que conecta doadores a bancos de sangue próximos,
com estoque por tipo sanguíneo e alertas só para quem tem o tipo necessário.

Esta versão é **para apresentar a ideia**: usa dados fictícios em memória e não precisa de
internet, chave de API nem conta no Firebase.

## Como abrir
1. Android Studio → **File > Open** → selecione a pasta `BloodLink`.
2. Aguarde o **Gradle Sync**. Se o Android Studio sugerir atualizar o Android Gradle Plugin, pode aceitar.
3. Rode em um emulador ou celular com Android 7.0 (API 24) ou superior.

## Roteiro da apresentação (2 a 3 minutos)
1. **Onboarding:** escolha o tipo sanguíneo e o sexo (define o intervalo de 60 ou 90 dias) e toque em Continuar.
2. **Home:** mapa com os bancos e pinos coloridos (vermelho, amarelo, verde). Mexa no controle de raio e veja o mapa e a lista mudarem.
3. **Detalhe do banco:** toque em um pino ou card. Cada tipo sanguíneo tem seu medidor, e o seu tipo vem destacado.
4. **Notificação inteligente:** volte à Home e toque em **Simular notificação push (demo)**. O estoque do seu tipo cai para crítico no banco mais próximo e chega uma notificação. Toque nela para abrir "Você pode ajudar agora".
5. **Agendamento:** escolha dia e horário (alguns aparecem lotados) e confirme.
6. **Perfil:** próxima doação calculada automaticamente, badges e histórico. Use **Simular doação hoje (demo)** para ver o contador e as conquistas mudarem.

## Do protótipo para a versão final
| Na demo | Na versão final |
|---|---|
| `MapaView` (mapa desenhado) | Google Maps SDK com marcadores dos hemocentros |
| `GaugeView` (medidor desenhado) | MPAndroidChart |
| `Dados` / `Sessao` (memória) | Firebase Firestore em tempo real + Room (cache local) |
| Botão "Simular notificação" | Firebase Cloud Messaging com tópicos por tipo sanguíneo |
| Nomes e endereços dos bancos | Dados reais dos hemocentros parceiros |

## Estrutura
- `OnboardingActivity`, `HomeActivity`, `DetalheBancoActivity`, `AlertaActivity`, `AgendamentoActivity`, `PerfilActivity`
- `Dados`, `BancoSangue`, `Sessao`, `Niveis`, `Conquistas`: modelo e regras
- `MapaView`, `GaugeView`: views customizadas
- Layouts em `app/src/main/res/layout`
