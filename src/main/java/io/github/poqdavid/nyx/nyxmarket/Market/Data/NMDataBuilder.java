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

import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class NMDataBuilder extends AbstractDataBuilder<NMData> implements DataManipulatorBuilder<NMData, NMImmutableData> {
    public NMDataBuilder() {
        super(NMData.class, 1);
    }

    @Override
    public NMData create() {
        return new NMData(UUID.randomUUID(), UUID.randomUUID(), new Date().toString(), 1, 1, 1, "");
    }

    @Override
    public Optional<NMData> createFrom(DataHolder dataHolder) {
        return create().fill(dataHolder);
    }

    @Override
    protected Optional<NMData> buildContent(DataView container) throws InvalidDataException {
        return create().from(container);
    }
}
