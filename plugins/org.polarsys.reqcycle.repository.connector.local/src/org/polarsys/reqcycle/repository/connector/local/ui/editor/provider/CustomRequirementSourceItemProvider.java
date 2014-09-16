/*******************************************************************************
 *  Copyright (c) 2013 AtoS
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html *
 *  Contributors:
 *    Anass Radouani (AtoS) - initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.polarsys.reqcycle.repository.connector.local.ui.editor.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.polarsys.reqcycle.repository.data.IDataModelManager;
import org.polarsys.reqcycle.repository.data.RequirementSourceConf.RequirementSource;
import org.polarsys.reqcycle.repository.data.RequirementSourceConf.RequirementSourceConfPackage;
import org.polarsys.reqcycle.repository.data.RequirementSourceConf.provider.RequirementSourceItemProvider;
import org.polarsys.reqcycle.repository.data.RequirementSourceData.RequirementSourceDataFactory;
import org.polarsys.reqcycle.repository.data.RequirementSourceData.RequirementSourceDataPackage;
import org.polarsys.reqcycle.repository.data.RequirementSourceData.Section;
import org.polarsys.reqcycle.repository.data.types.IDataModel;
import org.polarsys.reqcycle.repository.data.types.IRequirementType;
import org.polarsys.reqcycle.repository.data.types.IType;
import org.polarsys.reqcycle.utils.inject.ZigguratInject;

import com.google.common.collect.Lists;


/**
 * The Class CustomRequirementSourceItemProvider.
 */
public class CustomRequirementSourceItemProvider extends RequirementSourceItemProvider {

	IDataModelManager manager = ZigguratInject.make(IDataModelManager.class);

	/**
	 * Instantiates a new custom requirement source item provider.
	 * 
	 * @param adapterFactory
	 *        the adapter factory
	 */
	public CustomRequirementSourceItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if(childrenFeatures == null) {
			childrenFeatures = new ArrayList<EStructuralFeature>();
			childrenFeatures.add(RequirementSourceConfPackage.Literals.REQUIREMENT_SOURCE__CONTENTS);
		}
		return childrenFeatures;
	}

	@Override
	public String getText(Object object) {
		String text = "";

		String label = ((RequirementSource)object).getName();
		if(label != null && !label.isEmpty()) {
			text += label;
		}

		String uri = ((RequirementSource)object).getRepositoryUri();
		if(uri != null && !uri.isEmpty()) {
			text += " [ " + uri + " ] ";
		}

		return text;
	}

	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		//FIXME : Use element Data Model to get possible children
		//Gets Dynamic Data Model possible children
		// TODO
	}

}
