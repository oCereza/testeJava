import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

class Pessoa {
    protected final String nome;
    protected final LocalDate dataNascimento;
    
    public Pessoa(String nome, LocalDate dataNascimento) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }
    
    public String getNome() {
        return nome;
    }
    
    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
}

class Funcionario extends Pessoa {
    private BigDecimal salario;
    private final String funcao;
    
    public Funcionario(String nome, LocalDate dataNascimento, BigDecimal salario, String funcao) {
        super(nome, dataNascimento);
        this.salario = salario;
        this.funcao = funcao;
    }
    
    public BigDecimal getSalario() {
        return salario;
    }
    
    public String getFuncao() {
        return funcao;
    }
    
    public void aumentarSalario(BigDecimal percentual) {
        this.salario = this.salario.add(this.salario.multiply(percentual));
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat nf = NumberFormat.getInstance(new Locale("pt", "BR"));
        nf.setMinimumFractionDigits(2);
        return "Nome: " + nome + ", Data Nasc.: " + dataNascimento.format(formatter) +
               ", Salário: R$ " + nf.format(salario) + ", Função: " + funcao;
    }
}

public class Principal {
    private static final String NOME_REMOVER = "João";
    
    public static void main(String[] args) {
        List<Funcionario> funcionarios = new ArrayList<>(Arrays.asList(
            new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"),
            new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"),
            new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"),
            new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"),
            new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"),
            new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"),
            new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"),
            new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"),
            new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"),
            new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente")
        ));
        
        funcionarios.removeIf(f -> f.getNome().equals(NOME_REMOVER));
        
        funcionarios.forEach(System.out::println);
        
        funcionarios.forEach(f -> f.aumentarSalario(new BigDecimal("0.10")));
        
        Map<String, List<Funcionario>> funcionariosPorFuncao = funcionarios.stream()
            .collect(Collectors.groupingBy(Funcionario::getFuncao));
        
        funcionariosPorFuncao.forEach((funcao, lista) -> {
            System.out.println("\nFunção: " + funcao);
            lista.forEach(System.out::println);
        });
        
        System.out.println("\nFuncionários que fazem aniversário em outubro e dezembro:");
        funcionarios.stream()
            .filter(f -> f.getDataNascimento().getMonthValue() == 10 || f.getDataNascimento().getMonthValue() == 12)
            .forEach(System.out::println);
        
        Funcionario maisVelho = funcionarios.stream()
            .min(Comparator.comparing(Funcionario::getDataNascimento))
            .orElse(null);
        if (maisVelho != null) {
            int idade = LocalDate.now().getYear() - maisVelho.getDataNascimento().getYear();
            System.out.println("\nFuncionário mais velho: " + maisVelho.getNome() + ", Idade: " + idade);
        }
        
        System.out.println("\nFuncionários em ordem alfabética:");
        funcionarios.stream()
            .sorted(Comparator.comparing(Funcionario::getNome))
            .forEach(System.out::println);
        
        BigDecimal totalSalarios = funcionarios.stream()
            .map(Funcionario::getSalario)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("\nTotal dos salários: R$ " + NumberFormat.getInstance(new Locale("pt", "BR")).format(totalSalarios));
        
        System.out.println("\nSalários em múltiplos do mínimo:");
        BigDecimal salarioMinimo = new BigDecimal("1212.00");
        funcionarios.forEach(f -> {
            BigDecimal qtdSalariosMinimos = f.getSalario().divide(salarioMinimo, 2, RoundingMode.HALF_UP);
            System.out.println(f.getNome() + " ganha " + qtdSalariosMinimos + " salários mínimos.");
        });
    }
}
