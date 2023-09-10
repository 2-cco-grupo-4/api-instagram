package sptech.picme.instagramapi.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import sptech.picme.instagramapi.model.AccessToken;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.BodyInserters;
import sptech.picme.instagramapi.model.ItemData;
import sptech.picme.instagramapi.model.ListData;
import sptech.picme.instagramapi.model.Media;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Tag(name = "Instagram Service", description = "Serviço responsável por realizar as chamadas a API Externa do Instagram")

public class InstagramService {

    String picme_id = "1556911098134262";
    String picme_secret = "9e25f1e49cf74a41f362317268f06c5c";
    String grant_type = "authorization_code";
    String redirect_uri = "https://2-cco-grupo-4.github.io/site-instituicional/";

    private final WebClient webClient;

    public InstagramService(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.build();
    }

    @Operation(summary = "Obter access token do usuário do Instagram")
    public Mono<AccessToken> getAccessTokenApi(String codigo) {

        String endpoint_insta = "https://api.instagram.com/oauth/access_token";

        return webClient.post()
                .uri(endpoint_insta)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("client_id", picme_id)
                        .with("client_secret", picme_secret)
                        .with("grant_type", grant_type)
                        .with("redirect_uri", redirect_uri)
                        .with("code", codigo))
                .retrieve()
                .bodyToMono(AccessToken.class)
                .onErrorResume(e -> {
                    if (e instanceof WebClientResponseException) {
                        WebClientResponseException responseException = (WebClientResponseException) e;
                        String responseBody = new String(responseException.getResponseBodyAsByteArray());
                        System.out.println(responseBody);
                    }
                    return Mono.error(e);
                })
                .map(response -> {
                    AccessToken accessToken = new AccessToken();
                    accessToken.setAccess_token(response.getAccess_token());
                    accessToken.setUser_id(response.getUser_id());
                    return accessToken;
                });

    }

    @Operation(summary = "Listar medias de um usuário do Instagram")
    public Mono<ListData> getListMediasUser(String accessToken) {

        String endpoint_insta = "https://graph.instagram.com/me/media?fields=id,caption&access_token=" + accessToken;

        return webClient.get()
                .uri(endpoint_insta)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ListData.class)
                .onErrorResume(e -> {
                    if (e instanceof WebClientResponseException) {
                        WebClientResponseException responseException = (WebClientResponseException) e;
                        String responseBody = new String(responseException.getResponseBodyAsByteArray());
                        System.out.println(responseBody);
                    }
                    return Mono.error(e);
                })
                .map(response -> {
                    ListData listData = new ListData();
                    listData.setData(response.getData());
                    listData.setPaging(response.getPaging());
                    return listData;
                });

    }

    @Operation(summary = "Obter informações da media do Instagram")
    public Mono<Media> getInfoMedia(String idMedia, String accessToken) {

        String endpoint_insta = String.format("https://graph.instagram.com/%s?fields=id,media_type,media_url,permalink,caption,username,timestamp&access_token=%s", idMedia, accessToken);

        return webClient.get()
                .uri(endpoint_insta)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Media.class)
                .onErrorResume(e -> {
                    if (e instanceof WebClientResponseException) {
                        WebClientResponseException responseException = (WebClientResponseException) e;
                        String responseBody = new String(responseException.getResponseBodyAsByteArray());
                        System.out.println(responseBody);
                    }
                    return Mono.error(e);
                })
                .map(response -> {
                    Media media = new Media();
                    media.setId(response.getId());
                    media.setMedia_url(response.getMedia_url());
                    media.setMedia_type(response.getMedia_type());
                    media.setUsername(response.getUsername());
                    media.setTimestamp(response.getTimestamp());
                    media.setPermalink(response.getPermalink());
                    return media;
                });

    }

    @Operation(summary = "Obter as informações de todas as medias de um usuário do Instagram")
    public List<Media> getInfoAllMedia(String accessToken) {

        Mono<ListData> listDataMono = getListMediasUser(accessToken);

        ListData listData = listDataMono.block();

        List<ItemData> medias = listData.getData();

        List<Media> listMedia = new ArrayList<>();

        for (ItemData itemData : medias) {
            Mono<Media> mediaMono = getInfoMedia(itemData.getId(), accessToken);
            listMedia.add(mediaMono.block());
        }

        return listMedia;

    }

}
