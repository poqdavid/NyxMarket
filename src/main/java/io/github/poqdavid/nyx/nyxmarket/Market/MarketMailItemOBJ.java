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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class MarketMailItemOBJ implements Serializable {

    private final static long serialVersionUID = -2894608505001938694L;
    @SerializedName("Player")
    @Expose
    private UUID player;
    @SerializedName("World")
    @Expose
    private UUID world;
    @SerializedName("ItemData")
    @Expose
    private String itemData;
    @SerializedName("CreationTime")
    @Expose
    private Date creationTime;
    @SerializedName("Price")
    @Expose
    private BigDecimal price;
    @SerializedName("Amount")
    @Expose
    private Integer amount;

    public UUID getPlayer() {
        return player;
    }

    public void setPlayer(UUID player) {
        this.player = player;
    }

    public MarketMailItemOBJ withPlayer(UUID player) {
        this.player = player;
        return this;
    }

    public UUID getWorld() {
        return world;
    }

    public void setWorld(UUID world) {
        this.world = world;
    }

    public MarketMailItemOBJ withWorld(UUID world) {
        this.world = world;
        return this;
    }

    public String getItemData() {
        return itemData;
    }

    public void setItemData(String itemData) {
        this.itemData = itemData;
    }

    public MarketMailItemOBJ withItemData(String itemData) {
        this.itemData = itemData;
        return this;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public MarketMailItemOBJ withCreationTime(Date creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public MarketMailItemOBJ withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public MarketMailItemOBJ withAmount(Integer amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(player).append(world).append(itemData).append(creationTime).append(price).toHashCode();
    }

    public String getMD5() {
        return DigestUtils.md5Hex((new StringBuilder().append(player).append(world).append(itemData).append(creationTime).append(price)).toString());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MarketMailItemOBJ) == false) {
            return false;
        }
        MarketMailItemOBJ rhs = ((MarketMailItemOBJ) other);
        return new EqualsBuilder().append(player, rhs.player).append(world, rhs.world).append(itemData, rhs.itemData).append(creationTime, rhs.creationTime).append(price, rhs.price).append(amount, rhs.amount).isEquals();
    }
}
