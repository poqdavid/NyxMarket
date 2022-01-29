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

import io.github.poqdavid.nyx.nyxcore.Utils.CoreTools;
import io.github.poqdavid.nyx.nyxmarket.Market.Data.NMData;
import io.github.poqdavid.nyx.nyxmarket.Market.Data.NMKeys;
import io.github.poqdavid.nyx.nyxmarket.NyxMarket;
import io.github.poqdavid.nyx.nyxmarket.Utils.Tools;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
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
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MarketMail {
    private final List<SlotPos> itemspos;
    private final Player player_cmd_src;
    private final Inventory inventory;
    private final int size;
    private final ItemStack itemStackNext;
    private final ItemStack itemStackPrev;
    private int page = 1;
    private String events = "";

    public MarketMail(Player player_cmd_src) {
        this.player_cmd_src = player_cmd_src;
        this.size = 6;
        this.itemspos = new ArrayList<>();

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
                .property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(Text.of("Mail")))
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

                        this.marketmailclick(stack.createStack(), this.events, this.player_cmd_src);
                    }

                    this.events = "";
                })
                .build(NyxMarket.getInstance());
        this.loadmarketmail(this.page, player_cmd_src);
    }

    private void marketmailclick(ItemStack Is, String event, Player player) {
        if (Is.equalTo(this.itemStackNext)) {
            this.page++;
            this.loadmarketmail(this.page, player);
        }

        if (Is.equalTo(this.itemStackPrev)) {
            if (this.page > 1) {
                this.page = this.page - 1;
            }

            this.loadmarketmail(this.page, player);
        }

        if (!Is.equalTo(this.itemStackNext) & !Is.equalTo(this.itemStackPrev)) {
            if (Is.get(NMKeys.ITEM_ID).isPresent()) {
                final Optional<String> id = Is.get(NMKeys.ITEM_ID);
                if (id.isPresent()) {
                    if (NyxMarket.getInstance().MarketMailListings.containsKey(id.get())) {
                        final MarketMailItemOBJ mio = NyxMarket.getInstance().MarketMailListings.get(id.get());

                        switch (event) {
                            case "primary":

                                final ItemStack itemStack = CoreTools.Base64ToItemStack(mio.getItemData());
                                itemStack.setQuantity(mio.getAmount());
                                player_cmd_src.getInventory().offer(itemStack);
                                NyxMarket.getInstance().MarketMailRemove(id.get());

                                break;

                            case "primaryshift":

                                break;
                        }

                    } else {
                        player_cmd_src.sendMessage(Text.of("FAILED MAIL NOT AVAILABLE!"));
                    }
                } else {
                    player_cmd_src.sendMessage(Text.of("ID NOT PRESENT!"));
                }
            }

        }
    }

    private void loadmarketmail(int pages, Player player) {
        int page = pages;
        final Map<Integer, MarketMailItemOBJ[]> MarketMailPages = Tools.marketmailpages(NyxMarket.getInstance().MarketMailListings, player.getUniqueId());

        if (pages >= MarketMailPages.size()) {
            page = MarketMailPages.size();
            this.page = page;
        }

        this.inventory.clear();
        int nums2 = 0;

        if (MarketMailPages.get(page) != null) {
            for (MarketMailItemOBJ miobj : MarketMailPages.get(page)) {
                if (miobj != null) {
                    if (miobj.getPlayer() != null) {
                        if (miobj.getPlayer().equals(player.getUniqueId())) {
                            final ItemStack isx = CoreTools.Base64ToItemStack(miobj.getItemData());
                            Integer price = 0;
                            //try {
                            if (miobj.getPrice() != null) {
                                price = miobj.getPrice().intValue();
                            }
                            // } catch (Exception ex) {

                            // }

                            final NMData smd = new NMData(miobj.getPlayer(), miobj.getWorld(), miobj.getCreationTime().toString(), price, miobj.getAmount(), page, miobj.getMD5());

                            isx.offer(smd);

                            this.inventory.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(this.itemspos.get(nums2).getX(), this.itemspos.get(nums2).getY()))).set(isx);

                            //this.inventory.query(this.itemspos.get(nums2)).set(isx);//error index

                            nums2++;
                        }
                    }
                }

            }
        }

        this.inventory.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(8, 5))).set(this.itemStackNext);
        this.inventory.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(0, 5))).set(this.itemStackPrev);
    }

    public Inventory getmarketmail() {
        return this.inventory;
    }
}
