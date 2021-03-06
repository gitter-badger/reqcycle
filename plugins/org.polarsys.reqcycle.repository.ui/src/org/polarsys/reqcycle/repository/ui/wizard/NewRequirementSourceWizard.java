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
package org.polarsys.reqcycle.repository.ui.wizard;

import javax.inject.Inject;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.polarsys.reqcycle.repository.connector.ConnectorDescriptor;
import org.polarsys.reqcycle.repository.connector.ICallable;
import org.polarsys.reqcycle.repository.connector.IConnector;
import org.polarsys.reqcycle.repository.connector.local.ui.LocalSettingPage;
import org.polarsys.reqcycle.repository.connector.ui.wizard.IConnectorWizard;
import org.polarsys.reqcycle.repository.connector.ui.wizard.pages.AbstractSettingPage;
import org.polarsys.reqcycle.repository.data.IDataManager;
import org.polarsys.reqcycle.repository.data.RequirementSourceConf.RequirementSource;
import org.polarsys.reqcycle.repository.ui.wizard.pages.SelectConnectorPage;
import org.polarsys.reqcycle.utils.inject.ZigguratInject;

public class NewRequirementSourceWizard extends Wizard implements IWorkbenchWizard {

	/** connector selection wizard page */
	private SelectConnectorPage selectConnectorPage;

	private ICallable createRequirementSource = null;

	private IStructuredSelection selection;

	@Inject
	IDataManager requirementSourceManager;

	@Inject
	public NewRequirementSourceWizard(IDataManager requirementSourceManager) {
		this();
		this.requirementSourceManager = requirementSourceManager;
	}

	public NewRequirementSourceWizard() {
		setForcePreviousAndNextButtons(true);
		setNeedsProgressMonitor(true);
		setWindowTitle("Add Requirement Source");
	}

	@Override
	public void addPages() {
		selectConnectorPage = new SelectConnectorPage();
		ZigguratInject.inject(selectConnectorPage);
		addPage(selectConnectorPage);
	}

	private IDataManager getRequirementSourceManager() {
		if (requirementSourceManager == null) {
			requirementSourceManager = ZigguratInject.make(IDataManager.class);
		}
		return requirementSourceManager;
	}

	@Override
	public boolean performFinish() {
		IConnector connector = getConnector();
		if (connector instanceof IConnectorWizard) {
			boolean finish = ((IConnectorWizard) connector).performFinish();
			if (!finish) {
				return false;
			}
		}
		createRequirementSource = connector.getRequirementsCreator();
		try {
			if (createRequirementSource == null) {
				// FIXME : Exception

				throw new Exception("requirement source is null; Should not occur = bug");
			}
			RequirementSource source = requirementSourceManager.createRequirementSource();
			ConnectorDescriptor connectorDescriptor = getConnectorDescriptor();
			source.setConnectorId(connectorDescriptor.getId());
			String sourceName = getSourceName();
			source.setName(sourceName);
			if (connector instanceof IConnectorWizard) {
				IConnectorWizard wiz = (IConnectorWizard) connector;
				wiz.storeProperties(source);
			}
			createRequirementSource.fillRequirementSource(source);
			getRequirementSourceManager().addRequirementSource(source);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return createRequirementSource != null;
	}

	@Override
	public boolean canFinish() {
		if (getConnectorDescriptor() == null || getConnector() == null || getSourceName() == null || getSourceName().isEmpty()) {
			return false;
		}
		IConnector connector = getConnector();
		if (connector instanceof IConnectorWizard && ((IConnectorWizard) connector).getPageCount() == 0) {
			return false;
		}
		if (connector instanceof IConnectorWizard && !((IConnectorWizard) connector).canFinish()) {
			return false;
		}
		return true;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		IConnector connector = getConnector();
		if (page instanceof SelectConnectorPage && connector instanceof IConnectorWizard) {
			IConnectorWizard connectorWizard = (IConnectorWizard) connector;
			connectorWizard.init(selection, ((SelectConnectorPage) page).getSourceName());
			if (connectorWizard.getPageCount() == 0) {
				connectorWizard.addPages();
			}
			if (connectorWizard.getStartingPage() instanceof AbstractSettingPage) {
				AbstractSettingPage startingPage = (AbstractSettingPage) connectorWizard.getStartingPage();
				startingPage.setFileName(((SelectConnectorPage) page).getSourceName());
			}
			if (connectorWizard.getStartingPage() instanceof LocalSettingPage) {
				LocalSettingPage startingPage = (LocalSettingPage) connectorWizard.getStartingPage();
				startingPage.setFileName(((SelectConnectorPage) page).getSourceName());
			}
			IWizardPage startingPage = connectorWizard.getStartingPage();
			if (startingPage != null) {
				startingPage.setWizard(this);
			}
			return startingPage;
		}

		if (connector instanceof IConnectorWizard) {
			IWizardPage nextPage = ((IConnectorWizard) connector).getNextPage(page);
			if (nextPage != null) {
				nextPage.setWizard(this);
			}
			return nextPage;
		}
		return null;
	}

	public ConnectorDescriptor getConnectorDescriptor() {
		if (selectConnectorPage != null) {
			return selectConnectorPage.getConnectorDescriptor();
		}
		return null;
	}

	public IConnector getConnector() {
		if (selectConnectorPage != null) {
			return selectConnectorPage.getConnector();
		}
		return null;
	}

	public String getSourceName() {
		if (selectConnectorPage != null) {
			return selectConnectorPage.getSourceName();
		}
		return null;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

}
