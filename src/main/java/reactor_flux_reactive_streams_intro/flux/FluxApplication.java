package reactor_flux_reactive_streams_intro.flux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor_flux_reactive_streams_intro.flux.models.Usuario;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class FluxApplication implements CommandLineRunner {


    private static final Logger log = LoggerFactory.getLogger(FluxApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(FluxApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        List<String> usuariosList = new ArrayList<>();
        usuariosList.add("Andres Guzman");
        usuariosList.add("Pedro Fulano");
        usuariosList.add("Diego Sultano");
        usuariosList.add("Juan Mengano");
        usuariosList.add("Bruce Lee");
        usuariosList.add("Bruce Willis");

        Flux<String> nombres = Flux.fromIterable(usuariosList); /*Flux.just("Andres Guzman", "Pedro Fulano", "Diego Sultano", "Juan Mengano", "Bruce Lee", "Bruce Wills");*/

        Flux<Usuario> usuarios = nombres.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
                .filter(usuario ->  usuario.getNombre().toLowerCase().equals("bruce"))
                .doOnNext(usuario -> {
                    if (usuario == null) {
                        throw new RuntimeException("Nombres no pueden ser vacíos");
                    }
                    System.out.println(usuario.getNombre().concat(" ").concat(usuario.getApellido()));
                }).map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                });

//      Flux<String> nombres = Flux.just("Andres", "Pedro", "Diego", "Juan").doOnNext(System.out::println);    Esto sería lo mimso utilizando Jave 8

        usuarios.subscribe(elemento -> log.info(elemento.getNombre()),
                error -> log.error(error.getMessage()),
                new Runnable() {
                    @Override
                    public void run() {
                        log.info("Ha finalizado la ejecución del observable con éxito!");
                    }
                });

    }
}
