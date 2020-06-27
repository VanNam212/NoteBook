package com.example.notebook.controller.practise;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognizerIntent;
import android.transition.Explode;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notebook.R;
import com.example.notebook.model.Word;
import com.example.notebook.service.DBServiceImpl;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class ActivityPractiseSpeak extends AppCompatActivity {

    private static final int REQ_FINISH = 2411;

    private TextView textViewNumber;
    private TextView textViewText;
    private TextView textViewResult;
    private TextView correct;
    //    private TextView incorrect;
//    private TextView textView;
    private ImageButton imageSpeak;

    private DBServiceImpl dbWord = new DBServiceImpl(ActivityPractiseSpeak.this);
    private ArrayList<Word> wordArrayList;
    private static int number_question = 1;
    private static int questions = 0;
    private static int correct_number = 0;
    private static int incorrect_number = 0;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practise_speak);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupWindowAnimations();

        textViewNumber = (TextView) findViewById(R.id.textViewNumber);
        textViewText = (TextView) findViewById(R.id.textViewText);
        textViewResult = (TextView) findViewById(R.id.textViewResult);
        correct = (TextView) findViewById(R.id.correct);
//        incorrect = (TextView) findViewById(R.id.incorrect);
//        textView = (TextView) findViewById(R.id.textView);
        imageSpeak = (ImageButton) findViewById(R.id.imageSpeak);

//        wordArrayList = dbWord.getAllWords();
//        questions = wordArrayList.size();
//
//        setTextCorrect();
//        setTextNumberQuestion();
//        setTextSpeak(getWord());

        init();

        //speech to text
        imageSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //forward activity RecognizerIntent
                Intent intent1 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent1.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent1.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                try {
                    startActivityForResult(intent1, REQ_CODE_SPEECH_INPUT);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Your device don't support speech input",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //t thích cái này, nhưng mà test chưa thấy gì
    private void setupWindowAnimations() {
        Fade fade1 = new Fade();
        fade1.setDuration(1000);
        getWindow().setExitTransition(fade1);

        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);

        Explode explode = new Explode();
        explode.setDuration(1000);
        getWindow().setReturnTransition(explode);
    }

    private void init() {
        //đặt textview
        number_question = 1;    //bắt đầu từ câu 1
        correct_number = 0;     //số câu đúng =0
        incorrect_number = 0;   //số câu sai =0
        textViewResult.setVisibility(View.INVISIBLE);       //ẩn textview kq, sau khi nói mới hiện ra
        wordArrayList = dbWord.getAllWords();
        questions = wordArrayList.size();

        setTextCorrect();
        setTextNumberQuestion();
        setTextSpeak(getWord());
    }

    @Override
    protected void onStart() {
        super.onStart();
        number_question = 1;
        correct_number = 0;
        incorrect_number = 0;
        init();
    }

    private Word getWord() {
        if (wordArrayList.size() > 0) {
            int index = new Random().nextInt(wordArrayList.size());
            Word word = wordArrayList.get(index);
            wordArrayList.remove(index);
            return word;
        } else {
            return null;
        }
    }

    private void setTextCorrect() {
        //ví dụ: 0 correct/0 incorrect
//        if (textView.getText().toString().equalsIgnoreCase("question")) {
        correct.setText(correct_number + " correct / " + incorrect_number + " incorrect");
//            incorrect.setText(incorrect_number + " " + "incorrect");
//        } else {
//            correct.setText(correct_number + " đúng / " + incorrect_number + " sai");
////            incorrect.setText(incorrect_number + " " + "sai");
//        }
    }

    private void setTextNumberQuestion() {
        //ví dụ: 1/5 (câu 1 trên tổng 5 câu)
        textViewNumber.setText(number_question + "/" + questions);
    }

    private void setTextSpeak(Word word) {
        if (word.getLanguage_Origin().equalsIgnoreCase("english")) {
            textViewText.setText(word.getOriginal_Text());
        } else {
            textViewText.setText(word.getSub_Text());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //set text for textViewResult which user speak
        if (requestCode == REQ_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            textViewResult.setText(result.get(0));
            textViewResult.setVisibility(View.VISIBLE);
            if (textViewResult.getText().toString().trim().equalsIgnoreCase(textViewText.getText().toString().trim())) {
                correct_number++;
                setTextCorrect();
                textViewResult.setBackground(getDrawable(R.drawable.vien_dung));
            } else {
                incorrect_number++;
                setTextCorrect();
                textViewResult.setBackground(getDrawable(R.drawable.vien_do));
            }
            //after waiting 3 seconds, forward next question
            CountDownTimer timer = new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    Word word = getWord();
                    if (word != null) {
                        number_question++;
                        setTextSpeak(word);
                        setTextNumberQuestion();
                        textViewResult.setText("");
                        textViewResult.setVisibility(View.INVISIBLE);
                        textViewResult.setBackground(getDrawable(R.drawable.vien_practise));
                    } else {
                        Intent intent = new Intent(ActivityPractiseSpeak.this, FinishPractise.class);
                        startActivityForResult(intent, REQ_FINISH);
                    }
                }
            }.start();
        } else if (requestCode == REQ_FINISH && resultCode == RESULT_OK) {
            onStart();
        }
    }

}
