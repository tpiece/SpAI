package com.spai.service;

import jakarta.annotation.Resource;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.ai.openai.*;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class SpeechServiceImpl implements SpeechService{

    @Resource
    OpenAiAudioSpeechModel openAiAudioSpeechModel;

    @Resource
    OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;

    @Resource
    ChatModel chatModel;

    private final OpenAiAudioSpeechOptions speechOptions;

    private final OpenAiAudioTranscriptionOptions transcriptionOptions;

    public SpeechServiceImpl(){
        this.speechOptions = OpenAiAudioSpeechOptions.builder()
                .model("tts-1")
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY)
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .speed(1.0f)
                .build();

        //语音翻译配置
        this.transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
                .model("whisper-1")
                .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                .temperature(0f)
                .build();
    }


    //文本转语音
    @Override
    public SpeechResponse TextToSpeech(String text) {
        SpeechPrompt speechPrompt = new SpeechPrompt(text, this.speechOptions);

        SpeechResponse response = openAiAudioSpeechModel.call(speechPrompt);
//        byte[] responseAsBytes = this.response.getResult().getOutput();
        return response;
    }

    //语音翻译
    @Override
    public AudioTranscriptionResponse SpeechToText(FileSystemResource audioFile) {
        AudioTranscriptionPrompt transcriptionRequest = new AudioTranscriptionPrompt(audioFile, this.transcriptionOptions);
        AudioTranscriptionResponse response = openAiAudioTranscriptionModel.call(transcriptionRequest);

        //response.getResult().getOutput();
        return response;
    }

    @Override
    public String SpeechToText(String filePath) {
        FileSystemResource audioResource = new FileSystemResource(filePath);

        UserMessage userMessage = new UserMessage("解释一下这段音频，不要给出（这段音频描述了....）这样的术语，直接给出内容",
                List.of(new Media(MimeTypeUtils.parseMimeType("audio/mp3"), audioResource)));

        ChatResponse response = chatModel.call(new Prompt(userMessage,
                OpenAiChatOptions.builder().model(OpenAiApi.ChatModel.GPT_4_O.getValue()).build()));

        return response.getResult().getOutput().getText();
    }


}
