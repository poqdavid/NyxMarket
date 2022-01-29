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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class MarketHistoryOBJ implements Serializable {

    private final static long serialVersionUID = -1374853097266184010L;
    @SerializedName("datetime")
    @Expose
    private Date datetime;
    @SerializedName("playername")
    @Expose
    private String playername;
    @SerializedName("playeruuid")
    @Expose
    private UUID playeruuid;
    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("extradata")
    @Expose
    private String extradata;

    /**
     * No args constructor for use in serialization
     */
    public MarketHistoryOBJ() {
    }

    /**
     * @param playername
     * @param data
     * @param extradata
     * @param playeruuid
     * @param datetime
     */
    public MarketHistoryOBJ(Date datetime, String playername, UUID playeruuid, String data, String extradata) {
        super();
        this.datetime = datetime;
        this.playername = playername;
        this.playeruuid = playeruuid;
        this.data = data;
        this.extradata = extradata;
    }

    public Date getDateTime() {
        return datetime;
    }

    public void setDateTime(Date datetime) {
        this.datetime = datetime;
    }

    public MarketHistoryOBJ withDateTime(Date datetime) {
        this.datetime = datetime;
        return this;
    }

    public String getPlayerName() {
        return playername;
    }

    public void setPlayerName(String playername) {
        this.playername = playername;
    }

    public MarketHistoryOBJ withPlayerName(String playername) {
        this.playername = playername;
        return this;
    }

    public UUID getPlayerUUID() {
        return playeruuid;
    }

    public void setPlayerUUID(UUID playeruuid) {
        this.playeruuid = playeruuid;
    }

    public MarketHistoryOBJ withPlayerUUID(UUID playeruuid) {
        this.playeruuid = playeruuid;
        return this;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public MarketHistoryOBJ withData(String data) {
        this.data = data;
        return this;
    }

    public String getExtraData() {
        return extradata;
    }

    public void setExtraData(String extradata) {
        this.extradata = extradata;
    }

    public MarketHistoryOBJ withExtraData(String extradata) {
        this.extradata = extradata;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(datetime).append(playername).append(playeruuid).append(data).append(extradata).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MarketHistoryOBJ) == false) {
            return false;
        }
        MarketHistoryOBJ rhs = ((MarketHistoryOBJ) other);
        return new EqualsBuilder().append(datetime, rhs.datetime).append(playername, rhs.playername).append(playeruuid, rhs.playeruuid).append(data, rhs.data).append(extradata, rhs.extradata).isEquals();
    }

}
