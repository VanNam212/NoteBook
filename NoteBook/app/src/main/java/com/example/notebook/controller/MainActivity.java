package com.example.notebook.controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.transition.Fade;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notebook.R;
import com.example.notebook.adapter.ListAdapter;
import com.example.notebook.model.Word;
import com.example.notebook.service.DBServiceImpl;
import com.example.notebook.service.TTSServiceImpl;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import vn.momo.momo_partner.AppMoMoLib;

//import org.json.JSONException;
//import org.json.JSONObject;

//import org.json.JSONException;
//import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {


    //action
    private static final int MENU_ITEM_VIEW = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_DELETE = 444;
    private static final int FORM_ADD_UPDATE = 789;
    private static final int FORM_PRACTISE = 456;
    private static final int BUTTON_VOCABULARY = 1;
    private static final int BUTTON_GRAMMAR = 2;
    private static final int BUTTON_PRACTISE = 3;


    //form
    private ListView listView;
    private EditText editText;
    private TextView textViewTotal;
    private ImageButton buttonAdd;
    private Button buttonWord;
    private Button buttonGrammar;
    private Button buttonPractise;

    //date
    private static final String DATE_FORMAT_VN = "dd/MM/yyyy";
    private static final String DATE_FORMAT_US = "MMM dd, yyyy";
    private static String date = getDateString(new Date(), DATE_FORMAT_US);
    private static String localeNow = DATE_FORMAT_US;


    //data
    private Locale locale;
    private DBServiceImpl dbWord = new DBServiceImpl(this);
    private ArrayList<Word> wordArrayList = new ArrayList<>();
    private ArrayList<Word> grammarArrayList = new ArrayList<>();
    private int check_button = 1;
    private TTSServiceImpl ttsService;
    ShareDialog shareDialog;
    ShareLinkContent shareLinkContent;

    private String amount = "10000";
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "Demo SDK";
    private String merchantCode = "SCB01";
    private String merchantNameLabel = "Nhà cung cấp";
    private String description = "Donate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setTitle("Main");
        setupWindowAnimations();

        //ánh xạ
        listView = (ListView) findViewById(R.id.listview);
        editText = (EditText) findViewById(R.id.editText);
        buttonAdd = (ImageButton) findViewById(R.id.btnAdd);
        buttonWord = (Button) findViewById(R.id.btnWord);
        buttonGrammar = (Button) findViewById(R.id.btnGrammar);
        textViewTotal = (TextView) findViewById(R.id.textViewTotal);
        buttonPractise = (Button) findViewById(R.id.buttonPractise);

        //đây này, viết là MainActivity.this cũng đc
        //xác định tts dùng cho form activity_main
        ttsService = new TTSServiceImpl(MainActivity.this);

        findViewById(R.id.constraint).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                UIUtil.hideKeyboard(MainActivity.this);
                return true;
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if (isOpen) {
                    Toast.makeText(MainActivity.this, "keyboard opened", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "keyboard closed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsService.shutdown();
                UIUtil.hideKeyboard(MainActivity.this);

                Intent intent = new Intent(MainActivity.this, Add_UpdateForm.class);
                Bundle bundle = new Bundle();
                bundle.putString("origin", editText.getText().toString());
                if (check_button == BUTTON_VOCABULARY) {
                    bundle.putString("title", buttonWord.getText().toString());
                    bundle.putString("description", "add vocabulary");
                } else if (check_button == BUTTON_GRAMMAR) {
                    bundle.putString("title", buttonGrammar.getText().toString());
                    bundle.putString("description", "add grammar");
                }
                bundle.putString("date", date);
                intent.putExtra("data", bundle);
                startActivityForResult(intent, FORM_ADD_UPDATE);

            }
        });

        buttonWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtil.hideKeyboard(MainActivity.this);
                wordArrayList = dbWord.getAllWords();
                textViewTotal.setText("" + wordArrayList.size());
                addListView(wordArrayList);
                buttonWord.setTextColor(Color.parseColor("#0022FC"));
                buttonGrammar.setTextColor(Color.GRAY);
                buttonPractise.setTextColor(Color.GRAY);
                check_button = BUTTON_VOCABULARY;
                editText.setText("");
            }
        });

        buttonGrammar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtil.hideKeyboard(MainActivity.this);
                grammarArrayList = dbWord.getAllGrammars();
                textViewTotal.setText("" + grammarArrayList.size());
                addListView(grammarArrayList);
                buttonGrammar.setTextColor(Color.parseColor("#0022FC"));
                buttonWord.setTextColor(Color.GRAY);
                buttonPractise.setTextColor(Color.GRAY);
                check_button = BUTTON_GRAMMAR;
                editText.setText("");
            }
        });

        buttonPractise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsService.shutdown();
                check_button = BUTTON_PRACTISE;
                buttonPractise.setTextColor(Color.parseColor("#0022FC"));
                buttonWord.setTextColor(Color.GRAY);
                buttonGrammar.setTextColor(Color.GRAY);
                Intent intent = new Intent(MainActivity.this, ActivityPractise.class);
                startActivityForResult(intent, FORM_PRACTISE);
            }
        });

        //init arraylist, total and listview
        init();

        //set button's color
        setColorButton();

    }

    private void init() {
        //khởi tạo
//        dbWord.addUser();
        wordArrayList = dbWord.getAllWords();
        grammarArrayList = dbWord.getAllGrammars();

        //khởi tạo tổng số từ vựng hiện có
        if (check_button == BUTTON_VOCABULARY) {
            textViewTotal.setText("" + wordArrayList.size());
            addListView(wordArrayList);
        } else if (check_button == BUTTON_GRAMMAR) {
            textViewTotal.setText("" + grammarArrayList.size());
            addListView(grammarArrayList);
        }
    }

    private void setupWindowAnimations() {
        Explode explode1 = new Explode();
        explode1.setDuration(1000);
        getWindow().setExitTransition(explode1);

        Explode explode = new Explode();
        explode.setDuration(1000);
        getWindow().setEnterTransition(explode);

        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setReturnTransition(fade);
    }

    @Override
    protected void onStart() {
        super.onStart();
        check_button = BUTTON_VOCABULARY;
        textViewTotal.setText("" + wordArrayList.size());
        addListView(wordArrayList);
        setColorButton();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == FORM_ADD_UPDATE && resultCode == RESULT_OK) {
            updateArray(0);
            super.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == FORM_PRACTISE && resultCode == RESULT_OK) {
            check_button = BUTTON_VOCABULARY;
            textViewTotal.setText("" + wordArrayList.size());
            addListView(wordArrayList);
            setColorButton();
        } else if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if (data != null) {
                if (data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
//                    tvMessage.setText("message: " + "Get token " + data.getStringExtra("message"));
                    String token = data.getStringExtra("data"); //Token response
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if (env == null) {
                        env = "app";
//                        dbWord.updateUser();
                    }

                    if (token != null && !token.equals("")) {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order
//                        dbWord.updateUser();
                        Toast.makeText(MainActivity.this, "Bạn đã ủng hộ thành công!", Toast.LENGTH_SHORT).show();
                    } else {
//                        tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                    }
                } else if (data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    String message = data.getStringExtra("message") != null ? data.getStringExtra("message") : "Thất bại";
//                    tvMessage.setText("message: " + message);
                } else if (data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
//                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                } else {
                    //TOKEN FAIL
//                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                }
            } else {
//                tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
            }
        } else {
//            tvMessage.setText("message: " + this.getString(R.string.not_receive_info_err));
        }

    }

    private void changeLanguage(Locale locale) {
        //doi tuong luu thong tin kich thuoc trinh bay
        DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();

        //doi tuong cau hinh
        Configuration configuration = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);    //ap dung ma khi phien ban >=17
        } else {
            configuration.locale = locale;      //ap dung cho phien ban api <17
        }

        //cai dat ngon ngu
        getBaseContext().getResources().updateConfiguration(configuration, displayMetrics);

        //refresh activity
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_lang_en:
                this.locale = new Locale("en", "US");
                date = getDateString(new Date(), DATE_FORMAT_US);
                localeNow = DATE_FORMAT_US;
                changeLanguage(this.locale);
                break;
            case R.id.menu_lang_vi:
                this.locale = new Locale("vi", "VN");
                date = getDateString(new Date(), DATE_FORMAT_VN);
                localeNow = DATE_FORMAT_VN;
                changeLanguage(this.locale);
                break;
            case R.id.menu:
                getActionMenu();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private static String getDateString(Date date, String date_format) {
        SimpleDateFormat format = new SimpleDateFormat(date_format);
        return format.format(date);
    }

    private void setColorButton() {
        if (check_button == BUTTON_VOCABULARY) {
            buttonWord.setTextColor(Color.parseColor("#0022FC"));
            buttonGrammar.setTextColor(Color.GRAY);
            buttonPractise.setTextColor(Color.GRAY);
        } else if (check_button == BUTTON_GRAMMAR) {
            buttonGrammar.setTextColor(Color.parseColor("#0022FC"));
            buttonWord.setTextColor(Color.GRAY);
            buttonPractise.setTextColor(Color.GRAY);
        } else {
            buttonPractise.setTextColor(Color.parseColor("#0022FC"));
            buttonWord.setTextColor(Color.GRAY);
            buttonGrammar.setTextColor(Color.GRAY);
        }
    }

    //cancel focus of edittext
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
                    UIUtil.hideKeyboard(MainActivity.this);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //update database and show data to list view
    public void updateArray(int index) {
        editText.setText("");
        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black_24dp, 0, 0, 0);
        try {
            if (check_button == BUTTON_VOCABULARY) {
                ArrayList<Word> words = dbWord.getAllWords();
                textViewTotal.setText("" + words.size());
                addListView(words);
            } else if (check_button == BUTTON_GRAMMAR) {
                ArrayList<Word> words = dbWord.getAllGrammars();
                textViewTotal.setText("" + words.size());
                addListView(words);
            }
        } catch (Exception ex) {
            init();
        }
    }

    public void updateArray(int index, ArrayList<Word> arrayList) {
        editText.setText("");
        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black_24dp, 0, 0, 0);

        try {
            if (check_button == BUTTON_VOCABULARY) {
                textViewTotal.setText("" + arrayList.size());
                addListView(arrayList);
            } else if (check_button == BUTTON_GRAMMAR) {
                textViewTotal.setText("" + arrayList.size());
                addListView(arrayList);
            }
        } catch (Exception ex) {
            init();
        }
    }

    //add data to listview and event of edit text search
    public void addListView(ArrayList<Word> arrayList) {
        final ListAdapter adapter = new ListAdapter(MainActivity.this, arrayList);
        listView.setAdapter(adapter);
//        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        //đăng ký Context menu cho listview
        registerForContextMenu(this.listView);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean gotfocus) {
                // TODO Auto-generated method stub
                if (gotfocus)
                    editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black_24dp, 0, 0, 0);
                else
                    editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black_24dp, 0, 0, 0);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black_24dp, 0, 0, 0);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black_24dp, 0, 0, 0);
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black_24dp, 0, 0, 0);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.form_action, null);
        TextView actionTitle = (TextView) alertLayout.findViewById(R.id.textViewActionTitle);
        TextView actionView = (TextView) alertLayout.findViewById(R.id.textViewActionView);
        TextView actionEdit = (TextView) alertLayout.findViewById(R.id.textViewActionEdit);
        TextView actionDelete = (TextView) alertLayout.findViewById(R.id.textViewActionDel);


        UIUtil.hideKeyboard(MainActivity.this);
        menu.setHeaderTitle(actionTitle.getText());

        // groupId, itemId, order, title
        menu.add(0, MENU_ITEM_VIEW, 0, actionView.getText());
        menu.add(0, MENU_ITEM_EDIT, 1, actionEdit.getText());
        menu.add(0, MENU_ITEM_DELETE, 2, actionDelete.getText());
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Word word = (Word) this.listView.getItemAtPosition(info.position);
        Word selectedNote = new Word();
        if (word.getLanguage_Origin() != null) {
            selectedNote.setWord_ID(word.getWord_ID());
            selectedNote.setDate_Text(word.getDate_Text());
            selectedNote.setLanguage_Origin(word.getLanguage_Origin());
            selectedNote.setLanguage_Sub(word.getLanguage_Sub());
            selectedNote.setOriginal_Text(word.getOriginal_Text());
            selectedNote.setSub_Text(word.getSub_Text());
        } else {
            if (check_button == BUTTON_VOCABULARY) {
                for (int i = 0; i < wordArrayList.size(); i++) {
                    if (word.getOriginal_Text() == wordArrayList.get(i).getOriginal_Text() &&
                            word.getSub_Text() == wordArrayList.get(i).getSub_Text()) {
                        selectedNote.setWord_ID(wordArrayList.get(i).getWord_ID());
                        selectedNote.setDate_Text(wordArrayList.get(i).getDate_Text());
                        selectedNote.setLanguage_Origin(wordArrayList.get(i).getLanguage_Origin());
                        selectedNote.setLanguage_Sub(wordArrayList.get(i).getLanguage_Sub());
                        selectedNote.setOriginal_Text(wordArrayList.get(i).getOriginal_Text());
                        selectedNote.setSub_Text(wordArrayList.get(i).getSub_Text());
                        break;
                    }
                }
            } else {
                for (int i = 0; i < grammarArrayList.size(); i++) {
                    if (word.getOriginal_Text() == grammarArrayList.get(i).getOriginal_Text() &&
                            word.getSub_Text() == grammarArrayList.get(i).getSub_Text()) {
                        selectedNote.setWord_ID(wordArrayList.get(i).getWord_ID());
                        selectedNote.setDate_Text(wordArrayList.get(i).getDate_Text());
                        selectedNote.setLanguage_Origin(wordArrayList.get(i).getLanguage_Origin());
                        selectedNote.setLanguage_Sub(wordArrayList.get(i).getLanguage_Sub());
                        selectedNote.setOriginal_Text(wordArrayList.get(i).getOriginal_Text());
                        selectedNote.setSub_Text(wordArrayList.get(i).getSub_Text());
                        break;
                    }
                }
            }
        }

        if (item.getItemId() == MENU_ITEM_VIEW) {
            getInfo(selectedNote);
        } else if (item.getItemId() == MENU_ITEM_EDIT) {
            Intent intent = new Intent(MainActivity.this, Add_UpdateForm.class);
            Bundle bundle = new Bundle();
            bundle.putInt("id", selectedNote.getWord_ID());
            bundle.putString("origin", selectedNote.getOriginal_Text());
            bundle.putString("sub", selectedNote.getSub_Text());
            bundle.putString("source", selectedNote.getLanguage_Origin());
            bundle.putString("target", selectedNote.getLanguage_Sub());
            if (check_button == BUTTON_VOCABULARY) {
                bundle.putString("title", buttonWord.getText().toString());
                bundle.putString("description", "edit vocabulary");
            } else if (check_button == BUTTON_GRAMMAR) {
                bundle.putString("title", buttonGrammar.getText().toString());
                bundle.putString("description", "edit grammar");
            }
            bundle.putString("date", date);
            intent.putExtra("data", bundle);
            startActivityForResult(intent, FORM_ADD_UPDATE);

        } else if (item.getItemId() == MENU_ITEM_DELETE) {
            getFormDelete(selectedNote);
        } else {
            return false;
        }
        return true;
    }

    //show action menu
    public void getActionMenu() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.action_menu, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        final AlertDialog dialog = alert.create();
        dialog.show();

        //ánh xạ
        final Button btnSortByName = (Button) alertLayout.findViewById(R.id.btnSortByName);
        final Button btnSortByDate = (Button) alertLayout.findViewById(R.id.btnSortByDate);
        final Button btnShare = (Button) alertLayout.findViewById(R.id.btnShare);
        final Button btnExit = (Button) alertLayout.findViewById(R.id.btnExit);
//        final Button btnUpgrade = (Button) alertLayout.findViewById(R.id.btnUpgrade);

        //events
        btnSortByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.hide();
                if (check_button == BUTTON_VOCABULARY) {
                    Collections.sort(wordArrayList);
                    updateArray(0, wordArrayList);
//                    ArrayList<Word> wordArrayList1 = dbWord.getAllWords();
//                    updateArray(0, wordArrayList1);
                } else if (check_button == BUTTON_GRAMMAR) {
                    Collections.sort(grammarArrayList);
                    updateArray(0, grammarArrayList);
//                    ArrayList<Word> wordArrayList1 = dbWord.getAllGrammars();
//                    updateArray(0, wordArrayList1);
                }
            }
        });

        btnSortByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.hide();
                if (check_button == BUTTON_VOCABULARY) {
                    updateArray(0);
                } else if (check_button == BUTTON_GRAMMAR) {
                    updateArray(0);
                }
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.hide();
//                try {
//                    PackageInfo info = getPackageManager().getPackageInfo(
//                            "com.example.notebook",                  //Insert your own package name.
//                            PackageManager.GET_SIGNATURES);
//                    for (Signature signature : info.signatures) {
//                        MessageDigest md = MessageDigest.getInstance("SHA");
//                        md.update(signature.toByteArray());
//                        Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//                    }
//                } catch (PackageManager.NameNotFoundException e) {
//
//                } catch (NoSuchAlgorithmException e) {
//
//                }
                shareDialog = new ShareDialog(MainActivity.this);
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    shareLinkContent = new ShareLinkContent.Builder()
                            .setContentTitle("NoteBook")
                            .setContentDescription("Hỗ trợ học tiếng anh")
//                            .setContentUrl(Uri.parse("com.example.notebook"))
                            .setContentUrl(Uri.parse("https://sites.google.com/view/vannam211-antoan/home"))
                            .build();
                }
                shareDialog.show(shareLinkContent);
            }
        });

//        btnUpgrade.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (dbWord.getIsUpdate() == 0)
//                    getFormUpgrade();
////                else {
////                    Toast.makeText(MainActivity.this, "Bạn đã nâng cấp tài khoản!", Toast.LENGTH_SHORT).show();
////                }
//                dialog.dismiss();
//                dialog.hide();
//            }
//        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsService.shutdown();
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startActivity(startMain);
                finish();
            }
        });
    }

    public void getFormUpgrade() {
        LayoutInflater inflater = getLayoutInflater();
//        View alertLayout = inflater.inflate(R.layout.form_upgrade, null);
        View alertLayout = inflater.inflate(R.layout.form_donate, null);

        final TextView tvprice = (TextView) alertLayout.findViewById(R.id.tvprice);
        final Button btnOke = (Button) alertLayout.findViewById(R.id.btnOke);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        final AlertDialog dialog = alert.create();
        dialog.show();

        btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //forward form paypal
                dialog.dismiss();
                dialog.hide();
//                if (dbWord.getIsUpdate() == 0) {
                    //gọi đến app momo
                    AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION
                    //đặt giá
                    requestPayment(tvprice.getText().toString());
//                }
//                dbWord.updateUser();
//                else {
//                    Toast.makeText(MainActivity.this, "Bạn đã nâng cấp tài khoản thành công!", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    //show form info of data
    public void getInfo(final Word word) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_message_info, null);

        //ánh xạ
        final TextView tvOrigin = (TextView) alertLayout.findViewById(R.id.tvOrigin);
        final TextView tvSub = (TextView) alertLayout.findViewById(R.id.tvSub);
        final TextView tvChangeDate = (TextView) alertLayout.findViewById(R.id.tvChangeDate);
        final TextView tvLang1 = (TextView) alertLayout.findViewById(R.id.tvLang1);
        final TextView tvLang2 = (TextView) alertLayout.findViewById(R.id.tvLang2);
        final Button buttonSpeak1 = (Button) alertLayout.findViewById(R.id.buttonSpeak1);
        final Button buttonSpeak2 = (Button) alertLayout.findViewById(R.id.buttonSpeak2);
        final Button btnOke = (Button) alertLayout.findViewById(R.id.btnOke);

        //gán giá trị
        SimpleDateFormat format = new SimpleDateFormat(localeNow);
        try {
            Date date1 = format.parse(word.getDate_Text());
            tvChangeDate.setText(" " + getDateString(date1, localeNow));
        } catch (Exception e) {
            tvChangeDate.setText(" " + word.getDate_Text());
        }
        tvLang1.setText(word.getLanguage_Origin() + ": ");
        tvLang2.setText(word.getLanguage_Sub() + ": ");
        tvOrigin.setText(word.getOriginal_Text());
        tvSub.setText(word.getSub_Text());
        tvOrigin.setSelected(true);
        tvSub.setSelected(true);
        if (word.getLanguage_Origin().equalsIgnoreCase("English")) {
            buttonSpeak2.setVisibility(View.INVISIBLE);
        } else {
            buttonSpeak1.setVisibility(View.INVISIBLE);
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        final AlertDialog dialog = alert.create();


        buttonSpeak1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsService.speak(word.getOriginal_Text());
            }
        });

        buttonSpeak2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsService.speak(word.getSub_Text());
            }
        });

        btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsService.shutdown();
                dialog.dismiss();
                dialog.hide();
            }
        });
        dialog.show();
    }

    public void getFormDelete(final Word word) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_message_delete, null);

        //ánh xạ
        final Button btnYes = (Button) alertLayout.findViewById(R.id.btnYes);
        final Button btnNo = (Button) alertLayout.findViewById(R.id.btnNo);
        final TextView tvSuccess = (TextView) alertLayout.findViewById(R.id.tvSuccess);


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        final AlertDialog dialog = alert.create();


        //gán giá trị
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.hide();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_button == BUTTON_VOCABULARY) {
                    dbWord.deleteWord(word);
                    updateArray(0);
                } else if (check_button == BUTTON_GRAMMAR) {
                    dbWord.deleteGrammar(word);
                    updateArray(0);
                }
                Toast.makeText(getBaseContext(), tvSuccess.getText(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                dialog.hide();
            }
        });

        dialog.show();
    }


    //Get token through MoMo app
    private void requestPayment(String amount) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
//        if (edAmount.getText().toString() != null && edAmount.getText().toString().trim().length() != 0)
//            amount = edAmount.getText().toString().trim();
        this.amount = amount;

        //cái này t sẽ sửa khi test trên đt
        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        //mấy cái này để làm rõ cái form momo
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", amount); //Kiểu integer
        eventValue.put("orderId", "orderId123456789"); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
        eventValue.put("orderLabel", "Mã đơn hàng"); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
        eventValue.put("fee", 0); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId", merchantCode + "merchant_billId_" + System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "CGV Cresent Mall");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
            objExtraData.put("movie_format", "2D");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        //thêm description xong thì chuyển đén momo
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);


    }

    //Get token callback from MoMo app an submit to server side
//    void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
//            if (data != null) {
//                if (data.getIntExtra("status", -1) == 0) {
//                    //TOKEN IS AVAILABLE
//                    tvMessage.setText("message: " + "Get token " + data.getStringExtra("message"));
//                    String token = data.getStringExtra("data"); //Token response
//                    String phoneNumber = data.getStringExtra("phonenumber");
//                    String env = data.getStringExtra("env");
//                    if (env == null) {
//                        env = "app";
//                    }
//
//                    if (token != null && !token.equals("")) {
//                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
//                        // IF Momo topup success, continue to process your order
//                    } else {
//                        tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
//                    }
//                } else if (data.getIntExtra("status", -1) == 1) {
//                    //TOKEN FAIL
//                    String message = data.getStringExtra("message") != null ? data.getStringExtra("message") : "Thất bại";
//                    tvMessage.setText("message: " + message);
//                } else if (data.getIntExtra("status", -1) == 2) {
//                    //TOKEN FAIL
//                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
//                } else {
//                    //TOKEN FAIL
//                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
//                }
//            } else {
//                tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
//            }
//        } else {
//            tvMessage.setText("message: " + this.getString(R.string.not_receive_info_err));
//        }
//    }

}


