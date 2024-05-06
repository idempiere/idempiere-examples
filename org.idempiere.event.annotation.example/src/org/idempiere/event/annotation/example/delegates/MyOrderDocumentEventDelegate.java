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
 **********************************************************************/
package org.idempiere.event.annotation.example.delegates;

import org.adempiere.base.annotation.EventTopicDelegate;
import org.adempiere.base.annotation.ModelEventTopic;
import org.adempiere.base.event.EventManager;
import org.adempiere.base.event.annotations.ModelEventDelegate;
import org.adempiere.base.event.annotations.doc.AfterClose;
import org.adempiere.base.event.annotations.doc.AfterComplete;
import org.adempiere.base.event.annotations.doc.AfterPrepare;
import org.adempiere.base.event.annotations.doc.AfterReactivate;
import org.adempiere.base.event.annotations.doc.AfterVoid;
import org.adempiere.base.event.annotations.doc.BeforeClose;
import org.adempiere.base.event.annotations.doc.BeforeComplete;
import org.adempiere.base.event.annotations.doc.BeforePrepare;
import org.adempiere.base.event.annotations.doc.BeforeReactivate;
import org.adempiere.base.event.annotations.doc.BeforeVoid;
import org.adempiere.base.event.annotations.doc.DocAction;
import org.compiere.model.MOrder;
import org.compiere.process.DocActionEventData;
import org.osgi.service.event.Event;

@EventTopicDelegate
@ModelEventTopic(modelClass = MOrder.class)
public class MyOrderDocumentEventDelegate extends ModelEventDelegate<MOrder> {

	public MyOrderDocumentEventDelegate(MOrder po, Event event) {
		super(po, event);
	}

	@DocAction
	public void onDocAction() {
		DocActionEventData data = (DocActionEventData) event.getProperty(EventManager.EVENT_DATA);
		System.out.println("onDocAction: " + data.options);
	}
	
	@BeforePrepare
	public void onBeforePrepare() {
		System.out.println("onBeforePrepare: " + getModel().get_xmlString(null));
	}
	
	@AfterPrepare
	public void onAfterPrepare() {
		System.out.println("onAfterPrepare: " + getModel().get_xmlString(null));
	}
	
	@BeforeComplete
	public void onBeforeComplete() {
		System.out.println("onBeforeComplete: " + getModel().get_xmlString(null));
	}
	
	@AfterComplete
	public void onAfterComplete() {
		System.out.println("onAfterComplete: " + getModel().get_xmlString(null));
	}
	
	@BeforeClose
	public void onBeforeClose() {
		System.out.println("onBeforeClose: " + getModel().get_xmlString(null));
	}
	
	@AfterClose
	public void onAfterClose() {
		System.out.println("onAfterClose: " + getModel().get_xmlString(null));
	}
	
	@BeforeReactivate
	public void onBeforeReactivate() {
		System.out.println("onBeforeReactivate: " + getModel().get_xmlString(null));
	}
	
	@AfterReactivate
	public void onAfterReactivate() {
		System.out.println("onAfterReactivate: " + getModel().get_xmlString(null));
	}
	
	@BeforeVoid
	public void onBeforeVoid() {
		System.out.println("onBeforeVoid: " + getModel().get_xmlString(null));
	}
	
	@AfterVoid
	public void onAfterVoid() {
		System.out.println("onAfterVoid: " + getModel().get_xmlString(null));
	}
}
