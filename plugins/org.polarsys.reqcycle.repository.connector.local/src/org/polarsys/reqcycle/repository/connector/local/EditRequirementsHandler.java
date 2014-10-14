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
package org.polarsys.reqcycle.repository.connector.local;

import javax.inject.Inject;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;
import org.polarsys.reqcycle.repository.data.RequirementSourceConf.RequirementSource;
import org.polarsys.reqcycle.utils.configuration.IConfigurationManager;
import org.polarsys.reqcycle.utils.inject.ZigguratInject;

public class EditRequirementsHandler extends AbstractHandler {

	@Inject
	IConfigurationManager confManager;

	public EditRequirementsHandler() {
		ZigguratInject.inject(this);
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			Object firstElement = ((IStructuredSelection) selection).getFirstElement();
			if (firstElement instanceof RequirementSource) {
				RequirementSource requirementSource = (RequirementSource) firstElement;
				String connectorId = requirementSource.getConnectorId();
				if (LocalConnector.LOCAL_CONNECTOR_ID.equals(connectorId)) {
					Resource eResource = requirementSource.getContents().eResource();
					IEditorReference[] editorReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
					for (IEditorReference iEditorReference : editorReferences) {
						try {
							if (iEditorReference.getEditorInput() instanceof IURIEditorInput) {
								IURIEditorInput input = (IURIEditorInput) iEditorReference.getEditorInput();
								if (input.getURI().equals(eResource.getURI())) {
									MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Open Requirement Editor", "Please close the opened Requirements Editor before beginning a new edition.");
									return null;
								}
							}
						} catch (PartInitException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					// Resource eResource = requirementSource.eResource();
					// URI uri =
					// ((ConfigurationManagerImpl)confManager).getConfigurationFileUri(null,
					// null, DataManagerImpl.ID + "." +
					// requirementSource.getName());
					// uri =
					// uri.appendFragment(eResource.getURIFragment(requirementSource));
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					try {
						URIEditorInput editorInput = new URIEditorInput(eResource.getURI());
						IDE.openEditor(page, editorInput, "org.eclipse.emf.ecore.presentation.ReflectiveEditorID");
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				} else {
					MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Open Requiement Editor", "Can't Edit this Requirement Source. Only Local Requirement Source can be edited.");
					return null;
				}
			}
		}

		return null;
	}
}
