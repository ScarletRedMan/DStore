package ru.dragonestia.store;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import org.sql2o.Connection;
import ru.dragonestia.store.database.DataBase;
import ru.dragonestia.store.database.objects.PlayerShardsObject;
import ru.dragonestia.store.listener.MainListener;

public class DStore extends PluginBase {

    private static DStore instance;

    private DataBase dataBase;

    @Override
    public void onLoad() {
        instance = this;

        Config db = new Config("plugins/DStore/database.properties", Config.PROPERTIES);

        if(!db.exists("host")){
            db.set("host", "localhost");
        }
        if(!db.exists("user")){
            db.set("user", "root");
        }
        if(!db.exists("password")){
            db.set("password", "12345");
        }
        if(!db.exists("base")){
            db.set("base", "server");
        }
        if(!db.exists("port")){
            db.set("port", 3306);
        }
        db.save();

        dataBase = new DataBase(db.getString("host"), db.getString("user"), db.getString("password"), db.getString("base"), Integer.parseInt(db.getString("port")));
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new MainListener(this), this);
    }

    @Override
    public void onDisable() {

    }

    public DataBase getDataBase(){
        return dataBase;
    }

    public int getShards(Player player){
        try(Connection connection = dataBase.driver.open()){
            return connection.createQuery("SELECT * FROM `shards` WHERE `player` = :player LIMIT 1;")
                    .addParameter("player", player.getName().toLowerCase())
                    .executeAndFetch(PlayerShardsObject.class)
                    .get(0).shards;
        }catch (NullPointerException exception){
            return 0;
        }
    }

    public static DStore getInstance(){
        return instance;
    }

}
