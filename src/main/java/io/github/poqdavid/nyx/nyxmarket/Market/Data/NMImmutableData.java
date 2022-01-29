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

package io.github.poqdavid.nyx.nyxmarket.Market.Data;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import java.util.UUID;

public class NMImmutableData extends AbstractImmutableData<NMImmutableData, NMData> {
    private final UUID owner_id;
    private final UUID world_id;
    private final String creation_time;
    private final Integer item_price;
    private final Integer item_amount;
    private final Integer item_page;
    private final String item_id;


    public NMImmutableData(UUID owner_id, UUID world_id, String creation_time, Integer item_price, Integer item_amount, Integer item_page, String item_id) {
        //super();
        this.owner_id = owner_id;
        this.world_id = world_id;
        this.creation_time = creation_time;
        this.item_price = item_price;
        this.item_amount = item_amount;
        this.item_page = item_page;
        this.item_id = item_id;
        registerGetters();
    }

    @Override
    protected void registerGetters() {
        registerFieldGetter(NMKeys.OWNER_ID, () -> this.owner_id);
        registerFieldGetter(NMKeys.WORLD_ID, () -> this.world_id);
        registerFieldGetter(NMKeys.CREATION_TIME, () -> this.creation_time);
        registerFieldGetter(NMKeys.ITEM_PRICE, () -> this.item_price);
        registerFieldGetter(NMKeys.ITEM_AMOUNT, () -> this.item_amount);
        registerFieldGetter(NMKeys.ITEM_PAGE, () -> this.item_page);
        registerFieldGetter(NMKeys.ITEM_ID, () -> this.item_id);

        registerKeyValue(NMKeys.OWNER_ID, this::owner_id);
        registerKeyValue(NMKeys.WORLD_ID, this::world_id);
        registerKeyValue(NMKeys.CREATION_TIME, this::creation_time);
        registerKeyValue(NMKeys.ITEM_PRICE, this::item_price);
        registerKeyValue(NMKeys.ITEM_AMOUNT, this::item_amount);
        registerKeyValue(NMKeys.ITEM_PAGE, this::item_page);
        registerKeyValue(NMKeys.ITEM_ID, this::item_id);
    }

    public ImmutableValue<UUID> owner_id() {
        return Sponge.getRegistry().getValueFactory().createValue(NMKeys.OWNER_ID, owner_id).asImmutable();
    }

    public ImmutableValue<UUID> world_id() {
        return Sponge.getRegistry().getValueFactory().createValue(NMKeys.WORLD_ID, world_id).asImmutable();
    }

    public ImmutableValue<String> creation_time() {
        return Sponge.getRegistry().getValueFactory().createValue(NMKeys.CREATION_TIME, creation_time).asImmutable();
    }

    public ImmutableValue<Integer> item_price() {
        return Sponge.getRegistry().getValueFactory().createValue(NMKeys.ITEM_PRICE, item_price).asImmutable();
    }

    public ImmutableValue<Integer> item_amount() {
        return Sponge.getRegistry().getValueFactory().createValue(NMKeys.ITEM_AMOUNT, item_amount).asImmutable();
    }

    public ImmutableValue<Integer> item_page() {
        return Sponge.getRegistry().getValueFactory().createValue(NMKeys.ITEM_PAGE, item_page).asImmutable();
    }

    public ImmutableValue<String> item_id() {
        return Sponge.getRegistry().getValueFactory().createValue(NMKeys.ITEM_ID, item_id).asImmutable();
    }

    @Override
    public NMData asMutable() {
        return new NMData(this.owner_id, this.world_id, this.creation_time, this.item_price, this.item_amount, this.item_page, this.item_id);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer()
                .set(NMKeys.OWNER_ID, this.owner_id)
                .set(NMKeys.WORLD_ID, this.world_id)
                .set(NMKeys.CREATION_TIME, this.creation_time)
                .set(NMKeys.ITEM_PRICE, this.item_price)
                .set(NMKeys.ITEM_AMOUNT, this.item_amount)
                .set(NMKeys.ITEM_PAGE, this.item_page)
                .set(NMKeys.ITEM_ID, this.item_id);
    }
}