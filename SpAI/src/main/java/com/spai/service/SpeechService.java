package com.spai.service;

import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.core.io.FileSystemResource;
import reactor.core.publisher.Flux;

public interface SpeechService {
    public SpeechResponse TextToSpeech(String text);

    public AudioTranscriptionResponse SpeechToText(FileSystemResource audioFile);

    public String SpeechToText(String filePath);
}
