/**
 * Copyright (c) 2002-2015 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.unsafe.impl.batchimport.staging;

import org.neo4j.graphdb.ResourceIterator;

/**
 * Takes an Iterator and chops it up into batches downstream.
 */
public class IteratorBatcherStep<T> extends ProducerStep<T>
{
    private final ResourceIterator<T> data;

    public IteratorBatcherStep( StageControl control, String name, int batchSize, int movingAverageSize,
            ResourceIterator<T> data )
    {
        super( control, name, batchSize, movingAverageSize );
        this.data = data;
    }

    @Override
    protected T nextOrNull()
    {
        return data.hasNext() ? data.next() : null;
    }

    @Override
    public void close()
    {
        data.close();
    }
}
