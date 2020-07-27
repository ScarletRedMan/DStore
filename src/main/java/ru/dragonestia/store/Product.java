package ru.dragonestia.store;

import cn.nukkit.Player;
import com.google.gson.Gson;
import org.sql2o.Connection;
import ru.dragonestia.store.database.element.ProductData;
import ru.dragonestia.store.database.objects.ProductObject;
import ru.dragonestia.store.task.SendProductInfoTask;

public class Product {

    public final int id;

    public int price;

    public final String name, description, codeName;

    public final float sale;

    public Product(int id, String name, String description, int price, float sale, String codeName){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price - (int) (price * sale);
        this.sale = sale;
        this.codeName = codeName;
    }

    public Product(ProductObject object){
        id = object.id;
        sale = object.sale / 0.01f;
        price = price - (int) (price * sale);

        ProductData data = new Gson().fromJson(object.data, ProductData.class);
        name = data.name;
        description = data.description;
        codeName = data.codeName;
    }

    public void send(Player player){
        DStore main = DStore.getInstance();
        main.getServer().getScheduler().scheduleAsyncTask(main, new SendProductInfoTask(player, this));
    }

    public static Product get(int productId){
        try (Connection connection = DStore.getInstance().getDataBase().driver.open()){
            ProductObject object = connection.createQuery("SELECT * FROM `products` WHERE `id` = " + productId + " LIMIT 1;")
                    .executeAndFetch(ProductObject.class).get(0);

            return new Product(object);
        }catch (NullPointerException ex){
            return null;
        }
    }

}
