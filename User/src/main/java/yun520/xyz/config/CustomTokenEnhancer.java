package yun520.xyz.config;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        // Add custom parameters to the access token
        Map<String, Object> additionalInfo = new HashMap<>();
//        ArrayList<Map<String,String>> details = (ArrayList<Map<String,String>>) authentication.getUserAuthentication().getDetails();

//        在这里添加用户id
        additionalInfo.put("custom_param_1", "value1");
        additionalInfo.put("custom_param_2", "value2");
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}