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
package org.polarsys.reqcycle.ui.simplepropseditors.internal;

import org.polarsys.reqcycle.ui.eattrpropseditor.api.AbstractPropsEditorComponent;
import org.polarsys.reqcycle.ui.simplepropseditors.internal.components.StringPropsEditorComponent;

public class StringPropsEditor extends CharSequencePropsEditor {

	@Override
	protected AbstractPropsEditorComponent<CharSequence> initAndGetComponent() {
		return new StringPropsEditorComponent(getAttributeName(), getContainer(), getStyle());
	}

}
