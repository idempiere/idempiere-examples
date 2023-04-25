/***********************************************************************
 * This file is part of iDempiere ERP Open Source                      *
 * http://www.idempiere.org                                            *
 *                                                                     *
 * Copyright (C) Contributors                                          *
 *                                                                     *
 * This program is free software; you can redistribute it and/or       *
 * modify it under the terms of the GNU General Public License         *
 * as published by the Free Software Foundation; either version 2      *
 * of the License, or (at your option) any later version.              *
 *                                                                     *
 * This program is distributed in the hope that it will be useful,     *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of      *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the        *
 * GNU General Public License for more details.                        *
 *                                                                     *
 * You should have received a copy of the GNU General Public License   *
 * along with this program; if not, write to the Free Software         *
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,          *
 * MA 02110-1301, USA.                                                 *
 *                                                                     *
 * Contributors:                                                       *
 * - hengsin                         								   *
 **********************************************************************/
package org.idempiere.window.validator.example;

import org.adempiere.util.Callback;
import org.adempiere.webui.adwindow.validator.WindowValidator;
import org.adempiere.webui.adwindow.validator.WindowValidatorEvent;
import org.adempiere.webui.adwindow.validator.WindowValidatorEventType;
import org.adempiere.webui.editor.IProcessButton;
import org.adempiere.webui.window.Dialog;
import org.compiere.model.GridTab;
import org.compiere.model.MProcess;
import org.osgi.service.component.annotations.Component;

@Component(service = WindowValidator.class, immediate = true, property = {"AD_Window_UU=05af08b6-ca81-4f0b-b0f0-3e1e3d9a3a2e", "events=beforeProcess"})
public class CopyOrderLineWindowValidator implements WindowValidator {

	public CopyOrderLineWindowValidator() {
	}

	@Override
	public void onWindowEvent(WindowValidatorEvent event, Callback<Boolean> callback) {
		if (WindowValidatorEventType.BEFORE_PROCESS.getName().equals(event.getName())) {
			IProcessButton processButton = (IProcessButton) event.getData();
			MProcess process = MProcess.get(processButton.getProcess_ID());
			if (process.getClassname() != null && process.getClassname().equals("org.compiere.process.CopyFromOrder")) {
				GridTab gridTab = processButton.getADTabpanel().getGridTab();
				Dialog.ask("Sales Order " + gridTab.get_ValueAsString("DocumentNo"), 0, null, 
						"Copy order lines from other sales order document?", 
						new Callback<Boolean>() {										
							@Override
							public void onCallback(Boolean result) {
								callback.onCallback(result);
							}
						});		
				return;
			}
		}
		callback.onCallback(Boolean.TRUE);
	}

}
