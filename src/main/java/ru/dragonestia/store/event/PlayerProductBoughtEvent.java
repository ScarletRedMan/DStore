package ru.dragonestia.store.event;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import ru.dragonestia.store.Product;

public class PlayerProductBoughtEvent extends PlayerEvent {

    private final Product product;

    private static final HandlerList handlers = new HandlerList();

    public PlayerProductBoughtEvent(Player player, Product product){
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
