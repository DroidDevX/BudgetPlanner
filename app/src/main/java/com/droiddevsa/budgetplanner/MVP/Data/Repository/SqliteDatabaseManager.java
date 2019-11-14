package com.droiddevsa.budgetplanner.MVP.Data.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
//Test imports

import com.droiddevsa.budgetplanner.MVP.Data.Models.CategorySubtotal;
import com.droiddevsa.budgetplanner.Utilities.BudgetAppCurrency;
import com.droiddevsa.budgetplanner.MVP.Data.Models.BudgetItem;
import com.droiddevsa.budgetplanner.MVP.Data.Models.Budget;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class SqliteDatabaseManager extends SQLiteOpenHelper {
    //DEFAULT SETTINGS

    static boolean dbIsBusy;
    public final String TAG = "SQLiteDatabaseManager";
    public static final String defaultDBName="budgetApp.db";


    public SqliteDatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.d(TAG, "SqliteDatabaseManager.java, Start DatabaseHander(): constructor");
        dbIsBusy=false;
    }



    //Currencies
    public void setDefaultCurrency(int currencyID)
    {
        Log.d(TAG, "setDefaultCurrency: ");
        String query = "UPDATE DEFAULT_CURRENCY SET CURRENCY_ID="+ currencyID +" WHERE ID=1;";
        Log.i(TAG,"Query: "+query);

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();

    }

    public BudgetAppCurrency getDefaultCurrency()
    {
        Log.d(TAG,"getDefaultCurrency()");
        String query = "SELECT * FROM DEFAULT_CURRENCY JOIN CURRENCY USING(CURRENCY_ID);";
        Log.i(TAG,"Query: "+query);
        SQLiteDatabase db = getReadableDatabase();
        Cursor c =db.rawQuery(query,null);
        boolean atLeastOneRecordExists = c.moveToFirst();

        BudgetAppCurrency currency=null;
        if(atLeastOneRecordExists) {
            int currencyID = Integer.parseInt(c.getString(1));
            String name = c.getString(2);
            String code = c.getString(3);
            String symbol = c.getString(4);
            String pos = c.getString(5);
            currency = new BudgetAppCurrency(currencyID, name, code, symbol, pos);
            c.close();
            db.close();
            return currency;

        }
        return currency;
    }

    public  ArrayList<BudgetAppCurrency> getListOfWorldCurrencies()
    {
        Log.d(TAG,"getListOfWorldCurrencies()");
        String noneCurrency ="SELECT * FROM CURRENCY WHERE NAME='NONE';";
        String query = "SELECT * FROM CURRENCY WHERE NAME !='NONE' ORDER BY NAME;";
        Log.i(TAG,"Query: "+query);
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<BudgetAppCurrency> currencies = new ArrayList<BudgetAppCurrency>();


        Cursor c = db.rawQuery(noneCurrency,null);
        c.moveToFirst();
        int currencyID = Integer.parseInt(c.getString(0));
        String name = c.getString(1);
        String code = c.getString(2);
        String symbol = c.getString(3);
        String pos = c.getString(4);
        c.close();
        BudgetAppCurrency currency = new BudgetAppCurrency(currencyID,name,code,symbol,pos);
        currencies.add(currency);

        c = db.rawQuery(query,null);
        boolean atLeastOneRecordExists = c.moveToFirst();
        if(atLeastOneRecordExists)
            while (!c.isAfterLast())
            {
                currencyID = Integer.parseInt(c.getString(0));
                name = c.getString(1);
                code = c.getString(2);
                symbol = c.getString(3);
                pos = c.getString(4);
                currency = new BudgetAppCurrency(currencyID,name,code,symbol,pos);
                currencies.add(currency);
                Log.i(TAG,"Added a currency! Currency State: "+ currency.toString());
                c.moveToNext();
            }
        c.close();
        db.close();
        return  currencies;
    }


    //Budget table
    public int getLastGeneratedBudgetID() {
        //Description: Retrieves the generated item id (Primary key) of the most recent item description entered into the Item table;

        Log.d(TAG,"getLastGeneratedBudgetID()");
        String query = "SELECT BUDGET_ID FROM BUDGET ORDER BY BUDGET_ID DESC LIMIT 1;";
        Log.i(TAG,"Query: "+query);

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        int itemID;
        boolean atLeastOneRecordExist = c.moveToFirst();

        if(atLeastOneRecordExist){
            String itemIDStr = c.getString(c.getColumnIndex("BUDGET_ID"));
            itemID = Integer.parseInt(itemIDStr);
            c.close();
            db.close();
            Log.i(TAG,"getLastGeneratedBudgetID() -end");
            return itemID;
        }
        else {
            Log.i(TAG,"Error. Null results returned!Budget ID returned =-1");
            Log.i(TAG,"getLastGeneratedBudgetID() -end");
            c.close();
            db.close();
            return -1;
        }

    }

    public void createNewBudget() {
        /*Create a new budget. A new Corresponding budget record would be inserted in the Budget Table within the App db*/
        //Add values
        Log.d(TAG,"createNewBudget()");

        int newBudgetID = getLastGeneratedBudgetID();

        if(newBudgetID ==-1)
            newBudgetID = 1;
        else
            newBudgetID = newBudgetID+1;

        Log.i(TAG,"Query -> None used. Insert opertation performed using ContentValues class");
        Date currentDate = Calendar.getInstance().getTime();
        Log.i(TAG,"Created Date: "+ currentDate.toString());

        ContentValues values = new ContentValues();
        values.put("BUDGET_ID", newBudgetID);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        values.put("DATE",dateFormat.format(currentDate));

        //Execute
        SQLiteDatabase db = getWritableDatabase();
        db.insert("BUDGET", null, values);
        db.close();

        
    }

    public void deleteBudget(String budgetID){
        Log.d(TAG, "deleteBudget: ");
        //Perform cascade delete
        //Delete from ITEMENTRY then BUDGET tables
        String query1 = "DELETE FROM ITEMENTRY WHERE BUDGET_ID="+budgetID+";";
        String query2 = "DELETE FROM BUDGET WHERE BUDGET_ID="+budgetID;

        Log.i(TAG,"deleteBudget(), query 1: "+query1);
        Log.i(TAG,"deleteBudget(), query 2: "+query2);

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query1);
        db.execSQL(query2);
        db.close();
    }

    public Budget getBudget(String budgetID){
        Log.d(TAG,"getBudget()");
        String query = "SELECT * FROM BUDGET WHERE BUDGET_ID="+budgetID+";";
        Log.i(TAG,"Query: "+query);
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        boolean budgetExists = cursor.moveToFirst();
        Budget budget=null;
        if(budgetExists){
            BudgetAppCurrency defaultCurrency = getDefaultCurrency();
            int budgetid = cursor.getInt(0);
            String dateCreatedStr = cursor.getString(1);
            Date createdDate;
            try {
                createdDate =new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).parse(dateCreatedStr);
                budget = new Budget(budgetid,createdDate);
                budget.setDefaultCurrency(defaultCurrency);
            }catch (ParseException e){
                Log.e(TAG,"FATAL DATE PARSE ERROR: "+e.toString());
                System.exit(1);
            }

        }
        db.close();
        return budget;
    }

    public ArrayList<Budget> getAllBudgets(){
        Log.d(TAG, "getAllBudgets: ");
        String query = "SELECT * FROM BUDGET ORDER BY DATE;";
        Log.i(TAG,"Query: "+query);

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        boolean budgetExists = cursor.moveToFirst();
        ArrayList<Budget> budgetList = null;

        if(budgetExists){
            budgetList = new ArrayList<>();
            BudgetAppCurrency defaultCurrency = getDefaultCurrency();
            while(!cursor.isAfterLast()) {

                try {
                        int budgetid = cursor.getInt(0);
                        String dateCreatedStr = cursor.getString(1);
                        Date createdDate;
                        createdDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(dateCreatedStr);
                        Budget budget = new Budget(budgetid, createdDate);
                        budget.setDefaultCurrency(defaultCurrency);
                        budgetList.add(budget);
                    } catch (ParseException e) {

                            Log.e(TAG, "FATAL DATE PARSE ERROR: " + e.toString());
                            System.exit(1);
                }
                cursor.moveToNext();
            }
        }
        else
            Log.e(TAG,"getBudget() - no budgets returned");
        db.close();
        return budgetList;
    }

    public void setBudgetDate(String budgetID,Date newDate){
        Log.d(TAG, "setBudgetDate: ");
        String query ="UPDATE BUDGET SET DATE= ";
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues= new ContentValues();
        contentValues.put("BUDGET_ID",budgetID);//if fails use int?
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        contentValues.put("DATE",dateFormatter.format(newDate));

        db.update("BUDGET",contentValues,"BUDGET_ID="+budgetID,null);
        db.close();
    }


    //Item table

    public int getItemID(String itemName, String brand, String categoryName) {
        Log.d(TAG,"getItemID()");


        String query =  new StringBuilder().append("SELECT DISTINCT ITEM_ID FROM ITEM JOIN CATEGORY USING(CATEGORY_ID) WHERE ")
                .append("NAME='").append(itemName).append("' AND").append(" BRAND='").append(brand).append("' AND ")
                .append(" CATEGORY_NAME='").append(categoryName).append("';").toString();


        Log.i(TAG,"Query: "+query);

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query,null);

        boolean itemExists = c.moveToFirst();
        int itemID;
        if(itemExists)
        {
            itemID = Integer.parseInt(c.getString(0));
            db.close();
            return  itemID;
        }
        else {
            itemID = -1;
            Log.i(TAG,"Item ID: " + itemID);
            Log.i(TAG,"getItemID()-end");
            db.close();
            return itemID;
        }

    }

    public int getLastGeneratedItemID() {
        Log.d(TAG,"getLastGeneratedItemID()");
        //Description: Retreives the generated item id (Primary key) of the most recent item description entered into the Item table;
        String query = "SELECT ITEM_ID FROM ITEM ORDER BY ITEM_ID DESC LIMIT 1;";
        Log.i(TAG,"Query: "+query);

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        int itemID;
        boolean atLeastOneRecordExist = c.moveToFirst();

        if(atLeastOneRecordExist){
            String itemIDStr = c.getString(c.getColumnIndex("ITEM_ID"));
            itemID = Integer.parseInt(itemIDStr);
            c.close();
            db.close();
            Log.i(TAG,"getLastGeneratedItemID() -end");
            return itemID;
        }
        else {
            Log.i(TAG,"Error. Null results returned!");
            c.close();
            db.close();
            return -1;
        }
    }

    public int insertItem(BudgetItem newItem) {
            Log.d(TAG,"insertItemEntry()");

            SQLiteDatabase db = getWritableDatabase();
            String query = "INSERT INTO ITEM ('NAME','CATEGORY_ID','BRAND') VALUES ("+
                            "'"+ newItem.get_itemName() +"'"+
                           ",'"+ newItem.getCategoryID()+"'"+
                            ",'"+ newItem.getBrand()+"');";

            Log.i(TAG,"Query: "+query);

            db.execSQL(query);
            return getLastGeneratedItemID();
    }



    //Item entries table

    public void insertItemEntry(String budgetID, BudgetItem item){
        Log.d(TAG, "insertItemEntry: ");
        SQLiteDatabase db = getWritableDatabase();
        String[] valuesArgs = {budgetID,String.valueOf(item.getItemID()),String.valueOf(item.getAmount()),String.valueOf(item.getQuantity()),"'"+item.getCashFlow()+"'"};
        String values = TextUtils.join(",",valuesArgs);
        String query = "INSERT INTO ITEMENTRY ('BUDGET_ID','ITEM_ID','AMOUNT','QUANTITY','CASHFLOW') VALUES("+values +");";
        Log.i(TAG,"Query: "+query);

        db.execSQL(query);
        db.close();
    }

    public void insertMultipleItemEntries(String budgetID, ArrayList<BudgetItem> items){
        Log.d(TAG, "insertMultipleItemEntries: ");
        SQLiteDatabase db = getWritableDatabase();

        int numberOfColumns =5;
        String[] itemColumValue = new String[numberOfColumns] ;//ITEMENTRY Table has 5 columns values that are edited and 1 primary (auto generated)
        ArrayList<String> allValuesList = new ArrayList<>();
        for(int rowPos=0;rowPos < items.size();rowPos++) {
            BudgetItem item = items.get(rowPos);
            itemColumValue[0] = budgetID;
            itemColumValue[1] = String.valueOf(item.getItemID());
            itemColumValue[2] = String.valueOf(item.getAmount());
            itemColumValue[3] = String.valueOf(item.getQuantity());
            itemColumValue[4] = "'"+ item.getCashFlow() +"'";
            String values = "("+ TextUtils.join(",",itemColumValue) +")";
            allValuesList.add(values);
        }
        String allValues = TextUtils.join(",",allValuesList);

        String query = "INSERT INTO ITEMENTRY ('BUDGET_ID','ITEM_ID','AMOUNT','QUANTITY','CASHFLOW') VALUES"+allValues+";";
        Log.i(TAG,"Query: "+query);
        db.execSQL(query);
        db.close();
    }

    public void deleteBudgetItemEntry(BudgetItem budgeItem){
        Log.d(TAG, "deleteBudgetItemEntry: ");
        String query = new StringBuilder().append("DELETE FROM ITEMENTRY WHERE ITEMENTRY_ID =")
                                          .append(budgeItem.getItemEntryID()).append(";").toString();

        Log.i(TAG,"query: "+query);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public void deleteBudgetItemEntries(String budget_ID) {
        Log.d(TAG, "deleteBudgetItemEntries: ");
        String query = "DELETE FROM ITEMENTRY WHERE BUDGET_ID= " + budget_ID +" ;";

        Log.i(TAG, "Query: "+query);

        //Execute SQL and close DB connection
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public ArrayList<BudgetItem> getItemEntries(String budgetIDStr){
        Log.d(TAG, "getItemEntries: ");
        String query = "SELECT * FROM ITEMENTRY JOIN ITEM USING (ITEM_ID) JOIN (CATEGORY) USING(CATEGORY_ID) WHERE BUDGET_ID="+budgetIDStr+";";
        Log.i(TAG,"Query: "+query);

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        boolean atLeastOneRecordExists = cursor.moveToFirst();
        ArrayList<BudgetItem> itemEntries =null;
        if(atLeastOneRecordExists){
            itemEntries = new ArrayList<>();
            while(!cursor.isAfterLast()){
                int itemEntryID = cursor.getInt(0);
                int budgetID = cursor.getInt(1);
                int itemID = cursor.getInt(2);
                double amount = cursor.getDouble(3);
                int quantity  = cursor.getInt(4);
                String cashFlow = cursor.getString(5);
                String itemName = cursor.getString(6);
                int catID    = cursor.getInt(7);
                String brand  = cursor.getString(8);
                String categoryName = cursor.getString(9);

                itemEntries.add(new BudgetItem(itemEntryID,itemID,itemName,catID,categoryName,brand,amount,quantity,cashFlow));
                cursor.moveToNext();
            }

        }
        db.close();
        return itemEntries;
    }



    //Categories table

    public ArrayList<String> getCategories() {
        /*
         * Description: Obtains a list of categories from the DB, which can be used to specify which category the entries of a budget can belong too
         * For instance. If the user adds a new item called 'fish' to the budget, they can specify
         * that this item falls under the 'food' category.
         * */
        Log.d(TAG,"getCategories()");
        String query = "SELECT CATEGORY_NAME FROM CATEGORY;";
        Log.i(TAG,"Query: "+query);

        ArrayList<String> categories = new ArrayList<String>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        boolean atLeastOneRecordExists = c.moveToFirst();

        Log.i(TAG,"atLeastOneRecordExits: " + atLeastOneRecordExists);

        if(atLeastOneRecordExists)
            while (!c.isAfterLast())
            {
                String categoryName = c.getString(c.getColumnIndex("CATEGORY_NAME"));
                categories.add(categoryName);
                c.moveToNext();
            }
        c.close();
        db.close();
        return categories;
    }

    public ArrayList<CategorySubtotal> getSubtotalsByCategory(String budgetID, String cashFlow){
        Log.d(TAG,"getSubtotalsByCategory()");
        String query="SELECT CATEGORY_NAME,SUM(AMOUNT*QUANTITY) AS SUBTOTAL FROM ITEMENTRY JOIN ITEM USING (ITEM_ID) JOIN CATEGORY USING (CATEGORY_ID) " +
                "WHERE BUDGET_ID="+budgetID+" AND CASHFLOW='"+cashFlow+"' AND CATEGORY_NAME IN (SELECT CATEGORY_NAME FROM CATEGORY)  GROUP BY CATEGORY_NAME ORDER BY SUM(AMOUNT*QUANTITY) DESC;";

        Log.i(TAG,"Query: "+query);

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query,null);
        boolean atLeastOneRecordExist = c.moveToFirst();
        ArrayList<CategorySubtotal> subtotals=null;
        if(atLeastOneRecordExist) {
            subtotals = new ArrayList<>();
            while (!c.isAfterLast()) {
                int category = c.getPosition();
                String categoryName = c.getString(0);
                double subTotal = c.getDouble(1);
                subtotals.add(new CategorySubtotal(categoryName,subTotal));
                c.moveToNext();
            }
            db.close();
            c.close();

        }
        return subtotals;
    }

    private void setDefaultCategories(SQLiteDatabase db) {
        Log.d(TAG, "setDefaultCategories: ");
        String query = "INSERT INTO CATEGORY ('CATEGORY_NAME') VALUES ('Bills'),('Clohing')," +
                "('Entertainment'),('Equipment'),('Fees'),('Food'),('Gifts'),('Investments'),('Medical')," +
                "('Retirement'),('Salary/Wages'),('Tax'),('Transport'),('Utilities'),('Other');";

        Log.i(TAG,"Query: "+query);
        db.execSQL(query);
    }





    //Default

    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,"SqliteDatabaseManager.java, onCreate()");

        String createBudget     = "CREATE TABLE IF NOT EXISTS BUDGET ( `BUDGET_ID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `DATE` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP );";

        String createBudgetItem = "CREATE TABLE IF NOT EXISTS ITEM( `ITEM_ID` INTEGER NOT NULL, `NAME` TEXT NOT NULL, `CATEGORY_ID` INTEGER NOT NULL, `BRAND` TEXT NOT NULL, PRIMARY KEY(`ITEM_ID`), FOREIGN KEY(`CATEGORY_ID`) REFERENCES `CATEGORY`(`CATEGORY_ID`) )";

        String createItemEntry  = "CREATE TABLE IF NOT EXISTS ITEMENTRY ( `ITEMENTRY_ID` INTEGER NOT NULL, `BUDGET_ID` INTEGER NOT NULL, `ITEM_ID` INTEGER NOT NULL, `AMOUNT` REAL NOT NULL, `QUANTITY` INTEGER NOT NULL, `CASHFLOW` TEXT NOT NULL, FOREIGN KEY(`BUDGET_ID`) REFERENCES `BUDGET`(`BUDGET_ID`), FOREIGN KEY(`ITEM_ID`) REFERENCES `ITEM`(`ITEM_ID`), PRIMARY KEY(`ITEMENTRY_ID`) )";

        String createCategory   = "CREATE TABLE IF NOT EXISTS CATEGORY ( `CATEGORY_ID` INTEGER NOT NULL, `CATEGORY_NAME` TEXT NOT NULL, PRIMARY KEY(`CATEGORY_ID`));";

        String createDefaultCurrency = "CREATE TABLE IF NOT EXISTS DEFAULT_CURRENCY ( `ID` INTEGER NOT NULL UNIQUE, `CURRENCY_ID` INTEGER NOT NULL, FOREIGN KEY(`CURRENCY_ID`) REFERENCES `CURRENCY`(`CURRENCY_ID`), PRIMARY KEY(`ID`) )";

        String setAppDefaultCurrency = "INSERT INTO DEFAULT_CURRENCY VALUES (1,1);";

        String createCurrency = "CREATE TABLE IF NOT EXISTS CURRENCY ( `CURRENCY_ID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `NAME` TEXT NOT NULL UNIQUE, `CODE` TEXT NOT NULL UNIQUE, `SYMBOL` TEXT NOT NULL, `POSITION` TEXT NOT NULL )";


        db.execSQL(createBudget);
        db.execSQL(createBudgetItem);
        db.execSQL(createItemEntry);
        db.execSQL(createCategory);
        db.execSQL(createDefaultCurrency);
        db.execSQL(setAppDefaultCurrency);
        db.execSQL(createCurrency);

        setDefaultCurrencies(db);
        setDefaultCategories(db);

        Log.i(TAG,"Several SQL init operations have successfully been executed...check SQliteDatabasManager->onCreate() for more info");

    }

    public void setDefaultCurrencies(SQLiteDatabase db) {
        Log.d(TAG, "setDefaultCurrencies: ");
        String query = "INSERT INTO CURRENCY ('NAME','CODE','SYMBOL','POSITION') VALUES " +
                "('NONE',' ',' ','LEFT'),"+
                "('ANGOLAN KWANZA','AOA','AOA','LEFT'),"+
                "('ARGENTINE PESO','ARS','ARS','LEFT'),"+
                "('ARMENIAN DRAM','AMD','AMD','LEFT'),"+
                "('AUSTRALIAN DOLLAR','AUD','A$','LEFT'),"+
                "('BELARUSIAN RUBLE','BYR','p.','RIGHT'),"+
                "('BOLIVIAN BOLIVIANO','BOB','$b','LEFT'),"+
                "('BRAZILIAN REAL','BRL','R$','LEFT'),"+
                "('BULGARIAN LEV','BGN','лв.','RIGHT'),"+
                "('CANADIAN DOLLAR','CAD','$','RIGHT'),"+
                "('CHILEAN PESO','CLP','$','LEFT'),"+
                "('CROATIAN KUNA','HRK','HRK','RIGHT'),"+
                "('CZECH KORUNA','CZK','Kč','RIGHT'),"+
                "('DANISH KRONE','DKK','kr.','LEFT'),"+
                "('EAST CARIBBEAN DOLLAR','XCD','XCD','LEFT'),"+
                "('EURO','EUR','€','LEFT'),"+
                "('GEORGIAN LARI','GEL','GEL','RIGHT'),"+
                "('HUNGARIAN FORINT','HUF','HUF','RIGHT'),"+
                "('INDIAN RUPEE','INR','INR','LEFT'),"+
                "('INDONESIAN RUPIAH','IDR','Rp','LEFT'),"+
                "('ISRAELI NEW SHEQEL','ILS','₪','RIGHT'),"+
                "('JAPANESE YEN','JPY','¥ ','LEFT'),"+
                "('JORDANIAN DINAR','JOD','JOD','LEFT'),"+
                "('KAZAKHSTANI TENGE','KZT','₸','RIGHT'),"+
                "('KUWAITI DINAR','KWD','KWD','LEFT'),"+
                "('KYRGYZSTANI SOM','KGS','KGS','RIGHT'),"+
                "('MALAYSIAN RINGGIT','MYR','RM','LEFT'),"+
                "('MEXICAN PESO','MXN','$','LEFT'),"+
                "('MOLDOVAN LEU','MDL','L','RIGHT'),"+
                "('MOROCCAN DIRHAM','MAD','MAD','LEFT'),"+
                "('NORWEGIAN KRONE','NOK','kr','LEFT'),"+
                "('PAKISTANI RUPEE','PKR','Rs','LEFT'),"+
                "('PHILIPPINE PESO','PHP','₱ ','LEFT'),"+
                "('POLISH ZLOTY','PLN','zl','RIGHT'),"+
                "('POUND STERLING','GBP','£','LEFT'),"+
                "('ROMANIAN LEU','RON','RON','RIGHT'),"+
                "('RUSSIAN RUBLE','RUB','RUB','LEFT'),"+
                "('SAUDI ARABIAN RIYAL','SAR','SAR','LEFT'),"+
                "('SERBIAN DINAR','RSD','RSD','RIGHT'),"+
                "('SINGAPORE DOLLAR','SGD','HK$','LEFT'),"+
                "('SOUTH AFRICAN RAND','ZAR','R','LEFT'),"+
                "('SOUTH KOREAN WON','KRW','₩','LEFT'),"+
                "('SWEDISH KRONA','SEK','kr','RIGHT'),"+
                "('SWISS FRANC','CHF','CHF','RIGHT'),"+
                "('TAIWAN NEW DOLLAR','TWD','NT$','LEFT'),"+
                "('THAI BAHT','THB','THB','LEFT'),"+
                "('TURKISH LIRA','TRY','TRY','LEFT'),"+
                "('UKRAINIAN HRYVNIA','UAH','₴','RIGHT'),"+
                "('UNITED ARAB EMIRATES DIRHAM','AED','AED','LEFT'),"+
                "('URUGUAYAN PESO','UYU','$U','LEFT'),"+
                "('US DOLLAR','USD','$','LEFT')";

                db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: ");
    }







}

