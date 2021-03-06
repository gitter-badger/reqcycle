package org.polarsys.reqcycle.traceability.emf;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.polarsys.reqcycle.traceability.emf"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	public static final String OPTIONS_DEBUG = PLUGIN_ID + "/debug";

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Logs a message
	 * 
	 * @param severity
	 *            the severity; one of IStatus.OK, IStatus.ERROR, IStatus.INFO, IStatus.WARNING, or IStatus.CANCEL
	 * @param message
	 *            a human-readable message, localized to the current locale
	 * @param exception
	 *            a low-level exception, or null if not applicable
	 */
	public static void log(int severity, String message, Throwable exception) {
		IStatus status = new Status(severity, PLUGIN_ID, message, exception);
		getDefault().getLog().log(status);
	}

}
