package Data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import Model.Profit;
import Model.Purchases;
import Model.TypeOfProfit;
import Model.TypesOfPurchases;

@Database(entities = {Purchases.class, Profit.class, TypesOfPurchases.class, TypeOfProfit.class},version = 1)

public abstract class AppDatabase extends RoomDatabase {

    public abstract pur_prof_Dao getPur_Pro_Dao();

}
