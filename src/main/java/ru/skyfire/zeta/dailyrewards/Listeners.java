package ru.skyfire.zeta.dailyrewards;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import ru.skyfire.zeta.dailyrewards.database.SqliteEntry;

import java.util.concurrent.TimeUnit;

import static ru.skyfire.zeta.dailyrewards.DailyRewards.logger;

public class Listeners {
    @Listener
    public void onPlayerConnect(ClientConnectionEvent.Join event, @First Player player){

        SqliteEntry sqlite = DailyRewards.getInst().getSqlite();
        if(!player.hasPermission("dailyrewards.base")){
            return;
        }
        if (sqlite.getStatus(player.getUniqueId())==-1){
            sqlite.addEntry(player.getUniqueId(), 1, 0);
        }
        if (sqlite.getStatus(player.getUniqueId())==1){
            return;
        }
        if (DailyRewards.getInst().getRootDefNode().getNode("show-rewards-on-join").getBoolean(true)){
            if (sqlite.getStatus(player.getUniqueId())==0 && player.hasPermission("dailyrewards.base")){
                Util.showRewards(player);
            }
        }

        if(DailyRewards.getInst().getRootDefNode().getNode("messages-amount").getInt(0)>0){
            DailyRewards.getInst().getNotificationManager().addPlayer(player);
            if (DailyRewards.getInst().debug){
                logger.info("Notification for player "+player.getName()+" started");
            }
        }
    }
    @Listener
    public void onPlayerExit(ClientConnectionEvent.Disconnect event, @First Player player){
        DailyRewards.getInst().getNotificationManager().removePlayer(player);
    }
}
