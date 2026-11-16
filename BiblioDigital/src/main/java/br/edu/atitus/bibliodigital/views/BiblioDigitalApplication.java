
package br.edu.atitus.bibliodigital.views;

import br.edu.atitus.bibliodigital.entities.AudioLivro;
import br.edu.atitus.bibliodigital.entities.Livro;
import br.edu.atitus.bibliodigital.entities.LivroDigital;
import br.edu.atitus.bibliodigital.entities.LivroFisico;
import br.edu.atitus.bibliodigital.services.LivroService;

import java.util.List;

public class BiblioDigitalApplication {

    private final LivroService service = new LivroService();

    void main() {
        int opcao;

        do {
            mostrarMenu();
            opcao = lerInteiro("Escolha uma das opções acima: ");

            try {
                switch (opcao) {
                    case 1 -> cadastrar();
                    case 2 -> pesquisarTitulo();
                    case 3 -> pesquisarAutor();
                    case 4 -> alterar();
                    case 5 -> excluir();
                    case 0 -> IO.println("Até logo!!!");
                    default -> IO.println("Opção inválida");
                }
            } catch (RuntimeException erro) {
                IO.println("Erro: " + erro.getMessage());
            }

            if (opcao != 0)
                IO.readln("Pressione Enter para continuar...");

        } while (opcao != 0);
    }

    void mostrarMenu() {
        IO.println("""
                ===== Bem vindo ao BiblioDigital =====

                Menu:
                1 - Cadastrar novo Livro
                2 - Pesquisar Livro por título
                3 - Pesquisar Livro por autor
                4 - Alterar dados do Livro
                5 - Excluir Livro
                0 - Sair

                """);
    }

    int lerInteiro(String mensagem) {
        while (true) {
            try {
                return Integer.parseInt(IO.readln(mensagem).trim());
            } catch (NumberFormatException erro) {
                IO.println("Digite apenas números inteiros.");
            }
        }
    }

    double lerDouble(String mensagem) {
        while (true) {
            try {
                String valor = IO.readln(mensagem).trim();
                return Double.parseDouble(valor.replace(",", "."));
            } catch (NumberFormatException erro) {
                IO.println("Digite um número válido.");
            }
        }
    }

    Livro criarLivro(int tipo) {
        String titulo = IO.readln("Digite o título do livro: ");
        String autor = IO.readln("Digite o autor do livro: ");

        int anoPublicacao = lerInteiro(
                "Digite o ano de publicação do livro: "
        );

        if (tipo == 1) {
            int quantidadeExemplares = lerInteiro(
                    "Digite o número de exemplares: "
            );

            String localizacao = IO.readln("Digite a localização: ");

            return new LivroFisico(
                    titulo, autor, anoPublicacao,
                    quantidadeExemplares, localizacao.trim()
            );

        } else if (tipo == 2) {
            String formato = IO.readln("Digite o formato do arquivo: ");

            double tamanhoArquivo = lerDouble(
                    "Digite o tamanho do arquivo em MB: "
            );

            return new LivroDigital(
                    titulo, autor, anoPublicacao,
                    formato.trim(), tamanhoArquivo
            );

        } else {
            int duracao = lerInteiro(
                    "Digite a duração do audiolivro em minutos: "
            );

            String narrador = IO.readln("Digite o nome do narrador: ");

            return new AudioLivro(
                    titulo, autor, anoPublicacao,
                    duracao, narrador.trim()
            );
        }
    }

    void cadastrar() {
        IO.println("""
                Qual tipo de livro deseja cadastrar?
                1 - Livro físico
                2 - Livro digital
                3 - Audiolivro
                """);

        int tipo = lerInteiro("Escolha uma opção: ");

        if (tipo < 1 || tipo > 3) {
            IO.println("Opção de tipo de livro inválida");
            return;
        }

        Livro novoLivro = criarLivro(tipo);

        service.inserir(novoLivro);

        IO.println("Livro cadastrado com sucesso!");
    }

    void imprimirLivros(List<Livro> livros) {
        if (livros.isEmpty()) {
            IO.println("Nenhum livro encontrado.");
            return;
        }

        for (Livro livro : livros) {
            IO.println(livro);
            IO.println("Tipo: " + livro.getTipo());
            IO.println("----------------------------");
        }
    }

    void pesquisarTitulo() {
        String pesquisa = IO.readln("Pesquisa por título: ");

        List<Livro> livrosEncontrados = service.findByTitulo(pesquisa);

        imprimirLivros(livrosEncontrados);
    }

    void pesquisarAutor() {
        String pesquisa = IO.readln("Pesquisa por autor: ");

        List<Livro> livrosEncontrados = service.findByAutor(pesquisa);

        imprimirLivros(livrosEncontrados);
    }

    void listarLivros() {
        List<Livro> livros = service.findAll();

        if (livros.isEmpty()) {
            IO.println("Não existem livros cadastrados.");
            return;
        }

        IO.println("===== Livros cadastrados =====");

        for (int i = 0; i < livros.size(); i++) {
            IO.println((i + 1) + " - " + livros.get(i).getTitulo());
        }
    }

    void alterar() {
        List<Livro> livros = service.findAll();

        if (livros.isEmpty()) {
            IO.println("Não existem livros cadastrados.");
            return;
        }

        listarLivros();

        int indice = lerInteiro(
                "Informe o índice do livro que deseja alterar: "
        );

        Livro livroAtual = service.findByIndice(indice);

        IO.println("===== Dados atuais do livro =====");
        IO.println(livroAtual);
        IO.println("Tipo: " + livroAtual.getTipo());

        IO.println("===== Informe os novos dados =====");

        int tipo;

        if (livroAtual instanceof LivroFisico) {
            tipo = 1;
        } else if (livroAtual instanceof LivroDigital) {
            tipo = 2;
        } else {
            tipo = 3;
        }

        Livro novoLivro = criarLivro(tipo);

        service.alterar(indice, novoLivro);

        IO.println("Livro alterado com sucesso!");
    }

    void excluir() {
        List<Livro> livros = service.findAll();

        if (livros.isEmpty()) {
            IO.println("Não existem livros cadastrados.");
            return;
        }

        listarLivros();

        int indice = lerInteiro(
                "Informe o índice do livro que deseja excluir: "
        );

        Livro livro = service.findByIndice(indice);

        IO.println("Livro selecionado:");
        IO.println(livro);

        String confirmacao = IO.readln(
                "Tem certeza que deseja excluir? (S/N): "
        );

        if (confirmacao.trim().equalsIgnoreCase("S")) {
            service.excluir(indice);
            IO.println("Livro excluído com sucesso!");
        } else {
            IO.println("Exclusão cancelada.");
        }
    }
}
