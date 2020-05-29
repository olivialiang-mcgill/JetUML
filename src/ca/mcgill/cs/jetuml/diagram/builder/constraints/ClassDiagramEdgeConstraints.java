/*******************************************************************************
 * JetUML - A desktop application for fast UML diagramming.
 *
 * Copyright (C) 2020 by the contributors of the JetUML project.
 *     
 * See: https://github.com/prmr/JetUML
 *
 * This program is free software: you can redistribute it and/or modify
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
 * along with this program.  If not, see http://www.gnu.org/licenses.
 *******************************************************************************/

package ca.mcgill.cs.jetuml.diagram.builder.constraints;

import ca.mcgill.cs.jetuml.diagram.Edge;
import ca.mcgill.cs.jetuml.diagram.Node;
import ca.mcgill.cs.jetuml.diagram.edges.DependencyEdge;
import ca.mcgill.cs.jetuml.diagram.edges.GeneralizationEdge;

/**
 * Methods to create edge addition constraints that only apply to
 * class diagrams. CSOFF:
 */
public final class ClassDiagramEdgeConstraints
{
	private ClassDiagramEdgeConstraints() {}
	
	/*
	 * Self edges are not allowed for Generalization edges.
	 */
	public static Constraint noSelfGeneralization(Edge pEdge, Node pStart, Node pEnd)
	{
		return ()-> 
		{
			return !( pEdge.getClass() == GeneralizationEdge.class && pStart == pEnd );
		};
	}
	
	/*
	 * Self edges are not allowed for Dependency edges.
	 */
	public static Constraint noSelfDependency(Edge pEdge, Node pStart, Node pEnd)
	{
		return () ->
		{
			return !( pEdge.getClass() == DependencyEdge.class && pStart == pEnd );
		};
	}
	
	/*
	 * There can't be two dependencies, one in each direction, between two nodes.
	 * A bidirectional dependency should be used instead.
	 */
	public static Constraint noCircularDependencies(Edge pEdge, Node pStart, Node pEnd)
	{
		return () ->
		{
			if( pEdge.getClass() != DependencyEdge.class )
			{
				return true;
			}
			for( Edge edge : pStart.getDiagram().get().edgesConnectedTo(pStart) )
			{
				if( edge.getEnd() == pStart && edge.getStart() == pEnd )
				{
					return false;
				}
			}
			return true;
		};
	}
}
