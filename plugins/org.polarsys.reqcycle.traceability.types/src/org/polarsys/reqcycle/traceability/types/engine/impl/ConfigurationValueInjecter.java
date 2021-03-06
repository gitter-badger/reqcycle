/*******************************************************************************
 *  Copyright (c) 2013 AtoS
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html *
 *  Contributors:
 *    Tristan Faure (AtoS) - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.polarsys.reqcycle.traceability.types.engine.impl;

import org.polarsys.reqcycle.traceability.types.configuration.typeconfiguration.CustomType;
import org.polarsys.reqcycle.traceability.types.configuration.typeconfiguration.Entry;
import org.polarsys.reqcycle.types.IInjectedTypeChecker.IValueInjecter;
import org.polarsys.reqcycle.types.StringWrapper;

public class ConfigurationValueInjecter implements IValueInjecter {

	private CustomType type;

	public ConfigurationValueInjecter(CustomType type) {
		this.type = type;
	}

	@Override
	public <T> T getValue(String typeId, String name, Class<T> typeOfTheAttribute) {
		for (Entry e : ((CustomType) type).getEntries()) {
			if (e.getName().equalsIgnoreCase(name)) {
				if (e.getValue() instanceof String) {
					String valueString = (String) e.getValue();
					return StringWrapper.wrap(valueString, typeOfTheAttribute);
				} else if (typeOfTheAttribute.isInstance(e.getValue())) {
					return (T) e.getValue();
				}
			}
		}
		return null;
	}
}
