package ru.dragonestia.store.task;

import cn.nukkit.Player;
import cn.nukkit.scheduler.AsyncTask;
import org.sql2o.Connection;
import ru.dragonestia.store.DStore;

public class PlayerAccountCheckerTask extends AsyncTask {

    private final Player player;

    private final DStore main;

    public PlayerAccountCheckerTask(DStore main, Player player){
        this.player = player;
        this.main = main;
    }

    public void onRun() {
        try(Connection connection = main.getDataBase().driver.open()){
            connection.createQuery("INSERT IGNORE INTO `shards` SET `player` = :player ;")
                    .addParameter("player", player.getName().toLowerCase())
                    .executeUpdate();
        }
    }

}
