package Data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import Model.Purchases;
import Model.TypesOfPurchases;

@Dao
public interface purchasesDAO {

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
    long addTypeOfPurchases(TypesOfPurchases typesOfPurchases);

    @Update
    public void updateTypeOfPurchases(TypesOfPurchases typesOfPurchases);

    @Delete
    public void deleteTypeOfPurchases(TypesOfPurchases typesOfPurchases);

    @Query("select * from purchases_types_table")
    public List<TypesOfPurchases> getAllTypeOfPurchases();

    @Query("select * from purchases_types_table where type_purchases_id ==:typePurId ")
    public  Purchases getTypeOfPurchases(long typePurId);

}
