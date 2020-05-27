package com.prikolastochka.finanser;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

import Data.AppDatabase;
import Model.Purchases;

import Model.TypesOfPurchases;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;

    private ArrayList<TypesOfPurchases> typesOfPurchases= new ArrayList<>();
    private ArrayList<Purchases> purchasesArrayList= new ArrayList<>();
    private HashMap<String,Float> piechartItem= new HashMap<>();
    private NotificationManager notifManager;
    private float startX;

    private Date currentDate = new Date();
    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    private String dateText = dateFormat.format(currentDate);

    private AppDatabase appDatabase;
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    public static float Balance=0;
    public static int times;
    public static String typeSortPurchases;

    private ArrayList<String> types=new ArrayList<>();
    private  SharedPreferences sPrefSettings;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recylerView);
        sPrefSettings = PreferenceManager.getDefaultSharedPreferences(this);
        setDarkMode();

        appDatabase = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"AppDB")
                .allowMainThreadQueries()
                .build();

        loadData();

        if(typesOfPurchases.isEmpty())  firstStart();

    }

    private void firstStart(){
        appDatabase.getPur_Pro_Dao().addTypeOfPurchases(new TypesOfPurchases(0,"Авто"));
        appDatabase.getPur_Pro_Dao().addTypeOfPurchases(new TypesOfPurchases(0,"Еда"));
        appDatabase.getPur_Pro_Dao().addTypeOfPurchases(new TypesOfPurchases(0,"Медицина"));
        appDatabase.getPur_Pro_Dao().addTypeOfPurchases(new TypesOfPurchases(0,"Развлечения"));
        appDatabase.getPur_Pro_Dao().addTypeOfPurchases(new TypesOfPurchases(0,"Связь"));
        appDatabase.getPur_Pro_Dao().addTypeOfPurchases(new TypesOfPurchases(0,"Другое"));
        typesOfPurchases.addAll(appDatabase.getPur_Pro_Dao().getAllTypeOfPurchases());
    }

    private void loadData(){
        purchasesArrayList.clear();
        typesOfPurchases.clear();
        purchasesArrayList.addAll(appDatabase.getPur_Pro_Dao().getAllPurchases());
        typesOfPurchases.addAll(appDatabase.getPur_Pro_Dao().getAllTypeOfPurchases());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        context=this;
        loadItem("Balance",context);
        loadData();
        loadItem("times",context);
        loadItem("typeSortPurchases",context);
        sortingBytime();
        sortingByType();
      //  setRecyclerView();
        setPiechartItem();
        setTextView();
        checkLimit();
    }

    private void convertToString(){
        types.clear();
            for (TypesOfPurchases typePurchases : typesOfPurchases) {
                types.add(typePurchases.getType_name_purchases());
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void editPurchases(final Purchases purchases, final int position)
    {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        @SuppressLint("InflateParams") View view = layoutInflaterAndroid.inflate(R.layout.edit_item, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

         final EditText nameEditText = view.findViewById(R.id.nameEditText);
         final EditText priceEditText = view.findViewById(R.id.priceEditText);
         final EditText decriptionEditText=view.findViewById(R.id.descriptiontEdit);
         final EditText dateEditText=view.findViewById(R.id.dateEditText);
         final Spinner spinner=view.findViewById(R.id.spinerEdit);
        final TextView textView= view.findViewById(R.id.titleTV);

        textView.setText("Редактирование записей");


        priceEditText.setVisibility(View.VISIBLE);
        decriptionEditText.setVisibility(View.VISIBLE);
        dateEditText.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);

        if(purchases!=null) {
            nameEditText.setText(purchases.getName());
            priceEditText.setText(String.valueOf(purchases.getSum()));
            decriptionEditText.setText(purchases.getDescription());
            dateEditText.setText(purchases.getData());

            convertToString();

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
            for (int i = 0; i < types.size(); i++) {

                if (purchases.getTypesOfPurchasesName().equals(types.get(i))) {
                    spinner.setSelection(i);
                }
            }


            alertDialogBuilderUserInput.setCancelable(true)
                    .setPositiveButton("Обновить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deletePurchases(purchases, position);

                        }
                    })
                    .setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int id) {
                            dialog.cancel();
                        }
                    });
        }

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
       // Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(R.color.backgroundDialog);
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(nameEditText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Введите название покупки!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(priceEditText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Введите сумму!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(dateEditText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Введите дату!", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(decriptionEditText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Введите заметку!", Toast.LENGTH_SHORT).show();
                    return;
                } else{
                    alertDialog.dismiss();
                }


                if (purchases!=null) {

                    Balance+=purchases.getSum();
                    updatePurchases(nameEditText.getText().toString(),
                            decriptionEditText.getText().toString() ,
                            Float.parseFloat(priceEditText.getText().toString()),
                            dateEditText.getText().toString(),
                            new TypesOfPurchases(spinner.getSelectedItemId(),spinner.getSelectedItem().toString()),
                            position);
                }
            }
        });







    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updatePurchases(String name, String description, float sum, String date, TypesOfPurchases typeOfPurchases, int position){

        Balance-=sum;

        Purchases purchases=purchasesArrayList.get(position);

        purchases.setName(name);
        purchases.setDescription(description);
        purchases.setData(date);
        purchases.setSum(sum);
        purchases.setTypesOfPurchases(typeOfPurchases);

        new UpdatePurchasesAsync().execute(purchases);
        purchasesArrayList.set(position,purchases);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void deletePurchases(Purchases purchases, int position){

        Balance+=purchases.getSum();
        purchasesArrayList.remove(position);

        new DeletePurchasesAsync().execute(purchases);


    }

    @SuppressLint("ShowToast")
    public void showNotify(String Title,String bigText,String smallText){

        final int NOTIFY_ID = 0; // ID of notification
        String id = "LoL";
        String title ="kek";

        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, id);
            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentTitle(Title)                            // required
                    .setSmallIcon(R.drawable.ic_attach_money_black_24dp)   // required
                    .setContentText(smallText) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
        else {
            builder = new NotificationCompat.Builder(this, id);
            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentTitle(Title)                            // required
                    .setSmallIcon(R.drawable.ic_attach_money_black_24dp)   // required
                    .setContentText(smallText) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);

        Toast toast = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            toast = Toast.makeText(MainActivity.this, "Превышен ежемесячный бюджет", Toast.LENGTH_LONG);
        }
        toast.show();
    }

    private   void setDarkMode(){
        if(sPrefSettings.getBoolean("DarkMode",false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }

    private void checkLimit(){
        if(sPrefSettings.getBoolean("Notify",false)) {
            float limit = Float.parseFloat(sPrefSettings.getString("Limit", "50000"));
            float totalSum = 0;
            for (Purchases purchases : purchasesArrayList) {
                if(purchases.getMonth()==Integer.parseInt(dateText.substring(3,5)))
                totalSum += purchases.getSum();
            }
            if (totalSum > limit) {
                showNotify("Превышен ежемясяный бюджет!","Постарайтесь сократить ваши расходы!","Сократите доходы!");
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: //первое касание
                startX = event.getX();
                break;
            case MotionEvent.ACTION_UP: //отпускание
                float stopX = event.getX();
                if (stopX < startX) {
                    Intent intent = new Intent(this, Profit_Activity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.go_next_in, R.anim.go_next_out);

                }
                break;
        }
        return true;


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveItem("Balance",context);
    }

    @Override
    protected void onPause() {
        super.onPause();
       saveItem("Balance",context);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_profit, menu);
        return true;
    }

    public void start_settings(MenuItem item) {
        Intent intent = new Intent(this, Settings_Activity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sortingBytime(){
        if(times!=3){
            int month= Integer.parseInt(dateText.substring(3,5));
            month-=times;
            for(Iterator<Purchases> iterator=purchasesArrayList.iterator();iterator.hasNext();) {
                Purchases purchases=iterator.next();
                if(purchases.getMonth()!=month){
                   iterator.remove();
                }
            }
        }
        setPiechartItem();
        setRecyclerView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sortingByType(){
        if(!typeSortPurchases.equals("Все категории")){
            for(Iterator<Purchases> iterator=purchasesArrayList.iterator();iterator.hasNext();) {
                Purchases purchases=iterator.next();
                if(!purchases.getTypesOfPurchasesName().equals(typeSortPurchases)){
                    iterator.remove();
                }
            }
        }
        setPiechartItem();
        setRecyclerView();
    }

    public void setRecyclerView(){

        recyclerView = findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true);
        dataAdapter = new DataAdapter(purchasesArrayList, MainActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(dataAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
    }

    @SuppressLint("SetTextI18n")
    private void setTextView() {
        TextView textView=findViewById(R.id.balance_purchases);
        textView.setText(Balance+"");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onRestart() {
        super.onRestart();
        setPiechartItem();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setPiechartItem(){

        piechartItem.clear();
        PieChart mPieChart = findViewById(R.id.piechart);
        mPieChart.clearChart();

        if(!purchasesArrayList.isEmpty()) {

            Random rand = new Random();

            for (int i = 0; i < typesOfPurchases.size(); i++) {

                piechartItem.put(typesOfPurchases.get(i).getType_name_purchases(), (float) 0);
            }
            for (int i = 0; i < purchasesArrayList.size(); i++) {

                String type = purchasesArrayList.get(i).getTypesOfPurchasesName();
                try{
                piechartItem.put(type, piechartItem.get(type) + purchasesArrayList.get(i).getSum());
            } catch (NullPointerException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                  }
            }
            for (HashMap.Entry<String, Float> item : piechartItem.entrySet()) {
                float r = rand.nextFloat();
                float g = rand.nextFloat();
                float b = rand.nextFloat();
               if( item.getValue()!=0)
                mPieChart.addPieSlice(new PieModel(item.getKey(), item.getValue(), Color.rgb(r, g, b)));

            }
        }


    }

    public void tap_add_purchases(View view) {
        Intent intent = new Intent(this, Add_new.class);
        intent.putExtra("typeAdd","purchases");
        startActivity(intent);
    }

    public void GotToProfit(View view) {
        Intent intent = new Intent(this, Profit_Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.go_next_in, R.anim.go_next_out);
    }

    public void start_settings_sorting_type(MenuItem item) {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        @SuppressLint("InflateParams") View view = layoutInflaterAndroid.inflate(R.layout.edit_item, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);


        final TextView textView= view.findViewById(R.id.titleTV);
        final EditText nameEditText = view.findViewById(R.id.nameEditText);
        final EditText priceEditText = view.findViewById(R.id.priceEditText);
        final EditText decriptionEditText=view.findViewById(R.id.descriptiontEdit);
        final EditText dateEditText=view.findViewById(R.id.dateEditText);
        final Spinner spinner=view.findViewById(R.id.spinerEdit);

        textView.setText("Выберите категории:");
        nameEditText.setVisibility(View.GONE);
        priceEditText.setVisibility(View.GONE);
        decriptionEditText.setVisibility(View.GONE);
        dateEditText.setVisibility(View.GONE);

        spinner.setVisibility(View.VISIBLE);
        convertToString();
        types.add("Все категории");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        loadItem("typeSortPurchases",context);

        for (int i = 0; i < types.size(); i++){
            if(types.get(i).equals(typeSortPurchases)){
                spinner.setSelection(i);
            }
        }


        alertDialogBuilderUserInput.setCancelable(true)
                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        typeSortPurchases=spinner.getSelectedItem().toString();
                        saveItem("typeSortPurchases",context);
                        types.remove(types.size()-1);
                        loadData();
                        sortingByType();
                    }
                });
        alertDialogBuilderUserInput.setNegativeButton( "Отмена", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        } );


        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

    }

    public void start_settings_sorting_time(MenuItem item) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        @SuppressLint("InflateParams") View view = layoutInflaterAndroid.inflate(R.layout.edit_item, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);


        final TextView textView= view.findViewById(R.id.titleTV);
        final EditText nameEditText = view.findViewById(R.id.nameEditText);
        final EditText priceEditText = view.findViewById(R.id.priceEditText);
        final EditText decriptionEditText=view.findViewById(R.id.descriptiontEdit);
        final EditText dateEditText=view.findViewById(R.id.dateEditText);
        final Spinner spinner=view.findViewById(R.id.spinerEdit);

        textView.setText("Показывать расходы за:");
        nameEditText.setVisibility(View.GONE);
        priceEditText.setVisibility(View.GONE);
        decriptionEditText.setVisibility(View.GONE);
        dateEditText.setVisibility(View.GONE);

        spinner.setVisibility(View.VISIBLE);
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.times, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        loadItem("times",context);
        if(times==0) {
            spinner.setSelection(0);
        } else if(times==1){
            spinner.setSelection(1);
        } else if(times==2){
            spinner.setSelection(2);
        } else if(times==3) {
            spinner.setSelection(3);
        }



        alertDialogBuilderUserInput.setCancelable(true)
                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        times=spinner.getSelectedItemPosition();
                        saveItem("times",context);
                        loadData();
                        sortingBytime();
                    }
                });
        alertDialogBuilderUserInput.setNegativeButton( "Отмена", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        } );


        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();
    }

    public static void saveItem(String item, Context context){
        SharedPreferences sPref;
        if(item.equals("times")){
        sPref = context.getSharedPreferences("times",MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putInt("times", times);
        editor.apply();

        }

        if(item.equals("Balance")) {
            sPref =context.getSharedPreferences("Balance", MODE_PRIVATE);
            SharedPreferences.Editor editor = sPref.edit();
            editor.putFloat("Balance", Balance);
            editor.apply();
        }

        if(item.equals("typeSortPurchases")){
            sPref = context.getSharedPreferences("typeSortPurchases",MODE_PRIVATE);
            SharedPreferences.Editor editor = sPref.edit();
            editor.putString("typeSortPurchases", typeSortPurchases);
            editor.apply();
        }

        if(item.equals("typeSortProfit")){
            sPref = context.getSharedPreferences("typeSortProfit",MODE_PRIVATE);
            SharedPreferences.Editor editor = sPref.edit();
            editor.putString("typeSortProfit", Profit_Activity.typeSortProfit);
            editor.apply();
        }

    }

    public static void loadItem(String item,Context context){
        SharedPreferences sPref;
        if(item.equals("times")) {
            sPref = context.getSharedPreferences("times", MODE_PRIVATE);
            times = sPref.getInt("times", 0);
        }

        if(item.equals("Balance")) {
            sPref = context.getSharedPreferences("Balance", MODE_PRIVATE);
            Balance = sPref.getFloat("Balance", 0);
        }

        if(item.equals("typeSortPurchases")){
            sPref = context.getSharedPreferences("typeSortPurchases", MODE_PRIVATE);
            typeSortPurchases = sPref.getString("typeSortPurchases", "Все категории");
        }

        if(item.equals("typeSortProfit")){
            sPref = context.getSharedPreferences("typeSortProfit", MODE_PRIVATE);
            Profit_Activity.typeSortProfit = sPref.getString("typeSortProfit", "Все категории");
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class UpdatePurchasesAsync extends AsyncTask<Purchases,Void,Void>{

        @Override
        protected Void doInBackground(Purchases... purchases) {
            appDatabase.getPur_Pro_Dao().updatePurchases(purchases[0]);
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dataAdapter.notifyDataSetChanged();
            setPiechartItem();
            setTextView();
            checkLimit();
        }
    }

   @SuppressLint("StaticFieldLeak")
   private class DeletePurchasesAsync extends AsyncTask<Purchases,Void,Void>{
       @Override
       protected Void doInBackground(Purchases... purchases) {
           appDatabase.getPur_Pro_Dao().deletePurchases(purchases[0]);

           return null;
       }

       @RequiresApi(api = Build.VERSION_CODES.O)
       @Override
       protected void onPostExecute(Void aVoid) {
           super.onPostExecute(aVoid);
           dataAdapter.notifyDataSetChanged();
           setPiechartItem();
           setTextView();
           checkLimit();
       }
   }


}
