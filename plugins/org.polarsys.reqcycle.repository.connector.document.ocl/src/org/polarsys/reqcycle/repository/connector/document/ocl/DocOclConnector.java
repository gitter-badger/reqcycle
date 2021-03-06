/*******************************************************************************
 *  Copyright (c) 2013, 2014 AtoS and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html *
 *  Contributors:
 *  Malick WADE (AtoS) - initial API and implementation and/or initial documentation
 *
 *******************************************************************************/
package org.polarsys.reqcycle.repository.connector.document.ocl;

import javax.inject.Inject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.Wizard;
import org.polarsys.kitalpha.doc.doc2model.common.Common.DocumentModel;
import org.polarsys.kitalpha.doc.doc2model.core.Doc2Model;
import org.polarsys.reqcycle.ocl.OCLCallable;
import org.polarsys.reqcycle.ocl.ui.OCLConnector;
import org.polarsys.reqcycle.ocl.ui.OCLPage;
import org.polarsys.reqcycle.ocl.ui.SettingBean;
import org.polarsys.reqcycle.repository.connector.ICallable;
import org.polarsys.reqcycle.repository.connector.IURIValidatorConnector;
import org.polarsys.reqcycle.repository.connector.document.ocl.ui.DocOclSettingPage;
import org.polarsys.reqcycle.repository.connector.document.ocl.ui.OCLDocBean;
import org.polarsys.reqcycle.repository.connector.ui.wizard.IConnectorWizard;
import org.polarsys.reqcycle.repository.data.IDataManager;
import org.polarsys.reqcycle.repository.data.IDataModelManager;
import org.polarsys.reqcycle.repository.data.RequirementSourceConf.RequirementSource;
import org.polarsys.reqcycle.utils.inject.ZigguratInject;




public class DocOclConnector extends Wizard implements IConnectorWizard, IURIValidatorConnector {
	
	protected DocOclSettingPage docOclSettingPage;
	
	protected OCLPage oclMappingPage;
	
	private SettingBean bean ;
	
	private RequirementSource requirementSource = null;
	
	@Inject
	IDataModelManager manager;

	@Inject
	IDataManager dataManager;
	
	OCLConnector OCLConnector = new OCLConnector();

	
	@Override
	public void addPages() {
		
		bean =  new OCLDocBean(OCLConnector);
		
		docOclSettingPage = new DocOclSettingPage(bean);	
		oclMappingPage = new OCLPage(bean);
		addPage(docOclSettingPage);		
		addPage(oclMappingPage);
		super.addPages();		
	}
	

	@Override
	public ICallable getRequirementsCreator() {
		OCLCallable callable = new DocOclCallable();
		ZigguratInject.inject(callable);
		
		return callable;
	}

	@Override
	public boolean performFinish() {
		return true;
	}
	
	@Override
	public boolean canFinish() {
		return bean != null && bean.getOclUri() != null && bean.getDataModel() != null && bean.getUri() != null 
				&& !bean.getOclUri().isEmpty() && !bean.getUri().isEmpty()
				&& !bean.getOutputPath().isEmpty();
	}



	@Override
	public void initializeWithRequirementSource(RequirementSource requirementSource) {
		this.requirementSource = requirementSource;
	}


	@Override
	public void init(ISelection selection, String name) {
		
	}


	@Override
	public void storeProperties(RequirementSource source) {
		bean.storeProperties(source);
	}
	
	@Override
	public IStatus validate(URI uri) {
		String fileString = CommonPlugin.asLocalURI(uri).toFileString();
		Doc2Model<DocumentModel> doc2Model = new Doc2Model<DocumentModel>();
		String fileType = doc2Model.getFileType(fileString);
		if (fileType.toString() != null) {
			return Status.OK_STATUS;
		}
		String extension = uri.path().substring(uri.path().lastIndexOf(".")+1, uri.path().length());
		return new Status(IStatus.WARNING, Activator.PLUGIN_ID, "extension " + extension + " is not supported. Retry and choose document file");
	}


}
