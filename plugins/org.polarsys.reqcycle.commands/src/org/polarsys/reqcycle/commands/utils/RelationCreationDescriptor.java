/*******************************************************************************
 *  Copyright (c) 2013 AtoS
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html *
 *  Contributors:
 *    Philippe Roland (AtoS) - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.polarsys.reqcycle.commands.utils;

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.polarsys.reqcycle.traceability.types.configuration.typeconfiguration.Relation;
import org.polarsys.reqcycle.traceability.types.configuration.typeconfiguration.Type;

public class RelationCreationDescriptor {

	private int type;

	private Relation relation;

	public final static int DOWNSTREAM_TO_UPSTREAM = -1;

	public final static int UPSTREAM_TO_DOWNSTREAM = 1;

	public final static int BOTH = 0;

	private AdapterFactoryLabelProvider labelProvider;

	public RelationCreationDescriptor(int type, Relation relation) {
		this.type = type;
		this.relation = relation;
		ComposedAdapterFactory factory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		labelProvider = new AdapterFactoryLabelProvider(factory);
	}

	public Relation getRelation() {
		return relation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((relation == null) ? 0 : relation.hashCode());
		result = prime * result + type;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RelationCreationDescriptor other = (RelationCreationDescriptor) obj;
		if (relation == null) {
			if (other.relation != null)
				return false;
		} else if (!relation.equals(other.relation))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	public Type getSource() {
		if (isDownstreamToUpstream()){
			return relation.getDownstreamType();
		}
		else {
			return relation.getUpstreamType();
		}
	}

	public Type getTarget() {
		if (isDownstreamToUpstream()){
			return relation.getUpstreamType();
		}
		else {
			return relation.getDownstreamType();
		}
	}

	public String getLabel() {
		StringBuilder labelBuilder = new StringBuilder(relation.getKind().toUpperCase());
		// if both, then up and down types are the same
		if (type == UPSTREAM_TO_DOWNSTREAM || type == BOTH) {
			labelBuilder.append(" - FROM " + labelProvider.getText(relation.getUpstreamType()));
			labelBuilder.append(" TO " + labelProvider.getText(relation.getDownstreamType()));
		} else {
			labelBuilder.append(" - FROM " + labelProvider.getText(relation.getDownstreamType()));
			labelBuilder.append(" TO " + labelProvider.getText(relation.getUpstreamType()));
		}
		return labelBuilder.toString();
	}

	public boolean isDownstreamToUpstream() {
		return type == DOWNSTREAM_TO_UPSTREAM || type == BOTH;
	}

	public boolean isUpstreamToDownstream() {
		return type == UPSTREAM_TO_DOWNSTREAM || type == BOTH;
	}

}
