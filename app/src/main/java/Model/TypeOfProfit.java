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
}
