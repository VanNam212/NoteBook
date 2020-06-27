package com.example.notebook.controller;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notebook.R;
import com.example.notebook.model.Translator;
import com.example.notebook.model.Word;
import com.example.notebook.service.DBServiceImpl;
import com.example.notebook.service.TTSServiceImpl;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Add_UpdateForm extends AppCompatActivity {

    private TextView textViewTitle;
    private EditText et_OriginText;
    private EditText et_SubText;
    private TextView lang1;
    private TextView lang2;
    private ImageButton btnSwap;
    private ImageButton btnSpeak;
    private ImageButton btnSpeak2;
    private TextView lang3;
    private TextView lang4;
    private Button btnTrans;
    private ImageButton buttonClear;
    private Button btnOke;
    private Button btnCancel;
    private TextView tvAdd;
    private TextView tvEdit;
    private TextView tvFail2;
    private TextView tvSuccess;
    private TextView tvCancelClick;
    private Button btnCamera;
    private Button btnVoice;
    private ImageView imageView;

    private DBServiceImpl dbWord = new DBServiceImpl(this);
    private ArrayList<Word> wordArrayList;
    private Translator translator = new Translator();
    private TTSServiceImpl ttsService;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__update_form);
        setupWindowAnimations();

        //ánh xạ
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        et_OriginText = (EditText) findViewById(R.id.et_OriginText);
        et_SubText = (EditText) findViewById(R.id.et_SubText);
        lang1 = (TextView) findViewById(R.id.lang1);
        lang2 = (TextView) findViewById(R.id.lang2);
        lang3 = (TextView) findViewById(R.id.lang3);
        lang4 = (TextView) findViewById(R.id.lang4);
        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnSpeak2 = (ImageButton) findViewById(R.id.btnSpeak2);
        btnVoice = (Button) findViewById(R.id.btnVoice);
        btnTrans = (Button) findViewById(R.id.btnTrans);
        buttonClear = (ImageButton) findViewById(R.id.buttonClear);
        btnSwap = (ImageButton) findViewById(R.id.btnSwap);
        btnOke = (Button) findViewById(R.id.btnOke);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        tvAdd = (TextView) findViewById(R.id.tvAdd);
        tvEdit = (TextView) findViewById(R.id.tvEdit);
        tvSuccess = (TextView) findViewById(R.id.tvSuccess);
        tvFail2 = (TextView) findViewById(R.id.tvFail2);
        tvCancelClick = (TextView) findViewById(R.id.tvCancelClick);
        imageView = (ImageView) findViewById(R.id.imageView);

        ttsService = new TTSServiceImpl(Add_UpdateForm.this);
        wordArrayList = dbWord.getAllWords();

        final Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        final int id = bundle.getInt("id");
        final String description = bundle.getString("description");
        if (description.contains("add")) {
            textViewTitle.setText(tvAdd.getText().toString() + " " + bundle.getString("title"));
        } else {
            textViewTitle.setText(tvEdit.getText().toString() + " " + bundle.getString("title"));
            lang1.setText(bundle.getString("source"));
            lang3.setText(lang1.getText());
            lang2.setText(bundle.getString("target"));
            lang4.setText(lang2.getText());
            et_SubText.setText(bundle.getString("sub"));
        }
        et_OriginText.setText(bundle.getString("origin"));
        final String date = bundle.getString("date");

        if (lang3.getText().toString().equals("English")) {
            btnSpeak.setEnabled(true);
            btnSpeak2.setEnabled(false);
        } else {
            btnSpeak.setEnabled(false);
            btnSpeak2.setEnabled(true);
        }

        et_OriginText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });

        et_SubText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if (isOpen) {
                    Toast.makeText(Add_UpdateForm.this, "keyboard opened", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Add_UpdateForm.this, "keyboard closed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //event
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        btnTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translator.setTranslate_text(et_OriginText.getText().toString());
                translator.setSourceLanguage(lang1.getText().toString());
                translator.setTargetLanguage(lang2.getText().toString());
                translator.translate();
                et_SubText.setText(translator.getResult());
            }
        });

        btnSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = lang1.getText().toString();
                lang1.setText(lang2.getText().toString());
                lang2.setText(temp);
                temp = et_OriginText.getText().toString();
                et_OriginText.setText(et_SubText.getText().toString());
                et_SubText.setText(temp);
                temp = lang3.getText().toString();
                lang3.setText(lang4.getText().toString());
                lang4.setText(temp);
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_OriginText.setText("");
            }
        });

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsService.speak(et_OriginText.getText().toString().trim());
            }
        });

        btnSpeak2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsService.speak(et_SubText.getText().toString().trim());
            }
        });

        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //khai báo form RecognizerIntent.ACTION_RECOGNIZE_SPEECH
                Intent intent1 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                //đặt dữ liệu vào intent
                intent1.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent1.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                try {
                    //chuyển đến form speech
                    startActivityForResult(intent1, REQ_CODE_SPEECH_INPUT);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Your device don't support speech input",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsService.shutdown();
                boolean check = true;
                for (Word word : wordArrayList) {
                    if ((word.getOriginal_Text().equalsIgnoreCase(et_OriginText.getText().toString().trim()) && word.getSub_Text().equalsIgnoreCase(et_SubText.getText().toString().trim())) ||
                            (word.getOriginal_Text().equalsIgnoreCase(et_SubText.getText().toString().trim()) && word.getSub_Text().equalsIgnoreCase(et_OriginText.getText().toString().trim()))) {
                        Toast.makeText(getBaseContext(), "Existed", Toast.LENGTH_SHORT).show();
                        check = false;
                        break;
                    }
                }
                if (check) {
                    if (et_OriginText.getText().toString().trim().length() == 0 || et_SubText.getText().toString().trim().length() == 0) {
                        Toast.makeText(getBaseContext(), textViewTitle.getText() + " " + tvFail2.getText(), Toast.LENGTH_SHORT).show();
                    } else {
                        if (description.equals("add vocabulary")) {
                            dbWord.addWord(new Word(et_OriginText.getText().toString(), et_SubText.getText().toString(), date, lang1.getText().toString(), lang2.getText().toString()));
                        } else if (description.equals("add grammar")) {
                            dbWord.addGrammar(new Word(et_OriginText.getText().toString(), et_SubText.getText().toString(), date, lang1.getText().toString(), lang2.getText().toString()));
                        } else if (description.equals("edit vocabulary")) {
                            dbWord.updateWord(new Word(id, et_OriginText.getText().toString(), et_SubText.getText().toString(), date, lang1.getText().toString(), lang2.getText().toString()));
                        } else {
                            dbWord.updateGrammar(new Word(id, et_OriginText.getText().toString(), et_SubText.getText().toString(), date, lang1.getText().toString(), lang2.getText().toString()));
                        }
                        Toast.makeText(getBaseContext(), textViewTitle.getText() + " " + tvSuccess.getText(), Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsService.shutdown();
                Toast.makeText(getBaseContext(), tvCancelClick.getText(), Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void setupWindowAnimations() {
        Fade fade1 = new Fade();
        fade1.setDuration(1000);
        getWindow().setExitTransition(fade1);

        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);

        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setReturnTransition(slide);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    Log.d("focus", "touchevent");
                    v.clearFocus();
                    UIUtil.hideKeyboard(Add_UpdateForm.this);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            detectTextFormImage();
        }
        if (requestCode == REQ_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
		//lấy ra kết quả sau khi nói
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	//gán vào edit text et_OriginText
            et_OriginText.setText(result.get(0));
        }
    }

    private void detectTextFormImage() {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        firebaseVisionTextRecognizer.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                displayTextFormImage(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Add_UpdateForm.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayTextFormImage(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.TextBlock> blockList = firebaseVisionText.getTextBlocks();
        if (blockList.size() == 0) {
            Toast.makeText(this, "No text found in image", Toast.LENGTH_SHORT).show();
        } else {
            
            for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
                String text = block.getText();
                
                et_OriginText.setText(text);
            }
        }
    }

}
