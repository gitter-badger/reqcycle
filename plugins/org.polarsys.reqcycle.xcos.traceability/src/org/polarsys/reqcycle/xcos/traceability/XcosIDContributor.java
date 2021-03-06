/*******************************************************************************
 * Copyright (c) 2014 Samares Engineering
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *    Raphael Faudou (Samares Engineering) - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.polarsys.reqcycle.xcos.traceability;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;

import org.polarsys.reqcycle.uri.IIDContributor;
import org.polarsys.reqcycle.uri.IReachableCreator;
import org.polarsys.reqcycle.uri.IReachableManager;
import org.polarsys.reqcycle.uri.exceptions.IReachableHandlerException;
import org.polarsys.reqcycle.uri.model.Reachable;

public class XcosIDContributor implements IIDContributor {

	@Inject
	IReachableCreator creator;

	@Inject
	IReachableManager manager;

	public XcosIDContributor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Reachable getReachable(String logicalID) {

		URI uri;
		try {
			uri = new URI(logicalID);
			Reachable r = creator.getReachable(uri);
			return r;
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;

	}

}
