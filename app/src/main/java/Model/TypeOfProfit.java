package Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "profit_types_table")
public class TypeOfProfit {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="type_profit_id")
    private long id;

    private String type_profit_name;

    public TypeOfProfit(long id, String type_profit_name) {
        this.id = id;
        this.type_profit_name = type_profit_name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType_profit_name() {
        return type_profit_name;
    }

    public void setType_profit_name(String type_profit_name) {
        this.type_profit_name = type_profit_name;
    }
}
