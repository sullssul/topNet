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
     void addPurchases(Purchases purchases);

    @Update
     void updatePurchases(Purchases purchases);

    @Delete
     void deletePurchases(Purchases purchases);

    @Query("select * from purchases")
     List<Purchases> getAllPurchases();

//    @Query("select * from purchases where purchases_id ==:purId ")
//      Purchases getPurchases(long purId);

    @Insert
     void addTypeOfPurchases(TypesOfPurchases typesOfPurchases);

    @Update
     void updateTypeOfPurchases(TypesOfPurchases typesOfPurchases);

    @Delete
     void deleteTypeOfPurchases(TypesOfPurchases typesOfPurchases);

    @Query("select * from purchases_types_table")
     List<TypesOfPurchases> getAllTypeOfPurchases();

//    @Query("select * from purchases_types_table where type_purchases_id ==:typePurId ")
//      TypesOfPurchases getTypeOfPurchases(long typePurId);

    @Insert
     void addProfit(Profit profit);

    @Update
     void updateProfit(Profit profit);

    @Delete
     void deleteProfit(Profit profit);

    @Query("select * from profit")
     List<Profit> getAllProfit();

//    @Query("select * from profit where profit_id ==:profId ")
//    public  Profit getProfit(long profId);

    @Insert
     void addTypeOfProfit(TypeOfProfit typeOfProfit);

    @Update
     void updateTypeOfProfit( TypeOfProfit typeOfProfit);

    @Delete
     void deleteTypeOfProfit(TypeOfProfit typeOfProfit);

    @Query("select * from profit_types_table")
     List<TypeOfProfit> getAllTypeOfProfit();

//    @Query("select * from profit_types_table where type_profit_id ==:typeProfID ")
//      TypeOfProfit getTypeOfProfit(long typeProfID);
}
