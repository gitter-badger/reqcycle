/*******************************************************************************
 * * Copyright (c) 2013 AtoS
 * * All rights reserved. This program and the accompanying materials
 * * are made available under the terms of the Eclipse Public License v1.0
 * * which accompanies this distribution, and is available at
 * * http://www.eclipse.org/legal/epl-v10.html *
 * * Contributors:
 * * Olivier Melois (AtoS) - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.polarsys.reqcycle.utils.collect;

/**
 * A special version of Iterable<> that returns YieldAdapterIterators<>.
 */
public interface YieldAdapterIterable<T> extends Iterable<T> {

	/**
	 * Returns an iterator over the results.
	 */
	YieldAdapterIterator<T> iterator();
}
