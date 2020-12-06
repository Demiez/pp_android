package com.petrenko.guessword;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> words;
    ArrayList<String> hints;
    String word; //Слово для відгадування
    Button btn_submit; //Кнопка для відповіді
    TextView ti_guess; //Поле вводу тексту
    TextView tv_hint; //Опис слова
    TextView tv_cipher; // зашифроване слово ***
    AlertDialog.Builder builder;
    private enum TO_TOAST { WORD_OK, WORD_BAD, LETTER_OK, LETTER_BAD, TOTAL_BAD }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_submit = findViewById(R.id.btn_guess);
        ti_guess = findViewById(R.id.ti_guess);
        tv_hint = findViewById(R.id.tv_hint);
        tv_cipher = findViewById(R.id.tv_cipher);
        words = new ArrayList<>();
        hints = new ArrayList<>();
        setWords();
        initiate();
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(ti_guess.getText().toString());
            }
        });
        builder = new AlertDialog.Builder(this).setTitle(R.string.menu_about).setMessage(R.string.about).
                setNegativeButton(R.string.dialog_close,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
    }

    private void initiate() {
        setWord();
        tv_cipher.setText(setCipher());
    }

    private void reinit() {
        try{
            wait(1000);
            initiate();
        }
        catch(Exception e){
            Toast.makeText(MainActivity.this, "Error: " + e.getMessage().toString(), Toast.LENGTH_SHORT);
            Log.d("EXC", e.getMessage().toString());
        }
    }

    private void setWords(){
        // Встановимо різні слова та підказки для рівнів гри
        words.add("knure");
        words.add("flower");
        words.add("triethylborane");
        words.add("proxima");
        words.add("bird");
        words.add("flyer");
        hints.add("Abbreviation of university where you study at.");
        hints.add("A reproductive structure of a certain kind of plants.");
        hints.add("Toxic chemical. Widely used in rocketry and aircraft engineering, but is not a fuel.");
        hints.add("Second closest to the Earth star.");
        hints.add("A certain group of animals, vast majority of which are capable of sustaining themselves in the air.");
        hints.add("The first ever motorized, heavier-than-air piece of wood and cloth that lifter freely from the ground.");
    }
    // встановимо слово, яке потрібно відгадати
    private void setWord(){
        int pos;
        Random r = new Random();
        do{
            pos = r.nextInt(words.size());
        }
        while(word == words.get(pos));
        word = words.get(pos);
        tv_hint.setText(hints.get(pos));
    }
    // шифруємо перший раз
    private String setCipher(){
        String s = "";
        for(int i = 0; i < word.length(); i++){
            s += "*";
        }
        return s;
    }
    @NonNull
    private String setCipher(int position, char c){
        StringBuilder s = new StringBuilder(tv_cipher.getText().toString());
        s.setCharAt(position, c);
        return s.toString();
    }

    private void submit(String s){
        if(s.length() > 0){
            if(s.length() > 1){
                // якщо надано правильну відповідь
                if(ti_guess.getText().toString().equals(word)){
                    toaster(TO_TOAST.WORD_OK);
                    tv_cipher.setText(word);
                    initiate();
                }
                //неправильно вгадане слово
                else{
                    toaster(TO_TOAST.WORD_BAD);
                }
            }
            else{
                // якщо правильна літера
                if(word.contains(s)){
                    char[] charword = word.toCharArray();
                    int pos = 0;
                    for (char c : charword
                         ) {
                        if(c == s.charAt(0)){
                            tv_cipher.setText(setCipher(pos, c));
                        }
                        pos++;
                    }
                    if(tv_cipher.getText().toString().contains("*"))
                    toaster(TO_TOAST.LETTER_OK);
                    else { toaster(TO_TOAST.WORD_OK); initiate();}

                }
                // якщо літера неправильна
                else{
                    toaster(TO_TOAST.LETTER_BAD);
                }
            }
        }
        else toaster(TO_TOAST.TOTAL_BAD);
        ti_guess.setText(null);
    }

    private void toaster(TO_TOAST toToast){
        try{
            switch(toToast){
                case WORD_OK:
                    Toast.makeText(MainActivity.this, R.string.result_word_ok, Toast.LENGTH_SHORT).show();
                    break;
                case WORD_BAD:
                    Toast.makeText(MainActivity.this, R.string.result_word_bad, Toast.LENGTH_SHORT).show();
                    break;
                case LETTER_OK:
                    Toast.makeText(MainActivity.this, R.string.result_letter_ok, Toast.LENGTH_SHORT).show();
                    break;
                case LETTER_BAD:
                    Toast.makeText(MainActivity.this, R.string.result_letter_bad, Toast.LENGTH_SHORT).show();
                    break;
                case TOTAL_BAD:
                    Toast.makeText(MainActivity.this, R.string.result_total_bad, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        catch (Exception e){
            Log.d("EXC",e.getMessage());
        }
    }

    //Меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_about:
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.menu_restart:
                initiate();
                break;
        }
        return true;
    }


}
