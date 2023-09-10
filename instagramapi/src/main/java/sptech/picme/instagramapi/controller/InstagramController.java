package sptech.picme.instagramapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import sptech.picme.instagramapi.model.AccessToken;
import sptech.picme.instagramapi.model.ListData;
import sptech.picme.instagramapi.model.Media;
import sptech.picme.instagramapi.service.InstagramService;

import java.util.List;
import java.util.Optional;

@Tag(
        name = "Instagram Controller",
        description = "Controller responsável por realizar chamados a API do Instagram"
)

@RestController
@RequestMapping("/instagram")
public class InstagramController {

    @Autowired
    public InstagramService instagramService;

    @Operation(summary = "Obter access_token do usuário no Instagram", description = "Utilizamos o access_token para realizar chamadas a API do instagram com o acesso do usuário realizando os chamados")
    @PostMapping("/access_token")
    public ResponseEntity<AccessToken> getAccessToken(@RequestParam String codigo) {
        Mono<AccessToken> accessToken = instagramService.getAccessTokenApi(codigo);

        //Melhorar tratamento no retorno
        return ResponseEntity.status(200).body(accessToken.block());

    }

    @Operation(summary = "Obter lista de imagens do usuário", description = "Endpoint utilizado para listar as medias (imagens) do usuário no instagram")
    @GetMapping("/listaImagens")
    public ResponseEntity<ListData> getListData(@RequestParam String accessToken) {

        Mono<ListData> listDataMono = instagramService.getListMediasUser(accessToken);

        //Melhorar tratamento no retorno
        return ResponseEntity.status(200).body(listDataMono.block());

    }

    @Operation(summary = "Obter informações sobre a media", description = "Endpoint utilizado para obter detalhes sobre a media desejada")
    @GetMapping("/imagem")
    public ResponseEntity<Media> getMedia(@RequestParam String idImagem, @RequestParam String accessToken) {

        Mono<Media> media = instagramService.getInfoMedia(idImagem, accessToken);

        //Melhorar tratamento no retorno
        return ResponseEntity.status(200).body(media.block());

    }

    @Operation(summary = "Obter informações sobre todas as medias", description = "Endpoint utilizado para obter detalhes de todas as mídias ao mesmo tempo")
    @GetMapping("/imagens")
    public ResponseEntity<List<Media>> getListMedia(@RequestParam String accessToken) {

        List<Media> listMedia = instagramService.getInfoAllMedia(accessToken);

        return ResponseEntity.status(200).body(listMedia);

    }

}
