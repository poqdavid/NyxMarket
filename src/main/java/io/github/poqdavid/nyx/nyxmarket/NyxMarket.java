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

package io.github.poqdavid.nyx.nyxmarket;

import com.google.inject.Inject;
import io.github.nucleuspowered.nucleus.api.module.nickname.NucleusNicknameService;
import io.github.poqdavid.nyx.nyxcore.NyxCore;
import io.github.poqdavid.nyx.nyxcore.Utils.CText;
import io.github.poqdavid.nyx.nyxcore.Utils.NCLogger;
import io.github.poqdavid.nyx.nyxcore.Utils.Setting.NyxMarket.NMSettings;
import io.github.poqdavid.nyx.nyxmarket.Commands.CommandManager;
import io.github.poqdavid.nyx.nyxmarket.Market.Data.NMData;
import io.github.poqdavid.nyx.nyxmarket.Market.Data.NMDataBuilder;
import io.github.poqdavid.nyx.nyxmarket.Market.Data.NMImmutableData;
import io.github.poqdavid.nyx.nyxmarket.Market.Data.NMPriceData;
import io.github.poqdavid.nyx.nyxmarket.Market.MarketHistoryOBJ;
import io.github.poqdavid.nyx.nyxmarket.Market.MarketItemOBJ;
import io.github.poqdavid.nyx.nyxmarket.Market.MarketMailItemOBJ;
import io.github.poqdavid.nyx.nyxmarket.Tasks.itemExpirationTask;
import io.github.poqdavid.nyx.nyxmarket.Utils.Tools;
import org.apache.commons.lang3.time.DateUtils;
import org.bstats.sponge.Metrics;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.service.ChangeServiceProviderEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.permission.PermissionDescription;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Plugin(id = "nyxmarket", name = "@name@", version = "@version@", description = "@description@", url = "https://github.com/poqdavid/NyxMarket", authors = {"@authors@"}, dependencies = {@Dependency(id = "nyxcore", version = "1.5", optional = false)})
public class NyxMarket {
    public static final Text PREFIX = Text.of(TextColors.GOLD, "Market ", TextColors.GRAY, "\u00bb ", TextColors.DARK_AQUA);
    private static NyxMarket nyxmarket;
    private final Metrics metrics;
    private final NCLogger logger;
    private final Path mailboxDir;
    private final Path marketsDir;
    private final Path marketPricesfullpath;
    private final PluginContainer pluginContainer;
    public Map<String, MarketItemOBJ> MarketListings;
    public Map<String, NMPriceData> MarketPriceData;
    public Map<String, MarketMailItemOBJ> MarketMailListings;
    public PermissionService permservice;
    public EconomyService ecoservice;
    public NucleusNicknameService nNickService;
    public PermissionDescription.Builder permDescBuilder;
    public List<MarketHistoryOBJ> MarketHistory;
    public List<UUID> BroadcastIgnoreList;
    public Date LastHistorySave;
    public UserStorageService userStorage;
    public Path configfullpath;
    public Path historyDir;
    private Game game;
    private CommandManager cmdManager;

    @Inject
    public NyxMarket(Metrics.Factory metricsFactory, @ConfigDir(sharedRoot = true) Path path, Logger logger, PluginContainer container) {
        nyxmarket = this;
        this.pluginContainer = container;

        this.logger = NyxCore.getInstance().getLogger(CText.get(CText.Colors.BLUE, 1, "Nyx") + CText.get(CText.Colors.MAGENTA, 0, "Market"));

        this.logger.info(" ");
        this.logger.info(CText.get(CText.Colors.MAGENTA, 0, "@name@") + CText.get(CText.Colors.YELLOW, 0, " v" + this.getVersion()));
        this.logger.info("Starting...");
        this.logger.info(" ");

        this.mailboxDir = Paths.get(this.getConfigPath().toString(), "mailbox");
        this.marketsDir = Paths.get(this.getConfigPath().toString(), "markets");
        this.historyDir = Paths.get(this.getConfigPath().toString(), "history");
        this.configfullpath = Paths.get(this.getConfigPath().toString(), "config.json");
        this.marketPricesfullpath = Paths.get(this.getConfigPath().toString(), "prices.json");

        this.MarketListings = new HashMap<>();
        this.MarketPriceData = new HashMap<>();
        this.MarketHistory = new ArrayList<>();
        this.LastHistorySave = new Date();
        this.BroadcastIgnoreList = new ArrayList<>();

        int pluginId = 13660;
        metrics = metricsFactory.make(pluginId);
    }

    @Nonnull
    public static NyxMarket getInstance() {
        return nyxmarket;
    }

    @Nonnull
    public Path getConfigPath() {
        return NyxCore.getInstance().getMarketPath();
    }

    @Nonnull
    public PluginContainer getPluginContainer() {
        return this.pluginContainer;
    }

    @Nonnull
    public String getVersion() {
        if (this.getPluginContainer().getVersion().isPresent()) {
            return this.getPluginContainer().getVersion().get();
        } else {
            return "@version@";
        }
    }

    @Nonnull
    public NMSettings getSettings() {
        return NyxCore.getInstance().getMarketSettings();
    }

    @Nonnull
    public NCLogger getLogger() {
        return logger;
    }

    @Nonnull
    public Game getGame() {
        return game;
    }

    @Inject
    public void setGame(Game game) {
        this.game = game;
    }

    @Nonnull
    public EconomyService getEcoService() {
        return this.ecoservice;
    }

    @Listener
    public void onGamePreInit(@Nullable final GamePreInitializationEvent event) {
        this.logger.info(" ");
        this.logger.info(CText.get(CText.Colors.MAGENTA, 0, "@name@") + CText.get(CText.Colors.YELLOW, 0, " v" + this.getVersion()));
        this.logger.info("Initializing...");
        this.logger.info(" ");

        DataRegistration<NMData, NMImmutableData> nmdata = DataRegistration.builder()
                .dataClass(NMData.class)
                .immutableClass(NMImmutableData.class)
                .builder(new NMDataBuilder())
                .id("smdata")
                .name("NMData")
                .build();
        Sponge.getDataManager().registerLegacyManipulatorIds("nmdata", nmdata);
    }

    @Listener
    public void onChangeServiceProvider(ChangeServiceProviderEvent event) {
        if (event.getService().equals(PermissionService.class)) {
            this.permservice = (PermissionService) event.getNewProviderRegistration().getProvider();
        }
        if (event.getService().equals(EconomyService.class)) {
            this.ecoservice = (EconomyService) event.getNewProviderRegistration().getProvider();
        }
        if (event.getService().equals(NucleusNicknameService.class)) {
            this.nNickService = (NucleusNicknameService) event.getNewProviderRegistration().getProvider();
        }
    }

    @Listener
    public void onGameInit(@Nullable final GameInitializationEvent event) {
        this.userStorage = Sponge.getServiceManager().provideUnchecked(UserStorageService.class);

        if (Sponge.getServiceManager().getRegistration(PermissionService.class).get().getPlugin().getId().equalsIgnoreCase("sponge")) {
            this.logger.error("Unable to initialize plugin. NyxMarket requires a PermissionService like  LuckPerms, PEX, PermissionsManager.");
            return;
        }

        if (Sponge.getServiceManager().getRegistration(EconomyService.class).get().getPlugin().getId().equalsIgnoreCase("sponge")) {
            this.logger.error("Unable to initialize plugin. NyxMarket requires a EconomyService like EconomyLite.");
            return;
        }

        try {
            if (!Files.exists(this.getConfigPath())) {
                Files.createDirectories(this.getConfigPath());
            }
        } catch (final IOException ex) {
            this.logger.error("Error on creating root plugin directory: {}", ex);
        }

        //this.settings.Load(this.configfullpath, this);
        this.getSettings().Load(this.configfullpath);

        final Date date = new Date();
        final DateFormat df = new SimpleDateFormat("yyyy_MM_dd");

        this.MarketHistory = Tools.loadmarkethistory(Paths.get(this.historyDir.toString(), df.format(date) + ".json"));
        this.MarketMailListings = Tools.loadmarketmail(Paths.get(this.mailboxDir.toString(), "main.json"));
        //this.MarketMailPages = Tools.marketmailpages(this.MarketMailListings);

        this.MarketListings = Tools.loadmarket(Paths.get(this.marketsDir.toString(), "main.json"));
        //this.MarketPages = Tools.marketpages(this.MarketListings, "*");

        Iterator<Map.Entry<String, MarketItemOBJ>> itr = this.MarketListings.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, MarketItemOBJ> entry = itr.next();
            if (DateUtils.addMinutes(entry.getValue().getCreationTime(), this.getSettings().getItemExpiryTime()).before(new Date())) {
                this.MarketMailAdd(Tools.MIOtoMMIO(entry.getValue().getPlayer(), entry.getValue()));
                itr.remove();
                this.MarketRemove2(entry.getKey());
            }
        }

        Task.builder().execute(new itemExpirationTask(this))
                .async()
                .interval(1, TimeUnit.MINUTES)
                .name("Market Item Expiration Task").submit(this);


        this.MarketPriceData = Tools.loadmarketprices(this.marketPricesfullpath);

        this.logger.info("Plugin Initialized successfully!");
    }

    @Listener
    public void onServerStarting(GameStartingServerEvent event) {
        this.logger.info("Loading...");
        this.cmdManager = new CommandManager(this.game, this);
        this.logger.info("Loaded!");
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        //this.logger.info("Game Server  Started...");
    }

    public void MarketAdd(MarketItemOBJ mi) {
        this.MarketListings.putIfAbsent(mi.getMD5(), mi);
        //this.MarketPages = Tools.marketpages(this.MarketListings, "*");
        Tools.savemarket(this.MarketListings, Paths.get(this.marketsDir.toString(), "main.json"));
    }

    public void MarketRemove(String mi) {
        this.MarketListings.remove(mi);
        //this.MarketPages = Tools.marketpages(this.MarketListings, "*");
        Tools.savemarket(this.MarketListings, Paths.get(this.marketsDir.toString(), "main.json"));
    }


    public void MarketRemove2(String mi) {
        // this.MarketListings.remove(mi);
        //this.MarketPages = Tools.marketpages(this.MarketListings, "*");
        Tools.savemarket(this.MarketListings, Paths.get(this.marketsDir.toString(), "main.json"));
    }

    public void MarketMailAdd(MarketMailItemOBJ mi) {
        this.MarketMailListings.putIfAbsent(mi.getMD5(), mi);
        //this.MarketMailPages = Tools.marketmailpages(this.MarketMailListings);
        Tools.savemarketmail(this.MarketMailListings, Paths.get(this.mailboxDir.toString(), "main.json"));
    }

    public void MarketMailRemove(String mi) {
        this.MarketMailListings.remove(mi);
        //this.MarketMailPages = Tools.marketmailpages(this.MarketMailListings);
        Tools.savemarketmail(this.MarketMailListings, Paths.get(this.mailboxDir.toString(), "main.json"));
    }

    public void MarketPriceSet(String itemData, NMPriceData pd) {
        this.MarketPriceData.put(itemData, pd);
        Tools.savemarketprices(this.MarketPriceData, this.marketPricesfullpath);
    }

    public void MarketPriceRemove(String itemData) {
        this.MarketPriceData.remove(itemData);
        Tools.savemarketprices(this.MarketPriceData, this.marketPricesfullpath);
    }

    public void MarketHistoryAdd(MarketHistoryOBJ mho) {
        final Date date = new Date();
        final DateFormat df = new SimpleDateFormat("yyyy_MM_dd");
        if (df.format(mho.getDateTime()).equals(df.format(this.LastHistorySave))) {
            this.MarketHistory.add(mho);
        } else {
            this.MarketHistory.clear();
            this.MarketHistory.add(mho);
        }

        Tools.savemarkethistory(this.MarketHistory, Paths.get(this.historyDir.toString(), df.format(date) + ".json"));
        this.LastHistorySave = date;
    }
}
