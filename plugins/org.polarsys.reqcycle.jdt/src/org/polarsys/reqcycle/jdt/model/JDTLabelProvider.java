/*******************************************************************************
 *  Copyright (c) 2013 AtoS
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html *
 *  Contributors:
 *    Olivier Melois (AtoS) - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.polarsys.reqcycle.jdt.model;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.LabelProvider;
import org.polarsys.reqcycle.jdt.Activator;
import org.polarsys.reqcycle.uri.model.Reachable;
import org.eclipse.swt.graphics.Image;

public class JDTLabelProvider extends LabelProvider {

	private static final String ICONS_JMETH_OBJ_GIF = "/icons/jmeth_obj.gif";
	public static ImageDescriptor desc = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, ICONS_JMETH_OBJ_GIF);

	@Override
	public Image getImage(Object element) {
		Image image = JFaceResources.getImage(Activator.PLUGIN_ID + ICONS_JMETH_OBJ_GIF);
		if (image == null) {
			JFaceResources.getImageRegistry().put(Activator.PLUGIN_ID + ICONS_JMETH_OBJ_GIF, desc);
			image = JFaceResources.getImage(Activator.PLUGIN_ID + ICONS_JMETH_OBJ_GIF);
		}
		return image;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Reachable) {
			Reachable reach = (Reachable) element;
			return reach.getFragment();
		}
		return super.getText(element);
	}

}
