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
package org.polarsys.reqcycle.repository.data.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.polarsys.reqcycle.core.ILogger;
import org.polarsys.reqcycle.repository.data.IDataManager;
import org.polarsys.reqcycle.repository.data.MappingModel.MappingAttribute;
import org.polarsys.reqcycle.repository.data.MappingModel.MappingElement;
import org.polarsys.reqcycle.repository.data.RequirementSourceConf.RequirementSource;
import org.polarsys.reqcycle.repository.data.RequirementSourceData.AbstractElement;
import org.polarsys.reqcycle.repository.data.RequirementSourceData.Section;
import org.polarsys.reqcycle.repository.data.ScopeConf.Scope;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class DataUtil {

	/** Requirement Source Manager */
	@Inject
	static IDataManager requirementSourceManager;

	@Inject
	static ILogger logger;

	protected static ComposedAdapterFactory cAdapterFactory = new ComposedAdapterFactory(
			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

	public static String getLabel(Object obj) {
		IItemLabelProvider itemLabelProvider = (IItemLabelProvider) cAdapterFactory
				.adapt(obj, IItemLabelProvider.class);
		if (itemLabelProvider != null) {
			return itemLabelProvider.getText(obj);
		}
		if (obj instanceof Scope) {
			return ((Scope) obj).eClass().getName();
		}
		return obj.toString();
	}

	public static Image getImage(Object obj) {
		IItemLabelProvider itemLabelProvider = (IItemLabelProvider) cAdapterFactory
				.adapt(obj, IItemLabelProvider.class);
		if (itemLabelProvider != null) {
			return null;// Activator.getImageDescriptor(itemLabelProvider.getImage(obj).toString()).createImage();
		}
		return null;
	}

	public static LabelProvider labelProvider = new LabelProvider() {

		@Override
		public String getText(Object element) {
			return getLabel(element);
		};

		@Override
		public Image getImage(Object element) {
			return DataUtil.getImage(element);
		};

	};

	public static String getInformation(AbstractElement object) {
		String result = "";

		EList<EStructuralFeature> structuralFeatures = object.eClass()
				.getEStructuralFeatures();
		if (object.getId() != null && !object.getId().isEmpty()) {
			result += " id : " + object.getId() + " ";
		}
		if (object.getText() != null && !object.getText().isEmpty()) {
			if (!result.isEmpty()) {
				result += " | ";
			}
			result += " name : " + object.getText() + " ";
		}

		for (EStructuralFeature eStructuralFeature : structuralFeatures) {
			if (!(eStructuralFeature instanceof EReference)
					&& object.eGet(eStructuralFeature) != null) {
				result += "[ " + eStructuralFeature.getName() + " : "
						+ object.eGet(eStructuralFeature) + "]";
			}
		}
		if (result.isEmpty()) {
			result = "Requirement Type : " + object.eClass().getName()
					+ " [ this element doesn't have any attribute ]";
		}

		return result;
	}

	static Joiner joiner = Joiner.on("::");
	static Splitter splitter = Splitter.on("::");

	public static List<String> getQualifiedPath(AbstractElement element) {
		EObject currentElement = element;
		List<String> res = Lists.newArrayList();
		while (currentElement != null) {
			if (currentElement instanceof AbstractElement) {
				res.add(0, ((AbstractElement) currentElement).getId());
			}
			currentElement = currentElement.eContainer();
		}
		return res;
	}

	public static String getFlattenedQualifiedPath(AbstractElement element) {
		return joiner.join(getQualifiedPath(element));
	}

	public static AbstractElement getElement(Resource r,
			String flattenedQualifiedNamePath) {
		return getElement(r.getContents(),
				Lists.newArrayList(splitter.split(flattenedQualifiedNamePath)));
	}

	public static AbstractElement getElement(EObject root,
			List<String> qualifiedNamePath) {
		return getElement(root.eContents(), qualifiedNamePath);
	}

	public static AbstractElement getElement(Collection<EObject> roots,
			List<String> qualifiedNamePath) {
		if (!qualifiedNamePath.isEmpty()) {
			String firstName = qualifiedNamePath.get(0);
			for (EObject obj : roots) {
				if (obj instanceof AbstractElement
						&& firstName.equals(((AbstractElement) obj).getId())) {
					if (qualifiedNamePath.size() > 1) {
						ArrayList<String> newQualifiedNamePath = Lists
								.newArrayList(qualifiedNamePath);
						newQualifiedNamePath.remove(0);
						return getElement(obj.eContents(), newQualifiedNamePath);
					} else {
						return (AbstractElement) obj;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Retrieves attribute mapping by his ID from an element mapping.
	 * 
	 * @param mappingElement
	 *            the mapping element
	 * @param id
	 *            the attribute mapping ID
	 * @return the attribute mapping found or null
	 */
	public static MappingAttribute getAttributeMapping(
			MappingElement mappingElement, String id, String longName) {
		for (MappingAttribute attribute : mappingElement.getAttributes()) {
			if (id != null && id.equals(attribute.getSourceId())) {
				return attribute;
			} else if (longName != null
					&& longName.equals(attribute.getDescription())) {
				return attribute;
			}
		}
		return null;
	}

	/**
	 * Retrieves element mapping from a mapping by his qualifier.
	 * 
	 * @param mapping
	 *            the mapping
	 * @param qualifier
	 *            the source element qualifier
	 * @return the element mapping found or null
	 */
	public static MappingElement getElementMapping(
			Collection<MappingElement> mapping, String qualifier,
			String longName) {
		for (MappingElement mappingElement : mapping) {
			if (qualifier != null
					&& qualifier.equals(mappingElement.getSourceQualifier())) {
				return mappingElement;
			} else if (longName != null
					&& longName.equals(mappingElement.getDescription())) {
				return mappingElement;
			}
		}
		return null;
	}

	public static Collection<AbstractElement> getAllContainedElements(
			EList<AbstractElement> elements) {
		Collection<AbstractElement> result = new ArrayList<AbstractElement>();
		result.addAll(elements);
		for (AbstractElement element : elements) {
			if (element instanceof Section) {
				result.addAll(getAllContainedElements(((Section) element)
						.getChildren()));
			}
		}
		return result;
	}

	/**
	 * @param resourceSet
	 * @param modelLocation
	 * @return
	 */
	public static Collection<EClassifier> getTargetEPackage(
			ResourceSet resourceSet, String modelLocation) {
		// FIXME : check uri creation
		URI uriAttributeModel = URI.createPlatformPluginURI(modelLocation,
				false);
		Resource attributeModelResource = resourceSet.getResource(
				uriAttributeModel, true);
		EList<EObject> modelContent = attributeModelResource.getContents();
		if (modelContent.size() > 0) {
			Collection<EClassifier> result = new ArrayList<EClassifier>();
			Iterator<EObject> iter = modelContent.iterator();
			while (iter.hasNext()) {
				EObject eObject = iter.next();
				if (eObject instanceof EPackage) {
					Collection<EClassifier> filtered = Collections2.filter(
							((EPackage) eObject).getEClassifiers(),
							new Predicate<EClassifier>() {

								@Override
								public boolean apply(EClassifier arg0) {
									if (arg0 instanceof EClass) {
										EList<EClass> superTypes = ((EClass) arg0)
												.getEAllSuperTypes();
										for (EClassifier superType : superTypes) {
											if ("AbstractElement"
													.equals(superType.getName())) {
												return true;
											}
										}
									}
									return false;
								}
							});
					result.addAll(filtered);
				}
			}
			return result;
		}
		return null;
	}

	public static Collection<RequirementSource> getRepositories(Object obj) {
		if (obj instanceof String
				&& requirementSourceManager.getRequirementSources((String) obj) != null) {
			return requirementSourceManager.getRequirementSources((String) obj);
		}
		if (obj instanceof RequirementSource) {
			return Arrays.asList(((RequirementSource) obj));
		}
		return Collections.emptyList();
	}

}
