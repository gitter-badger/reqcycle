/*******************************************************************************
 *  Copyright (c) 2013 AtoS
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html *
 *  Contributors:
 *    Olivier Melois (AtoS) - initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.polarsys.reqcycle.traceability.table.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.polarsys.reqcycle.traceability.model.Link;
import org.polarsys.reqcycle.traceability.ui.LinkPropertySource;

/**
 * Creating a new link for deletion purpose.
 * 
 * @author omelois
 *
 */
public class TransverseLink extends Link implements IAdaptable {

	private IProject project;

	public TransverseLink(Link link, IProject project) {
		super(link.getId(), link.getKind(), link.getSources(), link.getTargets());
		this.project = project;
	}

	public IProject getProject() {
		return project;
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (IPropertySource.class.equals(adapter) || IPropertySource2.class.equals(adapter)) {
			return new LinkPropertySource(this, null);
		}
		return null;
	}

}
