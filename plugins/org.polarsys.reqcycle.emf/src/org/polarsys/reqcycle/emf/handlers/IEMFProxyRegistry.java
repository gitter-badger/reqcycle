/*******************************************************************************
 *  Copyright (c) 2013-2014 AtoS
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html *
 *  Contributors:
 *    Tristan Faure (AtoS) - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.polarsys.reqcycle.emf.handlers;

import org.eclipse.emf.common.util.URI;

public interface IEMFProxyRegistry {
	/**
	 * Determines if the {@link URI} is a proxy or not
	 * only works for platform:/ uris
	 * @param uri
	 * @return
	 */
	boolean isProxy (URI uri) ;
}
