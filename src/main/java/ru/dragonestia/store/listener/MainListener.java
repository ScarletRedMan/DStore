package ru.dragonestia.store.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerLoginEvent;
import ru.dragonestia.store.DStore;
import ru.dragonestia.store.task.PlayerAccountCheckerTask;

public class MainListener implements Listener {

    private final DStore main;

    public MainListener(DStore main){
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerLoginEvent event){
        Player player = event.getPlayer();

        main.getServer().getScheduler().scheduleAsyncTask(main, new PlayerAccountCheckerTask(main, player));
    }

}
