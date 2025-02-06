package br.com.usacucar.screenmatch.principal;

import br.com.usacucar.screenmatch.model.DadosEpisodio;
import br.com.usacucar.screenmatch.model.DadosSerie;
import br.com.usacucar.screenmatch.model.DadosTemporada;
import br.com.usacucar.screenmatch.model.Episodio;
import br.com.usacucar.screenmatch.service.ConsumoApi;
import br.com.usacucar.screenmatch.service.ConverteDados;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private final String ENDERECO ="http://www.omdbapi.com/?t=";
    private final String API_KEY="&apikey=a02072f4";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    public void exibeMenu(){
        System.out.println("Digite o nome da série a ser buscada: ");
        var nomeSerie = scanner.nextLine();
        var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();
		for(int i = 1; i<=dados.totalTemporadas(); i++){
			json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+")+"&season=" + i + API_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);

//        for(int i = 0; i< dados.totalTemporadas(); i++){
//            List<DadosEpisodio> espisodiosTemporada = temporadas.get(i).episodios();
//            for (DadosEpisodio dadosEpisodio : espisodiosTemporada) {
//                System.out.println(dadosEpisodio.titulo());
//            }
//        }
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

//        List<String> nomes = Arrays.asList("Jacque", "Iasmin", "Paulo", "Rodrigo", "Nico");
//
//        nomes.stream().sorted().limit(3).filter(n -> n.startsWith("N") || n.startsWith("J")).map(n -> n.toUpperCase()).forEach(System.out::println);

        List<DadosEpisodio> dadosEpisodios = temporadas.stream().flatMap(t -> t.episodios().stream()).toList();
        dadosEpisodios.stream().filter(e -> !e.avaliacao().equalsIgnoreCase("N/A")).sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed()).limit(5).forEach(System.out::println);
        List<Episodio> episodios = temporadas.stream().flatMap(t -> t.episodios().stream().map(d -> new Episodio(t.numerosTemporada(), d))).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("A partir de qual ano voce deseja ver os espisodios? ");
        var ano = scanner.nextInt();
        scanner.nextLine();
    }
}
