package com.github.dio.mensageria.zgohorse.repository;

/**
 * Classe responsável pelo implementação do {@link PacienteRepositoryCustomSQL},
 * essa implementação possui um único método
 * que tem como propósito retornar dados filtrado para
 * {@link com.github.dio.mensageria.zgohorse.controller.EstatisticasController}.
 *
 * @author diogenesssantos.
 */
//@Repository
//public class PacienteRepositoryImpl implements PacienteRepositoryCustomSQL {
//    private static final Logger log = LoggerFactory.getLogger(PacienteRepositoryImpl.class);
//
//    @Autowired
//    private EntityManager entityManager;
//
//
//    /**
//     * O método possui api de {@link CriteriaBuilder} para acesso ao banco de dados.
//     * leia documentação do CriteriaAPI.
//     * @param filtroPaciente
//     * @return List<Paciente>
//     */
//    public List<Paciente> filtrar(FiltroPaciente filtroPaciente) {
//        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
//        CriteriaQuery<Paciente> querry = builder.createQuery(Paciente.class);
//        Root<Paciente> root = querry.from(Paciente.class);
//        List<Predicate> predicates = new ArrayList();
//
//
//        if (filtroPaciente.getNome() != null) {
//            predicates.add(builder.like(root.get("nome"), filtroPaciente.getNome() + "%"));
//        }
//
//        if (filtroPaciente.getBairro() != null) {
//            predicates.add(builder.like(root.get("bairro"), filtroPaciente.getBairro() + "%"));
//        }
//
//        if (filtroPaciente.getDataMarcacaoInicial() != null) {
//            predicates.add(builder.greaterThanOrEqualTo(root.get("dataMarcacao"), filtroPaciente.getDataMarcacaoInicial()));
//        }
//
//        if (filtroPaciente.getDataMarcacaoFinal() != null) {
//            predicates.add(builder.lessThanOrEqualTo(root.get("dataMarcacao"), filtroPaciente.getDataMarcacaoFinal()));
//        }
//
//        if (filtroPaciente.getTipoConsulta() != null) {
//            predicates.add(builder.like(root.get("consulta"), filtroPaciente.getTipoConsulta() + "%"));
//        }
//
//        if (filtroPaciente.getMotivo() != null) {
//            predicates.add(builder.and(builder.equal(root.get("motivo"), filtroPaciente.getMotivo())));
//        }
//
//        querry.where(predicates.toArray(new Predicate[0]));
//        TypedQuery<Paciente> pacienteTypedQuery = this.entityManager.createQuery(querry);
//        return pacienteTypedQuery.getResultList();
//    }
//}
