package com.project.sisvencal.configuration;

import com.paypal.base.rest.*;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
public class PaypalConfig {

    // Inyección de propiedades desde el archivo de configuración
    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    // Configura el SDK de PayPal con el modo (sandbox o live)
    @Bean
    public Map<String, String> paypalSdkConfig() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", mode);
        return configMap;
    }

    // Crea y devuelve las credenciales OAuth para autenticación con PayPal
    @Bean
    public OAuthTokenCredential oAuthTokenCredential() {
        return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
    }

    // Crea y devuelve el contexto de la API de PayPal para realizar operaciones
    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        // Configura el contexto con el token de acceso y la configuración de PayPal
        APIContext context = new APIContext(oAuthTokenCredential().getAccessToken());
        context.setConfigurationMap(paypalSdkConfig());
        return context;
    }
}
