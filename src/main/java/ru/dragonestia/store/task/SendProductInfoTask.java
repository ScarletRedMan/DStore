package ru.dragonestia.store.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.dragonestia.store.DStore;
import ru.dragonestia.store.Product;
import ru.dragonestia.store.database.objects.PlayerShardsObject;
import ru.dragonestia.store.event.PlayerProductPreBuyEvent;
import ru.nukkitx.forms.elements.ModalForm;
import ru.nukkitx.forms.elements.SimpleForm;

public class SendProductInfoTask extends AsyncTask {

    private final Player player;

    private final Product product;

    public SendProductInfoTask(Player player, Product product) {
        this.player = player;
        this.product = product;
    }

    public void onRun() {
        Sql2o db = DStore.getInstance().getDataBase().driver;

        try(Connection connection = db.open()){
            PlayerShardsObject data = connection.createQuery("SELECT * FROM `shards` WHERE `player` = :player LIMIT 1;")
                    .addParameter("player", player.getName().toLowerCase())
                    .executeAndFetch(PlayerShardsObject.class).get(0);

            sendForm(player, product, data.shards);
        }catch (NullPointerException ex){
            player.sendMessage("§cПроизошла неизвестная ошибка при покупке товара.");
            ex.printStackTrace();
        }
    }

    private void sendForm(Player player, Product product, int balance){
        new SimpleForm("Покупка товара",
                "Название: §b" + product.name + "§f\n" +
                        "Ваш баланс: §b" + balance + " Шардов§f\n" +
                        "Цена: §b" + product.price + " Шардов" + (product.sale > 0? ("§e(Скидка " + (int) (product.sale * 100)) + " процентов)" : "") + "§f\n" +
                        "Описание: §3" + product.description + "§f\n")
                .addButton("Купить" + (product.price > balance? "\n§4(Недостаточно средств)" : ""))
                .addButton("Отмена")
                .send(player, (target, form, data) -> {
                    if(data != 0) return;

                    if(product.price > balance){
                        player.sendMessage("§cНедостаточно средств для покупки данного товара.");
                        return;
                    }

                    PlayerProductPreBuyEvent event = new PlayerProductPreBuyEvent(player, product);
                    Server.getInstance().getPluginManager().callEvent(event);
                    if(event.isCancelled()) return;

                    DStore main = DStore.getInstance();
                    main.getServer().getScheduler().scheduleAsyncTask(main, new BuyConfirmationTask(player, product, main, balance));
                });
    }

}
