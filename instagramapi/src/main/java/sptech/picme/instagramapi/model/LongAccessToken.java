package sptech.picme.instagramapi.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LongAccessToken {

    private String access_token;
    private String token_type;
    private int expires_in;

}
