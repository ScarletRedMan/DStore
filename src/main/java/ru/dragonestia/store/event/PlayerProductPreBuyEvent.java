package ru.dragonestia.store.event;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import ru.dragonestia.store.Product;

public class PlayerProductPreBuyEvent extends PlayerEvent implements Cancellable {

    private final Product product;

    private static final HandlerList handlers = new HandlerList();

    public PlayerProductPreBuyEvent(Player player, Product product){
        this.player = player;
        this.product = product;
    }

    public Product getProduct(){
        return product;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

}
