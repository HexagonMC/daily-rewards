package ru.skyfire.zeta.dailyrewards.commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import ru.skyfire.zeta.dailyrewards.DailyRewards;
import ru.skyfire.zeta.dailyrewards.Util;
import ru.skyfire.zeta.dailyrewards.database.SqliteEntry;

public class CmdDailySetStatus implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) {
        Player targetPlayer = args.<Player>getOne("player").orElse(null);
        Boolean targetStatus = args.<Boolean>getOne("status").orElse(null);

        if (targetStatus==null || targetPlayer==null){
            sender.sendMessage(Text.of(Util.trans("command-set-error")));
            return CommandResult.success();
        }

        SqliteEntry sqlite = DailyRewards.getInst().getSqlite();

        if(sqlite.getStatus(targetPlayer.getUniqueId())==-1){
            sender.sendMessage(Text.of(Util.trans("command-set-noplayer")));
            return CommandResult.success();
        }
        int status=0;
        if(targetStatus){
            status=1;
        }

        sqlite.updateEntry(targetPlayer.getUniqueId(), sqlite.getCurrentDay(targetPlayer.getUniqueId()), status);
        sender.sendMessage(Text.of(Util.trans("command-set-status-success")+status));
        return CommandResult.success();
    }
}
