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
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

public class NMPriceData implements Serializable {
    private final static long serialVersionUID = -2114229871473793830L;

    @SerializedName("PriceMIN")
    @Expose
    private BigDecimal priceMIN;

    @SerializedName("PriceMAX")
    @Expose
    private BigDecimal priceMAX;

    public NMPriceData() {
    }

    public NMPriceData(BigDecimal priceMIN, BigDecimal priceMAX) {
        super();
        this.priceMIN = priceMIN;
        this.priceMAX = priceMAX;
    }


    public BigDecimal getPriceMIN() {
        return priceMIN;
    }

    public void setPriceMIN(BigDecimal priceMIN) {
        this.priceMIN = priceMIN;
    }

    public NMPriceData withPriceMIN(BigDecimal priceMIN) {
        this.priceMIN = priceMIN;
        return this;
    }

    public BigDecimal getPriceMAX() {
        return priceMAX;
    }

    public void setPriceMAX(BigDecimal priceMAX) {
        this.priceMAX = priceMAX;
    }

    public NMPriceData withPriceMAX(BigDecimal priceMAX) {
        this.priceMAX = priceMAX;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(priceMIN).append(priceMAX).toHashCode();
    }

    public String getMD5() {
        return DigestUtils.md5Hex((new StringBuilder().append(priceMIN).append(priceMAX)).toString());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof NMPriceData) == false) {
            return false;
        }
        NMPriceData rhs = ((NMPriceData) other);
        return new EqualsBuilder().append(priceMIN, rhs.priceMIN).append(priceMAX, rhs.priceMAX).isEquals();
    }
}