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

package io.github.poqdavid.nyx.nyxmarket.Tasks;

import io.github.poqdavid.nyx.nyxmarket.Market.MarketItemOBJ;
import io.github.poqdavid.nyx.nyxmarket.NyxMarket;
import io.github.poqdavid.nyx.nyxmarket.Utils.Tools;
import org.apache.commons.lang3.time.DateUtils;
import org.spongepowered.api.scheduler.Task;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

public class itemExpirationTask implements Consumer<Task> {
    private final NyxMarket sm;
    private Boolean taskran = false;
    private Task task;

    public itemExpirationTask(NyxMarket sm) {
        this.sm = sm;
    }

    @Override
    public void accept(Task task) {

        if (!this.taskran) {
            this.task = task;
            this.taskran = true;
            this.sm.getLogger().info("Starting Task: " + task.getName());
        }


        if (this.sm.getSettings().getItemExpiryTime() == 0) {
            this.sm.getLogger().info("Stopping Task: " + task.getName());
            this.task.cancel();
        } else {
            this.Run();
        }

    }

    private void Run() {
        try {
            Iterator<Map.Entry<String, MarketItemOBJ>> itr = this.sm.MarketListings.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, MarketItemOBJ> entry = itr.next();
                if (DateUtils.addMinutes(entry.getValue().getCreationTime(), this.sm.getSettings().getItemExpiryTime()).before(new Date())) {
                    this.sm.MarketMailAdd(Tools.MIOtoMMIO(entry.getValue().getPlayer(), entry.getValue()));
                    itr.remove();
                    this.sm.MarketRemove2(entry.getKey());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
