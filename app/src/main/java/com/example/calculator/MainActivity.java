package com.example.calculator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.ArrayUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView inputEdit, resultEdit;
    private Button clear, percentage, point,plusMinus, divide, multiply, subtraction, addition, equalTo;
    private ImageView backspace;
    private Button n0, n1, n2, n3, n4, n5, n6, n7, n8, n9;

    private String op = "+";
    private String oldTxt = "", newTxt = "";
    boolean isNew = true;
    int counter = 0, count = 0,processCount = 0;

    private ListView listView;
    ArrayList<String> historyItems, historyItems1;
    String no = "", result;
    String pr[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        historyItems = new ArrayList<>();
//        historyItems1 = new ArrayList<>();

        inputEdit = findViewById(R.id.input);
        resultEdit = findViewById(R.id.input1);

//        history = findViewById(R.id.history);
        clear = findViewById(R.id.clear);
        backspace = findViewById(R.id.back);
        percentage = findViewById(R.id.percentage);
        point = findViewById(R.id.point);
        plusMinus = findViewById(R.id.plus_minus);
        divide = findViewById(R.id.divide);
        multiply = findViewById(R.id.multiply);
        subtraction = findViewById(R.id.minus);
        addition = findViewById(R.id.plus);
        equalTo = findViewById(R.id.equalTo);

        listView = findViewById(R.id.history_list);

        n0 = findViewById(R.id.n0);
        n1 = findViewById(R.id.n1);
        n2 = findViewById(R.id.n2);
        n3 = findViewById(R.id.n3);
        n4 = findViewById(R.id.n4);
        n5 = findViewById(R.id.n5);
        n6 = findViewById(R.id.n6);
        n7 = findViewById(R.id.n7);
        n8 = findViewById(R.id.n8);
        n9 = findViewById(R.id.n9);


        loadData();

        //backspace
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (no.length() > 0){
                    if (no.endsWith(")")){
                        getPlusMinus();
                    }else {
                        no = no.substring(0, no.length() - 1);
                        isNew = true;
                    }

                    result();
                    inputEdit.setText(no.replace("M","-"));
                    counter = 0;
                }
//                resultEdit.setHint(no.replace("M","-"));
            }
        });

        //all clear
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputEdit.setText("");
                resultEdit.setText("");
                resultEdit.setHint("");
                counter = 0;
                count = 0;
                no = "";
            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String s = listView.getItemAtPosition(position).toString();
//
//                inputEdit.setText(inputEdit.getText().toString() + s);
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {

        switch (item.getItemId()){
            case R.id.menu_history:
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.bottomsheet);
                bottomSheetDialog.setContentView(R.layout.history_dialog);
                bottomSheetDialog.show();

//                RelativeLayout layout = bottomSheetDialog.findViewById(R.id.rela);
//                layout.setBackground(null);
//
//                bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
//
//                bottomSheetDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


                RecyclerView bHlist = bottomSheetDialog.findViewById(R.id.bottom_history_list);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                bHlist.setLayoutManager(layoutManager);

                MyAdapter adapter = new MyAdapter(this, historyItems, historyItems1);
                bHlist.setAdapter(adapter);


//                CoordinatorLayout relativeLayout = bottomSheetDialog.findViewById(R.id.rela);
//                relativeLayout.setBackground(null);

//                getView().getParent().setBackgroundColor(Color.TRANSPARENT);

                ImageView bHimg = bottomSheetDialog.findViewById(R.id.bottom_history_img);
                bHimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences preferences = getSharedPreferences("Calculator", MODE_PRIVATE);
                        preferences.edit().clear().commit();
                        listView.removeAllViewsInLayout();
                        bHlist.removeAllViewsInLayout();
                        historyItems.clear();
                        historyItems1.clear();
                        inputEdit.setText("");

                        bottomSheetDialog.dismiss();
                    }
                });

//                bHlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        String s = bHlist.getItemAtPosition(position).toString();
//
//                        inputEdit.setText(inputEdit.getText().toString() + s);
//                        bottomSheetDialog.dismiss();
//                    }
//                });

                break;
            case R.id.menu_clear_history:
                SharedPreferences preferences = getSharedPreferences("Calculator", MODE_PRIVATE);
                preferences.edit().clear().commit();
                historyItems.clear();
                historyItems1.clear();
                listView.removeAllViewsInLayout();
                inputEdit.setText("");
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view){
        count = 0;
//        String no = inputEdit.getText().toString();
        oldTxt = no;//inputEdit.getText().toString();

        no.replace("M","-");
        no.replace("÷","/");

        switch (view.getId()){
            case R.id.n0:
                if(!no.endsWith(")") && !no.endsWith("%")){
                    if (!oldTxt.equals("0")){
                        if (oldTxt.endsWith("+") || oldTxt.endsWith("-") || oldTxt.endsWith("*") || oldTxt.endsWith("÷") || oldTxt.endsWith("%")){
                            no = oldTxt + "0";

                        }else {
                            if (oldTxt.endsWith("+0") || oldTxt.endsWith("-0") || oldTxt.endsWith("*0") || oldTxt.endsWith("÷0")){

                            }else {
                                no += "0";
                            }
                        }
                    }
                }
                break;
            case R.id.n1:
                if(!no.endsWith(")") && !no.endsWith("%"))
                    no += "1";
                break;
            case R.id.n2:
                if(!no.endsWith(")") && !no.endsWith("%"))
                    no += "2";
                break;
            case R.id.n3:
                if(!no.endsWith(")") && !no.endsWith("%"))
                    no += "3";
                break;
            case R.id.n4:
                if(!no.endsWith(")") && !no.endsWith("%"))
                    no += "4";
                break;
            case R.id.n5:
                if(!no.endsWith(")") && !no.endsWith("%"))
                    no += "5";
                break;
            case R.id.n6:
                if(!no.endsWith(")") && !no.endsWith("%"))
                    no += "6";
                break;
            case R.id.n7:
                if(!no.endsWith(")") && !no.endsWith("%"))
                    no += "7";
                break;
            case R.id.n8:
                if(!no.endsWith(")") && !no.endsWith("%"))
                    no += "8";
                break;
            case R.id.n9:
                if(!no.endsWith(")") && !no.endsWith("%"))
                    no += "9";
                break;
            case R.id.point:
                if(!no.endsWith(")") && !no.endsWith("%")){
                    if (counter == 0){
                        if (inputEdit.getText().toString().isEmpty()){
                            no = "0.";
                        }else{
                            if (oldTxt.endsWith("+") || oldTxt.endsWith("-") || oldTxt.endsWith("*") || oldTxt.endsWith("÷") || oldTxt.endsWith("%")){
                                no = no + "0.";
                            }else {
                                no = no + ".";
                                counter++;
                            }
                        }
                    }
                    counter++;
                }
                break;
            case R.id.plus_minus:
                getPlusMinus();

                break;
        }


        inputEdit.setText(no.replace("M", "-"));
        result();
    }

    private void getPlusMinus() {
        if (!oldTxt.isEmpty()){
            if (!oldTxt.endsWith("+") && !oldTxt.endsWith("-") && !oldTxt.endsWith("*") && !oldTxt.endsWith("÷") && !oldTxt.endsWith("%")){
                int lastIndex = 0;
//                        System.out.println("lastIndex = 0");
                lastIndex = no.lastIndexOf("+")> lastIndex? no.lastIndexOf("+"): lastIndex;
                lastIndex = no.lastIndexOf("-")> lastIndex? no.lastIndexOf("-"): lastIndex;
                lastIndex = no.lastIndexOf("*")> lastIndex? no.lastIndexOf("*"): lastIndex;
                lastIndex = no.lastIndexOf("÷")> lastIndex? no.lastIndexOf("÷"): lastIndex;

//                        System.out.println("last index = " +  lastIndex);
                String p1 = "";
                String p2 = "";
                if (lastIndex == 0){
                    p2 = no;
                }
                else
                {
                    p1 = no.substring(0, lastIndex + 1);
                    p2 = no.substring(lastIndex + 1, no.length());
                }
//                        System.out.println("before p1 = " + p1 + " p2 = " + p2);

                if (p2.endsWith(")")){
                    p2 = p2.replace("(M", "").replace(")", "");
                }else {
                    p2 = "(" + "M" + p2 + ")";
                }
                no = p1 + p2;
//                        System.out.println("after p1 = " + p1 + " p2 = " + p2);

            }
        }
    }

    public void operator(View view) {
        count = 0;
        counter = 0;
        if (inputEdit.getText().toString().length() > 0){
            oldTxt = no;
            switch (view.getId()){
                case R.id.plus:
                    op = "+";
                    counter = 0;
                    break;
                case R.id.minus:
                    op = "-";
                    counter = 0;
                    break;
                case R.id.multiply:
                    op = "*";
                    counter = 0;
                    break;
                case R.id.divide:
                    op = "÷";
                    counter = 0;
                    break;
                case R.id.percentage:
                    op = "%";
                    counter = 0;
                    break;
            }

            if (oldTxt.endsWith(".")){
                oldTxt = oldTxt.substring(0,oldTxt.length()-1) + "";
            }
            if (oldTxt.endsWith("+") || oldTxt.endsWith("-") || oldTxt.endsWith("*") || oldTxt.endsWith("÷")){
                no = oldTxt.substring(0, oldTxt.length() - 1) + op;

            }
            else {
                no = oldTxt + op;
            }
            inputEdit.setText(no.replace("M", "-"));
        }
    }

    private void result()   {
        if (!inputEdit.getText().toString().equals("")) {
            if (!inputEdit.getText().toString().endsWith("+") && !inputEdit.getText().toString().endsWith("-") && !inputEdit.getText().toString().endsWith("*") && !inputEdit.getText().toString().endsWith("÷") && !inputEdit.getText().toString().endsWith(".")) {

                String process = no.replace("M", "-").replace("÷", "/");
                if (no.contains("+") || no.contains("-") || no.contains("*") || no.contains("÷")) {
                    if (!(process.isEmpty())) {

                        if (no.endsWith("+") || no.endsWith("-") || no.endsWith("*") || no.endsWith("÷")) {
                            process = process.substring(0, process.length() - 1);
                        }

//                        if (no.contains("%")){
//                            pr = no.split("%",2);
//                            String s = pr[0];
//                            liveEvaluatePR(s);
//                        }else {
//
//                        }

                        process = process.replaceAll("÷", "/");
                        process = process.replaceAll("%", "/100");
                        process = process.replaceAll("M", "-");

                        Context rhino = Context.enter();

                        rhino.setOptimizationLevel(-1);

                        String finalResult = "";

                        Scriptable scriptable = rhino.initStandardObjects();
                        finalResult = rhino.evaluateString(scriptable, process, "javascript", 1, null).toString();
                        String result = finalResult;

                        String n[] = result.split("\\.");
                        if (n.length > 1) {
                            if (n[1].equals("0")) {
                                result = n[0];
                            }
                        }
                        resultEdit.setHint(result);

                    } else {
                        resultEdit.setHint("");
                    }
                }
            }
        }
    }

    public void ans(View view) {
        if (!inputEdit.getText().toString().equals("")) {
            if (!inputEdit.getText().toString().endsWith("+") && !inputEdit.getText().toString().endsWith("-") && !inputEdit.getText().toString().endsWith("*") && !inputEdit.getText().toString().endsWith("÷") && !inputEdit.getText().toString().endsWith(".")){

                if (no.contains("+") || no.contains("-") || no.contains("*") || no.contains("÷")){
                    if (!(no.isEmpty())){

                        no = no.replace("M","-");

                        if (no.contains("%")){
                            pr = no.split("%",2);
                            String s = pr[0];
                            evaluatePR(s);
                        }else {
                            no = no.replace("÷","/");

                            Context rhino = Context.enter();

                            rhino.setOptimizationLevel(-1);

                            String finalResult = "";

                            Scriptable scriptable = rhino.initStandardObjects();
                            finalResult = rhino.evaluateString(scriptable,no.replace("M","-"),"javascript",1,null).toString();

                            cutDecimal(finalResult);
                        }


                    }
                }

            }

        }

    }

    public String evaluatePR(String s) {
        System.out.println("default  String s = " + s);

        int lastOpIndex = 0;

        lastOpIndex = s.lastIndexOf("+")> lastOpIndex? s.lastIndexOf("+"): lastOpIndex;
        lastOpIndex = s.lastIndexOf("-")> lastOpIndex? s.lastIndexOf("-"): lastOpIndex;
        lastOpIndex = s.lastIndexOf("÷")> lastOpIndex? s.lastIndexOf("÷"): lastOpIndex;
        lastOpIndex = s.lastIndexOf("*")> lastOpIndex? s.lastIndexOf("*"): lastOpIndex;

        System.out.println("last operator position = " + lastOpIndex);
        String positionChar = String.valueOf(s.charAt(lastOpIndex));
        System.out.println("last index operator = " + s.charAt(lastOpIndex));

        String beforeOp = s.substring(0, lastOpIndex);
        String afterOp = s.substring(lastOpIndex +1 , s.length());
        System.out.println("before operator = " + beforeOp);
        System.out.println("after operator = " + afterOp);

        Context rhino = Context.enter();

        rhino.setOptimizationLevel(-1);

        Scriptable scriptable = rhino.initStandardObjects();
        beforeOp = rhino.evaluateString(scriptable,beforeOp.replace("÷","/"),"javascript",1,null).toString();
        System.out.println("calculate beforeOp --> " + beforeOp);

        if (s.contains("+") && positionChar.equals("+")){
            String Fresult = beforeOp + positionChar + beforeOp + "*" + afterOp + "/100";
            System.out.println("result ( + ) = " + Fresult);
            rhino.setOptimizationLevel(-1);
            result = rhino.evaluateString(scriptable,Fresult.replace("÷", "/"),"javascript",1,null).toString();
        }
        if (s.contains("-") && positionChar.equals("-")){
            String Fresult = beforeOp + positionChar + beforeOp + "*" + afterOp + "/100";
            System.out.println("result ( - ) = " + Fresult);
            rhino.setOptimizationLevel(-1);
            result = rhino.evaluateString(scriptable,Fresult.replace("÷", "/"),"javascript",1,null).toString();
        }

        if (s.contains("*") && positionChar.equals("*")){
            String Fresult = beforeOp + "*" + afterOp + "/100";
            System.out.println("result ( * ) = " + Fresult);
            rhino.setOptimizationLevel(-1);
            result = rhino.evaluateString(scriptable,Fresult,"javascript",1,null).toString();
        }

        if (s.contains("÷") && positionChar.equals("÷") && !s.contains("/100")){
            String Fresult = beforeOp + positionChar + afterOp + "*100";
            System.out.println("result ( ÷ ) = " + Fresult);
            rhino.setOptimizationLevel(-1);
            result = rhino.evaluateString(scriptable,Fresult.replace("÷", "/"),"javascript",1,null).toString();
        }

        System.out.println("process result --> " + result);

        pr = ArrayUtils.remove(pr, 0);

        no = result + pr[0];
        System.out.println("final string --> no = result + pr[0] ==> " + no);

        System.out.println("-------------END--------------" + " <--( " + processCount + " )-->" );

        if (no.contains("%")){
            processCount++;
            recall();
        }

        if (!no.contains("%")){
            String finalResult = "";
            rhino.setOptimizationLevel(-1);
            finalResult = rhino.evaluateString(scriptable,no.replace("M","-").replace("÷", "/"),"javascript",1,null).toString();

            System.out.println("FINAL ANS ==> " + finalResult);

            cutDecimal(finalResult);
        }

        return no;
    }

    private void recall() {
        if (no.contains("%")){
            pr = no.split("%",2);
            String s = pr[0];
            evaluatePR(s.replace("M","-"));
        }
    }

    private String cutDecimal(String result){

        if (count == 0){

            String n[] = result.split("\\.");
            if (n.length >1){
                if (n[1].equals("0")){
                    result = n[0];
                }
//                if (n[1].equals("0")){
//                    result = n[0];
//                }else {
//                    if (n[1].length() > 2){
//                        result = n[0] + "." + n[1].substring(0,4);
//                    }
//                    else {
//                        result = n[0] + "." + n[1];
//                    }
//                }
            }
            if (result.length() > 20){
                inputEdit.setText("Error");
                resultEdit.setHint("");
                resultEdit.setText("");
//            Toast.makeText(this, "Reached the maximum number of digits(15)", Toast.LENGTH_SHORT).show();
            }
            else {
                resultEdit.setText(result);

                historyItems.add(inputEdit.getText().toString());
                historyItems1.add("=" + resultEdit.getText().toString());

                saveData();

                MyAdapters adapter = new MyAdapters(this, historyItems, historyItems1);
                listView.setAdapter(adapter);

                inputEdit.setText(result);
//            inputEdit.setText("");
                resultEdit.setText("");
                resultEdit.setHint("");
            }

            count++;
        }

        return result;
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("Calculator", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(historyItems);
        editor.putString("historyItems", json);
        editor.apply();

        SharedPreferences.Editor editor1 = sharedPreferences.edit();
        Gson gson1 = new Gson();
        String json1 = gson1.toJson(historyItems1);
        editor1.putString("historyItems1", json1);
        editor1.apply();
    }

    private void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences("Calculator", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("historyItems", "");
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        historyItems = gson.fromJson(json, type);

        Gson gson1 = new Gson();
        String json1 = sharedPreferences.getString("historyItems1", "");
        Type type1 = new TypeToken<ArrayList<String>>() {}.getType();
        historyItems1 = gson1.fromJson(json1, type1);

        if (historyItems == null && historyItems1 == null) {
            historyItems = new ArrayList<>();
            historyItems1 = new ArrayList<>();
        }
        else{

            MyAdapters adapter = new MyAdapters(this, historyItems, historyItems1);
            listView.setAdapter(adapter);

        }
    }

    private String liveEvaluatePR(String s)  {
        System.out.println("default  String s = " + s);

        int lastOpIndex = 0;

        lastOpIndex = s.lastIndexOf("+")> lastOpIndex? s.lastIndexOf("+"): lastOpIndex;
        lastOpIndex = s.lastIndexOf("-")> lastOpIndex? s.lastIndexOf("-"): lastOpIndex;
        lastOpIndex = s.lastIndexOf("÷")> lastOpIndex? s.lastIndexOf("÷"): lastOpIndex;
        lastOpIndex = s.lastIndexOf("*")> lastOpIndex? s.lastIndexOf("*"): lastOpIndex;

        System.out.println("last operator position = " + lastOpIndex);
        String positionChar = String.valueOf(s.charAt(lastOpIndex));
        System.out.println("last index operator = " + s.charAt(lastOpIndex));

        String beforeOp = s.substring(0, lastOpIndex);
        String afterOp = s.substring(lastOpIndex +1 , s.length());
        System.out.println("before operator = " + beforeOp);
        System.out.println("after operator = " + afterOp);

        Context rhino = Context.enter();

        rhino.setOptimizationLevel(-1);

        Scriptable scriptable = rhino.initStandardObjects();
        beforeOp = rhino.evaluateString(scriptable,beforeOp,"javascript",1,null).toString();
        System.out.println("calculate beforeOp --> " + beforeOp);

        if (s.contains("*")){
            String Fresult = beforeOp + "*" + afterOp + "/100";
            System.out.println("result ( * ) ANS = " + Fresult);
            rhino.setOptimizationLevel(-1);
            result = rhino.evaluateString(scriptable,Fresult,"javascript",1,null).toString();
        }

        if (s.contains("÷") && !s.contains("/100")){
            String Fresult = beforeOp + positionChar + afterOp + "*100";
            System.out.println("result ( ÷ ) = " + Fresult);
            rhino.setOptimizationLevel(-1);
            result = rhino.evaluateString(scriptable,Fresult.replace("÷", "/"),"javascript",1,null).toString();
        }

        if (s.contains("+") && positionChar.equals("+")){
            String Fresult = beforeOp + positionChar + beforeOp + "*" + afterOp + "/100";
            System.out.println("result ( + ) = " + Fresult);
            rhino.setOptimizationLevel(-1);
            result = rhino.evaluateString(scriptable,Fresult.replace("÷", "/"),"javascript",1,null).toString();
        }

        if (s.contains("-") && positionChar.equals("-")){
            String Fresult = beforeOp + positionChar + beforeOp + "*" + afterOp + "/100";
            System.out.println("result ( - ) = " + Fresult);
            rhino.setOptimizationLevel(-1);
            result = rhino.evaluateString(scriptable,Fresult.replace("÷", "/"),"javascript",1,null).toString();
        }

        System.out.println("process result --> " + result);

        pr = ArrayUtils.remove(pr, 0);

        no = result + pr[0];
        String s1 = result + pr[0];
        System.out.println("final string no = result + pr[0] ==> " + no);

        if (s1.contains("%")){
            hintRecall();
        }

        String finalResult = "";
        rhino.setOptimizationLevel(-1);
        finalResult = rhino.evaluateString(scriptable,s1.replace("M","-").replace("÷", "/"),"javascript",1,null).toString();

        System.out.println("FINAL ANS ==> " + finalResult);

        System.out.println("-------------END--------------");

        resultEdit.setHint(finalResult);

        return no;
    }

    private void hintRecall() {
        if (no.contains("%")){
            pr = no.split("%",2);
            String s = pr[0];
            liveEvaluatePR(s.replace("M","-").replace("÷", "/"));
        }
    }

}