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


}
