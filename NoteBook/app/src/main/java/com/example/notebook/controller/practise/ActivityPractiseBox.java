package com.example.notebook.controller.practise;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notebook.R;
import com.example.notebook.model.Word;
import com.example.notebook.service.DBServiceImpl;

import java.util.ArrayList;
import java.util.Random;

public class ActivityPractiseBox extends AppCompatActivity {

    private static final int REQ_FINISH = 2411;

    private ImageButton buttonPre;
    private ImageButton buttonCheck;
    private ImageButton buttonNext;
    private TextView textViewNumber;
    private TextView textViewText;
    private TextView correct;
    private EditText editTextText;
    private ImageView imageView;

    private DBServiceImpl dbWord;
    private ArrayList<Word> wordArrayList;
    private ArrayList<Word> words = new ArrayList<>();
    private static int number_question = 1;
    private static int questions = 0;
    private static int correct_number = 0;
    private static int incorrect_number = 0;
    private int[] colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practise_box);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupWindowAnimations();

        buttonPre = (ImageButton) findViewById(R.id.buttonPre);
        buttonCheck = (ImageButton) findViewById(R.id.buttonCheck);
        buttonNext = (ImageButton) findViewById(R.id.buttonNext);
        textViewNumber = (TextView) findViewById(R.id.textViewNumber);
        textViewText = (TextView) findViewById(R.id.textViewText);
        editTextText = (EditText) findViewById(R.id.editTextText);
        correct = (TextView) findViewById(R.id.correct);
        imageView = (ImageView) findViewById(R.id.imageView2);

        init();

        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCheck.setVisibility(View.INVISIBLE);
                editTextText.setEnabled(false);
                if (editTextText.getText().toString().trim().equalsIgnoreCase(words.get(number_question - 1).getOriginal_Text())) {
                    editTextText.setBackground(getDrawable(R.drawable.vien_dung));
                    colors[number_question] = R.drawable.vien_dung;
                    imageView.setImageResource(R.drawable.img_true);
                    correct_number++;
                } else {
                    editTextText.setBackground(getDrawable(R.drawable.vien_do));
                    colors[number_question] = R.drawable.vien_do;
                    imageView.setImageResource(R.drawable.img_false);
                    incorrect_number++;
                }
                imageView.setVisibility(View.VISIBLE);
                words.get(number_question - 1).setOriginal_Text(editTextText.getText().toString().trim());
                setTextCorrect();
                if (correct_number + incorrect_number == questions) {
                    Intent intent = new Intent(ActivityPractiseBox.this, FinishPractise.class);
                    startActivityForResult(intent, REQ_FINISH);
                }
            }
        });

        //nếu bỏ button check, cho kiểm tra đáp án tự động và sau đó chuyển sang từ vựng khác
//        editTextText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                setTimer();
//            }
//        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPre.setVisibility(View.VISIBLE);      //cho phép hiển thị
                number_question++;
                textViewNumber.setText(number_question + "/" + questions);
                textViewText.setText(words.get(number_question - 1).getSub_Text());
                if (colors[number_question] == 0) {
                    buttonCheck.setVisibility(View.VISIBLE);
                    editTextText.setText("");
                    editTextText.setEnabled(true);
                    editTextText.setBackground(getDrawable(R.drawable.vien_practise));
                    imageView.setVisibility(View.INVISIBLE);    //không cho phép hiển thị
                } else {
                    editTextText.setText(words.get(number_question - 1).getOriginal_Text());
                    buttonCheck.setVisibility(View.INVISIBLE);
                    editTextText.setEnabled(false);
                    editTextText.setBackground(getDrawable(colors[number_question]));
                    if (colors[number_question] == R.drawable.vien_dung) {
                        imageView.setImageResource(R.drawable.img_true);
                    } else {
                        imageView.setImageResource(R.drawable.img_false);
                    }
                    imageView.setVisibility(View.VISIBLE);
                }
                if (number_question == questions) {
                    buttonNext.setVisibility(View.INVISIBLE);
                }
            }
        });

        buttonPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_question--;
                buttonNext.setVisibility(View.VISIBLE);
                textViewNumber.setText(number_question + "/" + questions);
                textViewText.setText(words.get(number_question - 1).getSub_Text());
                if (colors[number_question] == 0) {
                    buttonCheck.setVisibility(View.VISIBLE);
                    editTextText.setText("");
                    editTextText.setEnabled(true);
                    editTextText.setBackground(getDrawable(R.drawable.vien_practise));
                    imageView.setVisibility(View.INVISIBLE);
                } else {
                    editTextText.setText(words.get(number_question - 1).getOriginal_Text());
                    buttonCheck.setVisibility(View.INVISIBLE);
                    editTextText.setEnabled(false);
                    editTextText.setBackground(getDrawable(colors[number_question]));
                    if (colors[number_question] == R.drawable.vien_dung) {
                        imageView.setImageResource(R.drawable.img_true);
                    } else {
                        imageView.setImageResource(R.drawable.img_false);
                    }
                    imageView.setVisibility(View.VISIBLE);
                }
                if (number_question == 1) {
                    buttonPre.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void setTimer() {
        //sau 5s thì kiểm tra kết quả
        CountDownTimer timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                editTextText.setEnabled(false);
                if (editTextText.getText().toString().trim().equalsIgnoreCase(words.get(number_question - 1).getOriginal_Text())) {
                    editTextText.setBackground(getDrawable(R.drawable.vien_dung));
                    colors[number_question] = R.drawable.vien_dung;
                    imageView.setImageResource(R.drawable.img_true);
                    correct_number++;
                } else {
                    editTextText.setBackground(getDrawable(R.drawable.vien_do));
                    colors[number_question] = R.drawable.vien_do;
                    imageView.setImageResource(R.drawable.img_false);
                    incorrect_number++;
                }
                imageView.setVisibility(View.VISIBLE);
                words.get(number_question - 1).setOriginal_Text(editTextText.getText().toString().trim());
                setTextCorrect();
                if (correct_number + incorrect_number == questions) {
                    Intent intent = new Intent(ActivityPractiseBox.this, FinishPractise.class);
                    startActivityForResult(intent, REQ_FINISH);
                }

            }
        }.start();
        //sau 2 giây thì chuyển sang từ vựng khác
        timer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                number_question++;
                textViewNumber.setText(number_question + "/" + questions);
                textViewText.setText(words.get(number_question - 1).getSub_Text());
                if (colors[number_question] == 0) {
                    buttonCheck.setVisibility(View.VISIBLE);
                    editTextText.setText("");
                    editTextText.setEnabled(true);
                    editTextText.setBackground(getDrawable(R.drawable.vien_practise));
                    imageView.setVisibility(View.INVISIBLE);    //không cho phép hiển thị
                } else {
                    editTextText.setText(words.get(number_question - 1).getOriginal_Text());
                    buttonCheck.setVisibility(View.INVISIBLE);
                    editTextText.setEnabled(false);
                    editTextText.setBackground(getDrawable(colors[number_question]));
                    if (colors[number_question] == R.drawable.vien_dung) {
                        imageView.setImageResource(R.drawable.img_true);
                    } else {
                        imageView.setImageResource(R.drawable.img_false);
                    }
                    imageView.setVisibility(View.VISIBLE);
                }
                if (number_question == questions) {
                    buttonNext.setVisibility(View.INVISIBLE);
                }

            }
        }.start();
    }

    private void setupWindowAnimations() {
        Slide slide1 = new Slide();
        slide1.setDuration(1000);
        getWindow().setExitTransition(slide1);

        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setEnterTransition(slide);

        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setReturnTransition(fade);
    }

    private void init() {
        dbWord = new DBServiceImpl(ActivityPractiseBox.this);
        wordArrayList = dbWord.getAllWords();
        colors = new int[wordArrayList.size() + 1];
        questions = wordArrayList.size();
        getWord();

        setTextCorrect();
        textViewNumber.setText(number_question + "/" + questions);
        editTextText.setText("");
        editTextText.setEnabled(true);
        buttonCheck.setVisibility(View.VISIBLE);
        textViewText.setText(words.get(number_question - 1).getSub_Text());
        buttonPre.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        if (words.size() == 1) {
            buttonNext.setVisibility(View.INVISIBLE);
        } else {
            buttonNext.setVisibility(View.VISIBLE);
        }
        editTextText.setBackground(getDrawable(R.drawable.vien_practise));
    }

    private void getWord() {
        while (wordArrayList.size() != 0) {
            int index = new Random().nextInt(wordArrayList.size());
            words.add(wordArrayList.get(index));
            wordArrayList.remove(index);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        number_question = 1;
        correct_number = 0;
        incorrect_number = 0;
        init();
    }

    private void setTextCorrect() {
        correct.setText(correct_number + " correct / " + incorrect_number + " incorrect");
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_FINISH && resultCode == RESULT_OK) {
            onStart();
        }
    }
}
