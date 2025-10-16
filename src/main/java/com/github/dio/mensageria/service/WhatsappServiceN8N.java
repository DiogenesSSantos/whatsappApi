package com.github.dio.mensageria.service;


import com.github.dio.mensageria.infraestrutura.assembler.AssemblerPaciente;
import com.github.dio.mensageria.model.Paciente;
import com.github.dio.mensageria.model.PacienteNaoRespondido;
import com.github.dio.mensageria.model.modeloRepresentacional.PacienteMR;
import com.github.dio.mensageria.notificador.NotificadorThread;
import com.github.dio.mensageria.repository.PacienteRepository;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.companion.CompanionDevice;
import it.auties.whatsapp.model.jid.Jid;
import it.auties.whatsapp.model.signal.auth.Version;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

@Service
public class WhatsappServiceN8N {
    private static final Logger log = LoggerFactory.getLogger(WhatsappServiceN8N.class);
    @Value("${VERSAO_WHATSAPP}")
    private  String VERSION_WHATSAPP;


    public static final String NAO_RESPONDIDA_MSG_PADRAO_FINAL_DE_SEMANA=
            "Olá!\n\n" +
                    "Informamos que o prazo de 48 horas para retirada do seu comprovante de agendamento expirou.\n" +
                    "Se você já retirou o comprovante, por favor, desconsidere esta mensagem.\n" +
                    "Caso ainda não tenha feito a retirada, encaminharemos seu comprovante para a Unidade Básica de Saúde do seu bairro. " +
                    "Ele estará disponível para retirada a partir da próxima terça-feira.\n\n" +
                    "Atenciosamente,\n" +
                    "Equipe de Regulação de Saúde.";



    public static final String NAO_RESPONDIDA_MSG_PADRAO =
            "Olá!\n\n" +
            "Informamos que o prazo de 48 horas para retirada do seu comprovante de agendamento expirou.\n" +
            "Se você já retirou o comprovante, por favor, desconsidere esta mensagem.\n" +
            "Caso ainda não tenha feito a retirada, encaminharemos seu comprovante para a Unidade Básica de Saúde do seu bairro. " +
            "Ele estará disponível retirada em até 24 horas a partir do recebimento desta mensagem..\n\n" +
            "Atenciosamente,\n" +
            "Equipe de Regulação de Saúde.";



    private static CompletableFuture<Whatsapp> whatsappFuture;
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    public static final Set<String> pacienteSetStringUUID = new ConcurrentSkipListSet<>();
    public static final List<PacienteNaoRespondido> pacienteList = new LinkedList<>();
    public static Boolean isNotificado = Boolean.FALSE;


    private String qrCode;
    private Boolean isDisconnecting = true;
    private PacienteRepository pacienteRepository;
    @Autowired
    private N8NService n8NService;

    @Autowired
    public WhatsappServiceN8N(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }


    @PostConstruct
    public void init() {
        whatsappFuture = new CompletableFuture<>();


        Whatsapp whatsapp = Whatsapp.webBuilder()
                .lastConnection()
                .name("CMCE LOGIN")
                .unregistered(qrCode -> {
                    System.out.println("QRCodeRecebido");
                    this.qrCode = qrCode;

                })
                .addLoggedInListener(api -> {
                    System.out.printf("conectado: %s%n", api.store().privacySettings());
                    isDisconnecting = false;

                })
                .addDisconnectedListener(reason -> {
                    whatsappFuture = new CompletableFuture<>();
                    System.out.printf("disconectado: %s%n", reason);
                });

        whatsapp.store().setDevice(CompanionDevice.web(Version.of(VERSION_WHATSAPP)));
        whatsapp.connect();
        whatsappFuture.complete(whatsapp);

    }

    public String getQrCode() {
        return qrCode;
    }


    public void conectar() {
        init();
    }

    public void desconectar() {
        if (isDisconnecting) {
            System.out.println("Desconexão já está em andamento.");
            return;
        }

        try {
            if (whatsappFuture != null && whatsappFuture.get() != null) {
                isDisconnecting = true;
                whatsappFuture.get().disconnect().thenAccept(unused -> {
                    whatsappFuture = new CompletableFuture<>();
                    System.out.println("API DESCONECTADA");
                }).exceptionally(throwable -> {
                    log.warn("ERRO NA API" + throwable);
                    isDisconnecting = false;
                    return null;
                });

            }

        } catch (Exception e) {
            new RuntimeException("ALGUMA ERRO NA INICIALIZAÇÃO");
            e.printStackTrace();
        }
    }


    @Scheduled(initialDelay = 120000L, fixedDelay = 30000L)
    public void verificarConexao() throws InterruptedException {
        log.info("LIMPEZA_DO_GARBAGE_COLLECTION__VERSÃO");
        System.gc();
        TimeUnit.SECONDS.sleep(15);
        Whatsapp whatsapp = whatsappFuture.getNow(null);

        if (whatsapp == null) {
            log.warn("Instância do WhatsApp ainda não disponível.");
            conectar();
        } else if (!whatsapp.isConnected()) {
            log.warn("WhatsApp DESCONECTADO! É necessário reconectar.");
            conectar();
        }
    }






    public void enviarMensagemLista(List<PacienteMR> pacienteMRList) {
        pacienteMRList.forEach(pacienteMR -> {
            try {
                enviarMensagem(pacienteMR);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void enviarMensagem(PacienteMR paciente) throws InterruptedException {
        for (int i = 0; i < paciente.getNumeros().size(); i++) {
            enviandoMensagemTexto(paciente, "55" + paciente.getNumeros().get(i));
            Thread.sleep(10000L);
        }


    }



    private void enviandoMensagemTexto(PacienteMR pacienteMR, String numero) {

        whatsappFuture.thenAccept(whatsapp -> {

            if (!whatsapp.isConnected()) {
                System.err.println("O WhatsApp não está conectado.");
                return;
            }

            try {
                var contactJid = Jid.of(numero);
                if (whatsapp.hasWhatsapp(contactJid).get()) {
                    var pacientePersistido = salvandoIncialmenteAguardando(pacienteMR);

                    pacienteList.add(new PacienteNaoRespondido(pacientePersistido.getId(), numero));



                    System.out.println("Enviando mensagem para: " + numero);
                    String mensagem = String.format(
                            "Olá %S!%n%n" +
                                    "Somos da Secretaria de Saúde de Vitória de Santo Antão.%n" +
                                    "Temos o prazer de informá-lo sobre a sua consulta ou exame:%n%S.%n%n" +
                                    "Ao receber esta mensagem, por favor, responda com 'SIM' se você tem interesse na consulta ou exame, ou 'NÃO' caso contrário.%n" +
                                    "Informamos que aguardaremos sua resposta por 48 horas.%n%n" +
                                    "Atenciosamente,%n" +
                                    "Regulação de Saúde.",
                            pacienteMR.getNome(), pacienteMR.getConsulta());
                    n8NService.enviarPayload(String.valueOf(pacientePersistido.getId()),pacienteMR.getNome() , numero , mensagem);

                } else {
                    salvandoNaoPossuiWhatsapp(pacienteMR);
                }
            } catch (Exception e) {
                log.warn("Erro ao enviar mensagem: " + e.getMessage());
                e.printStackTrace();
            }


        }).exceptionally(ex -> {
            log.warn("Falha ao obter a instância do WhatsApp: " + ex.getMessage());
            return null;
        });

        NotificadorThread.notificar(this);
    }



    private Paciente salvandoIncialmenteAguardando(PacienteMR pacienteMR) {
        var pacienteBdExiste = pacienteRepository.findBycodigo(pacienteMR.getId().toString());
        var paciente = AssemblerPaciente.disassembleToObject(pacienteMR);


        if (pacienteBdExiste == null) {
            pacienteBdExiste = paciente;
            pacienteSetStringUUID.add(pacienteMR.getId().toString());
            return pacienteRepository.save(pacienteBdExiste);
        }


        if (!pacienteSetStringUUID.contains(pacienteMR.getId().toString())) {
            pacienteBdExiste.setMotivo(paciente.getMotivo());
            pacienteBdExiste.setNumero(paciente.getNumero());
            pacienteSetStringUUID.add(pacienteMR.getId().toString());
            return pacienteRepository.save(pacienteBdExiste);
        }

        return pacienteBdExiste;
    }


    private void salvandoNaoPossuiWhatsapp(PacienteMR pacienteMR) {
        var pacienteExiste = pacienteRepository.findBycodigo(pacienteMR.getId().toString());

        if (pacienteExiste == null) {
            var paciente = AssemblerPaciente.disassembleToObjectNaoPossuiWhatsapp(pacienteMR);
            pacienteRepository.save(paciente);
        }
    }

    private void salvandoNaoRespondido(Paciente paciente) {
        var pacienteBD = pacienteRepository.findById(paciente.getId());
        if (pacienteBD.isPresent()) {
            var pacienteExisteBD = pacienteBD.get();

            if (pacienteExisteBD.getMotivo().equalsIgnoreCase("AGUARDANDO")){
                paciente.setMotivo("NAO_RESPONDIDO");
                pacienteRepository.save(paciente);
            }
        }

    }


    private static boolean isFinalSemana() {
        LocalDate data = LocalDate.now(ZoneId.of("America/Recife"));
        return data.getDayOfWeek().toString().equalsIgnoreCase(DayOfWeek.SATURDAY.toString())
                || data.getDayOfWeek().toString().equalsIgnoreCase(DayOfWeek.SUNDAY.toString())
                || data.getDayOfWeek().toString().equalsIgnoreCase(DayOfWeek.FRIDAY.toString());
    }




    public void agendarLimpezaComDelayedExecutor() {
        if (WhatsappServiceN8N.isNotificado) return;
        WhatsappServiceN8N.isNotificado = true;

        CompletableFuture
                .runAsync(this::limpezaInterna,
                        CompletableFuture.delayedExecutor(48, TimeUnit.HOURS))
                .thenRun(() -> {
                    WhatsappServiceN8N.isNotificado = false;
                });
    }

    private void limpezaInterna() {

        if (!pacienteList.isEmpty()) {
            pacienteList.forEach(paciente -> {
                var pacienteBd = pacienteRepository.findById(paciente.getPacienteId());
                pacienteBd.ifPresent(this::salvandoNaoRespondido);


                try {
                    TimeUnit.SECONDS.sleep(10);

                    if (isFinalSemana()) {
                        n8NService.enviandoMensagemApos48Horas(
                                paciente.getNumero().toString(),
                                NAO_RESPONDIDA_MSG_PADRAO_FINAL_DE_SEMANA);
                    } else {
                        n8NService.enviandoMensagemApos48Horas(
                                paciente.getNumero().toString(),
                                NAO_RESPONDIDA_MSG_PADRAO);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            });
        }

        log.warn("____LIMPEZA_PERIÓDICA____");
        pacienteSetStringUUID.clear();
        pacienteList.clear();
    }




}
