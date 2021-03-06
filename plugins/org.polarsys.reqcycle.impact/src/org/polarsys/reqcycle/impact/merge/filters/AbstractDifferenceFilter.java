/**
 * Copyright (c) 2014 AtoS
 *     All rights reserved. This program and the accompanying materials
 *     are made available under the terms of the Eclipse Public License v1.0
 *     which accompanies this distribution, and is available at
 *     http://www.eclipse.org/legal/epl-v10.html *
 *     Contributors:
 *       Sebastien Lemanceau (AtoS) - initial API and implementation and/or initial documentation
 */
package org.polarsys.reqcycle.impact.merge.filters;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.diffmerge.api.diff.IDifference;
import org.polarsys.reqcycle.impact.merge.MergerException;

public abstract class AbstractDifferenceFilter implements IDifferenceFilter {

	public void filter(Collection<IDifference> diffs) throws MergerException {
		Iterator<IDifference> diffsIt = diffs.iterator();
		while (diffsIt.hasNext()) {
			IDifference diff = diffsIt.next();
			if (!mergeDiff(diff)) {
				diffsIt.remove();
			}
		}
	}

	public abstract boolean mergeDiff(IDifference diff) throws MergerException;
}
