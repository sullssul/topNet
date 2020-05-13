package Model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.vladkerasosi.ObjectSerializer;

import java.io.Serializable;

@Entity(tableName = "purchases")
public class Purchases extends ObjectSerializer implements Serializable {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name ="purchases_id")
    private long id;

    @ColumnInfo(name ="purchases_date")
    private String data;

    @ColumnInfo(name ="purchases_sum")
    private float sum;

    @Embedded
    private TypesOfPurchases typesOfPurchases;


    @ColumnInfo(name ="purchases_name")
    private String name;

    @ColumnInfo(name ="purchases_des")
    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TypesOfPurchases getTypesOfPurchases() {
        return typesOfPurchases;
    }

    public String getTypesOfPurchasesName() {
        return typesOfPurchases.getType_name_purchases();
    }

    public void setTypesOfPurchases(TypesOfPurchases typesOfPurchases) {
        this.typesOfPurchases = typesOfPurchases;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    @Ignore
    public Purchases() {
    }

    public Purchases(long id, String data, float sum, TypesOfPurchases typesOfPurchases, String name, String description) {
        this.id = id;
        this.data = data;
        this.sum = sum;
        this.typesOfPurchases = typesOfPurchases;
        this.name = name;
        this.description = description;
    }
}
