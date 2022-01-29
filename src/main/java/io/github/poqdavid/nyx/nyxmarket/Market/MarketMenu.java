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

package io.github.poqdavid.nyx.nyxmarket.Market;

import io.github.poqdavid.nyx.nyxcore.Permissions.MarketPermission;
import io.github.poqdavid.nyx.nyxcore.Utils.CoreTools;
import io.github.poqdavid.nyx.nyxmarket.Market.Data.NMData;
import io.github.poqdavid.nyx.nyxmarket.Market.Data.NMKeys;
import io.github.poqdavid.nyx.nyxmarket.NyxMarket;
import io.github.poqdavid.nyx.nyxmarket.Utils.Tools;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.*;

public class MarketMenu {
    private final List<SlotPos> itemspos;
    private final Player player_cmd_src;
    private final Inventory inventory;
    private final int size;
    private final ItemStack itemStackNext;
    private final ItemStack itemStackPrev;
    private int page = 1;
    private String events = "";
    private String search = "";

    public MarketMenu(Player player_cmd_src, String search) {
        this.player_cmd_src = player_cmd_src;
        this.size = 6;
        this.itemspos = new ArrayList<>();
        this.search = search;

        for (int x = 0; x <= 4; x++) {
            for (int y = 0; y <= 8; y++) {
                itemspos.add(new SlotPos(y, x));
            }
        }

        this.itemStackNext = ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).build();
        this.itemStackNext.setQuantity(1);
        this.itemStackNext.offer(Keys.DISPLAY_NAME, Text.of("Next page >>"));
        this.itemStackNext.offer(Keys.DYE_COLOR, DyeColors.RED);

        this.itemStackPrev = ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).build();
        this.itemStackPrev.setQuantity(1);
        this.itemStackPrev.offer(Keys.DISPLAY_NAME, Text.of("<< Previous page"));
        this.itemStackPrev.offer(Keys.DYE_COLOR, DyeColors.BLUE);

        this.inventory = Inventory.builder()
                .of(InventoryArchetypes.DOUBLE_CHEST)
                .property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(Text.of("Market")))
                .property(InventoryDimension.PROPERTY_NAME, InventoryDimension.of(9, this.size))
                .listener(ClickInventoryEvent.class, (ClickInventoryEvent event) -> {

                    event.setCancelled(true);
                    ItemStackSnapshot stack = ItemStackSnapshot.NONE;

                    if ((event instanceof ClickInventoryEvent.Primary)) {
                        this.events = "primary";
                        stack = event.getCursorTransaction().getFinal();
                    }

                    if ((event instanceof ClickInventoryEvent.Shift)) {
                        this.events = "primaryshift";
                        stack = event.getTransactions().get(0).getOriginal();
                    }

                    if (event.getTransactions().size() != 0) {

                        this.marketclick(stack.createStack(), this.events, event);
                    }

                    this.events = "";
                })
                .build(NyxMarket.getInstance());
        this.loadmarket(player_cmd_src, this.page);
    }

    private void marketclick(ItemStack Is, String event, ClickInventoryEvent eventz) {
        if (Is.equalTo(this.itemStackNext)) {
            this.page++;
            this.loadmarket(this.player_cmd_src, this.page);
        }

        if (Is.equalTo(this.itemStackPrev)) {
            if (this.page > 1) {
                this.page = this.page - 1;
            }

            this.loadmarket(this.player_cmd_src, this.page);
        }

        if (!Is.equalTo(this.itemStackNext) & !Is.equalTo(this.itemStackPrev)) {
            if (Is.get(NMKeys.ITEM_ID).isPresent()) {
                final Optional<String> id = Is.get(NMKeys.ITEM_ID);

                if (id.isPresent()) {
                    if (NyxMarket.getInstance().MarketListings.containsKey(id.get())) {
                        final MarketItemOBJ mio = NyxMarket.getInstance().MarketListings.get(id.get());

                        switch (event) {
                            case "primary":
                                EconomyService EcoService = NyxMarket.getInstance().getEcoService();
                                Optional<UniqueAccount> BuyerOpt = EcoService.getOrCreateAccount(this.player_cmd_src.getUniqueId());
                                Optional<UniqueAccount> SellerOpt = EcoService.getOrCreateAccount(mio.getPlayer());

                                if (BuyerOpt.isPresent()) {
                                    UniqueAccount BuyerAccount = BuyerOpt.get();
                                    if (SellerOpt.isPresent()) {
                                        UniqueAccount SellerAccount = SellerOpt.get();
                                        //TransactionResult BuyerSellerResult = BuyerAccount.transfer(SellerAccount, EcoService.getDefaultCurrency(), mio.getPrice(), Cause.source(this).build());
                                        TransactionResult BuyerSellerResult = BuyerAccount.transfer(SellerAccount, EcoService.getDefaultCurrency(), mio.getPrice(), Sponge.getCauseStackManager().getCurrentCause());
                                        if (BuyerSellerResult.getResult() == ResultType.SUCCESS) {
                                            NyxMarket.getInstance().MarketMailAdd(Tools.MIOtoMMIO(this.player_cmd_src.getUniqueId(), mio));
                                            NyxMarket.getInstance().MarketRemove(id.get());

                                            final Date date = new Date();
                                            NyxMarket.getInstance().MarketHistoryAdd(new MarketHistoryOBJ(date, player_cmd_src.getName(), player_cmd_src.getUniqueId(), "Player bought " + mio.getAmount() + " " + "<NAME>" + " for price of " + mio.getPrice() + " from the market.", Is.getType().getName() + "," + CoreTools.getItemName(Is).toPlain()));

                                            final Optional<Player> fromPlayer = Sponge.getServer().getPlayer(mio.getPlayer());

                                            final Text.Builder HoverTexts = Text.builder();
                                            if (fromPlayer.isPresent()) {
                                                HoverTexts.append(Text.of(TextColors.GREEN, "From: ", TextColors.WHITE, fromPlayer.get().getName()));
                                                HoverTexts.append(Text.of("\n"));
                                            }

                                            HoverTexts.append(Text.of(TextColors.GREEN, "To: ", TextColors.WHITE, player_cmd_src.getName()));
                                            HoverTexts.append(Text.of("\n"));
                                            HoverTexts.append(Text.of(TextColors.GREEN, "Amount: ", TextColors.WHITE, mio.getAmount()));
                                            HoverTexts.append(Text.of("\n"));
                                            HoverTexts.append(Text.of(TextColors.GREEN, "Price: ", TextColors.WHITE, mio.getPrice()));
                                            final Text transactionText = Text.builder().append(Text.of(TextColors.GREEN, "You have bought the item, hover for more info.")).onHover(TextActions.showText(HoverTexts.toText())).build();


                                            player_cmd_src.sendMessage(transactionText);
                                        } else if (BuyerSellerResult.getResult() == ResultType.FAILED || BuyerSellerResult.getResult() == ResultType.ACCOUNT_NO_FUNDS) {
                                            player_cmd_src.sendMessage(Text.of(TextColors.RED, TextStyles.ITALIC, "Transaction Failed, you have no money in your balance."));
                                        } else {
                                            player_cmd_src.sendMessage(Text.of(TextColors.RED, TextStyles.ITALIC, "Transaction Failed."));
                                        }
                                    }
                                }
                                break;

                            case "primaryshift":
                                if (this.player_cmd_src.hasPermission(MarketPermission.ADMIN_CANCEL) || this.player_cmd_src.getUniqueId().equals(mio.getPlayer())) {
                                    NyxMarket.getInstance().MarketMailAdd(Tools.MIOtoMMIO(mio.getPlayer(), mio));
                                    NyxMarket.getInstance().MarketRemove(id.get());

                                    final Date date = new Date();
                                    NyxMarket.getInstance().MarketHistoryAdd(new MarketHistoryOBJ(date, this.player_cmd_src.getName(), this.player_cmd_src.getUniqueId(), "Admin canceled " + mio.getAmount() + " " + "<NAME>" + " for price of " + mio.getPrice() + " from the market.", Is.getType().getName() + "," + CoreTools.getItemName(Is).toPlain()));
                                    this.player_cmd_src.sendMessage(Text.of(TextColors.GREEN, TextStyles.ITALIC, "The items have been canceled!"));
                                }
                                break;
                        }
                    } else {
                        switch (event) {
                            case "primary":
                                this.player_cmd_src.sendMessage(Text.of(TextColors.RED, TextStyles.ITALIC, "Can't buy the item, the item is not Available!"));
                                break;

                            case "primaryshift":
                                if (this.player_cmd_src.hasPermission(MarketPermission.ADMIN_CANCEL)) {
                                    this.player_cmd_src.sendMessage(Text.of(TextColors.RED, TextStyles.ITALIC, "Can't cancel the item, the item is not Available!"));
                                }
                                break;
                        }
                    }
                } else {
                    this.player_cmd_src.sendMessage(Text.of(TextColors.RED, "ID NOT PRESENT!"));
                }

            }

        }
    }

    private void loadmarket(Player player_cmd_src, int pages) {
        int page = pages;
        final Map<Integer, MarketItemOBJ[]> MarketPages = Tools.marketpages(NyxMarket.getInstance().MarketListings, this.search);

        if (pages >= MarketPages.size()) {
            page = MarketPages.size();
            this.page = page;
        }

        this.inventory.clear();
        int nums2 = 0;

        if (MarketPages.get(page) != null) {
            for (MarketItemOBJ miobj : MarketPages.get(page)) {
                if (miobj != null) {
                    if (miobj.getPlayer() != null) {

                        Optional<User> userOpt = NyxMarket.getInstance().userStorage.get(miobj.getPlayer());

                        List<Text> lore = new ArrayList<>();

                        userOpt.ifPresent(user -> lore.add(Text.of(TextColors.GREEN, "By : ", TextColors.WHITE, user.getName())));

                        lore.add(Text.of(TextColors.GREEN, "Price: ", TextColors.WHITE, miobj.getPrice()));
                        lore.add(Text.of(TextColors.GREEN, "Amount: ", TextColors.WHITE, miobj.getAmount()));
                        lore.add(Text.of(TextColors.GREEN, "Creation Time: ", TextColors.WHITE, miobj.getCreationTime().toString()));
                        lore.add(Text.of(TextColors.GRAY, TextStyles.ITALIC, "Left click To buy the item."));

                        if (player_cmd_src.hasPermission(MarketPermission.ADMIN_CANCEL) || this.player_cmd_src.getUniqueId().equals(miobj.getPlayer())) {
                            lore.add(Text.of(TextColors.RED, TextStyles.ITALIC, "Shift + Left click to cancel the item."));
                        }

                        ItemStack isx = CoreTools.Base64ToItemStack(miobj.getItemData());

                        final NMData smd = new NMData(miobj.getPlayer(), miobj.getWorld(), miobj.getCreationTime().toString(), miobj.getPrice().intValue(), miobj.getAmount(), page, miobj.getMD5());
                        isx.offer(smd);
                        isx.offer(Keys.ITEM_LORE, lore);

                        this.inventory.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(this.itemspos.get(nums2).getX(), this.itemspos.get(nums2).getY()))).set(isx);
                        //this.inventory.query(this.itemspos.get(nums2)).set(isx);//error index

                        nums2++;

                    }
                }

            }
        }

        this.inventory.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(8, 5))).set(this.itemStackNext);
        this.inventory.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(0, 5))).set(this.itemStackPrev);
    }

    public Inventory getmarketmenu() {
        return this.inventory;
    }
}
