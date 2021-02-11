package com.joelkell.demo.security;

import com.joelkell.demo.services.users.User;
import com.nimbusds.jose.shaded.json.JSONObject;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

    @Inject
    @Client("/")
    RxHttpClient client;

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        JSONObject user = new JSONObject();

        user.put("username", authenticationRequest.getIdentity());
        user.put("email", "email@email.com");
        user.put("password", authenticationRequest.getSecret());

        HttpRequest<?> request = HttpRequest.POST("/users/password", user);
        HttpResponse<Boolean> rsp = client.toBlocking().exchange(request, Boolean.class);

        return Flowable.create(emitter -> {
            if (rsp.body()) {
                HttpRequest<?> userRequest = HttpRequest.GET("/users/username/"+authenticationRequest.getIdentity());
                HttpResponse<User> userResponse = client.toBlocking().exchange(userRequest, User.class);
                emitter.onNext(new UserDetails(userResponse.body().getId().toString(), new ArrayList<>()));
                emitter.onComplete();
            } else {
                emitter.onError(new AuthenticationException(new AuthenticationFailed()));
            }
        }, BackpressureStrategy.ERROR);
    }
}
