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
import io.github.poqdavid.nyx.nyxmarket.Market.MarketHistoryOBJ;
import io.github.poqdavid.nyx.nyxmarket.Market.MarketMailItemOBJ;
import io.github.poqdavid.nyx.nyxmarket.NyxMarket;
import io.github.poqdavid.nyx.nyxmarket.Utils.Invs;
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
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.Date;

public class SendCMD implements CommandExecutor {
    private final Game game;
    private final Invs inv;

    public SendCMD(Game game, Invs inv) {
        this.game = game;
        this.inv = inv;
    }

    public static Text getDescription() {
        return Text.of("Sends an item to another player");
    }

    public static String[] getAlias() {
        return new String[]{"send"};
    }

    public static CommandElement[] getArgs() {
        return new CommandElement[]{GenericArguments.player(Text.of("player")), GenericArguments.optional(GenericArguments.integer(Text.of("amount")))};
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            final Player player_cmd_src = CoreTools.getPlayer(src);
            final Player player_to = args.<Player>getOne("player").orElse(player_cmd_src);

            if (src.hasPermission(MarketPermission.COMMAND_SEND)) {
                final MarketMailItemOBJ mmio = new MarketMailItemOBJ();

                final ItemStack is1 = player_cmd_src.getItemInHand(HandTypes.MAIN_HAND).orElse(null).copy();

                if (is1 == null || is1.getQuantity() == 0) {
                    throw new CommandException(Text.of("You don't have anything in your hand!"));
                }

                final ItemStack is2 = player_cmd_src.getItemInHand(HandTypes.MAIN_HAND).orElse(null).copy();
                final ItemStack is3 = player_cmd_src.getItemInHand(HandTypes.MAIN_HAND).orElse(null).copy();
                is3.setQuantity(1);

                final Integer amount = args.<Integer>getOne("amount").orElse(is1.getQuantity());
                final Date date = new Date();


                if (amount == 0) {
                    throw new CommandException(Text.of("You can't do that!"));
                }

                if (amount < 0) {
                    throw new CommandException(Text.of("You can't use negative number for amount!"));
                }

                if (amount > is1.getQuantity()) {
                    throw new CommandException(Text.of("You can't add more than you got in your hand!"));
                } else {
                    mmio.setPlayer(player_to.getUniqueId());
                    mmio.setCreationTime(date);
                    mmio.setWorld(player_to.getWorld().getUniqueId());
                    is1.setQuantity(amount);
                    mmio.setAmount(amount);
                    mmio.setItemData(CoreTools.ItemStackToBase64(is1));

                    if (amount == is2.getQuantity()) {
                        player_cmd_src.setItemInHand(HandTypes.MAIN_HAND, null);
                    } else {
                        final int newamount = is2.getQuantity() - amount;
                        is2.setQuantity(newamount);
                        player_cmd_src.setItemInHand(HandTypes.MAIN_HAND, is2);
                    }

                    NyxMarket.getInstance().MarketMailAdd(mmio);

                    final Text.Builder HoverTexts = Text.builder();

                    HoverTexts.append(Text.of(TextColors.GREEN, "From: ", TextColors.WHITE, player_cmd_src.getName()));
                    HoverTexts.append(Text.of("\n"));
                    HoverTexts.append(Text.of(TextColors.GREEN, "To: ", TextColors.WHITE, player_to.getName()));
                    HoverTexts.append(Text.of("\n"));
                    HoverTexts.append(Text.of(TextColors.GREEN, "Amount: ", TextColors.WHITE, mmio.getAmount()));
                    final Text transactionText = Text.builder().append(Text.of(TextColors.GREEN, "You have sent the item, hover for more info.")).onHover(TextActions.showText(HoverTexts.toText())).build();

                    player_cmd_src.sendMessage(transactionText);
                    NyxMarket.getInstance().MarketHistoryAdd(new MarketHistoryOBJ(date, player_cmd_src.getName(), player_cmd_src.getUniqueId(), "Player sent " + amount + " of " + "<NAME>" + " to " + player_to.getName() + ".", is1.getType().getName() + "," + CoreTools.getItemName(is1).toPlain()));
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
