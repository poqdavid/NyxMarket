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

import com.google.common.base.Charsets;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.poqdavid.nyx.nyxcore.Utils.CoreTools;
import io.github.poqdavid.nyx.nyxmarket.Market.Data.NMItemData;
import io.github.poqdavid.nyx.nyxmarket.Market.Data.NMPriceData;
import io.github.poqdavid.nyx.nyxmarket.Market.MarketHistoryOBJ;
import io.github.poqdavid.nyx.nyxmarket.Market.MarketItemOBJ;
import io.github.poqdavid.nyx.nyxmarket.Market.MarketMailItemOBJ;
import io.github.poqdavid.nyx.nyxmarket.NyxMarket;
import io.github.poqdavid.nyx.nyxmarket.Utils.gson.GsonUTCDateAdapter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Tools {

    public static void savemarket(Map<String, MarketItemOBJ> items, Path filePath) {
        final File file = filePath.toFile();
        final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).setPrettyPrinting().disableHtmlEscaping().create();

        if (items == null || items.isEmpty()) {
            CoreTools.WriteFile(file, "{}");
        } else {
            CoreTools.WriteFile(file, gson.toJson(items));
        }
    }

    public static Map<String, MarketItemOBJ> loadmarket(Path filePath) {
        final File file = filePath.toFile();
        if (!file.exists()) {
            CoreTools.WriteFile(file, "{}");
        }

        final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
        Type type = new TypeToken<Map<String, MarketItemOBJ>>() {
        }.getType();

        Map<String, MarketItemOBJ> data = null;
        Map<String, MarketItemOBJ> datafinal = new HashMap<>();

        try {
            data = gson.fromJson(FileUtils.readFileToString(file, Charsets.UTF_8), type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (data != null) {
            if (!data.isEmpty()) {
                for (MarketItemOBJ entry : data.values()) {
                    datafinal.putIfAbsent(entry.getMD5(), entry);
                }
            } else {
                return data;
            }
        } else {
            return data;
        }


        return datafinal;
    }

    public static Map<String, NMPriceData> loadmarketprices(Path filePath) {
        final File file = filePath.toFile();
        if (!file.exists()) {
            CoreTools.WriteFile(file, "{}");
        }

        final Gson gson = new Gson();
        Type type = new TypeToken<Map<String, NMPriceData>>() {
        }.getType();

        Map<String, NMPriceData> data = null;

        try {
            data = gson.fromJson(FileUtils.readFileToString(file, Charsets.UTF_8), type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static void savemarketprices(Map<String, NMPriceData> items, Path filePath) {
        final File file = filePath.toFile();
        final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        if (items == null || items.isEmpty()) {
            CoreTools.WriteFile(file, "{}");
        } else {
            CoreTools.WriteFile(file, gson.toJson(items));
        }
    }

    public static Map<Integer, MarketItemOBJ[]> marketpages(Map<String, MarketItemOBJ> marketlisting, String search) {
        final Map<Integer, MarketItemOBJ[]> mkpages = new HashMap<>();
        Type type = new TypeToken<Map<Integer, MarketItemOBJ>>() {
        }.getType();

        int nums = 1;
        int pagenum = 1;
        MarketItemOBJ[] objs1 = new MarketItemOBJ[45];
        for (MarketItemOBJ entry : marketlisting.values()) {
            if (Tools.MarketItemSearch(search, entry)) {
                objs1[(nums - 1)] = entry;
                mkpages.put(pagenum, objs1);
                if (nums == 45) {
                    pagenum++;
                    nums = 0;
                    objs1 = new MarketItemOBJ[45];
                }

                nums++;
            }
        }

        return mkpages;
    }

    public static void savemarketmail(Map<String, MarketMailItemOBJ> items, Path filePath) {
        final File file = filePath.toFile();
        final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).setPrettyPrinting().disableHtmlEscaping().create();

        if (items == null || items.isEmpty()) {
            CoreTools.WriteFile(file, "{}");
        } else {
            CoreTools.WriteFile(file, gson.toJson(items));
        }
    }

    public static Map<String, MarketMailItemOBJ> loadmarketmail(Path filePath) {
        final File file = filePath.toFile();
        if (!file.exists()) {
            CoreTools.WriteFile(file, "{}");
        }

        final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
        Type type = new TypeToken<Map<String, MarketMailItemOBJ>>() {
        }.getType();

        Map<String, MarketMailItemOBJ> data = null;
        Map<String, MarketMailItemOBJ> datafinal = new HashMap<>();

        try {
            data = gson.fromJson(FileUtils.readFileToString(file, Charsets.UTF_8), type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (data != null) {
            if (!data.isEmpty()) {
                for (MarketMailItemOBJ entry : data.values()) {

                    datafinal.putIfAbsent(entry.getMD5(), entry);

                }
            } else {
                return data;
            }
        } else {
            return data;
        }


        return datafinal;
    }

    public static Map<Integer, MarketMailItemOBJ[]> marketmailpages(Map<String, MarketMailItemOBJ> marketmaillisting, UUID mailowner) {
        final Map<Integer, MarketMailItemOBJ[]> mkmpages = new HashMap<>();
        final Type type = new TypeToken<Map<Integer, MarketMailItemOBJ>>() {
        }.getType();

        int nums = 1;
        int pagenum = 1;
        MarketMailItemOBJ[] objs1 = new MarketMailItemOBJ[45];
        for (MarketMailItemOBJ entry : marketmaillisting.values()) {
            if (entry.getPlayer().equals(mailowner)) {
                objs1[(nums - 1)] = entry;
                mkmpages.put(pagenum, objs1);
                if (nums == 45) {
                    pagenum++;
                    nums = 0;
                    objs1 = new MarketMailItemOBJ[45];
                }

                nums++;
            }
        }

        return mkmpages;
    }

    public static void savemarkethistory(List<MarketHistoryOBJ> items, Path filePath) {
        final File file = filePath.toFile();
        final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).setPrettyPrinting().disableHtmlEscaping().create();

        if (items == null || items.isEmpty()) {
            CoreTools.WriteFile(file, "[]");
        } else {
            CoreTools.WriteFile(file, gson.toJson(items));
        }
    }

    public static List<MarketHistoryOBJ> loadmarkethistory(Path filePath) {
        final File file = filePath.toFile();
        if (!file.exists()) {
            CoreTools.WriteFile(file, "[]");
        }

        final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
        Type type = new TypeToken<List<MarketHistoryOBJ>>() {
        }.getType();

        List<MarketHistoryOBJ> data = null;
        try {
            data = gson.fromJson(FileUtils.readFileToString(file, Charsets.UTF_8), type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static NMItemData GetItemDATA(ItemStack is) {
        List<String> itEch = new ArrayList<String>();
        itEch.add("NONE");
        if (is.get(Keys.ITEM_ENCHANTMENTS).isPresent()) {
            //itEch = is1.get(Keys.ITEM_ENCHANTMENTS).orElse(null);
            if (is.get(Keys.ITEM_ENCHANTMENTS).get() != null) {
                itEch.remove("NONE");
                for (Enchantment ite : is.get(Keys.ITEM_ENCHANTMENTS).get()) {

                    itEch.add(ite.getType().getId() + "_lvl" + ite.getLevel());
                }
            }
        }

        List<String> itLore = new ArrayList<String>();
        itLore.add("NONE");
        if (is.get(Keys.ITEM_LORE).isPresent()) {
            if (is.get(Keys.ITEM_LORE).get() != null) {
                itLore.remove("NONE");
                for (Text tx : is.get(Keys.ITEM_LORE).get()) {

                    itLore.add(tx.toString());
                }
            }
        }
        return new NMItemData(is.getType().getName(), itEch, itLore);
    }

    public static NMItemData GetItemDATA(ItemStackSnapshot is) {
        List<String> itEch = new ArrayList<String>();
        itEch.add("NONE");
        if (is.get(Keys.ITEM_ENCHANTMENTS).isPresent()) {
            //itEch = is1.get(Keys.ITEM_ENCHANTMENTS).orElse(null);
            if (is.get(Keys.ITEM_ENCHANTMENTS).get() != null) {
                itEch.remove("NONE");
                for (Enchantment ite : is.get(Keys.ITEM_ENCHANTMENTS).get()) {

                    itEch.add(ite.getType().getId() + "_lvl" + ite.getLevel());
                }
            }
        }

        List<String> itLore = new ArrayList<String>();
        itLore.add("NONE");
        if (is.get(Keys.ITEM_LORE).isPresent()) {
            if (is.get(Keys.ITEM_LORE).get() != null) {
                itLore.remove("NONE");
                for (Text tx : is.get(Keys.ITEM_LORE).get()) {

                    itLore.add(tx.toString());
                }
            }
        }
        return new NMItemData(is.getType().getName(), itEch, itLore);
    }

    public static String ItemDataToBase64(NMItemData pd) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return Base64.getEncoder().encodeToString(gson.toJson(pd).getBytes());
    }

    public static NMItemData Base64ToItemData(String pd) {
        Gson gson = new Gson();

        return gson.fromJson(Arrays.toString(Base64.getDecoder().decode(pd)), NMItemData.class);
    }

    public static List<Date> GetDatesBetween(Date start, Date end) {
        List<Date> ret = new ArrayList<Date>();
        for (Date date = start; !date.after(end); date = DateUtils.addDays(date, 1)) {
            ret.add(date);
        }
        return ret;
    }

    public static Boolean MarketHistorySearch(String search, MarketHistoryOBJ mhobj) {
        if (search.equalsIgnoreCase("*") || search.equalsIgnoreCase("all")) {
            return true;
        }

        Integer numKeys = 0;
        Integer numFound = 0;

        String ItemID = "*";
        String ItemName = "*";

        if (!mhobj.getExtraData().equalsIgnoreCase("none")) {
            try {
                ItemID = mhobj.getExtraData().split(",")[0];
            } catch (Exception ex) {
                ItemID = "*";
            }

            try {
                ItemName = mhobj.getExtraData().split(",")[1];
            } catch (Exception exx) {
                ItemName = "*";
            }
        }


        final String[] searchdatas = search.split("#");
        for (String searchdata : searchdatas) {
            final String[] splits = searchdata.split(":");
            final String key = splits[0].toLowerCase();
            switch (key) {
                case "p":
                case "player": {
                    numKeys++;
                    try {
                        if (mhobj.getPlayerName().equalsIgnoreCase(splits[1])) {
                            numFound++;
                        }
                    } catch (Exception ex) {
                    }

                    break;
                }
                case "tc":
                case "textcontain": {
                    numKeys++;
                    try {

                        if (!ItemName.equalsIgnoreCase("none") && !ItemName.equalsIgnoreCase("*")) {
                            if (mhobj.getData().replace("<NAME>", ItemName).toLowerCase().contains(splits[1].toLowerCase())) {
                                numFound++;
                            }
                        } else {
                            if (mhobj.getData().toLowerCase().contains(splits[1].toLowerCase())) {
                                numFound++;
                            }
                        }


                    } catch (Exception ex) {
                    }
                    break;
                }
                case "in":
                case "itemname": {
                    numKeys++;
                    try {
                        if (ItemName != "*") {
                            if (splits[1].equalsIgnoreCase(ItemName)) {
                                numFound++;
                            }
                        }

                    } catch (Exception ex) {
                    }
                    break;
                }
                case "iid":
                case "itemid": {
                    numKeys++;
                    if (ItemID != "*") {
                        if (splits[1].equalsIgnoreCase(ItemID)) {
                            numFound++;
                        }
                    }
                    break;
                }
                case "inc":
                case "itemnamecontain": {
                    numKeys++;
                    try {
                        if (ItemName != "*") {
                            if (ItemName.toLowerCase().contains(splits[1].toLowerCase())) {
                                numFound++;
                            }
                        }

                    } catch (Exception ex) {
                    }
                    break;
                }
                case "iidc":
                case "itemidcontain": {
                    numKeys++;
                    if (ItemID != "*") {
                        if (ItemID.toLowerCase().contains(splits[1].toLowerCase())) {
                            numFound++;
                        }
                    }
                    break;
                }
                default: {
                    break;
                }
            }
        }

        return numKeys == numFound;
    }

    public static void SendHistory(CommandSource src, String search) throws CommandException {
        List<Text> cmdHL = new ArrayList<>();
        List<MarketHistoryOBJ> mho = new ArrayList<>();

        final DateFormat df = new SimpleDateFormat("yyyy_MM_dd");
        Date datestart = null;
        Date dateend = null;

        final String[] searchdatas = search.split("#");
        for (String searchdata : searchdatas) {
            final String[] splits = searchdata.split(":");
            final String key = splits[0].toLowerCase();
            switch (key) {
                case "d":
                case "date": {
                    try {
                        if (splits[1].contains("to")) {
                            datestart = df.parse(splits[1].split("to")[0]);
                            if (splits[1].split("to")[1].equals("*")) {
                                dateend = NyxMarket.getInstance().LastHistorySave;
                            } else {
                                dateend = df.parse(splits[1].split("to")[1]);
                            }

                        } else {
                            datestart = df.parse(splits[1]);
                        }
                    } catch (ParseException e) {
                        throw new CommandException(Text.of("Please make sure you use the correct format > yyyy_MM_dd"));
                    }
                    break;
                }
                default: {
                    break;
                }
            }
        }


        if (dateend != null) {
            final List<Date> dates = GetDatesBetween(datestart, dateend);
            for (Date date : dates) {

                final Path ph = Paths.get(NyxMarket.getInstance().historyDir.toString(), df.format(date) + ".json");
                final File file = ph.toFile();

                if (file.exists()) {
                    mho.addAll(Tools.loadmarkethistory(ph));
                } else {
                    // throw new CommandException(Text.of("History for " + df.format(datestart) + " isn't available!"));
                }

            }

        } else {
            if (datestart != null) {
                final Path ph = Paths.get(NyxMarket.getInstance().historyDir.toString(), df.format(datestart) + ".json");
                final File file = ph.toFile();

                if (file.exists()) {
                    mho = Tools.loadmarkethistory(ph);
                } else {
                    throw new CommandException(Text.of("History for " + df.format(datestart) + " isn't available!"));
                }
            } else {
                mho = NyxMarket.getInstance().MarketHistory;
            }
        }


        for (MarketHistoryOBJ cs : mho) {
            if (Tools.MarketHistorySearch(search, cs)) {
                String ItemID = "*";
                String ItemName = "*";

                if (!cs.getExtraData().equalsIgnoreCase("none")) {
                    try {
                        ItemID = cs.getExtraData().split(",")[0];
                    } catch (Exception ex) {
                    }

                    try {
                        ItemName = cs.getExtraData().split(",")[1];
                    } catch (Exception exx) {
                    }
                }

                final Text.Builder HoverTexts = Text.builder();
                HoverTexts.append(Text.of("Date/Time: " + cs.getDateTime()));
                HoverTexts.append(Text.of("\n"));
                HoverTexts.append(Text.of("Player: " + cs.getPlayerName()));
                HoverTexts.append(Text.of("\n"));
                HoverTexts.append(Text.of("PlayerUUID: " + cs.getPlayerUUID()));


                Text logtext;
                if (cs.getExtraData().equalsIgnoreCase("none")) {
                    logtext = Text.builder().append(Text.of(TextColors.GOLD, "- ", TextColors.GRAY, cs.getData())).onHover(TextActions.showText(HoverTexts.toText())).build();
                } else {
                    HoverTexts.append(Text.of("\n"));
                    HoverTexts.append(Text.of("Item ID: " + ItemID));
                    HoverTexts.append(Text.of("\n"));
                    HoverTexts.append(Text.of("Item Name: " + ItemName));
                    logtext = Text.builder().append(Text.of(TextColors.GOLD, "- ", TextColors.GRAY, cs.getData().replace("<NAME>", ItemName))).onHover(TextActions.showText(HoverTexts.toText())).build();
                }


                cmdHL.add(logtext);
            }
        }

        PaginationService paginationService = NyxMarket.getInstance().getGame().getServiceManager().provide(PaginationService.class).get();
        PaginationList.Builder builder = paginationService.builder();

        //builder.title(Text.of(TextColors.DARK_AQUA, "MarketHistory - " + df.format(date)))
        builder.title(Text.of(TextColors.DARK_AQUA, "MarketHistory"))
                .contents(cmdHL)
                .padding(Text.of("="))
                .sendTo(src);

    }

    public static MarketMailItemOBJ MIOtoMMIO(UUID player, MarketItemOBJ mio) {
        final MarketMailItemOBJ mmio = new MarketMailItemOBJ();
        mmio.setAmount(mio.getAmount());
        mmio.setCreationTime(mio.getCreationTime());
        mmio.setItemData(mio.getItemData());
        mmio.setPlayer(player);
        mmio.setPrice(mio.getPrice());
        mmio.setWorld(mio.getWorld());
        return mmio;
    }

    public static Boolean TaskAvilable(String lookupst) {
        Boolean temp_out = false;
        for (Task task : Sponge.getScheduler().getScheduledTasks(NyxMarket.getInstance())) {
            if (task.getName().contains(lookupst)) {
                temp_out = true;
            }
        }
        return temp_out;
    }

    public static Boolean MarketItemSearch(String search, MarketItemOBJ miobj) {
        if (search.equalsIgnoreCase("*") || search.equalsIgnoreCase("all")) {
            return true;
        }

        Integer numKeys = 0;
        Integer numFound = 0;

        final String[] searchdatas = search.split("#");
        for (String searchdata : searchdatas) {
            final String[] splits = searchdata.split(":");
            final String key = splits[0].toLowerCase();
            switch (key) {
                case "p":
                case "player": {
                    numKeys++;
                    try {
                        Optional<User> userOpt = NyxMarket.getInstance().userStorage.get(miobj.getPlayer());
                        if (userOpt.get().getName().equalsIgnoreCase(splits[1])) {
                            numFound++;
                        }
                    } catch (Exception ex) {
                    }

                    break;
                }
                case "t":
                case "title": {
                    numKeys++;
                    try {

                        if (CoreTools.getItemName(CoreTools.Base64ToItemStack(miobj.getItemData())).toPlain().toLowerCase().replace(" ", "").equals(splits[1].toLowerCase())) {
                            numFound++;
                        }
                    } catch (Exception ex) {
                    }
                    break;
                }
                case "tc":
                case "titlecontain": {
                    numKeys++;
                    if (CoreTools.getItemName(CoreTools.Base64ToItemStack(miobj.getItemData())).toPlain().toLowerCase().replace(" ", "").contains(splits[1].toLowerCase())) {
                        numFound++;
                    }
                    break;
                }
                case "db":
                case "datebefore": {
                    numKeys++;
                    try {
                        final DateFormat df = new SimpleDateFormat("yyyy_MM_dd");
                        try {
                            if (df.parse(df.format(miobj.getCreationTime())).before(df.parse(splits[1]))) {
                                numFound++;
                            }
                        } catch (ParseException e) {


                        }

                    } catch (Exception ex) {
                    }

                    break;
                }
                case "da":
                case "dateafter": {
                    numKeys++;
                    try {
                        final DateFormat df = new SimpleDateFormat("yyyy_MM_dd");
                        try {
                            if (df.parse(df.format(miobj.getCreationTime())).after(df.parse(splits[1]))) {
                                numFound++;
                            }
                        } catch (ParseException e) {


                        }

                    } catch (Exception ex) {
                    }

                    break;
                }
                default: {
                    break;
                }
            }
        }

        return numKeys == numFound;
    }
}
