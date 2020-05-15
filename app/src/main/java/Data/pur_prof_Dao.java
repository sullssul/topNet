package Data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import Model.Profit;
import Model.Purchases;
import Model.TypeOfProfit;
import Model.TypesOfPurchases;

@Dao
public interface pur_prof_Dao {

    @Insert
    public long addPurchases(Purchases purchases);

    @Update
    public void updatePurchases(Purchases purchases);

    @Delete
    public void deletePurchases(Purchases purchases);

    @Query("select * from purchases")
    public List<Purchases> getAllPurchases();

    @Query("select * from purchases where purchases_id ==:purId ")
    public  Purchases getPurchases(long purId);

    @Insert
    public long addTypeOfPurchases(TypesOfPurchases typesOfPurchases);

    @Update
    public void updateTypeOfPurchases(TypesOfPurchases typesOfPurchases);

    @Delete
    public void deleteTypeOfPurchases(TypesOfPurchases typesOfPurchases);

    @Query("select * from purchases_types_table")
    public List<TypesOfPurchases> getAllTypeOfPurchases();

    @Query("select * from purchases_types_table where type_purchases_id ==:typePurId ")
    public  TypesOfPurchases getTypeOfPurchases(long typePurId);

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
    public long addTypeOfProfit(TypeOfProfit typeOfProfit);

    @Update
    public void updateTypeOfProfit( TypeOfProfit typeOfProfit);

    @Delete
    public void deleteTypeOfProfit(TypeOfProfit typeOfProfit);

    @Query("select * from profit_types_table")
    public List<TypeOfProfit> getAllTypeOfProfit();

    @Query("select * from profit_types_table where type_profit_id ==:typeProfID ")
    public  TypeOfProfit getTypeOfProfit(long typeProfID);
}
