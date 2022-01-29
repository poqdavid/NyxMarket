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
import io.github.poqdavid.nyx.nyxmarket.Market.MarketHistoryOBJ;
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
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class PriceCheckCMD implements CommandExecutor {
    private final Game game;
    private final Invs inv;

    public PriceCheckCMD(Game game, Invs inv) {
        this.game = game;
        this.inv = inv;
    }

    public static Text getDescription() {
        return Text.of("Checks the price of the item in your hand");
    }

    public static String[] getAlias() {
        return new String[]{"price", "pc"};
    }

    public static CommandElement[] getArgs() {
        return new CommandElement[]{GenericArguments.optional(GenericArguments.integer(Text.of("months")))};
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            final Player player_cmd_src = CoreTools.getPlayer(src);

            if (src.hasPermission(MarketPermission.COMMAND_PRICECHECK)) {
                final Integer months = args.<Integer>getOne("months").orElse(3);
                final DateFormat df = new SimpleDateFormat("yyyy_MM_dd");
                Date dateend = NyxMarket.getInstance().LastHistorySave;
                final ItemStack isx = player_cmd_src.getItemInHand(HandTypes.MAIN_HAND).orElse(null);
                if (isx == null || isx.getQuantity() == 0) {
                    throw new CommandException(Text.of("You don't have anything in your hand!"));
                }
                final ItemStack is1 = isx.copy();
                // List<MarketHistoryOBJ> mho = new ArrayList<>();

                Calendar cal = Calendar.getInstance();
                cal.setTime(dateend);
                cal.add(Calendar.DATE, (-30 * months));
                Date datestart = cal.getTime();

                final List<Date> dates = Tools.GetDatesBetween(datestart, dateend);
                List<Integer> prices = new ArrayList<>();

                for (Date date : dates) {

                    final Path ph = Paths.get(NyxMarket.getInstance().historyDir.toString(), df.format(date) + ".json");
                    final File file = ph.toFile();

                    if (file.exists()) {
                        List<MarketHistoryOBJ> mhos = Tools.loadmarkethistory(ph);

                        Integer num = 0;
                        Integer totalprice = 0;
                        for (MarketHistoryOBJ mho : mhos) {

                            if (mho.getData().toLowerCase().contains("player bought")) {

                                if (mho.getExtraData().split(",")[0].equalsIgnoreCase(is1.getType().getName())) {


                                    if (mho.getExtraData().split(",")[1].equalsIgnoreCase(CoreTools.getItemName(is1).toPlain())) {


                                        final Integer amount = Integer.parseInt(mho.getData().split(" ")[2]);
                                        final String st = mho.getData().split("price of ")[1].replace(" from the market.", "");

                                        totalprice = totalprice + Integer.valueOf(Math.round((Integer.parseInt(st) / amount)));


                                        num++;
                                    }
                                }
                            }
                        }

                        if (num != 0) {
                            prices.add(Integer.valueOf(Math.round((totalprice / num))));
                        }


                    } else {
                        // throw new CommandException(Text.of("History for " + df.format(datestart) + " isn't available!"));
                    }

                }

                Integer num = 0;
                Integer totalprice = 0;
                for (Integer pr : prices) {
                    totalprice = totalprice + pr;
                    num++;
                }

                Integer avrgprice = 0;

                if (totalprice != 0) {
                    if (num != 0) {
                        avrgprice = Integer.valueOf(Math.round((totalprice / num)));
                    }
                }

                List<Text> cmdHL = new ArrayList<>();

                cmdHL.add(Text.of(TextColors.RED, "Name: ", CoreTools.getItemName(is1)));
                cmdHL.add(Text.of(TextColors.RED, "ID: " + is1.getType().getName().toLowerCase()));
                cmdHL.add(Text.of(TextColors.GRAY, "Average price of one: " + avrgprice));
                if (avrgprice != 0) {
                    cmdHL.add(Text.of(TextColors.GRAY, "Average price of (" + is1.getQuantity() + "): " + (avrgprice * is1.getQuantity())));
                }
                final NMItemData itd = Tools.GetItemDATA(is1);

                // is1.setQuantity(1);

                final NMPriceData pdtemp = new NMPriceData(BigDecimal.valueOf(0), BigDecimal.valueOf(0));
                final NMPriceData pd = NyxMarket.getInstance().MarketPriceData.getOrDefault(Tools.ItemDataToBase64(itd), pdtemp);

                if (pd.getPriceMAX() == BigDecimal.valueOf(0)) {
                    cmdHL.add(Text.of(TextColors.GRAY, "MinPrice: " + pd.getPriceMIN()));
                } else {
                    cmdHL.add(Text.of(TextColors.GRAY, "MinPrice: " + pd.getPriceMIN() + ", MaxPrice: " + pd.getPriceMAX()));
                }


                PaginationService paginationService = NyxMarket.getInstance().getGame().getServiceManager().provide(PaginationService.class).get();
                PaginationList.Builder builder = paginationService.builder();

                builder.title(Text.of(TextColors.DARK_AQUA, "Price Check"))
                        .contents(cmdHL)
                        .padding(Text.of("-"))
                        .sendTo(src);

            } else {
                throw new CommandPermissionException(Text.of("You don't have permission to use this command."));
            }
        } else {
            throw new CommandException(Text.of("You can't use this command if you are not a player!"));
        }
        return CommandResult.success();
    }
}
