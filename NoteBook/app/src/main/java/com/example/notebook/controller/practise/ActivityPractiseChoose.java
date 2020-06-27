package com.example.notebook.controller.practise;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.transition.Explode;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notebook.R;
import com.example.notebook.model.Word;
import com.example.notebook.service.DBServiceImpl;

import java.util.ArrayList;
import java.util.Random;

public class ActivityPractiseChoose extends AppCompatActivity {

    //nếu mà trong cơ sở dữ liệu có "hello","hi", cả 2 có cùng nghĩa thì đ.a chỉ đc lấy 1 nghĩa thôi
    //cái đống string ở dưới để dùng cho trường hợp có 4 từ vựng và nghĩa trùng nhau, thì lấy 1 nghĩa đúng
    //và thêm cái đống string này để đặt cho các đ.a khác
    private static final String DA1_TA = "Heroes";
    private static final String DA2_TA = "Tonight";
    private static final String DA3_TA = "Release";

    private static final String DA1_TV = "Tượng";
    private static final String DA2_TV = "Nướng";
    private static final String DA3_TV = "Từ đó";

    //cái này để ktra xem mình cần đ.a là tiếng anh hay tiếng việt vì t random câu hỏi nên kb câu hỏi là t.a hay t.v
    private static final int DA_TA = 212;
    private static final int DA_TV = 211;
    //đặt id cho form khác
    private static final int REQ_FINISH = 2411;     //ngày sinh của c nhỉ :v

    private TextView textViewNumber;
    private TextView textViewText;
    private TextView correct;
    //    private TextView incorrect;
//    private TextView textView;
    private Button btnDA1;
    private Button btnDA2;
    private Button btnDA3;
    private Button btnDA4;

    private DBServiceImpl dbWord = new DBServiceImpl(ActivityPractiseChoose.this);
    private ArrayList<Word> wordArrayList;
    private ArrayList<Word> words;
    private boolean[] check;
    private static int number_question = 1;
    private static int questions = 0;
    private static int correct_number = 0;
    private static int incorrect_number = 0;
    private static int answer_correct = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practise_choose);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupWindowAnimations();

        textViewNumber = (TextView) findViewById(R.id.textViewNumber);
        textViewText = (TextView) findViewById(R.id.textViewText);
        correct = (TextView) findViewById(R.id.correct);
//        incorrect = (TextView) findViewById(R.id.incorrect);
//        textView = (TextView) findViewById(R.id.textView);
        btnDA1 = (Button) findViewById(R.id.btnDA1);
        btnDA2 = (Button) findViewById(R.id.btnDA2);
        btnDA3 = (Button) findViewById(R.id.btnDA3);
        btnDA4 = (Button) findViewById(R.id.btnDA4);

//        wordArrayList = dbWord.getAllWords();
//        words = dbWord.getAllWords();
//        questions = wordArrayList.size();
//        check = new boolean[questions];
//
//        setTextCorrect();
//        textViewNumber.setText(number_question + "/" + questions);
//        setTextViewText(getWord());

        init();

        btnDA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer_correct == 0) {
                    //nếu đ.a 1 đúng thì đặt màu cho cái đ.a đó
                    btnDA1.setBackground(getDrawable(R.drawable.vien_dung));
                    //số câu đúng tăng lên
                    correct_number++;
                } else {
                    //nếu chọn đ.a 1 nhưng bị sai thì đặt màu đỏ cho đ.a 1
                    btnDA1.setBackground(getDrawable(R.drawable.vien_do));
                    //đặt màu cho đ.a đúng
                    if (answer_correct == 1) {
                        btnDA2.setBackground(getDrawable(R.drawable.vien_dung));
                    }
                    if (answer_correct == 2) {
                        btnDA3.setBackground(getDrawable(R.drawable.vien_dung));
                    }
                    if (answer_correct == 3) {
                        btnDA4.setBackground(getDrawable(R.drawable.vien_dung));
                    }
                    incorrect_number++;
                }
                setTextCorrect();
                setTimer();
            }
        });

        btnDA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer_correct == 1) {
                    btnDA2.setBackground(getDrawable(R.drawable.vien_dung));
                    correct_number++;
                } else {
                    btnDA2.setBackground(getDrawable(R.drawable.vien_do));
                    if (answer_correct == 0) {
                        btnDA1.setBackground(getDrawable(R.drawable.vien_dung));
                    }
                    if (answer_correct == 2) {
                        btnDA3.setBackground(getDrawable(R.drawable.vien_dung));
                    }
                    if (answer_correct == 3) {
                        btnDA4.setBackground(getDrawable(R.drawable.vien_dung));
                    }
                    incorrect_number++;
                }
                setTextCorrect();
                setTimer();
            }
        });

        btnDA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer_correct == 2) {
                    btnDA3.setBackground(getDrawable(R.drawable.vien_dung));
                    correct_number++;
                } else {
                    btnDA3.setBackground(getDrawable(R.drawable.vien_do));
                    if (answer_correct == 1) {
                        btnDA2.setBackground(getDrawable(R.drawable.vien_dung));
                    }
                    if (answer_correct == 0) {
                        btnDA1.setBackground(getDrawable(R.drawable.vien_dung));
                    }
                    if (answer_correct == 3) {
                        btnDA4.setBackground(getDrawable(R.drawable.vien_dung));
                    }
                    incorrect_number++;
                }
                setTextCorrect();
                setTimer();
            }
        });

        btnDA4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer_correct == 3) {
                    btnDA4.setBackground(getDrawable(R.drawable.vien_dung));
                    correct_number++;
                } else {
                    btnDA4.setBackground(getDrawable(R.drawable.vien_do));
                    if (answer_correct == 1) {
                        btnDA2.setBackground(getDrawable(R.drawable.vien_dung));
                    }
                    if (answer_correct == 2) {
                        btnDA3.setBackground(getDrawable(R.drawable.vien_dung));
                    }
                    if (answer_correct == 0) {
                        btnDA1.setBackground(getDrawable(R.drawable.vien_dung));
                    }
                    incorrect_number++;
                }
                setTextCorrect();
                setTimer();
            }
        });
    }

    private void setupWindowAnimations() {
        Explode explode1 = new Explode();
        explode1.setDuration(1000);
        getWindow().setExitTransition(explode1);

        Explode explode = new Explode();
        explode.setDuration(1000);
        getWindow().setEnterTransition(explode);

        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setReturnTransition(slide);
    }

    private void init() {
        number_question = 1;
        correct_number = 0;
        incorrect_number = 0;
        wordArrayList = dbWord.getAllWords();
        words = dbWord.getAllWords();
        questions = wordArrayList.size();
        check = new boolean[questions];

        setTextCorrect();
        textViewNumber.setText(number_question + "/" + questions);
        btnDA1.setBackground(getDrawable(R.drawable.vien_practise));
        btnDA2.setBackground(getDrawable(R.drawable.vien_practise));
        btnDA3.setBackground(getDrawable(R.drawable.vien_practise));
        btnDA4.setBackground(getDrawable(R.drawable.vien_practise));
        setTextViewText(getWord());
    }

    @Override
    protected void onStart() {
        super.onStart();
        number_question = 1;
        correct_number = 0;
        incorrect_number = 0;
        init();
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

    private Word getWord() {
        words = dbWord.getAllWords();
        if (number_question < questions) {
            int index = new Random().nextInt(wordArrayList.size());
            while (check[index]) {
                index = new Random().nextInt(wordArrayList.size());
            }
            Word word = wordArrayList.get(index);
            words.remove(index);
            check[index] = true;
            return word;
        } else {
            return null;
        }
    }

    private void setTextCorrect() {
//        if (textView.getText().toString().equalsIgnoreCase("question")) {
        correct.setText(correct_number + " correct / " + incorrect_number + " incorrect");
//            incorrect.setText(incorrect_number + " " + "incorrect");
//        } else {
//            correct.setText(correct_number + " đúng / " + incorrect_number + " sai");
////            incorrect.setText(incorrect_number + " " + "sai");
//        }
    }

    private void setTextViewText(Word word) {
        int i = new Random().nextInt(2);
        if (i == 0) {
            textViewText.setText(word.getOriginal_Text());
            i = new Random().nextInt(4);
            if (word.getLanguage_Origin().equalsIgnoreCase("english")) {
                setTextTrueButton(i, word.getSub_Text(), DA_TV);
            } else {
                setTextTrueButton(i, word.getSub_Text(), DA_TA);
            }
        } else {
            textViewText.setText(word.getSub_Text());
            i = new Random().nextInt(4);
            if (word.getLanguage_Sub().equalsIgnoreCase("english")) {
                setTextTrueButton(i, word.getOriginal_Text(), DA_TV);
            } else {
                setTextTrueButton(i, word.getOriginal_Text(), DA_TA);
            }
        }
    }

    private void setTextTrueButton(int i, String res, int description) {
        if (i == 0) {
            answer_correct = 0;
            btnDA1.setText(res);
            String str = setTextAnotherButton(description);
            if (str.equalsIgnoreCase(res)) {
                if (description == DA_TA)
                    btnDA2.setText(DA1_TA);
                else
                    btnDA2.setText(DA1_TV);
            } else {
                btnDA2.setText(str);
            }
//            btnDA2.setText(setTextAnotherButton(description));
//            btnDA3.setText(setTextAnotherButton(description));
            str = setTextAnotherButton(description);
            if (str.equalsIgnoreCase(res)) {
                if (description == DA_TA)
                    btnDA3.setText(DA2_TA);
                else
                    btnDA3.setText(DA2_TV);
            } else {
                btnDA3.setText(str);
            }
//            btnDA4.setText(setTextAnotherButton(description));
            str = setTextAnotherButton(description);
            if (str.equalsIgnoreCase(res)) {
                if (description == DA_TA)
                    btnDA4.setText(DA3_TA);
                else
                    btnDA4.setText(DA3_TV);
            } else {
                btnDA4.setText(str);
            }
        } else if (i == 1) {
            answer_correct = 1;
            btnDA2.setText(res);
//            btnDA1.setText(setTextAnotherButton(description));
//            btnDA3.setText(setTextAnotherButton(description));
//            btnDA4.setText(setTextAnotherButton(description));
            String str = setTextAnotherButton(description);
            if (str.equalsIgnoreCase(res)) {
                if (description == DA_TA)
                    btnDA1.setText(DA1_TA);
                else
                    btnDA1.setText(DA1_TV);
            } else {
                btnDA1.setText(str);
            }
            str = setTextAnotherButton(description);
            if (str.equalsIgnoreCase(res)) {
                if (description == DA_TA)
                    btnDA3.setText(DA2_TA);
                else
                    btnDA3.setText(DA2_TV);
            } else {
                btnDA3.setText(str);
            }
            str = setTextAnotherButton(description);
            if (str.equalsIgnoreCase(res)) {
                if (description == DA_TA)
                    btnDA4.setText(DA3_TA);
                else
                    btnDA4.setText(DA3_TV);
            } else {
                btnDA4.setText(str);
            }
        } else if (i == 2) {
            answer_correct = 2;
            btnDA3.setText(res);
//            btnDA2.setText(setTextAnotherButton(description));
//            btnDA1.setText(setTextAnotherButton(description));
//            btnDA4.setText(setTextAnotherButton(description));
            String str = setTextAnotherButton(description);
            if (str.equalsIgnoreCase(res)) {
                if (description == DA_TA)
                    btnDA2.setText(DA1_TA);
                else
                    btnDA2.setText(DA1_TV);
            } else {
                btnDA2.setText(str);
            }
            str = setTextAnotherButton(description);
            if (str.equalsIgnoreCase(res)) {
                if (description == DA_TA)
                    btnDA1.setText(DA2_TA);
                else
                    btnDA1.setText(DA2_TV);
            } else {
                btnDA1.setText(str);
            }
            str = setTextAnotherButton(description);
            if (str.equalsIgnoreCase(res)) {
                if (description == DA_TA)
                    btnDA4.setText(DA3_TA);
                else
                    btnDA4.setText(DA3_TV);
            } else {
                btnDA4.setText(str);
            }
        } else {
            answer_correct = 3;
            btnDA4.setText(res);
//            btnDA2.setText(setTextAnotherButton(description));
//            btnDA1.setText(setTextAnotherButton(description));
//            btnDA3.setText(setTextAnotherButton(description));
            String str = setTextAnotherButton(description);
            if (str.equalsIgnoreCase(res)) {
                if (description == DA_TA)
                    btnDA2.setText(DA1_TA);
                else
                    btnDA2.setText(DA1_TV);
            } else {
                btnDA2.setText(str);
            }
            str = setTextAnotherButton(description);
            if (str.equalsIgnoreCase(res)) {
                if (description == DA_TA)
                    btnDA3.setText(DA2_TA);
                else
                    btnDA3.setText(DA2_TV);
            } else {
                btnDA3.setText(str);
            }
            str = setTextAnotherButton(description);
            if (str.equalsIgnoreCase(res)) {
                if (description == DA_TA)
                    btnDA1.setText(DA3_TA);
                else
                    btnDA1.setText(DA3_TV);
            } else {
                btnDA1.setText(str);
            }
        }
    }

    private String setTextAnotherButton(int description) {
        //set text for buttons which incorrect
        int i = new Random().nextInt(words.size());
        String text = "";
        if (description == DA_TA) {
            if (words.get(i).getLanguage_Sub().equalsIgnoreCase("english")) {
                text = words.get(i).getSub_Text();
            } else {
                text = words.get(i).getOriginal_Text();
            }
        } else {
            if (words.get(i).getLanguage_Sub().equalsIgnoreCase("english")) {
                text = words.get(i).getOriginal_Text();
            } else {
                text = words.get(i).getSub_Text();
            }
        }
        words.remove(i);
        return text;
    }

    private void setTimer() {
        CountDownTimer timer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Word word = getWord();
                if (word != null) {
                    number_question++;
                    textViewNumber.setText(number_question + "/" + questions);
                    setTextViewText(word);
                    btnDA1.setBackground(getDrawable(R.drawable.vien_practise));
                    btnDA2.setBackground(getDrawable(R.drawable.vien_practise));
                    btnDA3.setBackground(getDrawable(R.drawable.vien_practise));
                    btnDA4.setBackground(getDrawable(R.drawable.vien_practise));
                } else {
                    Intent intent = new Intent(ActivityPractiseChoose.this, FinishPractise.class);
                    startActivityForResult(intent, REQ_FINISH);
                }
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_FINISH && resultCode == RESULT_OK) {
            onStart();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
