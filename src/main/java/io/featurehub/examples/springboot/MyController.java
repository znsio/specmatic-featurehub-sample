package io.featurehub.examples.springboot;

import io.featurehub.client.ClientContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.inject.Provider;

@RestController
public class MyController {
    @Value("${featurehub.217027.id}")
    private String feature;
    private final Provider<ClientContext> ctxProvider;

    @Inject
    public MyController(Provider<ClientContext> ctxProvider) {
        this.ctxProvider = ctxProvider;
    }

    @GetMapping("/store/isOpen")
    public ResponseEntity<String> isStoreOpen() {
        ClientContext ctx = ctxProvider.get();
        boolean featureFlagState = ctx.feature(feature).isEnabled();
        if (featureFlagState) {
            return ResponseEntity.ok("Yes, the store is open for shopping");
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("No, the store is under construction yet");
    }

    @GetMapping("/store/isClose")
    public ResponseEntity<String> isStoreClose() {
        ClientContext ctx = ctxProvider.get();
        boolean featureFlagState = ctx.feature(feature).isEnabled();
        if (featureFlagState) {
            return ResponseEntity.ok("Yes, the store is open for shopping");
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("No, the store is under construction yet");
    }
}
