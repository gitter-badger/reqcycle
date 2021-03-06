/*******************************************************************************
 *  Copyright (c) 2013, 2014 AtoS and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html *
 *  Contributors:
 *  Malick WADE (AtoS) - initial API and implementation and/or initial documentation
 *  Raphael FAUDOU (Samares Engineering) - fixed bug concerning comparison of destination path (isPageComplete)
 *
 *******************************************************************************/
package org.polarsys.reqcycle.repository.connector.ui.wizard.pages;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.polarsys.reqcycle.repository.data.IDataModelManager;
import org.polarsys.reqcycle.repository.data.ScopeConf.Scope;
import org.polarsys.reqcycle.repository.data.types.IDataModel;
import org.polarsys.reqcycle.utils.inject.ZigguratInject;

public class AbstractSettingPage extends WizardPage implements IChangeListener {

	@Inject
	protected IDataModelManager dataManager;

	private ComboViewer cvDataModel;

	private ComboViewer cvScope;

	private Combo cScope;

	private Combo cDataModel;

	private Collection<Scope> inputScope = new ArrayList<Scope>();

	private Text txtFile;

	private Button btnBrowseDestinationFile;

	// Desactivate Reference Mode
	// private Button radioBtnCopyImport;
	// private Button radioBtnReferenceImport;
	// private Label lblMode;
	// private Composite radioBtnComposite;

	private Label lblCopyFile;
	private DataBindingContext bindingContext;

	private AbstractStorageBean bean;

	public AbstractSettingPage(String pageName, AbstractStorageBean bean) {
		super(pageName);
		ZigguratInject.inject(this);
		this.bean = bean;
	}

	public AbstractStorageBean getBean() {
		return bean;
	}

	@Override
	public void createControl(Composite parent) {

		Composite compositeContainer = new Composite(parent, SWT.NONE);
		compositeContainer = doCreateSpecific(compositeContainer);

		createModele(compositeContainer, "Model");
		createScope(compositeContainer, "Scope");
		// createCopyOrRefMode(compositeContainer);
		createDestinationFile(compositeContainer);
		hookListeners();
		bindingContext = new DataBindingContext();
		initDataBindings(bindingContext);
		observeBean(bindingContext, this);
	}

	public static void observeBean(DataBindingContext bindingContext,
			IChangeListener listener) {
		IObservableList list = bindingContext.getValidationStatusProviders();
		for (Object o : list) {
			Binding b = (Binding) o;
			b.getTarget().addChangeListener(listener);
		}
	}

	protected void createModele(Composite compositeContainer,
			String labelTitleModele) {

		Label lblSeparatorModele = new Label(compositeContainer, SWT.SEPARATOR
				| SWT.HORIZONTAL);
		lblSeparatorModele.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 4, 1));
		Label lblDataModel = new Label(compositeContainer, SWT.NONE);
		lblDataModel.setText(labelTitleModele);

		cvDataModel = new ComboViewer(compositeContainer);
		cDataModel = cvDataModel.getCombo();
		cDataModel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 3, 1));
		cvDataModel.setContentProvider(ArrayContentProvider.getInstance());
		cvDataModel.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {
				if (element instanceof IDataModel) {
					return ((IDataModel) element).getName();
				}
				return super.getText(element);
			}
		});
		cvDataModel.setInput(dataManager.getCurrentDataModels());

	}

	protected void createScope(Composite compositeContainer,
			String labelTitleScope) {

		Label lblScope = new Label(compositeContainer, SWT.NONE);
		lblScope.setText(labelTitleScope);

		cvScope = new ComboViewer(compositeContainer);
		cScope = cvScope.getCombo();
		cScope.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3,
				1));
		cScope.setEnabled(false);
		cvScope.setContentProvider(ArrayContentProvider.getInstance());
		cvScope.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {
				if (element instanceof Scope) {
					return ((Scope) element).getName();
				}
				return super.getText(element);
			}
		});
		cvScope.setInput(inputScope);

	}

	// protected void createCopyOrRefMode(Composite compositeContainer) {
	//
	// Label lblSeparatorModele = new Label(compositeContainer, SWT.SEPARATOR |
	// SWT.HORIZONTAL);
	// lblSeparatorModele.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
	// false, 4, 1));
	//
	// radioBtnComposite = new Composite(compositeContainer, SWT.NONE);
	// radioBtnComposite.setLayout(new GridLayout(5, false));
	// radioBtnComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
	// false, 2, 1));
	//
	// lblMode = new Label(radioBtnComposite, SWT.None);
	// lblMode.setText("Import Mode :");
	//
	// radioBtnCopyImport = new Button(radioBtnComposite, SWT.RADIO);
	// radioBtnCopyImport.setText("Copy");
	// radioBtnCopyImport.setSelection(true);
	//
	// radioBtnReferenceImport = new Button(radioBtnComposite, SWT.RADIO);
	// radioBtnReferenceImport.setText("Reference");
	// radioBtnReferenceImport.setEnabled(true);
	// new Label(radioBtnComposite, SWT.NONE);
	//
	// }

	protected void hookListeners() {
		getDataModelSelectionChangedListener();
		// getBtnReferenceImportSelectionListener();
		// getBtnCopyImportSelectionListener();
	}

	protected void createDestinationFile(Composite compositeContainer) {

		Label lblSeparator = new Label(compositeContainer, SWT.SEPARATOR
				| SWT.HORIZONTAL);
		lblSeparator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 4, 1));

		Composite radioBtnComposite = new Composite(compositeContainer,
				SWT.NONE);
		radioBtnComposite.setLayout(new GridLayout(1, false));
		radioBtnComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 4, 1));

		Composite compositeCopy = new Composite(radioBtnComposite, SWT.NONE);
		compositeCopy.setLayout(new GridLayout(3, false));
		compositeCopy.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));

		lblCopyFile = new Label(compositeCopy, SWT.NONE);
		lblCopyFile.setText("Repository Folder :");

		txtFile = new Text(compositeCopy, SWT.BORDER);
		txtFile.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		// txtFile.setEditable(false);
		txtFile.setEnabled(false);

		btnBrowseDestinationFile = new Button(compositeCopy, SWT.NONE);
		btnBrowseDestinationFile.setText("Browse");

		btnBrowseDestinationFile.setEnabled(true);
		lblCopyFile.setEnabled(true);
	}

	protected void getDataModelSelectionChangedListener() {

		cvDataModel
				.addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						inputScope.clear();
						cScope.setEnabled(false);
						ISelection selection = event.getSelection();
						if (selection instanceof IStructuredSelection) {
							Object obj = ((IStructuredSelection) selection)
									.getFirstElement();
							if (obj instanceof IDataModel) {
								cScope.setEnabled(true);
								inputScope.addAll(dataManager
										.getScopes((IDataModel) obj));

							}
						}
						cvScope.refresh();
					}
				});
	}

	protected void getDestinationFileSelectionListener() {

		btnBrowseDestinationFile.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ContainerSelectionDialog dialog = new ContainerSelectionDialog(
						getShell(), ResourcesPlugin.getWorkspace().getRoot(),
						true, "select location for save");
				if (Window.OK == dialog.open()) {
					Object[] result = dialog.getResult();
					if (result != null && result.length > 0) {
						if (result[0] instanceof IPath) {
							IPath path = (IPath) result[0];
							txtFile.setText(URI.createPlatformResourceURI(
									path.append(getBean().getFileName())
											.addFileExtension("reqcycle")
											.toString(), true).toString());
						}
					}
				}
			}
		});
	}

	// protected void getBtnReferenceImportSelectionListener() {
	// radioBtnReferenceImport.addSelectionListener(new SelectionAdapter() {
	// @Override
	// public void widgetSelected(SelectionEvent e) {
	// btnBrowseDestinationFile.setEnabled(false);
	// lblCopyFile.setEnabled(false);
	// }
	// });
	// }

	// protected void getBtnCopyImportSelectionListener() {
	// radioBtnCopyImport.addSelectionListener(new SelectionAdapter() {
	// @Override
	// public void widgetSelected(SelectionEvent e) {
	// btnBrowseDestinationFile.setEnabled(true);
	// lblCopyFile.setEnabled(true);
	// }
	// });
	// }

	protected final DataBindingContext initDataBindings(
			DataBindingContext bindingContext) {

		IObservableValue observeSingleSelectionCvDataModel = ViewerProperties
				.singleSelection().observe(cvDataModel);
		IObservableValue dataPackageBeanObserveValue = PojoProperties.value(
				"dataModel").observe(getBean());
		bindingContext.bindValue(observeSingleSelectionCvDataModel,
				dataPackageBeanObserveValue, null, null);
		//
		IObservableValue observeSingleSelectionCvScope = ViewerProperties
				.singleSelection().observe(cvScope);
		IObservableValue scopeBeanObserveValue = PojoProperties.value("scope")
				.observe(getBean());
		bindingContext.bindValue(observeSingleSelectionCvScope,
				scopeBeanObserveValue, null, null);
		//
		IObservableValue observeTextTxtFileObserveWidget = WidgetProperties
				.text(SWT.Modify).observe(txtFile);
		IObservableValue modelPathBeanObserveValue = PojoProperties.value(
				"outputPath").observe(getBean());
		bindingContext.bindValue(observeTextTxtFileObserveWidget,
				modelPathBeanObserveValue, null, null);
		//
		// IObservableValue observeSelectionRadioBtnReferenceImportObserveWidget
		// = WidgetProperties.selection().observe(radioBtnReferenceImport);
		// IObservableValue isReferenceBeanObserveValue =
		// PojoProperties.value("isReference").observe(getBean());
		// bindingContext.bindValue(observeSelectionRadioBtnReferenceImportObserveWidget,
		// isReferenceBeanObserveValue, null, null);
		//
		doSpecificInitDataBindings(bindingContext);
		return bindingContext;
	}

	protected void doSpecificInitDataBindings(DataBindingContext bindingContext) {
		// DO NOTHING
		// intended to be overridden

	}

	@Override
	public boolean isPageComplete() {
		StringBuffer error = new StringBuffer();
		boolean result = true;

		if ((getBean().getDataModel() == null)) {
			error.append("Choose a Data Model\n");
			result = false;
		}

		if (getBean().getScope() == null) {
			error.append("Choose a Scope\n");
			result = false;
		}

		// -RFa- fixed bug concerning comparison of empty string
		if (!(getBean().getIsReference())
				&& ((getBean().getOutputPath() == null) || "".equals(getBean()
						.getOutputPath()))) {
			error.append("Choose a destination path (Repository folder)\n");
			result = false;
		}

		if (!result) {
			setErrorMessage(error.toString());
		} else {
			setErrorMessage(null);
		}

		return result;
	}

	protected Composite doCreateSpecific(Composite parent) {
		return parent;
	}

	@Override
	public void handleChange(ChangeEvent event) {
		bindingContext.updateModels();
		bindingContext.updateTargets();
		getWizard().getContainer().updateButtons();
		getWizard().getContainer().updateMessage();
		IWizardPage p = getNextPage();
		while (p != null) {
			if (p != this && p instanceof IUpdatablePage) {
				((IUpdatablePage) p).hasChanged();
			}
			p = p.getNextPage();
		}
	}

	public void setFileName(String fileName) {
		getBean().setFileName(fileName);
	}

}
