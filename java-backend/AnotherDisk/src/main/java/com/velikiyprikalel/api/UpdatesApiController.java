package com.velikiyprikalel.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.NativeWebRequest;
import java.util.Optional;

@Controller
public class UpdatesApiController implements UpdatesApi {

    private final NativeWebRequest request;

    @Autowired
    public UpdatesApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

}
