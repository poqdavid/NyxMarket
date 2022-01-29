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

import io.github.poqdavid.nyx.nyxmarket.NyxMarket;
import io.github.poqdavid.nyx.nyxmarket.Utils.Invs;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.spec.CommandSpec;

public class CommandManager {
    public static CommandSpec helpCmd;
    public static CommandSpec createCmd;
    public static CommandSpec historyCmd;
    public static CommandSpec listingsCmd;
    public static CommandSpec mailCmd;
    public static CommandSpec pcCmd;
    public static CommandSpec plCmd;
    public static CommandSpec reloadCmd;
    public static CommandSpec sendCmd;
    public static CommandSpec setCmd;
    public static CommandSpec broadcastignoreCmd;
    private final NyxMarket nm;
    private final Game game;
    private final Invs inv;

    public CommandManager(Game game, NyxMarket nm) {
        this.game = game;
        this.nm = nm;
        this.inv = new Invs(game);
        registerCommands();
    }

    public void registerCommands() {

        createCmd = CommandSpec.builder()
                .description(SellCMD.getDescription())
                .executor(new SellCMD(this.game, this.inv))
                .arguments(SellCMD.getArgs())
                .build();
        historyCmd = CommandSpec.builder()
                .description(HistoryCMD.getDescription())
                .executor(new HistoryCMD(this.game, this.inv))
                .arguments(HistoryCMD.getArgs())
                .build();
        listingsCmd = CommandSpec.builder()
                .description(BuyCMD.getDescription())
                .executor(new BuyCMD(this.game, this.inv))
                .arguments(BuyCMD.getArgs())
                .build();
        mailCmd = CommandSpec.builder()
                .description(MailCMD.getDescription())
                .executor(new MailCMD(this.game, this.inv))
                .arguments(MailCMD.getArgs())
                .build();
        pcCmd = CommandSpec.builder()
                .description(PriceCheckCMD.getDescription())
                .executor(new PriceCheckCMD(this.game, this.inv))
                .arguments(PriceCheckCMD.getArgs())
                .build();
        plCmd = CommandSpec.builder()
                .description(PriceLimitCMD.getDescription())
                .executor(new PriceLimitCMD(this.game, this.inv))
                .arguments(PriceLimitCMD.getArgs())
                .build();
        reloadCmd = CommandSpec.builder()
                .description(ReloadCMD.getDescription())
                .executor(new ReloadCMD(this.game, this.inv))
                .build();
        sendCmd = CommandSpec.builder()
                .description(SendCMD.getDescription())
                .executor(new SendCMD(this.game, this.inv))
                .arguments(SendCMD.getArgs())
                .build();
        setCmd = CommandSpec.builder()
                .description(SetCMD.getDescription())
                .executor(new SetCMD(this.game, this.inv))
                .arguments(SetCMD.getArgs())
                .build();

        broadcastignoreCmd = CommandSpec.builder()
                .description(BroadcastIgnoreCMD.getDescription())
                .executor(new BroadcastIgnoreCMD(this.game))
                .build();

        helpCmd = CommandSpec.builder()
                .description(HelpCMD.getDescription())
                .executor(new HelpCMD())
                .build();

        CommandSpec smCommand = CommandSpec.builder()
                .description(MarketCMD.getDescription())
                .executor(new MarketCMD())
                .child(helpCmd, HelpCMD.getAlias())
                .child(createCmd, SellCMD.getAlias())
                .child(historyCmd, HistoryCMD.getAlias())
                .child(listingsCmd, BuyCMD.getAlias())
                .child(mailCmd, MailCMD.getAlias())
                .child(pcCmd, PriceCheckCMD.getAlias())
                .child(plCmd, PriceLimitCMD.getAlias())
                .child(reloadCmd, ReloadCMD.getAlias())
                .child(sendCmd, SendCMD.getAlias())
                .child(setCmd, SetCMD.getAlias())
                .child(broadcastignoreCmd, BroadcastIgnoreCMD.getAlias())
                .build();

        game.getCommandManager().register(nm, smCommand, MarketCMD.getAlias());
    }
}
