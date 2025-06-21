package com.spai.service;

import reactor.core.publisher.Flux;

public interface AIService {

    String botResponse(String message);

    String MediaResponse(String message);

}
