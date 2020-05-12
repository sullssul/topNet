package Model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.vladkerasosi.ObjectSerializer;

import java.io.IOException;
import java.util.ArrayList;

@Entity(tableName = "purchases_types_table")
public class TypesOfPurchases  {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="type_purchases_id")
    private long id;

    private String type_name_purchases;

    public TypesOfPurchases(long id, String type_name_purchases) {
        this.id = id;
        this.type_name_purchases = type_name_purchases;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType_name_purchases() {
        return type_name_purchases;
    }

    public void setType_name_purchases(String type_name_purchases) {
        this.type_name_purchases = type_name_purchases;
    }

    //    public int size(){
//        return this.typesOfPurchases.size();
//    }
//
//    public void add(String newType){
//        this.typesOfPurchases.add(newType);
//    }
//
//    public ArrayList<String> getTypesOfPurchases() {
//        return typesOfPurchases;
//    }
//
//    public void setTypesOfPurchases(ArrayList<String> typesOfPurchases) {
//        this.typesOfPurchases = typesOfPurchases;
//    }
//    public String get(int i){
//        return typesOfPurchases.get(i);
//    }
//
//    public TypesOfPurchases(Context context) {
//        sPref = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
//
//    }
//
//
//
//    public void save(){
//       // sPref = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = sPref.edit();
//        try {
//            editor.putString("typesOfPurchases", ObjectSerializer.serialize(typesOfPurchases));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        editor.apply();
//    }
//    public void load(){
//      // sPref = getSharedPreferences(PREFERENCE_NAME,MODE_PRIVATE);
//        try {
//            typesOfPurchases = ( ArrayList<String>) ObjectSerializer.deserialize(sPref.getString(PREFERENCE_NAME, ObjectSerializer.serialize(new ArrayList<String>())));
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
   // }
}
