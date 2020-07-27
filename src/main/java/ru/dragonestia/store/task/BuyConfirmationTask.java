package ru.dragonestia.store.task;

import cn.nukkit.Player;
import cn.nukkit.scheduler.AsyncTask;
import org.sql2o.Connection;
import ru.dragonestia.store.DStore;
import ru.dragonestia.store.Product;
import ru.dragonestia.store.event.PlayerProductBoughtEvent;

public class BuyConfirmationTask extends AsyncTask {

    private final Player player;

    private final Product product;

    private final DStore main;

    private final int balance;

    public BuyConfirmationTask(Player player, Product product, DStore main, int balance){
        this.player = player;
        this.product = product;
        this.main = main;
        this.balance = balance;
    }

    @Override
    public void onRun() {
        try(Connection connection = main.getDataBase().driver.open()){
            connection.createQuery("UPDATE `shards` SET `shards` = `shards` - " + product.price + " WHERE `player` = :player AND `shards` >= " + product.price + " LIMIT 1;")
                    .addParameter("player", player.getName().toLowerCase())
                    .executeUpdate();
            if(connection.getResult() == 0){
                player.sendMessage("§cНедостаточно средств для покупки данного товара.");
            }else{
                player.sendMessage("§bВы успешно приобрели товар §l" + product.name + "§r§b!");

                PlayerProductBoughtEvent event = new PlayerProductBoughtEvent(player, product);
                main.getServer().getPluginManager().callEvent(event);

                connection.createQuery("INSERT INTO `transactions` (`player`, `product_id`, `time`) VALUES (:player, " + product.id + ", UNIX_TIMESTAMP(NOW()));")
                        .addParameter("player", player.getName().toLowerCase())
                        .executeUpdate();
            }
        }
    }

}
