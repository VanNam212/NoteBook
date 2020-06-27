package com.example.notebook.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateRemoteModel;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.Set;

public class Translator {
    private String translate_text;
    private String SourceLanguage;
    private String TargetLanguage;
    private String result;

    public Translator() {
    }

    public Translator(String translate_text, String sourceLanguage, String targetLanguage) {
        this.translate_text = translate_text;
        SourceLanguage = sourceLanguage;
        TargetLanguage = targetLanguage;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTranslate_text() {
        return translate_text;
    }

    public void setTranslate_text(String translate_text) {
        this.translate_text = translate_text;
    }

    public String getSourceLanguage() {
        return SourceLanguage;
    }

    public void setSourceLanguage(String sourceLanguage) {
        SourceLanguage = sourceLanguage;
    }

    public String getTargetLanguage() {
        return TargetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        TargetLanguage = targetLanguage;
    }

    private int getLanguage(String language) {
        if (language.equalsIgnoreCase("vietnamese")) {
            return 57;
        } else {
            return 11;
        }
    }

    public void translate() {
        downloadTranslatorAndTranslate();
    }

    private void translateText(FirebaseTranslator langTranslator) {
        //translate source text to english
        langTranslator.translate(getTranslate_text())
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@NonNull String translatedText) {
                                setResult(translatedText);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
    }

    private void downloadTranslatorAndTranslate() {
        //create translator for source and target languages
        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(getLanguage(getSourceLanguage()))
                        .setTargetLanguage(getLanguage(getTargetLanguage()))
                        .build();
        final FirebaseTranslator langTranslator =
                FirebaseNaturalLanguage.getInstance().getTranslator(options);

        //download language models if needed
        final FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        langTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                translateText(langTranslator);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
    }
    private void deleteDownloadedModel(int source) {
        FirebaseModelManager modelManager = FirebaseModelManager.getInstance();

        // Get translation models stored on the device.
        modelManager.getDownloadedModels(FirebaseTranslateRemoteModel.class)
                .addOnSuccessListener(new OnSuccessListener<Set<FirebaseTranslateRemoteModel>>() {
                    @Override
                    public void onSuccess(Set<FirebaseTranslateRemoteModel> models) {
                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error.
                    }
                });

        // Delete the German model if it's on the device.
        FirebaseTranslateRemoteModel deModel =
                new FirebaseTranslateRemoteModel.Builder(source).build();
        modelManager.deleteDownloadedModel(deModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model deleted.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error.
                    }
                });
    }
}
