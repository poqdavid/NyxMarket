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
import io.github.poqdavid.nyx.nyxmarket.NyxMarket;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandPermissionException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HelpCMD implements CommandExecutor {

    public HelpCMD() {
    }

    public static String[] getAlias() {
        return new String[]{"help", "?"};
    }

    public static Text getDescription() {
        return Text.of(" ");
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src.hasPermission(MarketPermission.COMMAND_HELP)) {
            final List<Text> cmdHL = new ArrayList<>();

            final Text.Builder SearchHoverTexts = Text.builder();
            SearchHoverTexts.append(Text.of(TextColors.GRAY, "> p:player, t:title, tc:title contains"));
            SearchHoverTexts.append(Text.of("\n"));
            SearchHoverTexts.append(Text.of(TextColors.GRAY, "> db:<date before yyyy_MM_dd>, da:<date after yyyy_MM_dd>"));
            SearchHoverTexts.append(Text.of("\n"));
            SearchHoverTexts.append(Text.of("\n"));
            SearchHoverTexts.append(Text.of(TextColors.GRAY, "Usage> p:name#tc:stairs"));
            SearchHoverTexts.append(Text.of("\n"));
            SearchHoverTexts.append(Text.of(TextColors.GRAY, "Usage> p:name#tc:oakwoodstairs#db:2017_12_31"));


            final Text.Builder SettingHoverTexts = Text.builder();
            SettingHoverTexts.append(Text.of(TextColors.GRAY, "> itemexpirytime, iet"));
            SettingHoverTexts.append(Text.of("\n"));
            SettingHoverTexts.append(Text.of("\n"));
            SettingHoverTexts.append(Text.of(TextColors.GRAY, "Usage> /market set iet 5760"));
            SettingHoverTexts.append(Text.of("\n"));
            SettingHoverTexts.append(Text.of(TextColors.GRAY, "Note> value for (itemexpirytime, iet) is in minutes"));

            final Text.Builder SearchHHoverTexts = Text.builder();
            SearchHHoverTexts.append(Text.of(TextColors.GRAY, "> p:player, d:date, in:item name, iid:item id"));
            SearchHHoverTexts.append(Text.of("\n"));
            SearchHHoverTexts.append(Text.of(TextColors.GRAY, "> tc:text contain, inc:item name contain, iidc:item id contain"));
            SearchHHoverTexts.append(Text.of("\n"));
            SearchHHoverTexts.append(Text.of(TextColors.GRAY, "> d:<yyyy_MM_dd> or d:<yyyy_MM_dd>to<yyyy_MM_dd>"));
            SearchHHoverTexts.append(Text.of("\n"));
            SearchHHoverTexts.append(Text.of("\n"));
            SearchHHoverTexts.append(Text.of(TextColors.GRAY, "Usage> p:name#inc:stairs"));
            SearchHHoverTexts.append(Text.of("\n"));
            SearchHHoverTexts.append(Text.of(TextColors.GRAY, "Usage> p:name#inc:oakwoodstairs#d:2017_12_31"));

            final Text search = Text.builder().append(Text.of(TextColors.GRAY, "§6[§7<search>§6]§7")).onHover(TextActions.showText(SearchHoverTexts.toText())).build();
            final Text setting = Text.builder().append(Text.of(TextColors.GRAY, "§6<§7setting§6> §6<§7value§6>§7")).onHover(TextActions.showText(SettingHoverTexts.toText())).build();

            final Text searchh = Text.builder().append(Text.of(TextColors.GRAY, "§6[§7<search>§6]§7")).onHover(TextActions.showText(SearchHHoverTexts.toText())).build();

            cmdHL.add(Text.of(TextColors.BLUE, TextStyles.ITALIC, ""));
            cmdHL.add(Text.of(TextColors.GREEN, TextStyles.BOLD, "Commands"));

            cmdHL.add(Text.of("§6- /§7" + MarketCMD.getAlias()[0] + " " + SellCMD.getAlias()[0] + " §6<§7price§6> §6[§7<amount>§6]"));
            cmdHL.add(Text.of("§6- /§7" + MarketCMD.getAlias()[0] + " " + HistoryCMD.getAlias()[0] + " ", searchh));
            cmdHL.add(Text.of("§6- /§7" + MarketCMD.getAlias()[0] + " " + BuyCMD.getAlias()[0] + " ", search));
            cmdHL.add(Text.of("§6- /§7" + MarketCMD.getAlias()[0] + " " + MailCMD.getAlias()[0] + " §6[§7<player>§6]"));
            cmdHL.add(Text.of("§6- /§7" + MarketCMD.getAlias()[0] + " " + PriceCheckCMD.getAlias()[0] + " §6[§7<months>§6]"));
            cmdHL.add(Text.of("§6- /§7" + MarketCMD.getAlias()[0] + " " + PriceLimitCMD.getAlias()[0]));
            cmdHL.add(Text.of("§6- /§7" + MarketCMD.getAlias()[0] + " " + ReloadCMD.getAlias()[0]));
            cmdHL.add(Text.of("§6- /§7" + MarketCMD.getAlias()[0] + " " + SendCMD.getAlias()[0] + " §6<§7player§6> §6[§7<amount>§6]"));
            cmdHL.add(Text.of("§6- /§7" + MarketCMD.getAlias()[0] + " " + SetCMD.getAlias()[0] + " ", setting));
            cmdHL.add(Text.of("§6- /§7" + MarketCMD.getAlias()[0] + " " + BroadcastIgnoreCMD.getAlias()[0]));

            PaginationList.Builder builder = PaginationList.builder();
            URL url1 = null;
            try {
                url1 = new URL("https://github.com/poqdavid/NyxMarket/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            Text h1 = Text.builder("Author: ").color(TextColors.BLUE).style(TextStyles.BOLD).build();
            Text h2 = Text.builder("POQDavid").color(TextColors.GRAY).style(TextStyles.BOLD).onHover(TextActions.showText(Text.of(url1.toString()))).onClick(TextActions.openUrl(url1)).build();

            builder.title(Text.of("§9Nyx§5Market §7- §6V" + NyxMarket.getInstance().getVersion()))
                    .header(Text.of(h1, h2))
                    .contents(cmdHL)
                    .padding(Text.of("="))
                    .sendTo(src);
        } else {
            throw new CommandPermissionException(Text.of("You don't have permission to use this command."));
        }

        return CommandResult.success();
    }
}


