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
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;
import java.util.UUID;

public class NMData extends AbstractData<NMData, NMImmutableData> {
    private UUID owner_id;
    private UUID world_id;
    private String creation_time;
    private Integer item_price;
    private Integer item_amount;
    private Integer item_page;
    private String item_id;


    public NMData(UUID owner_id, UUID world_id, String creation_time, Integer item_price, Integer item_amount, Integer item_page, String item_id) {
        this.owner_id = owner_id;
        this.world_id = world_id;
        this.creation_time = creation_time;
        this.item_price = item_price;
        this.item_amount = item_amount;
        this.item_page = item_page;
        this.item_id = item_id;
        registerGettersAndSetters();
    }

    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(NMKeys.OWNER_ID, () -> this.owner_id);
        registerFieldGetter(NMKeys.WORLD_ID, () -> this.world_id);
        registerFieldGetter(NMKeys.CREATION_TIME, () -> this.creation_time);
        registerFieldGetter(NMKeys.ITEM_PRICE, () -> this.item_price);
        registerFieldGetter(NMKeys.ITEM_AMOUNT, () -> this.item_amount);
        registerFieldGetter(NMKeys.ITEM_PAGE, () -> this.item_page);
        registerFieldGetter(NMKeys.ITEM_ID, () -> this.item_id);

        registerFieldSetter(NMKeys.OWNER_ID, owner_id -> this.owner_id = owner_id);
        registerFieldSetter(NMKeys.WORLD_ID, world_id -> this.world_id = world_id);
        registerFieldSetter(NMKeys.CREATION_TIME, creation_time -> this.creation_time = creation_time);
        registerFieldSetter(NMKeys.ITEM_PRICE, item_price -> this.item_price = item_price);
        registerFieldSetter(NMKeys.ITEM_AMOUNT, item_amount -> this.item_amount = item_amount);
        registerFieldSetter(NMKeys.ITEM_PAGE, item_page -> this.item_page = item_page);
        registerFieldSetter(NMKeys.ITEM_ID, item_id -> this.item_id = item_id);

        registerKeyValue(NMKeys.OWNER_ID, this::owner_id);
        registerKeyValue(NMKeys.WORLD_ID, this::world_id);
        registerKeyValue(NMKeys.CREATION_TIME, this::creation_time);
        registerKeyValue(NMKeys.ITEM_PRICE, this::item_price);
        registerKeyValue(NMKeys.ITEM_AMOUNT, this::item_amount);
        registerKeyValue(NMKeys.ITEM_PAGE, this::item_page);
        registerKeyValue(NMKeys.ITEM_ID, this::item_id);
    }

    public Value<UUID> owner_id() {
        return Sponge.getRegistry().getValueFactory().createValue(NMKeys.OWNER_ID, owner_id);
    }

    public Value<UUID> world_id() {
        return Sponge.getRegistry().getValueFactory().createValue(NMKeys.WORLD_ID, world_id);
    }

    public Value<String> creation_time() {
        return Sponge.getRegistry().getValueFactory().createValue(NMKeys.CREATION_TIME, creation_time);
    }

    public Value<Integer> item_price() {
        return Sponge.getRegistry().getValueFactory().createValue(NMKeys.ITEM_PRICE, item_price);
    }

    public Value<Integer> item_amount() {
        return Sponge.getRegistry().getValueFactory().createValue(NMKeys.ITEM_AMOUNT, item_amount);
    }

    public Value<Integer> item_page() {
        return Sponge.getRegistry().getValueFactory().createValue(NMKeys.ITEM_PAGE, item_page);
    }

    public Value<String> item_id() {
        return Sponge.getRegistry().getValueFactory().createValue(NMKeys.ITEM_ID, item_id);
    }

    @Override
    public Optional<NMData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<NMData> otherData_ = dataHolder.get(NMData.class);
        if (otherData_.isPresent()) {
            NMData otherData = otherData_.get();
            NMData finalData = overlap.merge(this, otherData);
            this.owner_id = finalData.owner_id;
            this.world_id = finalData.world_id;
            this.creation_time = finalData.creation_time;
            this.item_price = finalData.item_price;
            this.item_amount = finalData.item_amount;
            this.item_page = finalData.item_page;
            this.item_id = finalData.item_id;
        }
        return Optional.of(this);
    }

    @Override
    public Optional<NMData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<NMData> from(DataView view) {
        if (view.contains(NMKeys.OWNER_ID.getQuery()) && view.contains(NMKeys.WORLD_ID.getQuery()) && view.contains(NMKeys.CREATION_TIME.getQuery()) && view.contains(NMKeys.ITEM_PRICE.getQuery()) && view.contains(NMKeys.ITEM_AMOUNT.getQuery()) && view.contains(NMKeys.ITEM_PAGE.getQuery()) && view.contains(NMKeys.ITEM_ID.getQuery())) {
            this.owner_id = view.getObject(NMKeys.OWNER_ID.getQuery(), UUID.class).get();
            this.world_id = view.getObject(NMKeys.WORLD_ID.getQuery(), UUID.class).get();
            this.creation_time = view.getString(NMKeys.CREATION_TIME.getQuery()).get();
            this.item_price = view.getInt(NMKeys.ITEM_PRICE.getQuery()).get();
            this.item_amount = view.getInt(NMKeys.ITEM_AMOUNT.getQuery()).get();
            this.item_page = view.getInt(NMKeys.ITEM_PAGE.getQuery()).get();
            this.item_id = view.getString(NMKeys.ITEM_ID.getQuery()).get();

            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public NMData copy() {
        return new NMData(this.owner_id, this.world_id, this.creation_time, this.item_price, this.item_amount, this.item_page, this.item_id);
    }

    @Override
    public NMImmutableData asImmutable() {
        return new NMImmutableData(this.owner_id, this.world_id, this.creation_time, this.item_price, this.item_amount, this.item_page, this.item_id);
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
