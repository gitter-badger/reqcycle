/*******************************************************************************
 * Copyright (c) 2014 Samares Engineering
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *    Raphael Faudou (Samares Engineering) - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.polarsys.reqcycle.xcos.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResource;

public class XcosModelFactory {

	private static Map<IResource, XcosModel> listModels = new HashMap<IResource, XcosModel>();

	public static XcosModel getModel(IResource res) {
		if (listModels.containsKey(res)) {
			return (XcosModel) listModels.get(res);
		} else {
			XcosModel model = new XcosModel(res.getName(), res);
			listModels.put(res, model);
			return model;
		}

	}

}
