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

package io.github.poqdavid.nyx.nyxmarket.Utils;

import io.github.poqdavid.nyx.nyxcore.Utils.CoreTools;
import io.github.poqdavid.nyx.nyxmarket.NyxMarket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.text.Text;

public class Invs {
    private final Game game;

    public Invs(Game game) {
        this.game = game;
    }

    public CommandResult Open(CommandSource src, CommandContext args, InventoryArchetype invArch, Text inventorytitle) {
        final Inventory invx = Inventory.builder().of(invArch)
                .property(InventoryTitle.PROPERTY_NAME, new InventoryTitle(inventorytitle))
                .build(NyxMarket.getInstance());
        return this.Open(src, args, invx);

    }

    public CommandResult Open(CommandSource src, CommandContext args, String invArch) {
        if (invArch == "enderchest") {
            final Player player = CoreTools.getPlayer(src);
            return this.Open(src, args, player.getEnderChestInventory());
        }
        return CommandResult.empty();
    }

    public CommandResult Open(CommandSource src, CommandContext args, InventoryArchetype invArch) {
        final Inventory invx = org.spongepowered.api.item.inventory.Inventory.builder().of(invArch)
                .build(NyxMarket.getInstance());
        return this.Open(src, args, invx);
    }

    public CommandResult Open(CommandSource src, CommandContext args, org.spongepowered.api.item.inventory.Inventory i) {
        final Player player = CoreTools.getPlayer(src);
        if (src.getCommandSource().isPresent() && src.getCommandSource().get() instanceof Player) {
            player.openInventory(i);
            return CommandResult.success();
        }
        return CommandResult.empty();
    }

    public CommandResult Open(CommandSource src, Container opencon, String inventoryTypeIn, String title) {
        final Player player = CoreTools.getPlayer(src);
        final EntityPlayerMP MPlayer = (EntityPlayerMP) player;
        MPlayer.getNextWindowId();
        MPlayer.connection.sendPacket(new SPacketOpenWindow(MPlayer.currentWindowId, inventoryTypeIn, new TextComponentString(title)));
        MPlayer.openContainer = opencon;
        MPlayer.openContainer.windowId = MPlayer.currentWindowId;
        MPlayer.openContainer.addListener(MPlayer);
        return CommandResult.success();
    }
}
