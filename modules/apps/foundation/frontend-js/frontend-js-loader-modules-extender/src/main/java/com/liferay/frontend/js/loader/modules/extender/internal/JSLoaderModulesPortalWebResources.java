/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.frontend.js.loader.modules.extender.internal;

import javax.servlet.ServletContext;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Raymond Augé
 */
@Component(immediate = true)
public class JSLoaderModulesPortalWebResources {

	@Activate
	protected void activate(BundleContext bundleContext) {
		try {
			com.liferay.portal.kernel.servlet.PortalWebResources
				portalWebResources = new InternalPortalWebResources();

			_serviceRegistration = bundleContext.registerService(
				com.liferay.portal.kernel.servlet.PortalWebResources.class,
				portalWebResources, null);
		}
		catch (NoClassDefFoundError ncdfe) {
			throw new RuntimeException(ncdfe);
		}
	}

	@Deactivate
	protected void deactivate() {
		if (_serviceRegistration != null) {
			_serviceRegistration.unregister();
		}
	}

	@Reference(unbind = "-")
	protected void setJSLoaderModulesServlet(
		JSLoaderModulesServlet jsLoaderModulesServlet) {

		_jsLoaderModulesServlet = jsLoaderModulesServlet;
	}

	@Reference(unbind = "-")
	protected void setJSLoaderModulesTracker(
		JSLoaderModulesTracker jsLoaderModulesTracker) {

		_jsLoaderModulesTracker = jsLoaderModulesTracker;
	}

	private JSLoaderModulesServlet _jsLoaderModulesServlet;
	private JSLoaderModulesTracker _jsLoaderModulesTracker;
	private ServiceRegistration<?> _serviceRegistration;

	private class InternalPortalWebResources
		implements com.liferay.portal.kernel.servlet.PortalWebResources {

		@Override
		public String getContextPath() {
			ServletContext servletContext = getServletContext();

			return servletContext.getContextPath();
		}

		@Override
		public long getLastModified() {
			return _jsLoaderModulesTracker.getTrackingCount();
		}

		@Override
		public String getResourceType() {
			return com.liferay.portal.kernel.servlet.PortalWebResourceConstants.
				RESOURCE_TYPE_JS_LOADER_MODULES;
		}

		@Override
		public ServletContext getServletContext() {
			return _jsLoaderModulesServlet.getServletContext();
		}

	}

}