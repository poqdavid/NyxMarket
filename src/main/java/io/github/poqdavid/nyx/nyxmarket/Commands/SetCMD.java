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
import org.spongepowered.api.text.Text;

public class SetCMD implements CommandExecutor {
    private final Game game;
    private final Invs inv;

    public SetCMD(Game game, Invs inv) {
        this.game = game;
        this.inv = inv;
    }

    public static Text getDescription() {
        return Text.of("For changing settings");
    }

    public static String[] getAlias() {
        return new String[]{"set"};
    }

    public static CommandElement[] getArgs() {
        return new CommandElement[]{GenericArguments.string(Text.of("setting")), GenericArguments.string(Text.of("value"))};
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src.hasPermission(MarketPermission.COMMAND_SETTING)) {
            final String setting = args.<String>getOne("setting").orElse("");
            final String value = args.<String>getOne("value").orElse("");

            switch (setting.toLowerCase()) {
                case "itemexpirytime":
                case "iet": {
                    try {
                        NyxMarket.getInstance().getSettings().setItemExpiryTime(Integer.parseInt(value));
                        NyxMarket.getInstance().getSettings().Save(NyxMarket.getInstance().configfullpath);
                    } catch (Exception ex) {
                        throw new CommandException(Text.of("value must be an Integer!"));
                    }

                    break;
                }

                default: {
                    break;
                }
            }

        } else {
            throw new CommandPermissionException(Text.of("You don't have permission to use this command."));
        }
        return CommandResult.success();
    }
}
