package Model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;

import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "profit")
public class Profit implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="profit_id")
    private long id;

    @ColumnInfo(name="profit_date")
    private String date;

    @ColumnInfo(name="profit_sum")
    private float sum;

    @ColumnInfo(name ="profit_name")
    private String name;

    @Embedded
    private TypeOfProfit typeOfProfit;

    public Profit(long id, String date, float sum, String name, TypeOfProfit typeOfProfit) {
        this.id = id;
        this.date = date;
        this.sum = sum;
        this.name = name;
        this.typeOfProfit = typeOfProfit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TypeOfProfit getTypeOfProfit() {
        return typeOfProfit;
    }

    public String getTypeOfProfitName() {
        return typeOfProfit.getType_profit_name();
    }

    public void setTypeOfProfit(TypeOfProfit typeOfProfit) {
        this.typeOfProfit = typeOfProfit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
