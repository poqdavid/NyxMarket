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

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.UUID;

import static org.spongepowered.api.data.DataQuery.of;

public class NMKeys {
    public static Key<Value<UUID>> OWNER_ID = Key.builder()
            .type(new TypeToken<Value<UUID>>() {
            })
            .id("sm:ownerid")
            .name("OwnerID")
            .query(of("OwnerID"))
            .build();
    public static Key<Value<UUID>> WORLD_ID = Key.builder()
            .type(new TypeToken<Value<UUID>>() {
            })
            .id("sm:world.id")
            .name("WorldID")
            .query(of("WorldID"))
            .build();
    public static Key<Value<String>> CREATION_TIME = Key.builder()
            .type(new TypeToken<Value<String>>() {
            })
            .id("sm:creation_time")
            .name("CreationTime")
            .query(of("CreationTime"))
            .build();
    public static Key<Value<Integer>> ITEM_PRICE = Key.builder()
            .type(new TypeToken<Value<Integer>>() {
            })
            .id("sm:smitem_price")
            .name("ItemPrice")
            .query(of("ItemPrice"))
            .build();
    public static Key<Value<Integer>> ITEM_AMOUNT = Key.builder()
            .type(new TypeToken<Value<Integer>>() {
            })
            .id("sm:smitem_amount")
            .name("ItemAmount")
            .query(of("ItemAmount"))
            .build();
    public static Key<Value<Integer>> ITEM_PAGE = Key.builder()
            .type(new TypeToken<Value<Integer>>() {
            })
            .id("sm:smitem_page")
            .name("ItemPage")
            .query(of("ItemPage"))
            .build();
    public static Key<Value<String>> ITEM_ID = Key.builder()
            .type(new TypeToken<Value<String>>() {
            })
            .id("sm:smitem_id")
            .name("ItemID")
            .query(of("ItemID"))
            .build();

    private NMKeys() {
    }
}
