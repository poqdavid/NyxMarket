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

import io.github.poqdavid.nyx.nyxcore.Permissions.MarketPermission;
import io.github.poqdavid.nyx.nyxcore.Utils.CoreTools;
import io.github.poqdavid.nyx.nyxmarket.Market.Data.NMItemData;
import io.github.poqdavid.nyx.nyxmarket.Market.Data.NMPriceData;
import io.github.poqdavid.nyx.nyxmarket.NyxMarket;
import io.github.poqdavid.nyx.nyxmarket.Utils.Invs;
import io.github.poqdavid.nyx.nyxmarket.Utils.Tools;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandPermissionException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;

public class PriceLimitCMD implements CommandExecutor {
    private final Game game;
    private final Invs inv;

    public PriceLimitCMD(Game game, Invs inv) {
        this.game = game;
        this.inv = inv;
    }

    public static Text getDescription() {
        return Text.of("For managing price limit for items sold on market");
    }

    public static String[] getAlias() {
        return new String[]{"pricelimit", "pl"};
    }

    public static CommandElement[] getArgs() {
        return new CommandElement[]{GenericArguments.integer(Text.of("pricemin")), GenericArguments.integer(Text.of("pricemax")), GenericArguments.flags().flag("r").buildWith(GenericArguments.none())};
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            final Player player_cmd_src = CoreTools.getPlayer(src);
            if (src.hasPermission(MarketPermission.COMMAND_PRICELIMIT)) {

                final NMPriceData pd = new NMPriceData();
                final ItemStack is1 = player_cmd_src.getItemInHand(HandTypes.MAIN_HAND).orElse(null);
                if (is1 == null || is1.getQuantity() == 0) {
                    throw new CommandException(Text.of("You don't have anything in your hand!"));
                }
                final NMItemData itd = Tools.GetItemDATA(is1);


                is1.setQuantity(1);

                final Integer pricemin = args.<Integer>getOne("pricemin").orElse(0);
                final Integer pricemax = args.<Integer>getOne("pricemax").orElse(0);

                if (pricemax < pricemin) {
                    throw new CommandException(Text.of("Max price can't be less than min price!"));
                }

                pd.setPriceMIN(BigDecimal.valueOf(pricemin));
                pd.setPriceMAX(BigDecimal.valueOf(pricemax));

                if (args.hasAny("r")) {
                    NyxMarket.getInstance().MarketPriceRemove(pd.getMD5());
                } else {

                    NyxMarket.getInstance().MarketPriceSet(Tools.ItemDataToBase64(itd), pd);
                    player_cmd_src.sendMessage(Text.of("Price limit is set, PriceMin: " + pd.getPriceMIN().intValue() + ", PriceMax: " + pd.getPriceMAX().intValue()));
                }

            } else {
                throw new CommandPermissionException(Text.of("You don't have permission to use this command."));
            }
        } else {
            throw new CommandException(Text.of("You can't use this command if you are not a player!"));
        }
        return CommandResult.success();
    }
}