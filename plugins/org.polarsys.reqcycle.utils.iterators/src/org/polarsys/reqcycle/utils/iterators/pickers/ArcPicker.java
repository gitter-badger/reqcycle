/*******************************************************************************
 * Copyright (c) 2013 Atos.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Atos - initial API and implementation
 ******************************************************************************/
package org.polarsys.reqcycle.utils.iterators.pickers;

import java.util.List;

import org.polarsys.reqcycle.utils.iterators.exceptions.PickerExecutionException;

import com.google.common.collect.Lists;

/**
 * Decorator for a picker : a picker
 */
public class ArcPicker implements IArcPicker {

	protected IPicker basicPicker;

	public ArcPicker(IPicker picker) {
		basicPicker = picker;
	}

	public Iterable<?> getNexts(Object element) throws PickerExecutionException {
		List<IArc> result = Lists.newLinkedList();
		if (element instanceof IArc) {
			IArc arc = (IArc) element;
			// Getting the destination of the arc : the nexts are
			// the arcs starting from the destination.
			Object destination = arc.getDestination();
			Iterable<?> nexts = basicPicker.getNexts(destination);
			if (nexts != null) {
				for (Object child : nexts) {
					IArc arcChild = new PickableArc(basicPicker, arc, destination, child);
					result.add(arcChild);
				}
			}
		}
		return result;
	}

	public IPicker getBasePicker() {
		return this.basicPicker;
	}

	/**
	 * Implementation of arc that ensures that arcs generated by two different pickers are different.
	 */
	protected class PickableArc implements IPickableArc {

		private Object origin;

		private Object destination;

		private IPicker picker;

		/**
		 * The arc this arc was generated from. HAS NO INFLUENCE ON THE EQUALITY OF ARCS.
		 */
		private IArc parentArc;

		public PickableArc(IPicker picker, IArc parentArc, Object origin, Object destination) {
			this.picker = picker;
			this.origin = origin;
			this.destination = destination;
			this.parentArc = parentArc;
		}

		public Object getOrigin() {
			return origin;
		}

		public Object getDestination() {
			return destination;
		}

		public IPicker getPicker() {
			return picker;
		}

		public IArc getParentArc() {
			return parentArc;
		}

		/**
		 * Checks that all attributes are equal between the two objects.
		 */
		@Override
		public boolean equals(Object arg0) {
			if (arg0 instanceof PickableArc) {
				PickableArc arc = (PickableArc) arg0;
				boolean sameOrigin = false;
				if (getOrigin() != null && arc.getOrigin() != null) {
					sameOrigin = this.getOrigin().equals(arc.getOrigin());
				} else {
					sameOrigin = (getOrigin() == null && arc.getOrigin() == null);
				}
				boolean sameDestination = false;
				if (getDestination() != null && arc.getDestination() != null) {
					sameDestination = this.getDestination().equals(arc.getDestination());
				} else {
					sameDestination = (getDestination() == null && arc.getDestination() == null);
				}
				boolean samePicker = false;
				if (getPicker() != null && arc.getDestination() != null) {
					samePicker = this.getPicker().equals(arc.getPicker());
				} else {
					samePicker = (getPicker() == null && arc.getPicker() == null);
				}
				return (sameOrigin && sameDestination && samePicker);
			}
			return false;
		}

		/**
		 * Ensuring that two PickableArcs have the same hashcodes.
		 */
		@Override
		public int hashCode() {
			return getOrigin().hashCode();
		}

		/**
		 * Tests whether this arc as an ancestor that equals itself. Allows the detection of cycles in a graph (Cycling <> redundant arcs)
		 */
		public boolean provokesCycling() {
			IArc currentAncestor = this.getParentArc();
			while (currentAncestor instanceof IPickableArc) {
				if (currentAncestor.equals(this)) {
					return true;
				} else {
					currentAncestor = ((IPickableArc) currentAncestor).getParentArc();
				}
			}
			return false;
		}

		public List<Object> getCycle() {
			List<Object> result = Lists.newArrayList();
			result.add(this.getDestination());
			result.add(this.getOrigin());

			IArc currentAncestor = this.getParentArc();
			while (currentAncestor instanceof IPickableArc && currentAncestor != this) {
				result.add(currentAncestor.getOrigin());
				currentAncestor = ((IPickableArc) currentAncestor).getParentArc();
			}
			return result;
		}

	}

}
