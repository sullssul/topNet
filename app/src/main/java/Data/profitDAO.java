package Data;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import Model.Profit;
import Model.Purchases;
import Model.TypeOfProfit;
import Model.TypesOfPurchases;

public interface profitDAO {

    @Insert
    public long addProfit(Profit profit);

    @Update
    public void updateProfit(Profit profit);

    @Delete
    public void deleteProfit(Profit profit);

    @Query("select * from profit")
    public List<Profit> getAllProfit();

    @Query("select * from profit where profit_id ==:profId ")
    public  Profit getProfit(long profId);

    @Insert
    long addTypeOfProfit(TypeOfProfit typeOfProfit);

    @Update
    public void updateTypeOfProfit( TypeOfProfit typeOfProfit);

    @Delete
    public void deleteTypeOfProfit(TypeOfProfit typeOfProfit);

    @Query("select * from profit_types_table")
    public List<TypeOfProfit> getAllTypeOfProfit();

    @Query("select * from profit_types_table where type_profit_id ==:typeProfID ")
    public  TypeOfProfit getTypeOfProfit(long typeProfID);
}
