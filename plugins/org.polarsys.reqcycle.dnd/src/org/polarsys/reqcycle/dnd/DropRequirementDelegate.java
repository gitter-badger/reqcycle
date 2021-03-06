/*******************************************************************************
 *  Copyright (c) 2013 AtoS
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html *
 *  Contributors:
 *    Tristan Faure (AtoS) - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.polarsys.reqcycle.dnd;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.part.IDropActionDelegate;
import org.polarsys.reqcycle.commands.Command;
import org.polarsys.reqcycle.commands.CreateRelationCommand;
import org.polarsys.reqcycle.commands.utils.RelationCommandUtils;
import org.polarsys.reqcycle.commands.utils.RelationCreationDescriptor;
import org.polarsys.reqcycle.traceability.types.ITypesConfigurationProvider;
import org.polarsys.reqcycle.traceability.types.configuration.preferences.dialogs.IconRegistry;
import org.polarsys.reqcycle.traceability.types.configuration.typeconfiguration.provider.TypeconfigurationItemProviderAdapterFactory;
import org.polarsys.reqcycle.types.ITypesManager;
import org.polarsys.reqcycle.uri.IReachableCreator;
import org.polarsys.reqcycle.uri.IReachableManager;
import org.polarsys.reqcycle.uri.exceptions.IReachableHandlerException;
import org.polarsys.reqcycle.uri.model.Reachable;
import org.polarsys.reqcycle.utils.inject.ZigguratInject;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class DropRequirementDelegate implements IDropActionDelegate {

	IReachableCreator creator = ZigguratInject.make(IReachableCreator.class);

	IReachableManager manager = ZigguratInject.make(IReachableManager.class);

	ITypesManager typesManager = ZigguratInject.make(ITypesManager.class);

	ITypesConfigurationProvider configManager = ZigguratInject.make(ITypesConfigurationProvider.class);

	@Override
	public boolean run(Object source, Object target) {
		Reachable targetReachable = null;

		if (source instanceof byte[] && isEObject(target)) {
			EObject targetEObj = getEObject(target);
			IFile file = WorkspaceSynchronizer.getFile(targetEObj.eResource());
			if (file != null) {
				try {
					targetReachable = manager.getHandlerFromObject(targetEObj).getFromObject(targetEObj).getReachable();
					if (targetReachable != null){
						byte[] data = (byte[]) source;
						List<Reachable> reachables = DNDReqCycle.getReachables(data);
						handleDrop(reachables, targetReachable, file);
					}
				} catch (IReachableHandlerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return true;

	}

	private boolean isEObject(Object target) {
		return getEObject(target) != null;
	}

	private EObject getEObject(Object target) {
		EObject result = null;
		if (target instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) target;
			result = (EObject) adaptable.getAdapter(EObject.class);
		}
		if (result == null) {
			result = (EObject) Platform.getAdapterManager().getAdapter(target, EObject.class);
		}
		if (result == null) {
			if (result instanceof EObject) {
				result = (EObject) target;
			}
		}
		return result;
	}

	public void handleDrop(List<Reachable> sourceReachables, Reachable targetReachable, IResource res) {
		final Set<RelationCreationDescriptor> allCommands = RelationCommandUtils.getAllRelationCommands(sourceReachables, Collections.singletonList(targetReachable));
		Iterable<RelationCreationDescriptor> upstreamToDownstreams = Iterables.filter(allCommands, new Predicate<RelationCreationDescriptor>() {

			public boolean apply(RelationCreationDescriptor desc) {
				return desc.isUpstreamToDownstream();
			}
		});
		Iterable<RelationCreationDescriptor> downstreamToUpstream = Iterables.filter(allCommands, new Predicate<RelationCreationDescriptor>() {

			public boolean apply(RelationCreationDescriptor desc) {
				return desc.isDownstreamToUpstream();
			}
		});
		Menu menu = new Menu(Display.getDefault().getActiveShell());
		Iterator<RelationCreationDescriptor> iteratorUD = upstreamToDownstreams.iterator();
		if (iteratorUD.hasNext()) {
			createMenu(menu, "Up To Down", iteratorUD, sourceReachables, targetReachable, RelationCreationDescriptor.UPSTREAM_TO_DOWNSTREAM);
		}
		Iterator<RelationCreationDescriptor> iteratorDU = downstreamToUpstream.iterator();
		if (iteratorDU.hasNext()) {
			createMenu(menu, "Down To Up", iteratorDU, sourceReachables,targetReachable, RelationCreationDescriptor.DOWNSTREAM_TO_UPSTREAM);
		}
		menu.setVisible(true);

	}

	public static void createMenu(Menu menu, String string, Iterator<RelationCreationDescriptor> iteratorUD, List<Reachable> sourceReachables, Reachable targetReachable, int direction) {
		MenuItem newItem = new MenuItem(menu, SWT.CASCADE);
		Menu newMenu = new Menu(menu);
		newItem.setMenu(newMenu);
		newItem.setText(string);
		for (; iteratorUD.hasNext();) {
			RelationCreationDescriptor desc = iteratorUD.next();
			MenuItem item = new MenuItem(newMenu, SWT.NONE);
			final List<CreateRelationCommand> commands = Lists.newArrayList();
			for (Reachable source : sourceReachables){
				CreateRelationCommand createRelationCommand = null;
				if (direction == RelationCreationDescriptor.DOWNSTREAM_TO_UPSTREAM){
						createRelationCommand = new CreateRelationCommand(desc.getRelation(), source, targetReachable);
				}
				else {
						createRelationCommand = new CreateRelationCommand(desc.getRelation(), targetReachable, source);
				}
				ZigguratInject.inject(createRelationCommand);
				commands.add(createRelationCommand);
			}
			item.setText(desc.getLabel());
			if ((desc.getRelation().getIcon() != null) && (desc.getRelation().getIcon().length() > 0)) {
				item.setImage(IconRegistry.getImage(desc.getRelation().getIcon()));
			}
			else {
				item.setImage(new AdapterFactoryLabelProvider(new TypeconfigurationItemProviderAdapterFactory()).getImage(desc.getRelation()));
			}
			item.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					for (Command c : commands){
						c.execute();
					}
				}

			});
		}
	}
}
