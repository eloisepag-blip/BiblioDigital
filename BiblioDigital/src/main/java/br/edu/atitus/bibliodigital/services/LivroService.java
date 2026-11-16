
package br.edu.atitus.bibliodigital.services;

import br.edu.atitus.bibliodigital.entities.Livro;
import br.edu.atitus.bibliodigital.repositories.LivroRepository;

import java.util.List;

public class LivroService {

    private final LivroRepository repository = new LivroRepository();

    private void validarLivro(Livro livro, Livro livroAtual) {
        if (livro == null)
            throw new RuntimeException("Objeto nulo");

        if (livro.getTitulo() == null || livro.getTitulo().isBlank())
            throw new RuntimeException("Título inválido");

        if (livro.getAutor() == null || livro.getAutor().isBlank())
            throw new RuntimeException("Autor inválido");

        if (livro.getAnoPublicacao() < 1900)
            throw new RuntimeException(
                    "O ano de publicação deve ser igual ou superior a 1900"
            );

        livro.setTitulo(livro.getTitulo().trim());
        livro.setAutor(livro.getAutor().trim());

        Livro existente = repository.findByTituloExato(livro.getTitulo());

        if (existente != null && existente != livroAtual)
            throw new RuntimeException(
                    "Já existe um livro cadastrado com esse título"
            );
    }

    public void inserir(Livro livro) {
        validarLivro(livro, null);
        repository.inserir(livro);
    }

    public List<Livro> findAll() {
        return repository.findAll();
    }

    public List<Livro> findByTitulo(String parametro) {
        if (parametro == null || parametro.isBlank())
            throw new RuntimeException("Parâmetro de pesquisa inválido");

        return repository.findByTitulo(parametro.trim());
    }

    public List<Livro> findByAutor(String parametro) {
        if (parametro == null || parametro.isBlank())
            throw new RuntimeException("Parâmetro de pesquisa inválido");

        return repository.findByAutor(parametro.trim());
    }

    public Livro findByIndice(int indice) {
        List<Livro> livros = repository.findAll();

        if (livros.isEmpty())
            throw new RuntimeException("Não existem livros cadastrados");

        if (indice < 1 || indice > livros.size())
            throw new RuntimeException("Índice inválido");

        return livros.get(indice - 1);
    }

    public void alterar(int indice, Livro novoLivro) {
        Livro livroAtual = findByIndice(indice);

        validarLivro(novoLivro, livroAtual);

        repository.alterar(indice - 1, novoLivro);
    }

    public void excluir(int indice) {
        findByIndice(indice);
        repository.excluir(indice - 1);
    }
}
