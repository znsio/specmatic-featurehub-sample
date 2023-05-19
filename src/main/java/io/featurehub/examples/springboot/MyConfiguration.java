package io.featurehub.examples.springboot;

import io.featurehub.client.ClientContext;
import io.featurehub.client.ClientFeatureRepository;
import io.featurehub.client.EdgeFeatureHubConfig;
import io.featurehub.client.FeatureHubConfig;
import io.featurehub.client.jersey.JerseyClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration {

    @Value("${featurehub.endpoint-url}")
    private String host;
    @Value("${featurehub.client-eval-key}")
    private String clientKey;
    @Bean
    public FeatureHubConfig featureHubConfig() {
        FeatureHubConfig config = new EdgeFeatureHubConfig(this.host, this.clientKey);
        ClientFeatureRepository repository = new ClientFeatureRepository();
        JerseyClient jerseyClient = new JerseyClient(config, true, repository, null);
        jerseyClient.setShutdownOnServerFailure(false);
        config.setEdgeService(() -> {
            return jerseyClient;
        });
        config.setRepository(repository);
        return config;
    }

    @Bean
    public ClientContext featureHubClient(FeatureHubConfig fhConfig) {
        return fhConfig.newContext();
    }

}
