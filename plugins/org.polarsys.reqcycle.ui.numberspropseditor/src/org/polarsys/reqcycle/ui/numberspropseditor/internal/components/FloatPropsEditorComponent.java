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
package org.polarsys.reqcycle.ui.numberspropseditor.internal.components;

import org.eclipse.swt.widgets.Composite;
import org.polarsys.reqcycle.ui.eattrpropseditor.api.AbstractPropsTextEditorComponent;

public class FloatPropsEditorComponent extends AbstractPropsTextEditorComponent<Float> {

	private String errorMessage;

	public FloatPropsEditorComponent(String attributeName, Composite parent, int style) {
		super(attributeName, Float.class, parent, style);
	}

	@Override
	protected Float convertFromString(String textValue) {
		try {
			return Float.parseFloat(textValue);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@Override
	protected boolean isTextValid(String textValue) {
		try {
			Float.parseFloat(textValue);
			return true;
		} catch (NumberFormatException e) {
			this.errorMessage = "Not a float. " + e.getMessage();
			return false;
		}
	}

	@Override
	protected String getErrorMessage() {
		return this.errorMessage;
	}

}
