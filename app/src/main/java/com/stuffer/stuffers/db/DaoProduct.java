package com.stuffer.stuffers.db;




import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface DaoProduct {


    @Query("SELECT * FROM product")
    List<Product> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Delete
    void delete(Product product);


    @Update
    void update(Product product);


    @Query("DELETE FROM Product")
    void delete();

    @Query("SELECT COUNT(id) FROM product")
    int getNumberOfRows();

    //GET STOCKs

    @Query("SELECT stock FROM product WHERE id=:id")
    int getStockForProductInCart(int id);

    //DELETE PRODUCT

    @Query("DELETE FROM product WHERE id=:id")
    int deleteProduct(int id);


    //Update stock
    @Query("UPDATE product SET stock=:stock WHERE id = :id")
    void updateProduct(int stock, int id);


    //Get Product Price

    @Query("SELECT price FROM product WHERE id=:id")
    int getPriceForProductInCart(int id);



    //Update price
    @Query("UPDATE product SET price=:price WHERE id = :id")
    void updateProductPrice(double price, int id);


}

