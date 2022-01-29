/*
 *     This file is part of NyxMarket.
 *
 *     NyxMarket is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     NyxMarket is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with NyxMarket.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     Copyright (c) POQDavid <https://github.com/poqdavid>
 *     Copyright (c) contributors
 */

package io.github.poqdavid.nyx.nyxmarket.Commands;

import io.github.poqdavid.nyx.nyxcore.Utils.CoreTools;
import io.github.poqdavid.nyx.nyxmarket.NyxMarket;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class BroadcastIgnoreCMD implements CommandExecutor {
    private final Game game;

    public BroadcastIgnoreCMD(Game game) {
        this.game = game;
    }

    public static String[] getAlias() {
        return new String[]{"ignore", "ig"};
    }

    public static Text getDescription() {
        return Text.of("Toggle's market broadcast");
    }


    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (src instanceof Player) {
            final Player player_cmd_src = CoreTools.getPlayer(src);

            if (NyxMarket.getInstance().BroadcastIgnoreList.contains(player_cmd_src.getUniqueId())) {
                NyxMarket.getInstance().BroadcastIgnoreList.remove(player_cmd_src.getUniqueId());
                player_cmd_src.sendMessage(Text.of(TextColors.GOLD, "Enabled market broadcast!"));
            } else {
                NyxMarket.getInstance().BroadcastIgnoreList.add(player_cmd_src.getUniqueId());
                player_cmd_src.sendMessage(Text.of(TextColors.GOLD, "Disabled market broadcast!"));
            }
        }

        return CommandResult.success();
    }
}