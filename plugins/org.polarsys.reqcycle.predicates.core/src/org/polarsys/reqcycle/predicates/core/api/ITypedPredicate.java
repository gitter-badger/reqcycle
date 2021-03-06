/*******************************************************************************
 *  Copyright (c) 2013 AtoS
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html *
 *  Contributors:
 *    Papa Issa Diakhate (AtoS) - initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
/**
 */
package org.polarsys.reqcycle.predicates.core.api;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>ITyped Predicate</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.polarsys.reqcycle.predicates.core.api.ITypedPredicate#getTypedElement <em>Typed Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.polarsys.reqcycle.predicates.core.PredicatesPackage#getITypedPredicate()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ITypedPredicate<T> extends IPredicateContainer {

	/**
	 * Returns the value of the '<em><b>Typed Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Typed Element</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Typed Element</em>' reference.
	 * @see #setTypedElement(Object)
	 * @see org.polarsys.reqcycle.predicates.core.PredicatesPackage#getITypedPredicate_TypedElement()
	 * @model kind="reference"
	 * @generated
	 */
	T getTypedElement();

	/**
	 * Sets the value of the '{@link org.polarsys.reqcycle.predicates.core.api.ITypedPredicate#getTypedElement <em>Typed Element</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Typed Element</em>' reference.
	 * @see #getTypedElement()
	 * @generated
	 */
	void setTypedElement(T value);
} // ITypedPredicate
