/**
 * Copyright (c) 2013 AtoS
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html *
 * Contributors:
 *   Anass Radouani (AtoS) - initial API and implementation and/or initial documentation
 */
package org.polarsys.reqcycle.repository.data.RequirementSourceConf;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see org.polarsys.reqcycle.repository.data.RequirementSourceConf.RequirementSourceConfPackage
 * @generated
 */
public interface RequirementSourceConfFactory extends EFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	RequirementSourceConfFactory eINSTANCE = org.polarsys.reqcycle.repository.data.RequirementSourceConf.impl.RequirementSourceConfFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Requirement Sources</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Requirement Sources</em>'.
	 * @generated
	 */
	RequirementSources createRequirementSources();

	/**
	 * Returns a new object of class '<em>Requirement Source</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Requirement Source</em>'.
	 * @generated
	 */
	RequirementSource createRequirementSource();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	RequirementSourceConfPackage getRequirementSourceConfPackage();

} // RequirementSourceConfFactory
