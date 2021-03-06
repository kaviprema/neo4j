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
package org.neo4j.cypher.internal.compiler.v2_2.pipes

import org.neo4j.cypher.internal.compiler.v2_2.ExecutionContext
import org.neo4j.cypher.internal.compiler.v2_2.executionplan.Effects
import org.neo4j.cypher.internal.compiler.v2_2.planDescription.InternalPlanDescription.Arguments._
import org.neo4j.cypher.internal.compiler.v2_2.planDescription.{NoChildren, PlanDescriptionImpl}
import org.neo4j.cypher.internal.compiler.v2_2.planner.logical.Cardinality
import org.neo4j.cypher.internal.compiler.v2_2.symbols._

case class AllNodesScanPipe(ident: String)(val estimatedCardinality: Option[Long] = None)
                           (implicit pipeMonitor: PipeMonitor) extends Pipe with RonjaPipe {

  protected def internalCreateResults(state: QueryState): Iterator[ExecutionContext] = {
    val baseContext = state.initialContext.getOrElse(ExecutionContext.empty)
    state.query.nodeOps.all.map(n => baseContext.newWith1(ident, n))
  }

  def exists(predicate: Pipe => Boolean): Boolean = predicate(this)

  def planDescription = PlanDescriptionImpl(this, "AllNodesScan", NoChildren, Seq(), identifiers)

  def symbols: SymbolTable = new SymbolTable(Map(ident -> CTNode))

  override def monitor = pipeMonitor

  override def localEffects: Effects = Effects.READS_NODES

  def dup(sources: List[Pipe]): Pipe = {
    require(sources.isEmpty)
    this
  }

  def sources: Seq[Pipe] = Seq.empty

  def withEstimatedCardinality(estimated: Long) = copy()(Some(estimated))
}
