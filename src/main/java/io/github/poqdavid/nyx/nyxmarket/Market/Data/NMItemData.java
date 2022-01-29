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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class NMItemData implements Serializable {

    private final static long serialVersionUID = 8283829145065280571L;
    @SerializedName("ItemType")
    @Expose
    private String itemType;
    @SerializedName("ItemEnchat")
    @Expose
    private List<String> itemEnchat;
    @SerializedName("ItemLore")
    @Expose
    private List<String> itemLore;

    public NMItemData() {
    }

    public NMItemData(String itemType, List<String> itemEnchat, List<String> itemLore) {
        super();
        this.itemType = itemType;
        this.itemEnchat = itemEnchat;
        this.itemLore = itemLore;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public NMItemData withItemType(String itemType) {
        this.itemType = itemType;
        return this;
    }

    public List<String> getItemEnchat() {
        return itemEnchat;
    }

    public void setItemEnchat(List<String> itemEnchat) {
        this.itemEnchat = itemEnchat;
    }

    public NMItemData withItemEnchat(List<String> itemEnchat) {
        this.itemEnchat = itemEnchat;
        return this;
    }

    public List<String> getItemLore() {
        return itemLore;
    }

    public void setItemName(List<String> itemName) {
        this.itemLore = itemLore;
    }

    public NMItemData withItemName(List<String> itemName) {
        this.itemLore = itemLore;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(itemType).append(itemEnchat).append(itemLore).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof NMItemData) == false) {
            return false;
        }
        NMItemData rhs = ((NMItemData) other);
        return new EqualsBuilder().append(itemType, rhs.itemType).append(itemEnchat, rhs.itemEnchat).append(itemLore, rhs.itemLore).isEquals();
    }
}
