import br.edu.atitus.bibliodigital.entities.AudioLivro;
import br.edu.atitus.bibliodigital.entities.Livro;
import br.edu.atitus.bibliodigital.entities.LivroDigital;
import br.edu.atitus.bibliodigital.entities.LivroFisico;

void main() {
      
    List<Livro> livros = new ArrayList<>();

    int opcao;
    do {
        mostrarMenu();
        while (true) {
            try {
                opcao = Integer.parseInt(
                        IO.readln("Escolha uma opção: ")
                );
                break;
            } catch (NumberFormatException erro) {
                IO.println("Digite apenas números para a opção.");
            }
        }
        switch (opcao) {
            case 1 -> cadastrar(livros);
            case 2 -> imprimirLivros(livros);
            case 0 -> IO.println("Até logo!!!");
            default -> IO.println("Opção Inválida");
        }
        IO.readln("Pressione Enter para Continuar...");
    } while (opcao != 0);
}

void mostrarMenu() {
    IO.println("""
            ========== BIBLIODIGITAL ==========
            1 - Cadastrar livro
            2 - Listar livros
            0 - Sair
            ===================================
            """);
}

void cadastrar(List<Livro> livros) {
    String tipoLivro = """
            Qual tipo de livro deseja cadastrar?
            1 - Livro físico
            2 - Livro digital
            3 - Audiolivro

            Escolha uma opção:
            """;
    int opcao;

    while (true) {
        try {
            opcao = Integer.parseInt(IO.readln(tipoLivro));
            break;
        } catch (NumberFormatException erro) {
            IO.println("Digite 1, 2 ou 3.");
        }
    }
    if (opcao <1 || opcao >3) {
        IO.println("Opção de tipo de livro inválida");
        return;
    }
    Livro novoLivro;
    String titulo = IO.readln("Digite o título do livro: ");
    String autor = IO.readln("Digite o autor do livro: ");
    int anoPublicacao;

    while (true) {
        try {
            anoPublicacao = Integer.parseInt(
                    IO.readln("Digite o ano de publicação do livro: ")
            );
            break;
        } catch (NumberFormatException erro) {
            IO.println("Digite apenas números para o ano.");
        }
    }
    if (opcao == 1) {
        int quantidadeExemplares;

        while (true) {
            try {
                quantidadeExemplares = Integer.parseInt(
                        IO.readln("Digite o número de exemplares: ")
                );
                break;
            } catch (NumberFormatException erro) {
                IO.println("Digite apenas números para a quantidade.");
            }
        }
        String localizacao = IO.readln("Digite a localização: ");
        novoLivro = new LivroFisico(titulo, autor, anoPublicacao, quantidadeExemplares, localizacao);
    } else if (opcao == 2) {
        String formato = IO.readln("Digite o formato do arquivo: ");
        double tamanhoArquivo;

        while (true) {
            try {
                tamanhoArquivo = Double.parseDouble(
                        IO.readln("Digite o tamanho do arquivo em MB: ")
                );
                break;
            } catch (NumberFormatException erro) {
                IO.println("Digite apenas números para o tamanho do arquivo.");
            }
        }
        novoLivro = new LivroDigital(titulo, autor, anoPublicacao, formato, tamanhoArquivo);
    } else {
        int duracao;

        while (true) {
            try {
                duracao = Integer.parseInt(
                        IO.readln("Digite a duração do audiolivro em minutos: ")
                );
                break;
            } catch (NumberFormatException erro) {
                IO.println("Digite apenas números para a duração.");
            }
        }
        String narrador = IO.readln("Digite o nome do narrador: ");
        novoLivro = new AudioLivro(titulo, autor, anoPublicacao, duracao, narrador);
    }  
    livros.add(novoLivro);
}

void imprimirLivros(List<Livro> livros) {
    if (livros.isEmpty()) {
        IO.println("Não existe nenhum livro cadastrado");
    } else {
        for (Livro livro : livros) {
            IO.println(livro);
            IO.println("Tipo: " + livro.getTipo());

            if (livro instanceof LivroFisico) {
                LivroFisico fisico = (LivroFisico) livro;
                IO.println("Exemplares: " + fisico.getQuantidadeExemplares());
            }
        }
    }
}